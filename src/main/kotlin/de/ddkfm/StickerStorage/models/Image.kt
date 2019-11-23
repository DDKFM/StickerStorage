package de.ddkfm.StickerStorage.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.*

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Image]")
data class Image(
    @Lob
    @Column
    var imageData : ByteArray,
    @Column
    var imageUrl : String,
    @OneToOne
    var event : Event?,
    @OneToOne
    var sticker : Sticker?
) :  AbstractPersistableEntity<Long>()


data class ImageCreation(
        var imageUrl : String,
        var fetchBytedataFromUrl : Boolean,
        var imageData : ByteArray?
)
data class SimpleImage(
        var imageDataCallback : String,
        var imageUrl : String,
        var eventId : Long?,
        var stickerId : Long?,
        val type : String = when {
            eventId != null -> "event"
            stickerId != null -> "sticker"
            else -> "unknown"
        }
)