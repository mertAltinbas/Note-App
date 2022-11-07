package com.example.note_app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DialogX(title:String, content:String, showCancel: Boolean = true, showDone: Boolean = true, cancelText:String? = null, doneText:String? = null, onCancelTap: (() -> Unit)? = null, onDoneTap: (() -> Unit)? = null) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
        Box() {
            Surface(modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center), shape = shapes.large) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = title)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = content)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (showCancel)
                            Button(modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red), onClick = { onCancelTap?.invoke() }) {
                                Text(text = cancelText ?: stringResource(id = android.R.string.cancel))
                            }
                        if (showDone)
                            Button(modifier = Modifier.weight(1f), onClick = { onDoneTap?.invoke() }) {
                                Text(text = doneText ?: stringResource(id = android.R.string.ok))
                            }
                    }
                }
            }
        }

    }


}