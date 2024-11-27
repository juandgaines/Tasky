package com.juandgaines.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.juandgaines.core.presentation.designsystem.R

@Composable
fun TaskyEditTextDialog(
    modifier: Modifier = Modifier,
    title: String,
    onDismiss: () -> Unit,
    textState: TextFieldState,
    isError: Boolean,
    primaryButton: @Composable() (RowScope.() -> Unit),
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    RoundedCornerShape(15.dp)
                )
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary
            )
            TaskyTextField(
                modifier = Modifier.fillMaxWidth(),
                state = textState,
                hint = stringResource(id = R.string.email_hint),
                error = isError,
                keyboardType = KeyboardType.Email
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                primaryButton()
            }
        }
    }
}