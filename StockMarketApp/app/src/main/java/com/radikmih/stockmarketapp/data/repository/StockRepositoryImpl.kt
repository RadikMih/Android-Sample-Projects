package com.radikmih.stockmarketapp.data.repository

import com.radikmih.stockmarketapp.data.csv.CsvParser
import com.radikmih.stockmarketapp.data.local.StockDataBase
import com.radikmih.stockmarketapp.data.mapper.toCompanyInfo
import com.radikmih.stockmarketapp.data.mapper.toCompanyListing
import com.radikmih.stockmarketapp.data.mapper.toCompanyListingEntity
import com.radikmih.stockmarketapp.data.remote.StockApi
import com.radikmih.stockmarketapp.domain.model.CompanyInfo
import com.radikmih.stockmarketapp.domain.model.CompanyListing
import com.radikmih.stockmarketapp.domain.model.IntraDayInfo
import com.radikmih.stockmarketapp.domain.repository.StockRepository
import com.radikmih.stockmarketapp.util.ResultUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    db: StockDataBase,
    private val companyListingsParser: CsvParser<CompanyListing>,
    private val intraDayInfoParser: CsvParser<IntraDayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<ResultUtil<List<CompanyListing>>> {
        return flow {
            emit(ResultUtil.Loading(true))

            val localListing = dao.searchCompanyListing(query)
            emit(ResultUtil.Success(data = localListing.map { it.toCompanyListing() }))

            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(ResultUtil.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings(StockApi.API_KEY)
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(ResultUtil.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(ResultUtil.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(ResultUtil.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(ResultUtil.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): ResultUtil<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            ResultUtil.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            ResultUtil.Error("Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            ResultUtil.Error("Couldn't load data")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): ResultUtil<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            ResultUtil.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            ResultUtil.Error("Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            ResultUtil.Error("Couldn't load data")
        }
    }
}