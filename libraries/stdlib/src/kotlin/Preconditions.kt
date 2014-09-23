package kotlin

// TODO should not need this - its here for the JS stuff
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Throws an [IllegalArgumentException] with an optional *message* if the *value* is false.
 *
 * {code test.collections.PreconditionsTest.failingRequireWithMessage}
 */
public fun require(value: Boolean, message: Any = "Failed requirement"): Unit {
    if (!value) {
        throw IllegalArgumentException(message.toString())
    }
}

/**
 * Throws an [IllegalArgumentException] if the *value* is false and uses *lazyMessage* to calculate message text.
 *
 * {code test.collections.PreconditionsTest.failingRequireWithLazyMessage}
 */
public inline fun require(value: Boolean, lazyMessage: () -> String): Unit {
    if (!value) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    }
}

/**
 * Throws an [IllegalArgumentException] with the given *message* if the *value* is null.
 * Otherwise the not null value is returned.
 *
 * {code test.collections.PreconditionsTest.requireNotNull}
 */
public fun <T:Any> requireNotNull(value: T?, message: Any = "Required value was null"): T {
    if (value == null) {
        throw IllegalArgumentException(message.toString())
    } else {
        return value
    }
}

/**
 * Throws an [IllegalStateException] with an optional *message* if the *value* is false.
 *
 * {code test.collections.PreconditionsTest.failingCheckWithMessage}
 */
public fun check(value: Boolean, message: Any = "Check failed"): Unit {
    if (!value) {
        throw IllegalStateException(message.toString())
    }
}

/**
 * Throws an [IllegalStateException] if the *value* is false and uses *lazyMessage* to calculate message text.
 *
 * {code test.collections.PreconditionsTest.failingCheckWithLazyMessage}
 */
public inline fun check(value: Boolean, lazyMessage: () -> String): Unit {
    if (!value) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    }
}

/**
 * Throws an [IllegalStateException] with the given *message* if the *value* is null.
 * Otherwise the not null value is returned.
 *
 * {code test.collections.PreconditionsTest.checkNotNull}
 */
public fun <T:Any> checkNotNull(value: T?, message: String = "Required value was null"): T {
    if (value == null) {
        throw IllegalStateException(message)
    } else {
        return value
    }
}

/**
 * Throws an [RuntimeException] with the given *message*.
 */
public fun error(message: String): Nothing = throw RuntimeException(message)
