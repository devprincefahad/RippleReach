package dev.prince.ripplereach.data

import com.google.gson.annotations.SerializedName

data class CompanyList(val companies: List<String>)

data class UniversityList(
    @SerializedName("universities") val universities: List<University>?
)

data class University(
    val rank: Int,
    val name: String,
    val city: String
)