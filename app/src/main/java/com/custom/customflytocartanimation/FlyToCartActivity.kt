package com.custom.customflytocartanimation

import android.R
import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.customflytocartanimation.adapter.ItemsAdapter
import com.custom.customflytocartanimation.databinding.ActivityFlyToCartBinding
import com.custom.customflytocartanimation.model.CartModel
import com.custom.customflytocartanimation.util.AnimationUtil


class FlyToCartActivity : AppCompatActivity() {

    private var ratingList = mutableListOf<CartModel>()
    private lateinit var binding: ActivityFlyToCartBinding
    lateinit var itemAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlyToCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        addedItem()
        initRecyclerView()
    }

    private fun addedItem() {
        ratingList = CartModel(
            id = 1,
            message = "This is Test", R.drawable.ic_android
        )

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvCartView.layoutManager = layoutManager
        itemAdapter =
            ItemsAdapter(this, ratingList) {
                makeFlyAnimation()
                Toast.makeText(this, "Click Here", Toast.LENGTH_SHORT).show()
            }
        binding.rcvCartView.adapter = itemAdapter
    }

    private fun makeFlyAnimation(targetView: ImageView) {
        val destView = binding.ivmCartLogo
        AnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000)
            .setDestView(destView).setAnimationListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
//                    addItemToCart()
                    Toast.makeText(
                        this@FlyToCartActivity,
                        "Continue Shopping...",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }).startAnimation()
    }
}