package com.rest.presentationlayer.controller

import com.rest.exceptions.UserServiceException
import com.rest.presentationlayer.model.request.UserDetailsRequestModel
import com.rest.presentationlayer.model.response.*
import com.rest.service.UserService
import com.rest.shared.dto.UserDto
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("users")
class UserController {
    @Autowired
    var userService: UserService? = null

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable id: String?): UserRest {
        val returnValue = UserRest()

        val userDto = userService?.getUserByUserId(id) ?: throw UserServiceException("User service is not initialized")

        BeanUtils.copyProperties(userDto, returnValue)

        return returnValue
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun createUser(@RequestBody userDetails: UserDetailsRequestModel): UserRest {
        val returnValue = UserRest()

        val isFirstNameEmpty = userDetails.firstName?.isEmpty() ?: true

        if (isFirstNameEmpty) throw UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.errorMessage)


        val userDto = UserDto()

        BeanUtils.copyProperties(userDetails, userDto)

        val createdUser = userService?.createUser(userDto) ?: throw UserServiceException("User service is not initialized")

        BeanUtils.copyProperties(createdUser, returnValue)


        return returnValue
    }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun updateUser(@RequestBody userDetails: UserDetailsRequestModel, @PathVariable id: String?): UserRest {
        val returnValue = UserRest()

        val isFirstNameEmpty = userDetails.firstName?.isEmpty() ?: true

        if (isFirstNameEmpty) {
            throw UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.errorMessage)
        }

        val userDto = UserDto()

        BeanUtils.copyProperties(userDetails, userDto)

        val updateUser = userService?.updateUser(id, userDto) ?: throw UserServiceException("User service is not initialized")

        BeanUtils.copyProperties(updateUser, returnValue)

        return returnValue
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteUser(@PathVariable id: String?): OperationStatusModel {
        val returnValue = OperationStatusModel()

        userService?.deleteUser(id) ?: throw UserServiceException("User service is not initialized")

        returnValue.operationName = RequestOperationName.DELETE.name
        returnValue.operationResult = RequestOperationStatus.SUCCESS.name

        return returnValue
    }
}