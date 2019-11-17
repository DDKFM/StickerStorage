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
    var isExternalEvent : Boolean = false,
    @Column(length = 255, nullable = true)
    var thumbnailUrl : String?
) :  AbstractPersistableEntity<Long>()