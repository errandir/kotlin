val a: Int by <!DELEGATE_PD_METHOD_NONE_APPLICABLE!>Delegate()<!>

class Delegate {
    fun get(t: Any?, p: PropertyMetadata): Int {
        t.equals(p) // to avoid UNUSED_PARAMETER warning
        return 1
    }

    fun delegatedTo() {}

    fun delegatedTo(a: Int) {
        a.equals(a)
    }

    fun delegatedTo(a: String) {
        a.equals(a)
    }

    fun delegatedTo(p: PropertyMetadata, a: Int) {
        p.equals(a)
    }
}
