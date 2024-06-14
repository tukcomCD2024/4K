package com.example.front_end_android

import com.example.front_end_android.dataclass.AcceptFriendRequest
import com.example.front_end_android.dataclass.AcceptFriendResponse
import com.example.front_end_android.dataclass.AddFriendRequest
import com.example.front_end_android.dataclass.AddFriendResponse
import com.example.front_end_android.dataclass.DeleteRequest
import com.example.front_end_android.dataclass.DeleteResponse
import com.example.front_end_android.dataclass.FindMyFriendsRequest
import com.example.front_end_android.dataclass.FindMyFriendsResponse
import com.example.front_end_android.dataclass.FindRequest
import com.example.front_end_android.dataclass.FindResponse
import com.example.front_end_android.dataclass.FriendRequestListRequest
import com.example.front_end_android.dataclass.FriendRequestListResponse
import com.example.front_end_android.dataclass.LoginRequest
import com.example.front_end_android.dataclass.LoginResponse
import com.example.front_end_android.dataclass.RejectFriendRequest
import com.example.front_end_android.dataclass.RejectFriendResponse
import com.example.front_end_android.dataclass.SignUpRequest
import com.example.front_end_android.dataclass.SignUpResponse
import com.example.front_end_android.dataclass.UpdateRequest
import com.example.front_end_android.dataclass.UpdateResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {

    @POST("member/login")
    fun loginRetrofit(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("member/signup")
    fun signUpRetrofit(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("friendship/sendFriendRequest")
    fun sendFriendRequestRetrofit(@Body addFriendRequest: AddFriendRequest): Call<AddFriendResponse>

    @POST("friendship/findByFriendIdAndStatus")
    fun friendRequestListRetrofit(@Body friendRequestListRequest: FriendRequestListRequest): Call<List<FriendRequestListResponse>>

    @POST("friendship/acceptFriendRequestById")
    fun acceptFriendRequestRetrofit(@Body acceptFriendRequest: AcceptFriendRequest): Call<AcceptFriendResponse>

    @POST("friendship/rejectFriendRequestById")
    fun rejectFriendRequestRetrofit(@Body rejectFriendRequest: RejectFriendRequest): Call<RejectFriendResponse>

    @POST("friendship/findByUserIdAndStatusOrFriendIdAndStatus")
    fun findMyFriendsRetrofit(@Body findMyFriendsRequest: FindMyFriendsRequest): Call<List<FindMyFriendsResponse>>

    @POST("member/find")
    fun findRetrofit(@Body findRequest: FindRequest): Call<FindResponse>

    @POST("member/delete")
    fun deleteRetrofit(@Body deleteRequest: DeleteRequest): Call<DeleteResponse>

    @POST("member/update")
    fun updateRetrofit(@Body updateRequest: UpdateRequest): Call<UpdateResponse>

}