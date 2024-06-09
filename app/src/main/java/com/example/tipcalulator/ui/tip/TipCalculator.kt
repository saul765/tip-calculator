package com.example.tipcalulator.ui.tip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalulator.EMPTY_CHARACTER
import com.example.tipcalulator.R
import com.example.tipcalulator.ui.base.InputField
import com.example.tipcalulator.ui.base.RoundIconButton
import com.example.tipcalulator.ui.theme.Purple80
import com.example.tipcalulator.ui.theme.TipCalulatorTheme
import com.example.tipcalulator.utils.CalculationUtils.calculateAbsolutePercentage
import com.example.tipcalulator.utils.CalculationUtils.calculateTipAmount
import com.example.tipcalulator.utils.CalculationUtils.calculateTotalPerPerson
import com.example.tipcalulator.utils.CalculationUtils.formatTwoDecimals

const val DEFAULT_TOTAL_PER_PERSON = 0.0
const val DEFAULT_PERSONS = 1
const val DEFAULT_TIP_PERCENTAGE = 0.0
const val DEFAULT_SLIDER_POSITION = 0f
const val DEFAULT_TIP_AMOUNT = 0.0

@Composable
fun TipCalculator(modifier: Modifier = Modifier) {
    var billAmount by remember { mutableStateOf(EMPTY_CHARACTER) }
    var persons by remember { mutableIntStateOf(DEFAULT_PERSONS) }
    var totalPerPerson by remember { mutableDoubleStateOf(DEFAULT_TOTAL_PER_PERSON) }
    var tipAmount by remember { mutableDoubleStateOf(DEFAULT_TIP_PERCENTAGE) }

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalAmount(
                amount = totalPerPerson
            )
            Calculator(
                billAmount = billAmount,
                totalPersons = persons,
                onIncrementClicked = {
                    persons++
                },
                onDecreaseClicked = {
                    if (persons > 1) {
                        persons--
                    }
                },
                billInputListener = {
                    billAmount = it
                    totalPerPerson = calculateTotalPerPerson(
                        billAmount,
                        tipAmount,
                        persons
                    )
                },
                tipAmountListener = {
                    tipAmount = it
                    totalPerPerson = calculateTotalPerPerson(
                        billAmount,
                        it,
                        persons
                    )
                })
        }
    }
}


@Composable
private fun Calculator(
    billAmount: String,
    totalPersons: Int, onIncrementClicked: () -> Unit = {},
    onDecreaseClicked: () -> Unit = {},
    billInputListener: (String) -> Unit = {},
    tipAmountListener: (Double) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BillInputField(valueChangeListener = billInputListener, initialValue = billAmount)
            SplitSection(
                totalPersons = totalPersons,
                onIncrementClicked = onIncrementClicked,
                onDecreaseClicked = onDecreaseClicked
            )
            TipSection(totalAmount = billAmount, tipAmountListener = tipAmountListener)
        }
    }
}

@Composable
fun SplitSection(
    totalPersons: Int, onIncrementClicked: () -> Unit = {},
    onDecreaseClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.calculatorSplitLabel))
        SplitButtonsSection(
            totalPersons = totalPersons,
            onIncrementClicked = onIncrementClicked,
            onDecreaseClicked = onDecreaseClicked
        )
    }
}

@Composable
fun TipSection(totalAmount: String = EMPTY_CHARACTER, tipAmountListener: (Double) -> Unit = {}) {
    var sliderPosition by remember { mutableFloatStateOf(DEFAULT_SLIDER_POSITION) }

    var tipAmount by remember { mutableDoubleStateOf(DEFAULT_TIP_AMOUNT) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.calculatorTipLabel))
            Text(
                modifier = Modifier.padding(end = 50.dp),
                text = stringResource(
                    id = R.string.calculatorTipTotalLabel,
                    tipAmount
                )
            )
        }
        Text(
            text = stringResource(
                id = R.string.calculatorTipPercentageLabel,
                sliderPosition.calculateAbsolutePercentage()
            )
        )
        Slider(
            onValueChange = {
                sliderPosition = it
                tipAmount = calculateTipAmount(
                    totalAmount.toDoubleOrNull() ?: 0.00,
                    it
                )
                tipAmountListener(tipAmount)
            },
            value = sliderPosition,
            steps = 100
        )
    }

}

@Composable
fun SplitButtonsSection(
    totalPersons: Int,
    onIncrementClicked: () -> Unit = {},
    onDecreaseClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RoundIconButton(
            modifier = Modifier,
            onClick = onDecreaseClicked,
            contentDescription = R.string.calculatorRoundedIconDescription,
            imageVector = Icons.Default.Remove
        )
        Text(text = "$totalPersons")
        RoundIconButton(
            modifier = Modifier,
            onClick = onIncrementClicked,
            contentDescription = R.string.calculatorRoundedIconDescription,
            imageVector = Icons.Default.Add
        )

    }
}

@Composable
private fun BillInputField(initialValue: String, valueChangeListener: (String) -> Unit = {}) {
    val validState = remember(initialValue) {
        initialValue.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    InputField(
        modifier = Modifier,
        initialValue = initialValue,
        labelId = R.string.calculatorBillLabel,
        onAction = KeyboardActions {
            if (!validState) return@KeyboardActions
            keyboardController?.hide()
        }, onValueChangeListener = {
            valueChangeListener(it)
        }
    )
}

@Composable
private fun TotalAmount(amount: Double = 0.00) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Purple80),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.calculatorPersonsLabel),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(
                    id = R.string.calculatorTotalLabel,
                    amount.formatTwoDecimals()
                ),
                style = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalulatorTheme {
        TipCalculator()
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    TipCalulatorTheme {
        Calculator(billAmount = "100", 5)
    }
}

@Preview(showBackground = true)
@Composable
fun TotalAmountPreview() {
    TipCalulatorTheme {
        TotalAmount(100.0)
    }
}

@Preview(showBackground = true)
@Composable
fun BillInputFieldPreview() {
    TipCalulatorTheme {
        BillInputField(initialValue = "100")
    }
}

@Preview(showBackground = true)
@Composable
fun SplitSectionPreview() {
    TipCalulatorTheme {
        SplitSection(totalPersons = 1)
    }
}


@Preview(showBackground = true)
@Composable
fun TipSectionPreview() {
    TipCalulatorTheme {
        TipSection()
    }
}