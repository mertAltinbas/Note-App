package com.example.note_app.presentation.screens.note.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.note_app.R
import com.example.note_app.presentation.screens.note.view_model.NoteInputViewModel
import com.example.note_app.presentation.ui.theme.NotDefterimTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class NoteInputActivity : ComponentActivity() {
    private val viewModel: NoteInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.extras?.getInt("id")
        if (id != null)
            viewModel.getData(id)

        setContent {
            NotDefterimTheme {
                rememberSystemUiController().setSystemBarsColor(
                    colors.background,
                    darkIcons = false
                )
                Content()
            }
        }
    }

    override fun onBackPressed() {
        viewModel.save()
        super.onBackPressed()
    }


    @Composable
    private fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(backgroundColor = colors.background, title = {}, navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                })
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column(Modifier.padding(10.dp)) {
                    val title by viewModel.title.observeAsState()
                    val content by viewModel.content.observeAsState()
                    val configuration = LocalConfiguration.current
                    val height = configuration.screenHeightDp
                    TextField(
                        value = title ?: "",
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                        placeholder = {
                            Text(
                                text = getString(R.string.title),
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        onValueChange = { if(it.length < 50) viewModel.title.value = it })

                    TextField(
                        value = content ?: "",
                        modifier = Modifier
                            .height(height.dp - 200.dp)
                            .fillMaxWidth(),
                        placeholder = {
                            Text(text = getString(R.string.content))
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        onValueChange = { viewModel.content.value = it })

                    Spacer(modifier = Modifier.weight(1f))
                    Divider(modifier = Modifier.padding(start = 50.dp, end = 50.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        val data by viewModel.data.observeAsState()
                        val noteColor by viewModel.noteColor.observeAsState()

                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {viewModel.isMoreColorVisible.value=true}) {
                            Surface(
                                color = noteColor!!,
                                shape = CircleShape,
                                modifier = Modifier.size(25.dp)
                            ) {
                            }
                        }
                        if (data != null) {
                            Text(
                                text = data!!.date
                                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")).toString(),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                            viewModel.isMoreOptionVisible.value = true
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                        MoreContextMenu()

                    }
                }
                ColorMenu(modifier = Modifier.align(Alignment.BottomStart))
            }
        }
    }
    @Composable
    fun MoreContextMenu() {
        val configuration = LocalConfiguration.current
        val width = configuration.screenWidthDp
        val isMoreOptionVisible by viewModel.isMoreOptionVisible.observeAsState()

        if (isMoreOptionVisible == true)
            DropdownMenu(
                expanded = true,
                offset = DpOffset(width.dp, 0.dp),
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ),
                onDismissRequest = {
                    viewModel.isMoreOptionVisible.value = false
                }) {
                val context = LocalContext.current
                if (viewModel.isEdit)
                    DropdownMenuItem(onClick = { viewModel.deleteNote(context) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = getString(R.string.delete))
                        }
                    }
            }
    }

    @Composable
    fun ColorMenu(modifier: Modifier = Modifier) {
        val isMoreColorVisible by viewModel.isMoreColorVisible.observeAsState()
        if (isMoreColorVisible == true)
            Surface(shape = shapes.large, modifier = Modifier.padding(horizontal =  12.dp, vertical = 16.dp).then(modifier)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    viewModel.noteColors.map {
                        Surface(
                            color = it,
                            shape = CircleShape,
                            modifier = Modifier.size(25.dp).clickable {
                                viewModel.noteColor.value = it
                                viewModel.isMoreColorVisible.value = false
                            }
                        ){

                        }
                    }.toList()
                }
            }
    }
}