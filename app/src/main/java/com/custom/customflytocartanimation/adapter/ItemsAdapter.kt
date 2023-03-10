package com.custom.customflytocartanimation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.custom.customflytocartanimation.R
import com.custom.customflytocartanimation.listener.ItemActionListener
import com.custom.customflytocartanimation.model.CartModel


class ItemsAdapter(
    val context: Context?,
    private val itemList: List<CartModel>?,
    val itemActionListener: ItemActionListener,
    private val onItemClick: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_single_item, parent, false)
        return MyViewHolder(view) { position ->
            onItemClick(position)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = itemList?.get(position)
        list?.resourcesId?.let { holder.imvImage.setImageResource(it) }
        holder.txvMessage.text = list?.message
        holder.imvImage.setOnClickListener {
            itemActionListener.onItemTap(holder.imvImage)
        }
    }

    override fun getItemCount(): Int = itemList?.size ?: 0

}