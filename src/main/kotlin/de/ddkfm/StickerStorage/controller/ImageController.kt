package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Image
import de.ddkfm.StickerStorage.models.SimpleEvent
import de.ddkfm.StickerStorage.models.SimpleImage
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.utils.created
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.withParams
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/events/{id}")
class ImageController(private val images: ImageRepository) {

    @GetMapping("/image")
    fun get(@PathVariable("id") id: Long): ResponseEntity<SimpleImage> {
        val image = images.findByEventId(id)
                .firstOrNull()
                ?: return notFound().build()
        return ok(image.toModel())
    }

}