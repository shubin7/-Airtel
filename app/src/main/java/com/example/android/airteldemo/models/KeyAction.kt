package com.example.android.airteldemo.models

import com.google.gson.annotations.SerializedName

data class KeyAction(
    @SerializedName("hasInputs") val hasInputs: Boolean?,
    @SerializedName("inputs") val inputs: ArrayList<*>?,
    @SerializedName("numSeconds") val numSeconds: Int?,
    @SerializedName("_id") val _id: String?,
    @SerializedName("action") val action: String?,
    @SerializedName("company") val company: String?,
    @SerializedName("keyCode") val keyCode: String?,
    @SerializedName("__v") val __v: Int?
)