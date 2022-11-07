package com.example.note_app.presentation.screens.note.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note_app.R
import com.example.note_app.presentation.components.DialogX
import com.example.note_app.presentation.screens.note.view_model.NoteListViewModel
import com.example.note_app.presentation.tiles.NoteListTile
import com.example.note_app.presentation.ui.theme.NotDefterimTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteListActivity : ComponentActivity() {
    private val viewModel: NoteListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onResume() {
        viewModel.getNotes()
        super.onResume()
    }

    @Composable
    fun Content() {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            topBar = {
                val showGrid by viewModel.showGrid.observeAsState()
                TopAppBar(backgroundColor = colors.background, elevation = 0.dp, title = {
                    Text(
                        text = getString(R.string.app_name),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    )
                }, actions = {
                    IconButton(onClick = {
                        if (viewModel.noteForDelete.value == null) {
                            viewModel.changeVisibility()
                        }
                    }) {
                        Icon(
                            imageVector = if (showGrid!!) Icons.Rounded.MoreVert else Icons.Rounded.List,
                            contentDescription = null
                        )
                    }
                })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if(viewModel.noteForDelete.value == null) {
                            val intent = Intent(this, NoteInputActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    backgroundColor = colors.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = getString(R.string.create_note)
                    )
                }
            },
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                val showGrid by viewModel.showGrid.observeAsState()
                val noteList by viewModel.noteList.observeAsState()
                if (noteList!!.isEmpty()) EmptyScreen()
                else if (showGrid!!) NoteGridView()
                else NoteListView()


                DeleteDialog()
            }
        }
    }

    @Composable
    private fun DeleteDialog() {
        val note by viewModel.noteForDelete.observeAsState()
        if(note != null)
            DialogX(
                title = stringResource(id = R.string.delete_note_dialog_title),
                content = stringResource(id = R.string.delete_note_dialog_content),
                onCancelTap = { viewModel.noteForDelete.value = null }
            ){
                viewModel.deleteNote(note!!)
            }
    }

    @Composable
    private fun NoteListView() {
        val noteList by viewModel.noteList.observeAsState()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(noteList!!) { note ->
                NoteListTile(
                    note = note,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(applicationContext, NoteInputActivity::class.java)
                            intent.putExtra("id",   note.id)
                            startActivity(intent)
                        },
                    onDeleteClick = {
                       viewModel.noteForDelete.value = note
                    }
                )
            }
        }
    }

    @Composable
    private fun NoteGridView() {
        val noteList by viewModel.noteList.observeAsState()
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(20.dp),
            columns = GridCells.Adaptive(120.dp),
            content = {
                items(noteList!!) { note ->
                    NoteListTile(
                        note = note,
                        modifier = Modifier
                            .height(120.dp)
                            .clickable {
                                val intent =
                                    Intent(applicationContext, NoteInputActivity::class.java)
                                intent.putExtra("id", note.id)
                                startActivity(intent)
                            },
                        onDeleteClick = {
                            viewModel.noteForDelete.value = note
                        }
                    )
                }

            })

    }

    @Composable
    private fun EmptyScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Clear, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Red.copy(.4f))
                Text(
                    text = "Nothing to see here",
                    style = TextStyle(
                        fontSize = 17.sp
                    )
                )
            }

        }
    }
}








