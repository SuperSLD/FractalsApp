package online.jutter.ui.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun createHandler(block: (Throwable)->Unit) = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
    block(throwable)
}

fun createEmptyHandler() = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

fun CoroutineScope.launchUI(callback: suspend () -> Unit) = launch(Dispatchers.Main) { callback() }

fun CoroutineScope.launchUI(handler: CoroutineExceptionHandler, callback: suspend () -> Unit) =
    launch(Dispatchers.Main + handler) { callback() }

fun CoroutineScope.launchIO(callback: suspend () -> Unit) = launch(Dispatchers.IO) { callback() }

fun CoroutineScope.launchIO(handler: CoroutineExceptionHandler, callback: suspend () -> Unit) =
    launch(Dispatchers.IO + handler) { callback() }

suspend fun <T> withIO(callback: suspend () -> T) = withContext(Dispatchers.IO) { callback() }

suspend fun <T> withUI(callback: suspend () -> T) = withContext(Dispatchers.Main) { callback() }

fun <T> CoroutineScope.asyncIO(callback: suspend () -> T) = async(Dispatchers.IO) { callback() }
