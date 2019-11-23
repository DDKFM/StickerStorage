package de.ddkfm.StickerStorage.repository

import de.ddkfm.StickerStorage.models.Event
import de.ddkfm.StickerStorage.models.Sticker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StickerRepository : JpaRepository<Sticker, Long> {

    fun findByNameIgnoreCaseContaining(name : String) : List<Sticker>

    fun findByLocationNameIgnoreCaseContaining(name : String) : List<Sticker>

    fun findByEventNameIgnoreCaseContaining(name : String) : List<Sticker>


    fun findByEventNameIgnoreCaseContainingAndLocationNameIgnoreCaseContainingAndNameIgnoreCaseContaining(event : String, location : String, name : String) : List<Sticker>
    @JvmDefault
    fun findWithParameters(event : String, location : String, name : String) : List<Sticker> {
        return findByEventNameIgnoreCaseContainingAndLocationNameIgnoreCaseContainingAndNameIgnoreCaseContaining(event, location, name)
    }

    fun findByName(name : String) : List<Sticker>
}