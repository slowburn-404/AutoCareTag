package dev.borisochieng.autocaretag.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.borisochieng.autocaretag.R
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.colorScheme
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.shape
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.typography

@Composable
fun CustomTextField(
    label: String,
    placeHolder: String,
    inputType: Inputs,
    isTrailingIcon: Boolean,
    onTrailingIconClick: () -> Unit,
    onInputValueChange: (String) -> Unit,
    inputValue: String,
    errorMessage: String? = null,
    isReadable: Boolean = false,
//    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                onInputValueChange(it)
            },
            readOnly = isReadable,
            shape = shape.button,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = checkInputType(inputType)),
            modifier = Modifier.fillMaxWidth(), //.clickable(onClick = onClick),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.background,
                unfocusedContainerColor = colorScheme.background,
                focusedIndicatorColor = colorScheme.primary,
                unfocusedIndicatorColor = colorScheme.onBackground,
                cursorColor = colorScheme.primary
            ),
            placeholder = {
                Text(
                    text = placeHolder,
                    style = typography.bodyLight
                )
            },
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = typography.body,
                        color = colorScheme.error,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            },
            trailingIcon = {
                if (isTrailingIcon) {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = stringResource(
                                R.string.appointment_date
                            )
                        )
                    }
                }
            }
        )
    }
}

private fun checkInputType(inputType: Inputs): KeyboardType =
    when (inputType) {
        is Inputs.PhoneNumber -> KeyboardType.Phone
        else -> KeyboardType.Text
    }

sealed class Inputs {
    data object PhoneNumber : Inputs()
    data object Text : Inputs()
}


@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        label = "Owner Name",
        placeHolder = "Enter vehicle owner's name",
        inputType = Inputs.PhoneNumber,
        isTrailingIcon = true,
        onTrailingIconClick = {},
        inputValue = "Rasta man",
        onInputValueChange = {},
//        onClick = {}
    )
}