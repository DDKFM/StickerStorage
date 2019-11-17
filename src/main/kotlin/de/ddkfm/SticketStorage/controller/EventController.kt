package de.ddkfm.SticketStorage.controller

import de.ddkfm.SticketStorage.models.Event
import de.ddkfm.SticketStorage.repository.EventRepository
import de.ddkfm.SticketStorage.utils.created
import de.ddkfm.SticketStorage.utils.location
import de.ddkfm.SticketStorage.utils.withParams
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/events")
class EventController(private val events: EventRepository) {


    @GetMapping("")
    fun all(): ResponseEntity<List<Event>> {
        val start = System.currentTimeMillis()
        val allEvents = events.findAll()
        println("${System.currentTimeMillis() - start} ms")
        return ok(allEvents)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Event> {
        val event = events.findById(id)
                .orElse(null)
                ?: return notFound().build()
        return ok(event)
    }

    @PostMapping("")
    fun create(@RequestBody event : Event, request : HttpServletRequest) : ResponseEntity<*> {
        val existingEvent = events.findByName(event.name)
                .firstOrNull()
        if(existingEvent != null)
            return badRequest()
                    .body("event already exists")
        val saved = events.save(event)
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