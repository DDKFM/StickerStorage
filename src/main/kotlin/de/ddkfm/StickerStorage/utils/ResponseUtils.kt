package de.ddkfm.StickerStorage.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.location(path : String) : UriComponentsBuilder {
    return ServletUriComponentsBuilder
            .fromContextPath(this)
            .path(path)
}

fun UriComponentsBuilder.withParams(vararg params : Any?) : UriComponents {
    return this.buildAndExpand(params)
}

fun UriComponents.created() : ResponseEntity.BodyBuilder {
    return ResponseEntity.created(this.toUri())
}
