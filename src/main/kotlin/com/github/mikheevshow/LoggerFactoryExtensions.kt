package com.github.mikheevshow

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun Logger.trace(throwable: Throwable, block: () -> String) {
    if (isTraceEnabled) {
        trace(block.invoke(), throwable)
    }
}

inline fun Logger.info(block: () -> String) {
    if (isInfoEnabled) {
        info(block.invoke())
    }
}