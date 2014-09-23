package kotlin

/**
 * Creates a tuple of type [Pair<A,B>] from *this* and *that*
 *
 * Can be useful for creating [Map] literals, for example:
 * {code test.collections.MapTest.createUsingTo}
 */
public fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

/**
 * Run function *f*
 */
public inline fun <T> run(f: () -> T): T = f()

/**
 * Execute *f* with the given *receiver*
 */
public inline fun <T, R> with(receiver: T, f: T.() -> R): R = receiver.f()

/**
 * Converts receiver to parameter of the given function *f*
 */
public inline fun <T : Any, R> T.let(f: (T) -> R): R = f(this)
