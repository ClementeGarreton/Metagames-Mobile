// data/repository/AuthRepository.kt
class AuthRepository(
    private val api: AuthApi,
    private val prefs: AuthPrefs
) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val res = api.login(AuthRequest(email, password))
            if (res.success && res.token != null) {
                prefs.saveUser(email, res.token)
                Result.success(res.token)
            } else {
                Result.failure(Exception(res.error ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, password: String): Result<String> {
        return try {
            val res = api.signup(AuthRequest(email, password))
            if (res.success && res.token != null) {
                prefs.saveUser(email, res.token)
                Result.success(res.token)
            } else {
                Result.failure(Exception(res.error ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
