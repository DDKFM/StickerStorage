package de.ddkfm.SticketStorage.models

import com.fasterxml.jackson.annotation.JsonIgnore
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
@Table(name = "[Event]")
data class Event(
    @Column(length = 255, nullable = false)
    var name : String,
    @Column(name = "externalEvent")
    var isExternalEvent : Boolean = false,
    @Column(length = 255, nullable = true)
    var thumbnailUrl : String?
) :  AbstractPersistableEntity<Long>()