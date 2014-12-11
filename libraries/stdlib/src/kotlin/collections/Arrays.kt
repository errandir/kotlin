package kotlin

public inline fun <reified T> Array(n: Int, block: (Int) -> T): Array<T> {
    val result = arrayOfNulls<T>(n)

    for (i in result.indices) {
        result[i] = block(i)
    }

    return result as Array<T>
}

public val BooleanArray.lastIndex: Int
    get() = this.size - 1

public val ByteArray.lastIndex: Int
    get() = this.size - 1

public val ShortArray.lastIndex: Int
    get() = this.size - 1

public val IntArray.lastIndex: Int
    get() = this.size - 1

public val LongArray.lastIndex: Int
    get() = this.size - 1

public val FloatArray.lastIndex: Int
    get() = this.size - 1

public val DoubleArray.lastIndex: Int
    get() = this.size - 1

public val CharArray.lastIndex: Int
    get() = this.size - 1

public val Array<*>.lastIndex: Int
    get() = this.size - 1


public val BooleanArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val ByteArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val ShortArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val IntArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val LongArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val FloatArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val DoubleArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val CharArray.indices: IntRange
    get() = IntRange(0, this.size - 1)

public val Array<*>.indices: IntRange
    get() = IntRange(0, this.size - 1)
