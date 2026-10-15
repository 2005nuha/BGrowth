package com.example.bgrowth.data.repository

import com.example.bgrowth.data.remote.CreateCustomerRequest
import com.example.bgrowth.data.remote.CustomerApi
import com.example.bgrowth.data.remote.CustomerDto
import com.example.bgrowth.data.remote.RetrofitClient

class CustomerRepository(
    private val api: CustomerApi = RetrofitClient.customerApi
) {
    suspend fun getCustomers(query: String? = null): Result<List<CustomerDto>> {
        return runCatching {
            api.getCustomers(query)
        }
    }

    suspend fun createCustomer(name: String, phone: String, address: String): Result<CustomerDto> {
        return runCatching {
            api.createCustomer(CreateCustomerRequest(name, phone, address))
        }
    }

    suspend fun getCustomerDetail(id: Int): Result<CustomerDto> {
        return runCatching {
            api.getCustomerDetail(id)
        }
    }
}