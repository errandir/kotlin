package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

object TestObject {
    var `throw`: Int = 0

    fun test() {
        testNotRenamed("throw", { `throw` })
    }
}

fun box(): String {
    TestObject.test()

    return "OK"
}