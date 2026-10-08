package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.AddCustomerRequest
import com.example.bgrowth.data.model.CustomerResponse
import com.example.bgrowth.data.remote.RetrofitClient

class CustomerRepository {
    private val api = RetrofitClient.customerApi

    suspend fun getCustomers(): Result<List<CustomerResponse>> {
        return try {
            val response = api.getCustomers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطأ في جلب العملاء: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCustomerById(id: Int): Result<CustomerResponse> {
        return try {
            val response = api.getCustomerById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطأ في جلب تفاصيل العميل: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCustomer(request: AddCustomerRequest): Result<CustomerResponse> {
        return try {
            val response = api.addCustomer(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطأ في إضافة العميل: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}