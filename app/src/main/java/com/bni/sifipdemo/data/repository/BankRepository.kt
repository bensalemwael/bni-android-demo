package com.bni.sifipdemo.data.repository

import com.bni.sifipdemo.data.model.BankAccount
import com.bni.sifipdemo.data.model.Transaction

/**
 * Read-only demo data for the dashboard. Numbers are in XOF (FCFA),
 * la devise officielle de la Côte d'Ivoire.
 */
class BankRepository {

    fun loadAccount(): BankAccount = BankAccount(
        holder = "Aboubacar Koné",
        accountNumberMasked = "•••• 7142",
        balanceXof = 4_285_600,
        transactions = listOf(
            Transaction("t1", "Salaire — SIFCA SA", "31/05/2026", 1_850_000),
            Transaction("t2", "Carte BNI — Cap Sud Abidjan", "30/05/2026", -45_000),
            Transaction("t3", "Orange Money — Recharge", "29/05/2026", -25_000),
            Transaction("t4", "Virement reçu — Aminata T.", "27/05/2026", 120_000),
            Transaction("t5", "Prélèvement CIE (électricité)", "25/05/2026", -38_400),
        ),
    )
}
