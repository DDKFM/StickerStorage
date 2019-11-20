package de.ddkfm.StickerStorage.security

import de.ddkfm.StickerStorage.models.Token
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
object JwtTokenProvider {

    fun getAuthentication(token: String): Authentication {
        return UsernamePasswordAuthenticationToken(SecurityConstants.AUTHENTICATION_USERNAME, SecurityConstants.AUTHENTICATION_PASSWORD)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun createToken(username: String): Token {
        val jwtToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(username)
                .setExpiration(Date(System.currentTimeMillis() + 864000000))
                .compact()
        return Token(jwtToken)
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }

    }
}
