package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

class TestClass {
    class object {
        fun `for`() { `for`() }

        fun test() {
            testNotRenamed("for", { ::`for` })
        }
    }
}

fun box(): String {
    TestClass.test()

    return "OK"
}