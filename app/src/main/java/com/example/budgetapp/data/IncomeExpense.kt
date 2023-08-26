package com.example.budgetapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeExpense(
    val docId: String?,
    val title: String?,
    val price: Double?,
    var incomeExpenseType: Boolean,
    val category:String?=null



    ):Parcelable
