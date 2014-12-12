package multilineCall

//KT-3080 Debugger doesn't stop on a multi-line static function call
fun main(args: Array<String>) {
    //Breakpoint!
    var x = 1
    foo(
            1 + 1
    )
}

fun foo(a: Int) {}

// STEP_INTO: 3