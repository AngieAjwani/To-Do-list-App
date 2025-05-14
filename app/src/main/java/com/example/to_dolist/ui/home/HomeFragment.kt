package com.example.to_dolist.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.network.QuoteService
import com.example.to_dolist.network.adapter.QuoteAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var quoteRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        quoteRecyclerView = view.findViewById(R.id.quoteRecyclerView)
        quoteRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                val response = QuoteService.api.getQuotes()
                Log.d("durshal", "onViewCreated: "+response)
                if (response.isNotEmpty()) {
                    val adapter = QuoteAdapter(response)
                    quoteRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Failed to load quotes", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}