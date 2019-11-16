package de.ddkfm.SticketStorage.controller

import de.ddkfm.SticketStorage.models.Location
import de.ddkfm.SticketStorage.models.LocationNotFoundException
import de.ddkfm.SticketStorage.repository.LocationRepository
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
@RequestMapping("/v1/locations")
class LocationController(private val locations: LocationRepository) {


    @GetMapping("")
    fun all(): ResponseEntity<List<Location>> {
        return ok(this.locations.findAll())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Location> {
        println(id)
        val location = this.locations.findById(id)
                .orElse(null)
                ?: return notFound().build()
        return ok(location)
    }

    @PostMapping("")
    fun create(@RequestBody location: Location, request: HttpServletRequest): ResponseEntity<Location> {
        val saved = this.locations.save(location)
        return created(
                ServletUriComponentsBuilder
                        .fromContextPath(request)
                        .path("/v1/location/{id}")
                        .buildAndExpand(saved.id)
                        .toUri())
                .build()
    }

}