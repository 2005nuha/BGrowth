package com.example.bgrowth.data.repository

import com.example.bgrowth.data.remote.CreateDebtRequest
import com.example.bgrowth.data.remote.DebtApi
import com.example.bgrowth.data.remote.DebtDto
import com.example.bgrowth.data.remote.RecordDebtPaymentRequest
import com.example.bgrowth.data.remote.RetrofitClient

class DebtRepository(
    private val api: DebtApi = RetrofitClient.debtApi
) {
    suspend fun getDebts(debtType: String? = null, status: String? = null): Result<List<DebtDto>> {
        return runCatching {
            api.getDebts(debtType, status)
        }
    }

    suspend fun createDebt(
        debtType: String,
        partyName: String,
        amount: String,
        customerId: Int? = null,
        dueDate: String? = null,
        notes: String = ""
    ): Result<DebtDto> {
        return runCatching {
            api.createDebt(
                CreateDebtRequest(
                    debt_type = debtType,
                    party_name = partyName,
                    original_amount = amount,
                    customer = customerId,
                    due_date = dueDate,
                    notes = notes
                )
            )
        }
    }

    suspend fun getDebtDetail(id: Int): Result<DebtDto> {
        return runCatching {
            api.getDebtDetail(id)
        }
    }

    suspend fun addPayment(debtId: Int, amount: String, note: String = ""): Result<DebtDto> {
        return runCatching {
            api.addPayment(debtId, RecordDebtPaymentRequest(amount, note))
        }
    }
}