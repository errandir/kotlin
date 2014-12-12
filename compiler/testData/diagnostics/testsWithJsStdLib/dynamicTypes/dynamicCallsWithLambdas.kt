// !MARK_DYNAMIC_CALLS

fun test(d: dynamic) {
    d.<!DEBUG_INFO_DYNAMIC!>foo<!> {}

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { <!UNRESOLVED_REFERENCE!>it<!> }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { x -> }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { (x: Int) -> "" }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { x, y -> "" }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { (x: String, y: Int) -> "" }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { (x, y: Int) -> "" }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { (x: String, y: Int): Int -> <!TYPE_MISMATCH!>""<!> }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!> { String.(x: String, y: Int): Int -> length() }

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>({})

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>({ x -> })

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>({ x -> } : (Int) -> Unit)

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(@label { x -> })

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(@label ({ x, y -> }))

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>((@label ({ (x, y: Int) -> })))

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>(({ x -> }))

    d.<!DEBUG_INFO_DYNAMIC!>foo<!>((({ x -> })))
}
