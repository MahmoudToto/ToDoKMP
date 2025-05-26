package domain

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

sealed class RequestState<out T> {
    data object Idle: RequestState<Nothing>()
    data object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Error(val massage: String): RequestState<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

    fun getSuccessData() = (this as Success).data
    fun gerSuccessDataOrNull() : T? {
        return try {
            (this as Success).data
        }
        catch (e: Exception){
            null
        }
    }

    fun getErrorMassage() = (this as Error).massage
    fun getErrorMassageOrEmpty() : String {
        return try {
            (this as Error).massage
        }
        catch (e: Exception){
            ""
        }
    }

    @Composable
    fun DisplayResult(
        onIdle: (@Composable () -> Unit)? = null,
        onLoading: @Composable () -> Unit ,
        onSuccess: @Composable (T) -> Unit,
        onError: @Composable (String) -> Unit,
        transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
            fadeIn(tween(durationMillis = 300)) togetherWith
                    fadeOut(tween(durationMillis = 300))
        }
    ){
        AnimatedContent(
            targetState = this,
            transitionSpec = transitionSpec,
            label = "Animated State"
        ){ state ->
            when(state){
                is Idle ->{
                    onIdle?.invoke()
                }
                is Loading ->{
                    onLoading()
                }
                is Success ->{
                    onSuccess(state.getSuccessData())
            }
                is Error ->{
                    onError(state.getErrorMassage())
                }
            }

        }
    }

}