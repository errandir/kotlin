package q

import kotlin.platform.platformStatic

object Foo {
    platformStatic fun main(s: Array<String>) {
        println("Object")
    }
}

class Bar {
    class object {
        platformStatic fun main(s: Array<String>) {
            println("Class")
        }
    }
}

class Baz {
    fun main(s: Array<String>) {
        println("???")
    }
}