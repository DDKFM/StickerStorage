package de.ddkfm.StickerStorage.repository

import de.ddkfm.StickerStorage.models.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ImageRepository : JpaRepository<Image, Long> {
    @Transactional
    @Query("SELECT i FROM Image i WHERE i.event.id = ?1")
    fun findByEventId(id: Long) : List<Image>

    @Transactional
    @Query("SELECT i FROM Image i WHERE i.sticker.id = ?1")
    fun findByStickerId(id: Long) : List<Image>
}