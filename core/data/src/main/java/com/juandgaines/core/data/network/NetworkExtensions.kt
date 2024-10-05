package com.juandgaines.core.data.network
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

inline fun <reified T> safeCall (execute: () -> Response<T>): Result<T, Network> {
    val response = try {
        execute()
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        return Result.Error(Network.NO_INTERNET)
    }catch (e: SerializationException){
        e.printStackTrace()
        return Result.Error(Network.SERIALIZATION)
    }
    catch (e: SocketTimeoutException){
        e.printStackTrace()
        return Result.Error(Network.REQUEST_TIMEOUT)
    }
    catch (e: Exception){
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(Network.UNKNOWN)
    }
    return responseToResult<T>(response)
}


inline fun <reified T> responseToResult(response: Response<T>): Result<T, Network> {
    return when (response.code()){
        in 200..299 -> {
            Result.Success(response.body() as T)
        }
        400 -> Result.Error(Network.BAD_REQUEST)
        401 -> Result.Error(Network.UNAUTHORIZED)
        403 -> Result.Error(Network.FORBIDDEN)
        409 -> Result.Error(Network.CONFLICT)
        413 -> Result.Error(Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(Network.SERVER_ERROR)
        else -> Result.Error(Network.UNKNOWN)
    }
}



