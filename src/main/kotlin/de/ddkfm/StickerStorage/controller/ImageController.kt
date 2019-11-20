package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.SimpleImage
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.withParams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/v1/events/{eventId}/images")
@SecurityRequirement(name = "api")
@Tag(name = "images")
class ImageController(private val images: ImageRepository) {

    @GetMapping("")
    fun get(@PathVariable("eventId") id: Long, request : HttpServletRequest): ResponseEntity<SimpleImage> {
        val image = images.findByEventId(id)
                .firstOrNull()
                ?: return notFound().build()
        val imageCallback = request
                .location("/v1/events/{eventId}/images/{imageId}")
                .withParams(mapOf(
                        "eventId" to image.event?.id,
                        "imageId" to image.id
                ))
                .toUri()
        return ok(SimpleImage(imageCallback.toString(), image.imageUrl, image.event?.id))
    }

    @GetMapping("/{imageId}")
    fun getImageData(@PathVariable("eventId") eventId: Long,
                     @PathVariable("imageId") imageId : Long,
                     response: HttpServletResponse): ResponseEntity<ByteArray> {
        val image = images.findById(imageId).orElse(null)
                ?: return notFound().build()
        if(image.event?.id != eventId)
            return badRequest().build()
        return ok(image.imageData)
    }

}