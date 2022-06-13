package com.example.artmedicalproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.artmedicalproject.R
import com.example.artmedicalproject.model.Item

class Adapter(private val list: List<Item>): BaseAdapter() {


    //    RecyclerView.Adapter<Adapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
//        return ViewHolder(view.rootView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(list[position])
//    }
//
//    override fun getItemCount(): Int {
//        return list.count()
//    }
//
//    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//
//        fun bind(item: Item){
//            if(item.wasSelected && item.color == null){
//                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.black))
//            } else if(item.wasSelected && item.color != null){
//                itemView.setBackgroundColor(item.color)
//            }
//        }
//    }

    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return list[p0].id.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(p2?.context).inflate(R.layout.grid_item, p2, false)
        val itemView = layoutInflater.rootView
        val item = list[p0]
        if(item.wasSelected && item.color == null){
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.black))
        } else if(item.wasSelected && item.color != null){
            itemView.setBackgroundColor(item.color)
        }
        return itemView
    }
}