package kotlin

import java.util.*

private object EmptyList : List<Any> {
    private val list = ArrayList<Any>()

    override fun contains(o: Any?): Boolean = list.contains(o)
    override fun containsAll(c: Collection<Any?>): Boolean = list.containsAll(c)
    override fun get(index: Int): Any = list.get(index)
    override fun indexOf(o: Any?): Int = list.indexOf(o)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun iterator(): Iterator<Any> = list.iterator()
    override fun lastIndexOf(o: Any?): Int = list.lastIndexOf(o)
    override fun listIterator(): ListIterator<Any> = list.listIterator()
    override fun listIterator(index: Int): ListIterator<Any> =list.listIterator(index)
    override fun size(): Int = list.size()
    override fun subList(fromIndex: Int, toIndex: Int): List<Any> = list.subList(fromIndex, toIndex)
    override fun equals(other: Any?): Boolean = list.equals(other)
    override fun hashCode(): Int = list.hashCode()
    override fun toString(): String = list.toString()
}

private object EmptyMap : Map<Any, Any> by HashMap<Any, Any>() {}

private object EmptySet : Set<Any> {
    private val set = HashSet<Any>()

    override fun contains(o: Any?): Boolean = set.contains(o)
    override fun containsAll(c: Collection<Any?>): Boolean = set.containsAll(c)
    override fun isEmpty(): Boolean = set.isEmpty()
    override fun iterator(): Iterator<Any> = set.iterator()
    override fun size(): Int = set.size()
    override fun equals(other: Any?): Boolean = set.equals(other)
    override fun hashCode(): Int = set.hashCode()
    override fun toString(): String = set.toString()
}

public fun emptyList<T>(): List<T> = EmptyList as List<T>
public fun emptyMap<K, V>(): Map<K, V> = EmptyMap as Map<K, V>
public fun emptySet<T>(): Set<T> = EmptySet as Set<T>

/** Returns a new read-only list of given elements */
public fun listOf<T>(vararg values: T): List<T> = if (values.size() == 0) emptyList() else arrayListOf(*values)

/** Returns an empty list */
public fun listOf<T>(): List<T> = emptyList()

/** Returns a new read-only map of given pairs, where the first value is the key, and the second is value */
public fun mapOf<K, V>(vararg values: Pair<K, V>): Map<K, V> = if (values.size() == 0) emptyMap() else linkedMapOf(*values)

/** Returns an empty read-only map */
public fun mapOf<K, V>(): Map<K, V> = emptyMap()

/** Returns a new read-only set of given elements */
public fun setOf<T>(vararg values: T): Set<T> = if (values.size() == 0) emptySet() else values.toCollection(LinkedHashSet<T>())

/** Returns a new LinkedList with a variable number of initial elements */
public fun linkedListOf<T>(vararg values: T): LinkedList<T> = values.toCollection(LinkedList<T>())

/** Returns a new ArrayList with a variable number of initial elements */
public fun arrayListOf<T>(vararg values: T): ArrayList<T> = values.toCollection(ArrayList(values.size()))

/** Returns a new HashSet with a variable number of initial elements */
public fun hashSetOf<T>(vararg values: T): HashSet<T> = values.toCollection(HashSet(values.size()))

/**
 * Returns a new [[HashMap]] populated with the given pairs where the first value in each pair
 * is the key and the second value is the value
 *
 * @includeFunctionBody ../../test/collections/MapTest.kt createUsingPairs
 */
public fun <K, V> hashMapOf(vararg values: Pair<K, V>): HashMap<K, V> {
    val answer = HashMap<K, V>(values.size())
    answer.putAll(*values)
    return answer
}

/**
 * Returns a new [[LinkedHashMap]] populated with the given pairs where the first value in each pair
 * is the key and the second value is the value. This map preserves insertion order so iterating through
 * the map's entries will be in the same order
 *
 * @includeFunctionBody ../../test/collections/MapTest.kt createLinkedMap
 */
public fun <K, V> linkedMapOf(vararg values: Pair<K, V>): LinkedHashMap<K, V> {
    val answer = LinkedHashMap<K, V>(values.size())
    answer.putAll(*values)
    return answer
}

public val Collection<*>.indices: IntRange
    get() = 0..size() - 1

public val Int.indices: IntRange
    get() = 0..this - 1

/**
 * Returns the index of the last item in the list or -1 if the list is empty
 *
 * @includeFunctionBody ../../test/collections/ListSpecificTest.kt lastIndex
 */
public val <T> List<T>.lastIndex: Int
    get() = this.size() - 1


/** Returns true if the collection is not empty */
public fun <T> Collection<T>.isNotEmpty(): Boolean = !this.isEmpty()

/** Returns true if this collection is not empty */
public val Collection<*>.notEmpty: Boolean
    get() = isNotEmpty()

/** Returns the Collection if its not null otherwise it returns the empty list */
public fun <T> Collection<T>?.orEmpty(): Collection<T> = this ?: emptyList()

// List APIs

/** Returns the List if its not null otherwise returns the empty list */
public fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()
