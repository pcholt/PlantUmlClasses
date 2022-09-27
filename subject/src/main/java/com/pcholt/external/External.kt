package com.pcholt.external

import com.pcholt.LinkField
import com.pcholt.MyAnnotation

@MyAnnotation
data class ExternalData(
    @LinkField(-1)
    val k: ExternalDataReferencec
)

@MyAnnotation
data class ExternalDataReferencec(
    val dddd: String
)