public inline fun <reified T> Array(n: Int, block: (Int) -> T): Array<T> = null!!
fun foo(i: Int) {
    Array(1, { 42 })[(i + i<caret>)]
}
