package hse_github_research.core.ext

inline fun <T> Iterable<T>.doActionWithIterableCount(action: (Int) -> Unit): Iterable<T> {
    action(count())
    return this
}
