package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

class enum { class object {} }

fun box(): String {
    testNotRenamed("enum", { enum })

    return "OK"
}