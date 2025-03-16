package com.packt.chaptersix.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsAPI {
    //定义一个名为 CatsAPI 的接口，用于声明网络请求方法。
    @GET("cats")
    suspend fun fetchCats(
        @Query("tag") tag: String,
    ): Response<List<Cat>>
    // @GET("cats"):
    //使用 @GET 注解指定 HTTP GET 请求的相对路径为 "cats"。
    //这意味着完整的 URL 将是 https://cataas.com/api/cats。
    //
    // suspend fun fetchCats( ... ): Response<List<Cat>>:
    //定义一个名为 fetchCats 的挂起函数，用于执行网络请求。
    //suspend 关键字表示这是一个 Kotlin 协程挂起函数，可以在协程中安全地调用。
    //Response<List<Cat>> 表示该函数返回一个 Response 对象，其中包含一个 List<Cat>，
    // 即猫咪数据列表。
    //@Query("tag") tag: String:
    //
    //使用 @Query 注解添加一个名为 "tag" 的查询参数。
    //tag: String 表示该参数的值是一个字符串。
    //这意味着请求的 URL 将类似于 https://cataas.com/api/cats?tag=someTag，
    // 其中 someTag 是传递给 tag 参数的值。
}