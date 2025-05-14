package com.example.to_dolist.ui.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.model.NoteEntity

class NoteAdapter(private var notes: List<NoteEntity>, private val listener: NoteItemListener) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface NoteItemListener {
        fun onNoteClicked(note: NoteEntity)
        fun onNoteLongClicked(note: NoteEntity)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.noteTitle)
        val contentText: TextView = itemView.findViewById(R.id.noteContent)

        init {

            itemView.setOnClickListener {
                val note = notes[adapterPosition]
                listener.onNoteClicked(note)
            }
            itemView.setOnLongClickListener {
                val note = notes[adapterPosition]
                listener.onNoteLongClicked(note)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleText.text = note.title
        holder.contentText.text = note.content
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<NoteEntity>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
