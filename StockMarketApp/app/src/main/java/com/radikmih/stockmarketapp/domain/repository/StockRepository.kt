package com.radikmih.stockmarketapp.domain.repository

import com.radikmih.stockmarketapp.domain.model.CompanyInfo
import com.radikmih.stockmarketapp.domain.model.CompanyListing
import com.radikmih.stockmarketapp.domain.model.IntraDayInfo
import com.radikmih.stockmarketapp.util.ResultUtil
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<ResultUtil<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): ResultUtil<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): ResultUtil<CompanyInfo>
}