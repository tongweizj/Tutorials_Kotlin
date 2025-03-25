package com.centennial.quicktasks.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(duedate:String,onSave: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDateLabel = remember { mutableStateOf(duedate) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDateLabel.value,
            onValueChange = { },
            label = { Text("Due Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            fun Long.convertMillisToDate(): String {
                // Create a calendar instance in the default time zone
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = this@convertMillisToDate
                    // Adjust for the time zone offset to get the correct local date
                    val zoneOffset = get(Calendar.ZONE_OFFSET)
                    val dstOffset = get(Calendar.DST_OFFSET)
                    add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
                }
                // Format the calendar time in the specified format
                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                return sdf.format(calendar.time)
            }
            // DatePickerDialog component with custom colors and button behaviors
            DatePickerDialog(
                onDismissRequest = {
                    // Action when the dialog is dismissed without selecting a date
                    showDatePicker = false
                },
                confirmButton = {
                    // Confirm button with custom action and styling
                    TextButton(
                        onClick = {
                            // Action to set the selected date and close the dialog
                            showDatePicker = false
                            selectedDateLabel.value =
                                datePickerState.selectedDateMillis?.convertMillisToDate() ?: ""

                            onSave( selectedDateLabel.value)
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    // Dismiss button to close the dialog without selecting a date
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("CANCEL")
                    }
                }
            ) {
                // The actual DatePicker component within the dialog
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}