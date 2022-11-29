package com.rest.presentationlayer.controller;


import com.rest.presentationlayer.model.response.FileUploadResponseMessage;
import com.rest.service.CityService;
import com.rest.service.FileService;
import com.rest.shared.dto.CityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    CityService cityService;

    @Autowired
    FileService fileService;


    // API "GET" retournant une liste des objets en format JSON
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE })
    public List<CityDto> getUsers(){
        return cityService.getCities();
    }

    // API "POST" offrant la possibilité d'ajouter une 1 objet à la base de données en le passant en paramètre.
    @PostMapping(path = "/newCity",produces = {MediaType.APPLICATION_JSON_VALUE })
    public CityDto addCity(@RequestBody CityDto cityDto){
        return cityService.addCity(cityDto);
    }

    // API "POST" offrant la possibilité d'ajouter une 1 objet à la base de données en générant des valeurs aléatoires
    @PostMapping(path = "/newRandomCity",produces = {MediaType.APPLICATION_JSON_VALUE })
    public CityDto addRandomCity(){
        return cityService.addRandomCity();
    }


    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            fileService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new FileUploadResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileUploadResponseMessage(message));
        }
    }
}
