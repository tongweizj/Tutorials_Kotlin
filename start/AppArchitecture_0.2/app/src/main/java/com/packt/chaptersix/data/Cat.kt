package com.packt.chaptersix.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("id")
    val id: String,
    @SerialName("owner")
    val owner: String = "",
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("updatedAt")
    val updatedAt: String = ""
)
//    {
//        "id": "05Xd4JtN14983pns",
//        "tags": [
//            "Cute"
//        ],
//        "mimetype": "image/jpeg",
//        "createdAt": "2024-05-27T17:55:08.552Z"
//    },
// @Serializable:
// 这是 Kotlin Serialization 库的注解，
// 表示 Cat 类可以被序列化和反序列化。
// 这使得可以使用 Kotlin Serialization 库将 Cat 对象转换为 JSON 字符串，
// 或将 JSON 字符串转换为 Cat 对象。