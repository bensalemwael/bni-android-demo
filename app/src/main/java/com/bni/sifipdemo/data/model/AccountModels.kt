package com.bni.sifipdemo.data.model

data class BankAccount(
    val holder: String,
    val accountNumberMasked: String,
    val balanceXof: Long,
    val transactions: List<Transaction>,
)

data class Transaction(
    val id: String,
    val label: String,
    val date: String,
    val amountXof: Long, // negative = debit
)
