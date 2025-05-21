package me.yeahapps.mypetai.core.ui.utils.modifier

import androidx.compose.ui.Modifier

inline fun Modifier.then(
    block: Modifier.() -> Modifier
) = Modifier.then(block())

inline fun Modifier.thenIf(
    condition: Boolean,
    onTrue: Modifier.() -> Modifier
) = if (condition) {
    then(onTrue)
} else this

inline fun Modifier.thenIfNot(
    condition: Boolean,
    onFalse: Modifier.() -> Modifier
) = thenIf(!condition, onFalse)

inline fun <T> Modifier.thenIfNotNull(
    value: T?,
    onNotNull: Modifier.(value: T) -> Modifier
) = if (value != null) {
    Modifier.then(onNotNull(value))
} else this

inline fun <T> Modifier.thenIfNull(
    value: T?,
    onNull: Modifier.() -> Modifier
) = if (value == null) {
    Modifier.then(onNull())
} else this
