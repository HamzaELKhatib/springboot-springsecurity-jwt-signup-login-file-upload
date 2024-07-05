package com.rest.presentationlayer.model.request

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class UserDetailsRequestModel {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
}
