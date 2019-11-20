package de.ddkfm.StickerStorage.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Sticker]")
data class Sticker(
    @Column(length = 255, nullable = false)
    var name : String,

    @OneToOne
    var event : Event,
    @Column
    var amount : Int,
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    var keywords : MutableList<String>,
    @Column
    var comment : String
) :  AbstractPersistableEntity<Long>() {
    fun toSimple() : SimpleSticker {
        return SimpleSticker(
                id = this.id,
                name = this.name,
                eventId = this.event.id,
                amount = this.amount,
                keywords = this.keywords,
                comment = this.comment
        )
    }
}

data class SimpleSticker(
        var id : Long?,
        var name : String,
        var eventId : Long?,
        var amount : Int,
        var keywords : MutableList<String>,
        var comment : String
)