package de.ddkfm.StickerStorage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@Configuration
@EnableTransactionManagement
class SticketStorageApplication

fun main(args: Array<String>) {
	runApplication<SticketStorageApplication>(*args)
}
