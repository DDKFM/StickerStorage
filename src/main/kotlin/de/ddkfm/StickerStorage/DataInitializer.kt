package de.ddkfm.StickerStorage

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Location
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.LocationRepository
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

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val location = locations.findAll().firstOrNull()
        if(location == null) {
            val newLocation = Location("Test", "Test")
            locations.save(newLocation)
        }
        val clt2019 = events.findByName("Chemnitzer Linuxtage 2019").firstOrNull()
        if(clt2019 == null) {
            Event("Chemnitzer Linux Tage 2019", false, "")
                    .let { events.save(it) }
        }
        cacheEvents()

    }

    fun cacheEvents() {
        Unirest.get("https://media.ccc.de/a").asObjectAsync { resp ->
            val html = resp.contentAsString
            val document = Jsoup.parse(html)
            val contents = document.select(".thumbnail")
            contents.forEach { content ->
                val name = content.selectFirst(".caption").html()
                val imgUrl = content.selectFirst("img").attr("src")
                val externalEvent = Event(name, true, imgUrl)
                val existing = events.findByName(externalEvent.name).firstOrNull()
                if(existing == null) {
                    events.save(externalEvent)
                } else {
                    existing.apply {
                        isExternalEvent = true
                        thumbnailUrl = imgUrl
                    }.let { events.save(it) }
                }
            }
        }
    }
}