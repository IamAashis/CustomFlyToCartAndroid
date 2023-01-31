package com.custom.customflytocartanimation

import android.animation.Animator
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.customflytocartanimation.adapter.ItemsAdapter
import com.custom.customflytocartanimation.databinding.ActivityFlyToCartBinding
import com.custom.customflytocartanimation.listener.ItemActionListener
import com.custom.customflytocartanimation.model.CartModel
import com.custom.customflytocartanimation.util.AnimationUtil


class FlyToCartActivity : AppCompatActivity() {

    private var list = mutableListOf<CartModel>()
    private lateinit var binding: ActivityFlyToCartBinding
    lateinit var itemAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlyToCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        makeCartList()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvCartView.layoutManager = layoutManager
        itemAdapter = ItemsAdapter(this, list, itemActionListener = object : ItemActionListener {
            override fun onItemTap(imageView: ImageView?) {
                if (imageView != null) {
                    makeFlyAnimation(imageView)
                }
            }
        }) {
            Toast.makeText(this, "Click Here", Toast.LENGTH_SHORT).show()
        }
        binding.rcvCartView.adapter = itemAdapter
    }

    private fun makeFlyAnimation(targetView: ImageView) {
        val destView = binding.ivmCartLogo
//        binding.ivmCartLogo.setImageDrawable(targetView)
        AnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000)
            .setDestView(destView).setAnimationListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
//                    addItemToCart()
                    Toast.makeText(this@FlyToCartActivity, "Continue Shopping...", Toast.LENGTH_SHORT).show()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }).startAnimation()
    }

    private fun makeCartList() {
//        val list: MutableList<CartModel> = ArrayList()
        var i = 1
        var c = 1
        while (i <= 50) {
            if (c > 4) c = 1
            when (c) {
                1 -> list.add(
                    CartModel(
                        i, "product_$i", com.custom.customflytocartanimation.R.drawable.ic_android
                    )
                )
                2 -> list.add(
                    CartModel(
                        i, "product_$i", com.custom.customflytocartanimation.R.drawable.ic_android
                    )
                )
                3 -> list.add(
                    CartModel(
                        i, "product_$i", com.custom.customflytocartanimation.R.drawable.ic_android
                    )
                )
                4 -> list.add(
                    CartModel(
                        i, "product_$i", com.custom.customflytocartanimation.R.drawable.ic_android
                    )
                )
            }
            i++
            c++
        }
    }
}