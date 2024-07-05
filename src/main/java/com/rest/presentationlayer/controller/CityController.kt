package com.rest.presentationlayer.controller

import com.rest.presentationlayer.model.response.FileUploadResponseMessage
import com.rest.service.CityService
import com.rest.service.FileService
import com.rest.shared.dto.CityDto
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/city")
class CityController @Autowired constructor(
    private val cityService: CityService,
    private val fileService: FileService
) {
    @get:GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @get:ApiImplicitParams(ApiImplicitParam(name = "authorization", value = "Bearer JWT Token", paramType = "header"))
    val users: List<CityDto>
        get() = cityService.cities

    @ApiImplicitParams(ApiImplicitParam(name = "authorization", value = "Bearer JWT Token", paramType = "header"))
    @PostMapping(path = ["/newCity"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addCity(@RequestBody cityDto: CityDto?): CityDto {
        return cityService.addCity(cityDto)
    }

    @ApiImplicitParams(ApiImplicitParam(name = "authorization", value = "Bearer JWT Token", paramType = "header"))
    @PostMapping(path = ["/newRandomCity"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addRandomCity(): CityDto {
        return cityService.addRandomCity()
    }

    @ApiImplicitParams(ApiImplicitParam(name = "authorization", value = "Bearer JWT Token", paramType = "header"))
    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileUploadResponseMessage> {
        var message: String
        try {
            fileService.save(file)

            message = "Uploaded the file successfully: " + file.originalFilename
            return ResponseEntity.status(HttpStatus.OK).body(FileUploadResponseMessage().apply { this.message = message })
        } catch (e: Exception) {
            message = "Could not upload the file: " + file.originalFilename + ". Error: " + e.message
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(FileUploadResponseMessage().apply { this.message = message })
        }
    }
}