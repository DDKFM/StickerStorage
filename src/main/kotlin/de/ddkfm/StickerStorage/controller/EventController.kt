package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.SimpleEvent
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
@RequestMapping("/v1/events")
class EventController(private val events: EventRepository, private val images: ImageRepository) {


    @GetMapping("")
    fun all(): ResponseEntity<List<SimpleEvent>> {
        val start = System.currentTimeMillis()
        val allEvents = events.findAll().map { it.toModel() }
        println("${System.currentTimeMillis() - start} ms")
        return ok(allEvents)
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
                    .body("event already exists")
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
                ?: return badRequest().body("requested event does not exist")


        val newEvent = existingEvent.apply {
            this.name = event.name
        }.let { events.save(it) }
        return request
                .location("/v1/events/{id}")
                .withParams(newEvent.id)
                .created()
                .body(newEvent)
    }

}