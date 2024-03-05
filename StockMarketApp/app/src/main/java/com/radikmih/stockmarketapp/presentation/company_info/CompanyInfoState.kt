package com.radikmih.stockmarketapp.presentation.company_info

import com.radikmih.stockmarketapp.domain.model.CompanyInfo
import com.radikmih.stockmarketapp.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfos: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)