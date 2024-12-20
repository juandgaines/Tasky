package com.juandgaines.agenda.presentation.home.componets.selector_date.preview_providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.juandgaines.agenda.presentation.home.componets.selector_date.SelectorDayData
import java.time.LocalDate
import java.time.ZonedDateTime

class SelectorDayPreviewParameterProvider: PreviewParameterProvider<List<SelectorDayData>> {
    private val date = LocalDate.now()
    override val values = sequenceOf(
        listOf(
            SelectorDayData(
                date.dayOfWeek.toString().first(),
                1,
                true,
                dayTime = date
            ),
            SelectorDayData(
                date.plusDays(1).dayOfWeek.toString().first(),
                2,
                false,
                dayTime = date.plusDays(1)
            ),
            SelectorDayData(
                date.plusDays(2).dayOfWeek.toString().first(),
                3,
                false,
                dayTime = date.plusDays(2)
            ),
            SelectorDayData(
                date.plusDays(3).dayOfWeek.toString().first(),
                4,
                false,
                dayTime = date.plusDays(3)
            ),
            SelectorDayData(
                date.plusDays(4).dayOfWeek.toString().first(),
                5,
                false,
                dayTime = date.plusDays(4)
            ),
            SelectorDayData(
                date.plusDays(5).dayOfWeek.toString().first(),
                6,
                false,
                dayTime = date.plusDays(5)
            ),
            SelectorDayData(
                date.plusDays(6).dayOfWeek.toString().first(),
                7,
                false,
                dayTime = date.plusDays(6)
            ),
        )
    )
}