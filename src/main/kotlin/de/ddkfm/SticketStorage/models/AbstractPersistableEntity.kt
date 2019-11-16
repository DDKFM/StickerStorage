package de.ddkfm.SticketStorage.models

import lombok.Getter
import lombok.Setter

import javax.persistence.*
import java.io.Serializable

@Setter
@Getter
@MappedSuperclass
abstract class AbstractPersistableEntity<ID> : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: ID? = null

    @Version
    val version: Long? = null
}