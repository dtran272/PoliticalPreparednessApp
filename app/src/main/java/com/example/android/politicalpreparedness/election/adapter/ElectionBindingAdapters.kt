package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listElections")
fun bindElectionList(recyclerView: RecyclerView, electionData: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter

    adapter.submitList(electionData)
}

@BindingAdapter("dateFormatText")
fun bindTextViewToDateFormat(textView: TextView, date: Date?) {
    if (date == null) {
        textView.visibility = View.GONE
        return
    }

    val formatter = SimpleDateFormat("EEE MMM dd yyyy @HH:mm:ss z")
    val formattedDate = formatter.format(date)

    textView.text = formattedDate
    textView.visibility = View.VISIBLE
}
