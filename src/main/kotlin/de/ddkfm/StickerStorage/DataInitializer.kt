package de.ddkfm.StickerStorage

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Image
import de.ddkfm.StickerStorage.models.Location
import de.ddkfm.StickerStorage.models.Sticker
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
import de.ddkfm.StickerStorage.repository.LocationRepository
import de.ddkfm.StickerStorage.repository.StickerRepository
import de.ddkfm.StickerStorage.utils.saveIn
import kong.unirest.Unirest
import lombok.extern.slf4j.Slf4j
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
@Slf4j
class DataInitializer : CommandLineRunner {

    @Autowired
    lateinit var locations: LocationRepository

    @Autowired
    lateinit var events: EventRepository

    @Autowired
    lateinit var images: ImageRepository

    @Autowired
    lateinit var stickers : StickerRepository

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        cacheEvents()
        val location = locations
                .findByName("Test").firstOrNull()
                ?: Location("Test", "Test").saveIn(locations)
                ?: return
        val event = events
                .findByName("Chemnitzer Linux Tage 2019").firstOrNull()
                ?: Event("Chemnitzer Linux Tage 2019", false).saveIn(events)
                ?: return

        val sticker = stickers
                .findByName("Test Sticker").firstOrNull()
                ?: Sticker("Test Sticker", event, 42, mutableListOf("Test"), "Test", location).saveIn(stickers)
        val allEvents = events.findAll()
        for(i in 0 until 100) {
            stickers
                    .findByName("Test Sticker ${UUID.randomUUID()}")
                    .ifEmpty { Sticker("Test Sticker ${UUID.randomUUID()}", allEvents.random(), 42, mutableListOf("Test"), "Test", location).saveIn(stickers) }
        }

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
                val imgData = Unirest.get(imgUrl).asBytes().body
                val externalEvent = Event(name, true)
                val existing = dbEvents.firstOrNull { it.name == externalEvent.name }
                if(existing == null) {
                    val newEvent = externalEvent.saveIn(events)
                    if(newEvent == null) {
                        println("eventId ${externalEvent.name} was not saved")
                        return@forEach
                    }
                    val newImage = Image(imgData, imgUrl, newEvent, null).saveIn(images)
                    if(newImage == null) {
                        println("image for eventId ${externalEvent.name} was not saved")
                        return@forEach
                    }
                }
            }
        }.get()
    }
}