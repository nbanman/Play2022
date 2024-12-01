package org.gristle.adventOfCode.utilities

interface Indexer<T> {
    fun getOrPut(value: T): Int
    operator fun get(value: T): Int?
    fun put(value: T): Int?
    fun valueOrNull(index: Int): T?
    fun removeByIndex(index: Int): T?
    fun removeByValue(value: T): Int?
}

class IndexerImpl<T>() : Indexer<T> {
    private var id = 0
    private val indexToValue = mutableMapOf<Int, T>()
    private val valueToIndex = mutableMapOf<T, Int>()
    override fun getOrPut(value: T): Int {
        val index = valueToIndex[value]
            ?: run {
                val valueId = id++
                valueToIndex[value] = valueId
                indexToValue[valueId] = value
                valueId
            }
        return index
    }

    override fun get(value: T): Int? = valueToIndex[value]

    override fun put(value: T): Int? {
        return if (valueToIndex.contains(value)) {
            null
        } else {
            val valueId = id++
            valueToIndex[value] = valueId
            indexToValue[valueId] = value
            valueId
        }
    }

    override fun valueOrNull(index: Int): T? = indexToValue[index]

    override fun removeByIndex(index: Int): T? = indexToValue
        .remove(index)
        ?.also { valueToIndex.remove(it) }

    override fun removeByValue(value: T): Int? = valueToIndex
        .remove(value)
        ?.also { indexToValue.remove(it)  }
}

fun <T> Indexer(): Indexer<T> = IndexerImpl<T>()