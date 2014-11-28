import kotlin.jvm.*
import kotlin.platform.*

class WithNative {
    class object {
        platformStatic native fun bar(l: Long, s: String): Double = null!!
    }
}

object ObjWithNative {
    platformStatic native fun bar(l: Long, s: String): Double = null!!
}

fun box(): String {
    var d = 0.0
    try {
        d = WithNative.bar(1, "")
        return "Link error expected"
    }
    catch (e: java.lang.UnsatisfiedLinkError) {
        if (e.getMessage() != "WithNative.bar(JLjava/lang/String;)D") return "Fail 1: " + e.getMessage()
    }

    try {
        d = ObjWithNative.bar(1, "")
        return "Link error expected on object"
    }
    catch (e: java.lang.UnsatisfiedLinkError) {
        if (e.getMessage() != "ObjWithNative.bar(JLjava/lang/String;)D") return "Fail 2: " + e.getMessage()
    }
    return "OK"
}