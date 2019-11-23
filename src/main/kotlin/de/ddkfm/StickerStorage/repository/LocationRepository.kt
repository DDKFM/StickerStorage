package de.ddkfm.StickerStorage.repository

import de.ddkfm.StickerStorage.models.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long> {
    fun findByName(name : String) : List<Location>
}