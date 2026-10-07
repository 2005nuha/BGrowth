package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.Business
import com.example.bgrowth.data.model.CreateBusinessRequest
import com.example.bgrowth.data.remote.BusinessApi
import com.example.bgrowth.data.remote.RetrofitClient

class BusinessRepository(
    private val businessApi: BusinessApi =
        RetrofitClient.businessApi
) {

    suspend fun getBusiness(): Business {
        return businessApi.getBusiness()
    }

    suspend fun createBusiness(
        name: String,
        businessType: String,
        currency: String,
        phone: String = "",
        address: String = ""
    ): Business {

        return businessApi.createBusiness(
            CreateBusinessRequest(
                name = name.trim(),
                businessType = businessType.trim(),
                currency = currency.trim(),
                phone = phone.trim(),
                address = address.trim()
            )
        )
    }
}