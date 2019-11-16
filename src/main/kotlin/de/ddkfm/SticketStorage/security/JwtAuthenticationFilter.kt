package de.ddkfm.SticketStorage.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(val manager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    init {
        this.authenticationManager = manager
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        val auth = request.getHeader("Authorization") ?: return notAuth()
        val encoded = Base64
                .getDecoder()
                .decode(auth.replace("Basic ", "").toByteArray())
                ?.let { String(it) }
                ?: return notAuth()
        if(!encoded.contains(":"))
            return notAuth()
        val username = encoded.split(":")[0]
        val password = encoded.split(":")[1]
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(authenticationToken)
    }

    fun notAuth() : Authentication {
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(null, null))
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse,
                                          filterChain: FilterChain?, authentication: Authentication) {
        val user = authentication.principal as User

        val roles = user.authorities
                .map { it.authority }

        val signingKey = SecurityConstants.JWT_SECRET.toByteArray()

        val token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.username)
                .setExpiration(Date(System.currentTimeMillis() + 864000000))
                .claim("role", roles)
                .compact()
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token)
        response.outputStream.write(token.toByteArray())
    }
}