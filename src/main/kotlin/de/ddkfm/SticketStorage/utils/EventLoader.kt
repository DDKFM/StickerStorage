package de.ddkfm.SticketStorage.utils

import de.ddkfm.SticketStorage.models.MediaCCCEvent
import kong.unirest.Unirest
import org.jsoup.Jsoup

object EventLoader {
    val events : List<MediaCCCEvent> by lazy {
        val future = Unirest.get("https://media.ccc.de/a").asObjectAsync {resp ->
            val html = resp.contentAsString
            val document = Jsoup.parse(html)
            val contents = document.select(".thumbnail")
            contents.map { content ->
                val name = content.selectFirst(".caption").html()
                val imgUrl = content.selectFirst("img").attr("src")
                val imgData = Unirest.get(imgUrl).asString().body
                MediaCCCEvent(name, imgUrl, imgData)
            }
        }
        return@lazy future.get().body ?: emptyList()
    }
}