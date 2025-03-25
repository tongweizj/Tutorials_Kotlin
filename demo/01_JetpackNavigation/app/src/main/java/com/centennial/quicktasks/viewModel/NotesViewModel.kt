package com.centennial.quicktasks.viewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.centennial.quicktasks.data.Note
import java.time.LocalDate
import java.util.UUID

class NotesViewModel : ViewModel() {

    var notes = mutableStateListOf(
        Note("Meeting Notes", "Discuss project updates and deadlines.",
            dueDate = "2025-0201"
        ),
        Note("Grocery List", "Buy milk, eggs, and bread.","2025-02-01"),
        Note("Workout Plan", "Cardio on Monday, Strength training on Wednesday.","2025-02-19")
    )
    // 用于存储笔记列表
//    private val _notes = mutableStateListOf<Note>()
    //val notes: List<Note> get() = _notes

    // 添加新笔记

    fun addNote(newTitle: String, newContent: String, newDueDate: String) {
        notes.add(Note(newTitle, newContent,newDueDate))
    }

    // 删除笔记

    fun removeNote(note: Note) {
        notes.remove(note)
    }


    fun updateNote(id: String, newTitle: String, newContent: String, newDueDate: String) {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            notes[index].title = newTitle
            notes[index].content = newContent
            notes[index].dueDate = newDueDate
        }
    }


    fun getNote(id: String): Note {
        var item:Note? = this.notes.find { it.id == id };
        if(item ==null){
            item = Note("Meeting Notes", "Discuss project updates and deadlines.",
                "2025-02-20"
            )
        }
        return item
    }
//    fun getNotes(): SnapshotStateList<Note>{
//        return this.notes
//    }
}
