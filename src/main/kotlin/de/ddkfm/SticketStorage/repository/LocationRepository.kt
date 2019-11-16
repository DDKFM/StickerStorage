package de.ddkfm.SticketStorage.repository

import de.ddkfm.SticketStorage.models.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long>