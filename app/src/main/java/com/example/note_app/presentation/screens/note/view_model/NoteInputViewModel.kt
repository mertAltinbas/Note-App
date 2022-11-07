package com.example.note_app.presentation.screens.note.view_model

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_app.infrastructure.model.NoteModel
import com.example.note_app.domain.repositories.INoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject



@HiltViewModel
class NoteInputViewModel @Inject constructor(private val repository: INoteRepository) : ViewModel() {
    var title = MutableLiveData("")
        private set

    var content = MutableLiveData("")
        private set

    var data = MutableLiveData<NoteModel>(null)
        private set

    var isMoreOptionVisible = MutableLiveData(false)


    var isMoreColorVisible = MutableLiveData(false)

    var noteColor = MutableLiveData(Color.Red)

    val noteColors = listOf(Color.Red, Color.Blue, Color.Magenta, Color.Cyan, Color.Yellow)

    val isEdit : Boolean
        get() = data.value != null


    fun getData(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getNote(id)
            if(result != null)
                viewModelScope.launch {
                    title.value = result.title
                    content.value = result.content
                    data.value = result
                    noteColor.value = Color(result.color)
                }
        }
    }

    fun save() {
        if(title.value.isNullOrEmpty() && content.value.isNullOrEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            val note = NoteModel(
                id = data.value?.id,
                title = title.value ?: "",
                content = content.value ?: "",
                date = LocalDateTime.now(),
                color = noteColor.value!!.toArgb()
            )
            repository.insertNote(note)
        }

    }

    fun deleteNote(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNote(data.value!!)
            (context as ComponentActivity).finish()
        }
    }
}