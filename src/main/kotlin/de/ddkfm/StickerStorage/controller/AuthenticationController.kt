package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Token
import de.ddkfm.StickerStorage.security.JwtTokenProvider
import de.ddkfm.StickerStorage.security.SecurityConstants
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/authenticate")
@Tag(name = "Authentication")
@SecurityRequirement(name = "login")
class AuthenticationController {
    @GetMapping("")
    fun login(request : HttpServletRequest): ResponseEntity<Token> {
        val forbidden = ResponseEntity.status(HttpStatus.FORBIDDEN).build<Token>()
        val auth = request.getHeader("Authorization")
                ?: return forbidden
        val encoded = Base64
                .getDecoder()
                .decode(auth.replace("Basic ", "").toByteArray())
                ?.let { String(it) }
                ?: return forbidden
        if(!encoded.contains(":"))
            return forbidden
        val username = encoded.split(":")[0]
        val password = encoded.split(":")[1]
        if(username != SecurityConstants.AUTHENTICATION_USERNAME || password != SecurityConstants.AUTHENTICATION_PASSWORD)
            return forbidden
        return ok(JwtTokenProvider.createToken(username))
    }
}