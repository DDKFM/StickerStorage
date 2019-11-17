package de.ddkfm.StickerStorage.security

object SecurityConstants {
    @JvmStatic
    val AUTH_LOGIN_URL = "/v1/authenticate"
    @JvmStatic
    val JWT_SECRET = System.getenv("JWT_SECRET") ?: "HalloWelt"

    // JWT token defaults
    @JvmStatic
    val TOKEN_HEADER = "Authorization"
    @JvmStatic
    val TOKEN_PREFIX = "Bearer "
    @JvmStatic
    val TOKEN_TYPE = "JWT"
    @JvmStatic
    val TOKEN_ISSUER = "Sticker Storage"
    @JvmStatic
    val TOKEN_AUDIENCE = "Sticker Storage"

    @JvmStatic
    val AUTHENTICATION_USERNAME = System.getenv("AUTHENTICATION_USERNAME") ?: "admin"

    @JvmStatic
    val AUTHENTICATION_PASSWORD = System.getenv("AUTHENTICATION_PASSWORD") ?: "admin"
}