package ru.phrogs.moretechhackathon.feature_visit_history.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem

class HistoryStorage(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private companion object {
        const val SHARED_PREF_NAME = "shared_pref"
        const val CASH_MACHINE_KEY = "cash_machine"
        const val BANK_BRANCH_KEY = "bank_branch"
    }

    fun saveHistoryItem(historyItem: HistoryItem) {
        when (historyItem) {
            is HistoryItem.CashMachine -> {
                val list:List<HistoryItem.CashMachine> = getCashMachineList() + historyItem
                sharedPreferences.edit().putString(CASH_MACHINE_KEY, gson.toJson(list))
                    .apply()
            }

            is HistoryItem.BankBranch -> {
                val list: List<HistoryItem.BankBranch> = getBankBranchList() + historyItem
                sharedPreferences.edit().putString(
                    BANK_BRANCH_KEY, gson.toJson(list)
                ).apply()
            }
        }
    }

    fun getHistory(): List<HistoryItem> {
        return getBankBranchList() + getCashMachineList()
    }

    private fun getCashMachineList(): List<HistoryItem.CashMachine> {
        val historyJson = sharedPreferences.getString(CASH_MACHINE_KEY, null)
        val type = object : TypeToken<List<HistoryItem.CashMachine>>() {}.type
        return historyJson?.let {
            gson.fromJson(it, type)
        } ?: emptyList()
    }

    private fun getBankBranchList(): List<HistoryItem.BankBranch> {
        val historyJson = sharedPreferences.getString(BANK_BRANCH_KEY, null)
        val type = object : TypeToken<List<HistoryItem.BankBranch>>() {}.type
        return historyJson?.let {
            gson.fromJson(it, type)
        } ?: emptyList()
    }
}