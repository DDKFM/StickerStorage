package de.ddkfm.SticketStorage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@Configuration
@EnableTransactionManagement
class SticketStorageApplication

fun main(args: Array<String>) {
	runApplication<SticketStorageApplication>(*args)
}
