// !MARK_DYNAMIC_CALLS

fun test(d: dynamic) {
    val a = array(1, 2, 3)

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*d)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(1, "2", *a)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(1, *a) { }
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a) { "" }
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a, *a)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a, *a) { "" }
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a, 1, { "" }, *a)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a, 1)
    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(*a, *a, { "" })
}