@file:OptIn(ExperimentalLayoutApi::class)

package com.juandgaines.agenda.presentation.agenda_item.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.juandgaines.agenda.domain.agenda.Photo
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyLightBlue
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun PhotosSection(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    canEditField: Boolean,
    isInternetConnected: Boolean,
    photos: List<Photo> = emptyList(),
    local: List<Uri> = emptyList(),
    onAddPhoto: (Uri) -> Unit,
    navigateToPhoto: (String) -> Unit = {}
){
    val pickMedia = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                onAddPhoto(uri)
            }
        }
    )

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = TaskyLight
            ).padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        Text(
            text = stringResource(id = com.juandgaines.agenda.presentation.R.string.photos),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        FlowRow(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            photos.forEach {
                AsyncImage(
                    model = it.url,
                    contentDescription = "Photo",
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 1.dp,
                            color = TaskyLightBlue,
                            shape = RoundedCornerShape(
                                8.dp
                            )
                        ).clickable {
                            navigateToPhoto(it.url)
                        },
                )
            }
            local.forEach {
                AsyncImage(
                    model = it,
                    contentDescription = "Photo",
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 1.dp,
                            color = TaskyLightBlue,
                            shape = RoundedCornerShape(
                                8.dp
                            )
                        ).clickable {
                            navigateToPhoto(
                                it.toString()
                            )
                        },
                )
            }
            if (isEditing && canEditField) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 1.dp,
                            color = TaskyLightBlue,
                            shape = RoundedCornerShape(
                                8.dp
                            )
                        )
                        .clickable (isInternetConnected){
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ImageOnly
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add photo",
                        tint = TaskyLightBlue,
                    )
                }
            }
        }
    }

}
@Preview(
    showBackground = true
)
@Composable
fun PhotosSectionPreview() {
    TaskyTheme {
        PhotosSection(
            isEditing = true,
            canEditField = true,
            isInternetConnected = true,
            photos = listOf(
                Photo(
                    key = "photo1",
                    url = "https://picsum.photos/200/300"
                ),
            ),
            local = listOf(
                Uri.parse("content://com.juandgaines.agenda.provider/external_files/photo1.jpg"),
                Uri.parse("content://com.juandgaines.agenda.provider/external_files/photo2.jpg"),
            ),
            onAddPhoto = {}
        )
    }
}