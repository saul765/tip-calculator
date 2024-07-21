package com.example.tipcalulator.ui.base

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.tipcalulator.EMPTY_CHARACTER
import com.example.tipcalulator.utils.CalculationUtils


@Stable
class TipCalculatorState {

    companion object {
        const val DEFAULT_PERSONS = 1
        const val DEFAULT_TIP_AMOUNT = 0.0
    }

    var billAmount by mutableStateOf(EMPTY_CHARACTER)
        private set


    var tipAmount by mutableDoubleStateOf(DEFAULT_TIP_AMOUNT)
        private set

    var persons by mutableIntStateOf(DEFAULT_PERSONS)
        private set


    fun updateTipAmount(tipPercentage: Float) {
        tipAmount =  CalculationUtils.calculateTipAmount(billAmount.toDoubleOrNull() ?: 0.0, tipPercentage)
    }

    fun updatePersons(newPersons: Int) {
        persons = newPersons
    }

    fun updateBillAmount(newBillAmount: String) {
        billAmount = newBillAmount
    }

    fun calculateTotalPerPerson() =
        CalculationUtils.calculateTotalPerPerson(billAmount, tipAmount, persons)
}