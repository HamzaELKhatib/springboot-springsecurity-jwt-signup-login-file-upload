package com.rest.presentationlayer.model.response

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class OperationStatusModel {
    var operationResult: String? = null
    var operationName: String? = null
}