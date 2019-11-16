package de.ddkfm.SticketStorage.utils

object SecurityConstants {
    @JvmStatic
    val AUTH_LOGIN_URL = "/v1/authenticate"
    @JvmStatic
    val JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y\$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf"

    // JWT token defaults
    @JvmStatic
    val TOKEN_HEADER = "Authorization"
    @JvmStatic
    val TOKEN_PREFIX = "Bearer "
    @JvmStatic
    val TOKEN_TYPE = "JWT"
    @JvmStatic
    val TOKEN_ISSUER = "secure-api"
    @JvmStatic
    val TOKEN_AUDIENCE = "secure-app"

    @JvmStatic
    val AUTHENTICATION_USERNAME = "username"

    @JvmStatic
    val AUTHENTICATION_PASSWORD = "password"
}