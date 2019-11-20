package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.SimpleEvent
import de.ddkfm.StickerStorage.models.SimpleSticker
import de.ddkfm.StickerStorage.models.Sticker
import de.ddkfm.StickerStorage.repository.EventRepository
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
class StickerController(private val stickers: StickerRepository, private val events : EventRepository) {


    @GetMapping("")
    fun all(): ResponseEntity<List<SimpleSticker>> {
        val allSticker = stickers.findAll()
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
        val savedSticker = Sticker(
                name = sticker.name,
                event = event,
                keywords = sticker.keywords,
                comment = sticker.comment,
                amount = sticker.amount
        ).saveIn(stickers)
        if(savedSticker?.id == null)
            return badRequest().build()
        return request
                .location("/v1/stickers/{ID}")
                .withParams(savedSticker.id)
                .created()
                .body(savedSticker.toSimple())
    }

}