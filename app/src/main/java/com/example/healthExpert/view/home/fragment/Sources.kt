package com.example.healthExpert.view.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.SourcesCompatFragment
import com.example.healthExpert.databinding.FragmentSourcesBinding
import com.example.healthExpert.model.News
import androidx.lifecycle.Observer
import com.example.healthExpert.view.calories.CaloriesEdit
import com.example.healthExpert.view.news.NewActivity
import com.squareup.picasso.Picasso


class Sources : SourcesCompatFragment() {
    private lateinit var binding: FragmentSourcesBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSourcesBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.sourcesViewmodel = sourcesViewModel
        sourcesViewModel.getNews()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        sourcesViewModel.news.observe(viewLifecycleOwner, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                recyclerView.adapter = SourcesAdapter(sourcesViewModel.news,this.requireContext())
            }
        })


        return binding.root
    }
}

// RecycleView Adapter
class SourcesAdapter(private val sourcesSet: MutableLiveData<MutableList<News>?>, private val activity: Context)
    : RecyclerView.Adapter<SourcesAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.title)
        var date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_new_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener {
            Log.d("holder", sourcesSet.value?.get(position)?.id.toString())
            val intent = Intent(activity, NewActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", sourcesSet.value!![position].id)
            bundle.putString("title", sourcesSet.value!![position].Title)
            bundle.putString("content", sourcesSet.value!![position].Content)
            bundle.putString("date", sourcesSet.value!![position].Date.toString())
            bundle.putString("author", sourcesSet.value!![position].Author)
            bundle.putString("image", sourcesSet.value!![position].Image)
            intent.putExtras(bundle)
            activity.startActivity(intent)

        })
        Picasso.get().load(sourcesSet.value?.get(position)?.Image).into(holder.image);
        holder.title.text = sourcesSet.value?.get(position)?.Title ?: ""
        holder.date.text = SimpleDateFormat("YYYY-mm-dd").format(sourcesSet.value?.get(position)?.Date)
    }

    override fun getItemCount()= sourcesSet.value?.size ?:0
}