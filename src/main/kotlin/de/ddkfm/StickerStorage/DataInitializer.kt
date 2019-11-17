package de.ddkfm.StickerStorage

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Image
import de.ddkfm.StickerStorage.models.Location
import de.ddkfm.StickerStorage.repository.EventRepository
import de.ddkfm.StickerStorage.repository.ImageRepository
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

    @Autowired
    lateinit var images: ImageRepository

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val location = locations.findAll().firstOrNull()
        if(location == null) {
            val newLocation = Location("Test", "Test")
            locations.save(newLocation)
        }
        val clt2019 = events.findByName("Chemnitzer Linuxtage 2019").firstOrNull()
        if(clt2019 == null) {
            val saved = Event("Chemnitzer Linux Tage 2019", false)
                    .let { events.save(it) }
            Image(ByteArray(0), "", saved).let { images.save(it) }
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
                val imgData = Unirest.get(imgUrl).asString().body
                val externalEvent = Event(name, true)
                val existing = events.findByName(externalEvent.name).firstOrNull()
                if(existing == null) {
                    val savedEvent = events.save(externalEvent)
                    images.save(Image(imgData.toByteArray(), imgUrl, savedEvent))
                } else {
                    val savedEvent = existing.apply {
                        isExternalEvent = true
                    }.let { events.save(it) }
                    val existingImage = images.findByEventId(savedEvent.id ?: -1).firstOrNull()
                    if(existingImage == null) {
                        Image(imgData.toByteArray(), imgUrl, savedEvent).let { images.save(it) }
                    } else {
                        existingImage.apply {
                            this.imageData = imgData.toByteArray()
                            this.imageUrl = imgUrl
                        }.let { images.save(it) }
                    }
                }
            }
        }
    }
}