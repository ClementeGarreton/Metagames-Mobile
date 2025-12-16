// data/remote/api/AuthApi.kt
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    suspend fun login(
        @Body body: AuthRequest
    ): AuthResponse

    @POST("signup")
    suspend fun signup(
        @Body body: AuthRequest
    ): AuthResponse
}
