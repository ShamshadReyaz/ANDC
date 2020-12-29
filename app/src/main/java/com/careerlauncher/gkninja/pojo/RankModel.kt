package com.careerlauncher.gkninja.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RankModel(
    @field:Expose @field:SerializedName("rank") var rank: String,
    @field:Expose @field:SerializedName("imaId") var imaId: String,
    @field:Expose @field:SerializedName("name") var name: String,
    @field:Expose @field:SerializedName("points") var points: String,
    @field:Expose @field:SerializedName("type") var type: String,
    @field:Expose @field:SerializedName("status") var status: String
) : Serializable 