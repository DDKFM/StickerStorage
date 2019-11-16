package de.ddkfm.SticketStorage.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable
import javax.persistence.*

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Location]")
data class Location(
    @Column(length = 255)
    var name : String,
    @Column(length = 255)
    var comment : String
) :  AbstractPersistableEntity<Long>()

class LocationNotFoundException : Exception()