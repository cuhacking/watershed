package com.cuhacking.watershed.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineDispatchers {
    val main: CoroutineDispatcher = Dispatchers.Main
    val io: CoroutineDispatcher = Dispatchers.IO
    val computation: CoroutineDispatcher = Dispatchers.Default
}