package de.ddkfm.StickerStorage.utils

import de.ddkfm.StickerStorage.models.AbstractPersistableEntity
import org.springframework.data.jpa.repository.JpaRepository

fun <T : AbstractPersistableEntity<Long>> T.saveIn(repository : JpaRepository<T, Long>) : T? {
    return repository.save(this)
}