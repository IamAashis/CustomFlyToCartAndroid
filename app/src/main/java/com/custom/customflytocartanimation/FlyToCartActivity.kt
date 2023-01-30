package com.custom.customflytocartanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.customflytocartanimation.adapter.ItemsAdapter
import com.custom.customflytocartanimation.databinding.ActivityFlyToCartBinding
import com.custom.customflytocartanimation.model.CartModel

class FlyToCartActivity : AppCompatActivity() {

    private var ratingList = mutableListOf<CartModel>()
    private lateinit var binding: ActivityFlyToCartBinding
    lateinit var itemAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlyToCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
    }
    
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvCartView?.layoutManager = layoutManager
        itemAdapter =
            ItemsAdapter(this, ratingList) {
                Toast.makeText(this, "Click Here", Toast.LENGTH_SHORT).show()
            }
        binding.rcvCartView.adapter = itemAdapter
    }
}