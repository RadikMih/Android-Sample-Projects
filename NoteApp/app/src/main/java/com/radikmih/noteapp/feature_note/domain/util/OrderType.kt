package com.radikmih.noteapp.feature_note.domain.util

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}