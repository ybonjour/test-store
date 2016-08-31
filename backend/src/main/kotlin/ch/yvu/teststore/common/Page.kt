package ch.yvu.teststore.common

data class Page<T>(val results: List<T>, val nextPage: String?)