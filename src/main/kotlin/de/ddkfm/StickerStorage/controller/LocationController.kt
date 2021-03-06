package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.models.Location
import de.ddkfm.StickerStorage.repository.LocationRepository
import de.ddkfm.StickerStorage.utils.created
import de.ddkfm.StickerStorage.utils.location
import de.ddkfm.StickerStorage.utils.saveIn
import de.ddkfm.StickerStorage.utils.withParams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/locations")
@SecurityRequirement(name = "api")
@Tag(name = "locations")
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
        return request
                .location("/v1/locations/{id}")
                .withParams(saved.id)
                .created()
                .body(saved)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id : Long, @RequestBody location : Location, request: HttpServletRequest) : ResponseEntity<Location> {
        val existing = locations.findById(id).orElseGet(null)
                ?: return notFound().build()
        val newLocation = existing.apply {
            this.name = location.name
            this.comment = location.comment
        }.saveIn(locations)
        return request
                .location("/v1/locations/{id}")
                .withParams(newLocation?.id)
                .created()
                .body(newLocation)
    }


}