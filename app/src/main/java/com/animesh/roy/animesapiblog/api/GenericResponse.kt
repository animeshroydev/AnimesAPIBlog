package com.animesh.roy.animesapiblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericResponse(

    @SerializedName("response")
    @Expose
    var response: String?
)