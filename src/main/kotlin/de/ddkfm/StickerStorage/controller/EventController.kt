package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.*
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.utils.created
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.saveIn
import de.ddkfm.StickerStorage.utils.withParams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kong.unirest.Unirest
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/events")
@Tag(name = "events")
@SecurityRequirement(name = "api")
class EventController(private val events: EventRepository, private val images: ImageRepository) {


    @GetMapping("")
    fun all(@RequestParam("name", defaultValue = "") name : String = ""): ResponseEntity<List<SimpleEvent>> {
        val allEvents = when {
            name.isNotEmpty() -> events.findByNameIgnoreCaseContaining(name)
            else -> events.findAll()
        }
        return ok(allEvents.map { it.toModel() })
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<SimpleEvent> {
        val event = events.findById(id)
                .orElse(null)
                ?: return notFound().build()
        return ok(event.toModel())
    }

    @PostMapping("")
    fun create(@RequestBody event : SimpleEvent, request : HttpServletRequest) : ResponseEntity<*> {
        val existingEvent = events.findByName(event.name)
                .firstOrNull()
        if(existingEvent != null)
            return badRequest()
                    .body("eventId already exists")
        val saved = events.save(Event(event.name, event.isExternalEvent))
        return request
                .location("/v1/events/{ID}")
                .withParams(saved.id)
                .created()
                .body(saved)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id : Long,
               @RequestBody event : Event, request: HttpServletRequest) : ResponseEntity<*> {
        val existingEvent = events.findById(id).orElse(null)
                ?: return badRequest().body("requested eventId does not exist")


        val newEvent = existingEvent.apply {
            this.name = event.name
        }.let { events.save(it) }
        return request
                .location("/v1/events/{id}")
                .withParams(newEvent.id)
                .created()
                .body(newEvent)
    }

    @GetMapping("/{eventId}/image")
    fun getImage(@PathVariable("eventId") id: Long, request : HttpServletRequest): ResponseEntity<SimpleImage> {
        val image = images.findByEventId(id)
                .firstOrNull()
                ?: return notFound().build()
        val imageCallback = request
                .location("/v1/images/{imageId}")
                .withParams(image.id)
                .toUriString()
        return ok(SimpleImage(imageCallback, image.imageUrl, image.event?.id, null))
    }

    @PostMapping("/{eventId}/image")
    fun createImage(
            @PathVariable("eventId") id: Long,
            @RequestBody imageToCreate : ImageCreation, request : HttpServletRequest) : ResponseEntity<SimpleImage> {
        val event = events.findById(id).orElse(null)
                ?: return badRequest().build()
        try {
            //take the incoming bytedata if the flag is not set, otherwise fetch data from url
            val imageData = imageToCreate.takeIf { !it.fetchBytedataFromUrl }?.imageData
                    ?: Unirest.get(imageToCreate.imageUrl).asBytes().body
            val image = Image(imageData, imageToCreate.imageUrl, event, null).saveIn(images)
                    ?: return badRequest().build()
            val imageCallback = request
                    .location("/v1/images/{imageId}")
                    .withParams(image.id)
                    .toUri()
            return ok(SimpleImage(imageCallback.toString(), image.imageUrl, event.id, null))
        } catch (e: Exception) {
            e.printStackTrace()
            return badRequest().build()
        }
    }

    @PutMapping("/{eventId}/image")
    fun updateImage(
            @PathVariable("eventId") id: Long,
            @RequestBody imageToCreate : ImageCreation, request : HttpServletRequest) : ResponseEntity<SimpleImage> {
        val event = events.findById(id).orElse(null)
                ?: return badRequest().build()
        val existingImage = images.findByEventId(id).firstOrNull()
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
            return ok(SimpleImage(imageCallback.toString(), image.imageUrl, event.id, null))
        } catch (e: Exception) {
            e.printStackTrace()
            return badRequest().build()
        }
    }

}