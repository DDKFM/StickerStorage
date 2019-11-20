package de.ddkfm.StickerStorage.controller

import de.ddkfm.StickerStorage.repository.ImageRepository
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/v1/images")
@SecurityRequirement(name = "api")
@Tag(name = "images")
class ImageController(private val images: ImageRepository) {

    @GetMapping("/{imageId}")
    fun getImageData(@PathVariable("imageId") imageId : Long,
                     response: HttpServletResponse): ResponseEntity<ByteArray> {
        val image = images.findById(imageId).orElse(null)
                ?: return notFound().build()
        response.setHeader("Content-Disposition", "attachment; filename=\"${image.imageUrl.substringAfterLast("/")}\"")
        return ok(image.imageData)
    }

}