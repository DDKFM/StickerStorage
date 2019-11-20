package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.*
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.repository.LocationRepository
import de.ddkfm.StickerStorage.repository.StickerRepository
import de.ddkfm.StickerStorage.utils.created
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.saveIn
import de.ddkfm.StickerStorage.utils.withParams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/stickers")
@Tag(name = "stickers")
@SecurityRequirement(name = "api")
class StickerController(private val stickers: StickerRepository,
                        private val events : EventRepository,
                        private val locations : LocationRepository,
                        private val images : ImageRepository) {


    @GetMapping("")
    fun all(
            @RequestParam("event", defaultValue = "") event : String = "",
            @RequestParam("location", defaultValue = "") location : String = "",
            @RequestParam("query", defaultValue = "") query : String = ""): ResponseEntity<List<SimpleSticker>> {

        val allSticker = when {
            query.isNotEmpty() -> stickers.findByNameIgnoreCaseContaining(query)
            else -> stickers.findAll()
        }
        return ok(allSticker.map { it.toSimple() })
    }

    @GetMapping("/{id}")

    fun get(@PathVariable("id") id: Long): ResponseEntity<SimpleSticker> {
        val sticker = stickers.findById(id)
                .orElse(null)
                ?: return notFound().build()
        return ok(sticker.toSimple())
    }

    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody sticker : SimpleSticker, request : HttpServletRequest) : ResponseEntity<SimpleSticker> {
        val event = sticker.eventId
                ?.let { events.findById(it).orElse(null) }
                ?: return badRequest().build()
        val location = sticker.locationId
                ?.let { locations.findById(it).orElse(null) }
                ?: return badRequest().build()

        val savedSticker = Sticker(
                name = sticker.name,
                event = event,
                keywords = sticker.keywords,
                comment = sticker.comment,
                amount = sticker.amount,
                location = location
        ).saveIn(stickers)
        if(savedSticker?.id == null)
            return badRequest().build()
        return request
                .location("/v1/stickers/{ID}")
                .withParams(savedSticker.id)
                .created()
                .body(savedSticker.toSimple())
    }

    @GetMapping("/{stickerId}/image")
    fun getImage(@PathVariable("stickerId") id: Long, request : HttpServletRequest): ResponseEntity<SimpleImage> {
        val image = images.findByStickerId(id)
                .firstOrNull()
                ?: return notFound().build()
        val imageCallback = request
                .location("/v1/images/{imageId}")
                .withParams(image.id)
                .toUri()
        return ok(SimpleImage(imageCallback.toString(), image.imageUrl, null, image.sticker?.id))
    }


}