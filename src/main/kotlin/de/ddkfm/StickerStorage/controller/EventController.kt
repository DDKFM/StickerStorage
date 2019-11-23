package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.SimpleEvent
import de.ddkfm.StickerStorage.models.SimpleImage
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.utils.created
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.withParams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
    fun all(@RequestParam("query", defaultValue = "") query : String = ""): ResponseEntity<List<SimpleEvent>> {
        val allEvents = when {
            query.isNotEmpty() -> events.findByNameIgnoreCaseContaining(query)
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

}