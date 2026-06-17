package br.edu.ifsp.scl.sc303769x.postviewer.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)
