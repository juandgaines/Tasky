@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.edit_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun EditFieldScreenRoot(
    viewModel: EditFieldViewModel,
    onSave: (String,String) -> Unit,
    navigateBack: () -> Boolean,
) {
    val state = viewModel.state
    val events = viewModel.events
    EditFieldScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EditFieldScreen(
    state: EditFieldState,
    onAction: (EditFieldAction) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.edit_title, state.fieldName.uppercase()),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                actions = {
                    Text(
                        text = stringResource(R.string.save),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                onAction(EditFieldAction.Save)
                            },
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
            .imePadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            BasicTextField(
                state = state.fieldValue,
                textStyle = if(state.fieldName == AgendaItems.TITLE)
                    MaterialTheme.typography.displaySmall
                else
                    MaterialTheme.typography.bodySmall
                ,
                modifier = Modifier.fillMaxSize().padding(16.dp),
                decorator = { innerTextField ->
                    innerTextField()
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun EditFieldScreenPreview() {
    TaskyTheme {
        EditFieldScreen(
            state = EditFieldState(
                fieldName = "Name"
            ),
            onAction = {}
        )
    }
}