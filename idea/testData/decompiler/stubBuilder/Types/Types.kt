package test

abstract class Types {
    val nullable: Int? = null
    abstract val list: List<Int>
    abstract val map: Map<Int, Int>
    abstract val nullableMap: Map<Int?, Int?>?
    abstract val projections: Map<in Int, out String>
//    val function: () -> Unit = {}
}