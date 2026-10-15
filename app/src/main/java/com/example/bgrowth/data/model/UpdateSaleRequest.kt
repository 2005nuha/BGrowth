
package com.example.bgrowth.data.model

data class UpdateSaleRequest(
    val customer: Int? = null,
    val items: List<CreateSaleItemRequest>? = null
)
