class Delegate {
    var inner = "OK"
    fun get(t: Any?, p: PropertyMetadata): String = inner

    private fun delegatedTo(p: PropertyMetadata) { inner = "fail" }
    fun delegatedTo() { inner = "fail" }
    fun delegatedTo(a: Int) { inner = "fail" }
    fun delegatedTo(a: String) { inner = "fail" }
    fun delegatedTo(p: PropertyMetadata, a: Int) { inner = "fail" }
    fun delegatedTo<T>(p: PropertyMetadata, s: String = "") { inner = "fail" }
}

val prop by Delegate()

fun box(): String {
    return prop
}
