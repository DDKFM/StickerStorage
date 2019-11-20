package de.ddkfm.StickerStorage

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Image
import de.ddkfm.StickerStorage.models.Location
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.repository.LocationRepository
import de.ddkfm.StickerStorage.utils.saveIn
import kong.unirest.Unirest
import lombok.extern.slf4j.Slf4j
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Slf4j
class DataInitializer : CommandLineRunner {

    @Autowired
    lateinit var locations: LocationRepository

    @Autowired
    lateinit var events: EventRepository

    @Autowired
    lateinit var images: ImageRepository

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val location = locations.findAll().firstOrNull()
        if(location == null) {
            val newLocation = Location("Test", "Test")
            locations.save(newLocation)
        }
        cacheEvents()

    }

    fun cacheEvents() {
        Unirest.get("https://media.ccc.de/a").asObjectAsync { resp ->
            val html = resp.contentAsString
            val document = Jsoup.parse(html)
            val contents = document.select(".thumbnail")
            val dbEvents = this.events.findAll()
            contents.forEach { content ->
                val name = content.selectFirst(".caption").html()
                val imgUrl = content.selectFirst("img").attr("src")
                val imgData = Unirest.get(imgUrl).asString().body
                val externalEvent = Event(name, true)
                val existing = dbEvents.firstOrNull { it.name == externalEvent.name }
                if(existing == null) {
                    val newEvent = externalEvent.saveIn(events)
                    if(newEvent == null) {
                        println("event ${externalEvent.name} was not saved")
                        return@forEach
                    }
                    val newImage = Image(imgData.toByteArray(), imgUrl, newEvent).saveIn(images)
                    if(newImage == null) {
                        println("image for event ${externalEvent.name} was not saved")
                        return@forEach
                    }
                }
            }
        }
    }
}