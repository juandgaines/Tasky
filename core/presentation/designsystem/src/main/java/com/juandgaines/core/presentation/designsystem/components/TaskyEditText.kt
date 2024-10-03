package com.juandgaines.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.CheckIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    endIcon: ImageVector?,
    isFocused: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
    hint: String,
    error: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                )
                .border(
                    width = 1.dp,
                    color = when {
                        isFocused && !error-> MaterialTheme.colorScheme.secondary
                        error -> MaterialTheme.colorScheme.error
                        else ->  Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .height(64.dp)
                .padding(12.dp)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                },
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if(state.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerBox()
                    }
                    if(
                        endIcon != null &&
                        !error &&
                        state.text.isNotEmpty()
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = endIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
        )
    }
}


@Preview
@Composable
fun TaskyTextFieldPreview() {
    TaskyTheme {
        var isFocused by remember { mutableStateOf(false) }

        TaskyTextField(
            state = rememberTextFieldState(),
            hint = "example@email.com",
            isFocused = isFocused,
            onFocusChanged = {
                isFocused = it
            },
            error = false,
            endIcon = CheckIcon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}