package com.example.android.airteldemo.network

import com.example.android.airteldemo.models.KeyAction
import com.google.gson.annotations.SerializedName

data class AllCheckpointsResponse(
    @SerializedName("status") val status: Int?,
    @SerializedName("info") val info: ArrayList<KeyAction>?
)