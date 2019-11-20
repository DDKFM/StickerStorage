package de.ddkfm.StickerStorage.repository

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Sticker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.name = ?1")
    fun findByName(name : String) : List<Event>


    fun findByNameIgnoreCaseContaining(name : String) : List<Event>
}