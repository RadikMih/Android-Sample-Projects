package com.radikmih.stockmarketapp.data.mapper

import com.radikmih.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.radikmih.stockmarketapp.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {

    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)

    return IntraDayInfo(
        date = localDateTime,
        close = close
    )
}