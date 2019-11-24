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
import kong.unirest.Unirest
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
            @RequestParam("name", defaultValue = "") name : String = ""): ResponseEntity<List<SimpleSticker>> {

        val allSticker = stickers.findWithParameters(event, location, name)
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

    @PutMapping("/{stickerId}")
    fun update(@PathVariable("stickerId") stickerId : Long, @RequestBody sticker : SimpleSticker, request: HttpServletRequest) : ResponseEntity<*> {
        val existingSticker = stickers.findById(stickerId).orElse(null)
                ?: return badRequest().body("with ID $stickerId could not be found")
        val event = sticker.eventId?.let(events::findById)?.orElse(null)
                ?: return badRequest().body("eventId ${sticker.eventId} is not valid")
        val location = sticker.locationId?.let(locations::findById)?.orElse(null)
                ?: return badRequest().body("locationId ${sticker.locationId} is not valid")
        val newSticker = existingSticker.apply {
            this.event = event
            this.location = location
            this.amount = sticker.amount
            this.comment = sticker.comment
            this.name = sticker.name
            this.keywords = sticker.keywords
        }.saveIn(stickers)
                ?: return badRequest().body("sticker could not be saved")
        return request
                .location("/v1/stickers/{ID}")
                .withParams(newSticker.id)
                .created()
                .body(newSticker.toSimple())
    }

    @PostMapping("/{stickerId}/image")
    fun createImage(
            @PathVariable("stickerId") id: Long,
            @RequestBody imageToCreate : ImageCreation, request : HttpServletRequest) : ResponseEntity<SimpleImage> {
        val sticker = stickers.findById(id).orElse(null)
                ?: return badRequest().build()
        try {
            //take the incoming bytedata if the flag is not set, otherwise fetch data from url
            val imageData = imageToCreate.takeIf { !it.fetchBytedataFromUrl }?.imageData
                    ?: Unirest.get(imageToCreate.imageUrl).asBytes().body
            val image = Image(imageData, imageToCreate.imageUrl, null, sticker).saveIn(images)
                    ?: return badRequest().build()
            val imageCallback = request
                    .location("/v1/images/{imageId}")
                    .withParams(image.id)
                    .toUri()
            return ok(SimpleImage(imageCallback.toString(), image.imageUrl, null, sticker.id))
        } catch (e: Exception) {
            e.printStackTrace()
            return badRequest().build()
        }
    }

    @PutMapping("/{stickerId}/image")
    fun updateImage(
            @PathVariable("stickerId") id: Long,
            @RequestBody imageToCreate : ImageCreation, request : HttpServletRequest) : ResponseEntity<SimpleImage> {
        val sticker = stickers.findById(id).orElse(null)
                ?: return badRequest().build()
        val existingImage = images.findByStickerId(id).firstOrNull()
                ?: return badRequest().build()
        try {
            //take the incoming bytedata if the flag is not set, otherwise fetch data from url
            val imageData = imageToCreate.takeIf { !it.fetchBytedataFromUrl }?.imageData
                    ?: Unirest.get(imageToCreate.imageUrl).asBytes().body
            val image = existingImage.apply {
                this.imageData = imageData
                this.imageUrl = imageToCreate.imageUrl
            }.saveIn(images)
                    ?: return badRequest().build()
            val imageCallback = request
                    .location("/v1/images/{imageId}")
                    .withParams(image.id)
                    .toUri()
            return ok(SimpleImage(imageCallback.toString(), image.imageUrl, null, sticker.id))
        } catch (e: Exception) {
            e.printStackTrace()
            return badRequest().build()
        }
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