package com.bni.sifipdemo.data.repository

import com.bni.sifipdemo.data.model.BankAccount
import com.bni.sifipdemo.data.model.Transaction

/**
 * Read-only demo data for the dashboard. Numbers are in MGA (Ariary),
 * la devise officielle de Madagascar.
 */
class BankRepository {

    fun loadAccount(): BankAccount = BankAccount(
        holder = "Hery Razafindrakoto",
        accountNumberMasked = "•••• 0191",
        balanceMga = 4_938_598,
        transactions = listOf(
            Transaction("t1", "Salaire — STAR Madagascar", "31/05/2026", 2_450_000),
            Transaction("t2", "Carte BNI — Tanà City", "30/05/2026", -185_000),
            Transaction("t3", "Telma Mobile Money — Recharge", "29/05/2026", -30_000),
            Transaction("t4", "Virement reçu — Mialy R.", "27/05/2026", 320_000),
            Transaction("t5", "Prélèvement JIRAMA", "25/05/2026", -86_400),
        ),
    )
}
