// data/remote/dto/AuthDtos.kt
data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val error: String? = null
)
