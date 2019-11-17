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
@Table(name = "[Event]")
data class Event(
    @Column(length = 255, nullable = false)
    var name : String,
    @Column(name = "externalEvent")
    var isExternalEvent : Boolean = false
) :  AbstractPersistableEntity<Long>() {
    fun toModel() : SimpleEvent {
        return SimpleEvent(id!!, name, isExternalEvent)
    }
}



data class SimpleEvent(
        var id : Long,
        var name : String,
        var isExternalEvent: Boolean
)