package com.example.tipcalulator.utils

import kotlin.math.floor

object CalculationUtils {
    fun Double.formatTwoDecimals(): String {
        return String.format("%.2f", this)
    }

    fun Float.calculateAbsolutePercentage(): Int {
        return (this * 100).toInt()
    }

    fun Float.formatTwoDecimals(): Double {
        val roundedValue = floor(this * 100) / 100
        return String.format("%.2f", roundedValue).toDouble()
    }

    fun calculateTipAmount(billAmount: Double, tipPercentage: Float): Double {
        return if (billAmount > 1 && billAmount.toString().isNotEmpty() && tipPercentage > 0.0) {
            (billAmount * (tipPercentage.formatTwoDecimals())).formatTwoDecimals().toDouble()
        } else {
            0.0
        }
    }

    fun calculateTotalPerPerson(billAmount: String, tipAmount: Double, totalPersons: Int): Double {
        val billAmountNumber = billAmount.toDoubleOrNull() ?: 0.0

        return if (billAmountNumber > 1 && billAmount.isNotEmpty()) {
            (billAmountNumber + tipAmount) / totalPersons
        } else {
            0.0
        }
    }
}