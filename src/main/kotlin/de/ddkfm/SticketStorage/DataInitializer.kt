package de.ddkfm.SticketStorage

import de.ddkfm.SticketStorage.models.Location
import de.ddkfm.SticketStorage.repository.LocationRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Slf4j
class DataInitializer : CommandLineRunner {

    @Autowired
    lateinit var locations: LocationRepository


    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val location = locations.findAll().firstOrNull()
        if(location == null) {
            val newLocation = Location("Test", "Test")
            locations.save(newLocation)
        }
    }
}