@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.photo_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.TaskyBlack
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold

@Composable
fun PhotoDetailScreenRoot(
    viewModel: PhotoDetailViewModel,
    onDelete: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    val state by  viewModel.state.collectAsState()
    PhotoDetailScreen(
        state = state,
        onClose = navigateBack,
        onDelete = onDelete
    )
}

@Composable
fun PhotoDetailScreen(
    state: PhotoState,
    onClose: () -> Unit,
    onDelete: (String) -> Unit,
) {
    TaskyScaffold (
        colorBackGround = TaskyBlack,
        topAppBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Back",
                    modifier =  Modifier.clickable {
                        onClose()
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.photo_detail_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                ){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Back",
                        modifier =  Modifier.clickable {
                            onDelete(state.url)
                        }
                    )
                }
            }
        },
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = TaskyBlack
                ),
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                model = state.url,
                contentDescription = "Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview
@Composable
fun PreviewPhotoDetailScreen() {
    TaskyTheme {
        PhotoDetailScreen(
            state = PhotoState(
                url = "https://picsum.photos/200/300"
            ),
            onClose = {},
            onDelete = {}
        )
    }
}