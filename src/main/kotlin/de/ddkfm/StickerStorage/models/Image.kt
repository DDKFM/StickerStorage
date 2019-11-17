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
    var event : Event?
) :  AbstractPersistableEntity<Long>() {
    fun toModel() : SimpleImage {
        return SimpleImage(imageData, imageUrl, event?.id)
    }
}


data class SimpleImage(
        var imageData : ByteArray,
        var imageUrl : String,
        var event : Long?
)