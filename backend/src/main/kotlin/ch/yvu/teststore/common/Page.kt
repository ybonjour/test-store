package ch.yvu.teststore.common

data class Page<T>(val results: List<T>, val nextPage: String?) {

    fun <R> map(f: (T) -> R): Page<R> {
        return Page(results.map(f), nextPage)
    }
}