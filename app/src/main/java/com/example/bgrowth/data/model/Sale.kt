
package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class Sale(
    val id: Int,

    val customer: Int?,

    @SerializedName("total_amount")
    val totalAmount: String,

    @SerializedName("paid_amount")
    val paidAmount: String,

    @SerializedName("remaining_amount")
    val remainingAmount: String,

    @SerializedName("payment_status")
    val paymentStatus: String,

    @SerializedName("sold_at")
    val soldAt: String,

    val items: List<SaleItem>
)

data class SaleItem(
    val id: Int,
    val product: Int,
    val quantity: Int,

    @SerializedName("unit_price")
    val unitPrice: String,

    val subtotal: String
)
