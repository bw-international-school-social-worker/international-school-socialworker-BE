package com.intworkers.application.controller.image

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/image")
class ImageController {

    companion object {
        val cloudinary = Cloudinary(ObjectUtils.asMap(
                "cloud_name", System.getenv("CLOUDINARY_NAME"),
                "api_key", System.getenv("CLOUDINARY_API_KEY"),
                "api_secret", System.getenv("CLOUDINARY_API_SECRET")))
    }

    @ApiOperation(value = "Uploads an image and returns url")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Image uploaded", response = Map::class)
    ])
    @PostMapping(value = ["/upload"], produces = ["application/json"])
    fun uploadImage(@ApiParam(value = "Image to be uploaded", required = true)
                    @RequestParam("image") image: MultipartFile): ResponseEntity<*> {
        val uploadResult = cloudinary.uploader().upload(image.bytes, ObjectUtils.emptyMap())
        return ResponseEntity(uploadResult, HttpStatus.OK)
    }
}