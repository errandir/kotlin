fun foo() {
    val aaa1 = 1
    val t<caret> = 2
    val aaa3 = 2
}

// INVOCATION_COUNT: 1
// EXIST: aaa1
