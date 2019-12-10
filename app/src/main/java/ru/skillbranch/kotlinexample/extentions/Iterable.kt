package ru.skillbranch.kotlinexample.extentions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    var last = this.size
    for ((i, item) in this.withIndex()) {
        if (predicate(item)) last = i
    }
    return this.subList(0, last)
}
