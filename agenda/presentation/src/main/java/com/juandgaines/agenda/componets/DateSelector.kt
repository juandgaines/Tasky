package com.juandgaines.agenda.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.SelectorDayData
import com.juandgaines.agenda.presentation.preview_providers.SelectorDayPreviewParameterProvider
import com.juandgaines.core.presentation.designsystem.TaskyDarkGray
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyOrange
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun DateSelector(
    modifier: Modifier,
    daysList: List<SelectorDayData>,
) {
    Row(
        modifier = modifier,
    ) {
        daysList.forEach { selectorDayData ->
            DayItem(
                modifier = Modifier
                    .width(40.dp)
                    .height(60.dp),
                dayItem = selectorDayData
            )
        }
    }
}

@Composable
fun DayItem(
    modifier: Modifier,
    dayItem: SelectorDayData,
    onClickDate: (Long) -> Unit = {

    }
){
    Column(
        modifier = modifier
            .then(
                if (dayItem.isSelected)
                    Modifier.background(
                        color = TaskyOrange,
                        shape = RoundedCornerShape(24.dp)
                    )
                else Modifier
            ).clickable {
                onClickDate(dayItem.dayTime)
            },
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            style = MaterialTheme.typography.labelSmall,
            color = if (dayItem.isSelected) TaskyDarkGray else TaskyGray,
            fontWeight = FontWeight.Bold,
            text = dayItem.initialDayOfWeek.uppercase()
        )
        Text(
            style = MaterialTheme.typography.bodySmall,
            color = TaskyDarkGray,
            fontWeight = FontWeight.Bold,
            text = dayItem.dayOfMonth.toString()
        )
    }
}

@Composable
@Preview
fun DateSelectorPreview(
    @PreviewParameter (SelectorDayPreviewParameterProvider::class) daysList: List<SelectorDayData>
) {
    TaskyTheme {
        DateSelector(
            daysList = daysList,
            modifier = Modifier
        )
    }
}

