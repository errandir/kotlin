package kotlin

import java.io.PrintWriter
import java.io.PrintStream

/**
 * Allows a stack trace to be printed from [Throwable] into specified [PrintWriter].
 * $$reciever: [Throwable] to print stack trace from.
 * $writer: [PrintWriter] to send textual representation of a stack trace to.
 */
public fun Throwable.printStackTrace(writer: PrintWriter): Unit {
    val jlt = this as java.lang.Throwable
    jlt.printStackTrace(writer)
}

/**
 * Allows a stack trace to be printed from [Throwable] into specified [PrintStream]
 * $$reciever: [Throwable] to print stack trace from.
 * $stream: [PrintStream] to send textual representation of a stack trace to.
 */
public fun Throwable.printStackTrace(stream: PrintStream): Unit {
    val jlt = this as java.lang.Throwable
    jlt.printStackTrace(stream)
}

/**
 * Gets a stack trace from the [Throwable].
 * $$reciever: [Throwable] to get stack trace from.
 * $$return: array of [StackTraceElement] items representing stack trace.
 */
public fun Throwable.getStackTrace(): Array<StackTraceElement> {
    val jlt = this as java.lang.Throwable
    return jlt.getStackTrace()!!
}

