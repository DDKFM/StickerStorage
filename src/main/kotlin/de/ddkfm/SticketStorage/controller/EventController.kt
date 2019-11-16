package de.ddkfm.SticketStorage.controller

import de.ddkfm.SticketStorage.models.Location
import de.ddkfm.SticketStorage.models.LocationNotFoundException
import de.ddkfm.SticketStorage.models.MediaCCCEvent
import de.ddkfm.SticketStorage.repository.LocationRepository
import de.ddkfm.SticketStorage.utils.EventLoader
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping



@RestController
@RequestMapping("/v1/events")
class EventController {


    @GetMapping("")
    fun all(): ResponseEntity<List<MediaCCCEvent>> {
        return ok(EventLoader.events.map { it.copy(thumbnailContent = "") })
    }

    @GetMapping("/{name}")
    fun get(@PathVariable("id") name: String): ResponseEntity<MediaCCCEvent> {
        val event = EventLoader.events.firstOrNull { it.name == name }
                ?: return notFound().build()
        return ok(event)
    }

}