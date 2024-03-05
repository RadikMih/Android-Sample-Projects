package com.radikmih.stockmarketapp.di

import com.radikmih.stockmarketapp.data.csv.CompanyListingParser
import com.radikmih.stockmarketapp.data.csv.CsvParser
import com.radikmih.stockmarketapp.data.csv.IntraDayInfoParser
import com.radikmih.stockmarketapp.data.repository.StockRepositoryImpl
import com.radikmih.stockmarketapp.domain.model.CompanyListing
import com.radikmih.stockmarketapp.domain.model.IntraDayInfo
import com.radikmih.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser: CompanyListingParser
    ): CsvParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CsvParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}