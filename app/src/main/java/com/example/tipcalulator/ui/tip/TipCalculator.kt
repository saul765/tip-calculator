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
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.tipcalulator.R
import com.example.tipcalulator.ui.base.InputField
import com.example.tipcalulator.ui.base.RoundIconButton
import com.example.tipcalulator.ui.base.TipCalculatorState
import com.example.tipcalulator.ui.theme.Purple80
import com.example.tipcalulator.ui.theme.TipCalulatorTheme
import com.example.tipcalulator.utils.CalculationUtils.calculateAbsolutePercentage
import com.example.tipcalulator.utils.CalculationUtils.formatTwoDecimals


const val DEFAULT_TIP_PERCENTAGE = 0.0
const val DEFAULT_SLIDER_POSITION = 0f

@Composable
fun rememberTipCalculatorState(): TipCalculatorState =
    remember { TipCalculatorState() }

@Composable
fun TipCalculator(modifier: Modifier = Modifier) {
    val calculatorState = rememberTipCalculatorState()

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalAmount(
                amount = calculatorState.calculateTotalPerPerson()
            )
            Calculator(
                calculatorState = calculatorState,
                onBillInputChange = { calculatorState.updateBillAmount(it) },
                onPersonsChanged = { calculatorState.updatePersons(it) },
                onTipChanged = { calculatorState.updateTipAmount(it) }
            )
        }
    }
}


@Composable
private fun Calculator(
    calculatorState: TipCalculatorState,
    onBillInputChange: (String) -> Unit = {},
    onPersonsChanged: (Int) -> Unit = {},
    onTipChanged: (Float) -> Unit = {}

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
            BillInputField(
                valueChangeListener = { onBillInputChange(it) },
                initialValue = calculatorState.billAmount
            )
            SplitSection(
                totalPersons = calculatorState.persons,
                onIncrementClicked = {
                    onPersonsChanged(calculatorState.persons + 1)
                },
                onDecreaseClicked = {
                    if (calculatorState.persons > 1) {
                        onPersonsChanged(calculatorState.persons - 1)
                    }
                }
            )
            TipSection(
                tipAmount = calculatorState.tipAmount,
                tipPercentageListener = { onTipChanged(it) }

            )
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
fun TipSection(
    tipAmount: Double = DEFAULT_TIP_PERCENTAGE,
    tipPercentageListener: (Float) -> Unit = {}
) {
    var sliderPosition by remember { mutableFloatStateOf(DEFAULT_SLIDER_POSITION) }

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
                    tipAmount.formatTwoDecimals()
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
                tipPercentageListener(it)
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
    val state = rememberTipCalculatorState()
    TipCalulatorTheme {
        Calculator(calculatorState = state)
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