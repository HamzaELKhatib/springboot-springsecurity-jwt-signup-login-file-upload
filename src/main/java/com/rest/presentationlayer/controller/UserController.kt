package com.rest.presentationlayer.controller;

import com.rest.exceptions.UserServiceException;
import com.rest.presentationlayer.model.request.UserDetailsRequestModel;
import com.rest.presentationlayer.model.response.*;
import com.rest.service.UserService;
import com.rest.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    //Front requesting from backend
    // "produces" manages the response type
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    // http://localhost:8080/users/{id}
    public UserRest getUser(@PathVariable String id) {

        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);

        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    // @RequestBody allows createUser to read the body from the http request and convert that body to a java object
    // UserDetailsRequestModel is the class we're using to create a java object out of this body
    // UserDetailsRequestModel will have fields that match the request body
    // UserRest will be used a response model class
    //sending to the backend
    // "consumes" manages the request type, produces manages the response type
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {

        // The information to be returned
        UserRest returnValue = new UserRest();

        // Custom exception to be thrown if the first name is not provided
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());


        UserDto userDto = new UserDto();

        // Populating the dto with information received from the request body
        BeanUtils.copyProperties(userDetails, userDto);

        // Creating a new dto (createdUser) for the response and applying createUser method from userService to it
        // createUser is a method for creating elements that will be used to fill up the fields in the response(UserRest)
        UserDto createdUser = userService.createUser(userDto);

        // Populating returnValue with information from the createdUser Dto
        BeanUtils.copyProperties(createdUser, returnValue);


        return returnValue;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    //sending/updating
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {

        // The information to be returned
        UserRest returnValue = new UserRest();

        // Custom exception to be thrown if the first name is not provided
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());


        UserDto userDto = new UserDto();

        // Populating the dto with information received from the request body
        BeanUtils.copyProperties(userDetails, userDto);

        // Creating a new dto (createdUser) for the response and applying createUser method from userService to it
        // createUser is a method for creating elements that will be used to fill up the fields in the response(UserRest)
        UserDto updateUser = userService.updateUser(id, userDto);

        // Populating returnValue with information from the createdUser Dto
        BeanUtils.copyProperties(updateUser, returnValue);


        return returnValue;
    }

    @DeleteMapping(path = "/{id}") //delete
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();

        userService.deleteUser(id);

        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }
}
