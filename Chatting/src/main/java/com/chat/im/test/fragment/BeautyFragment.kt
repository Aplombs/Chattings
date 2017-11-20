package com.chat.im.test.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chat.im.R
import com.chat.im.helper.LogHelper
import kotlinx.android.synthetic.main.fragment_test_kotlin_beauty.*


class BeautyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_kotlin_beauty, null)
        //ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        animation_test_button?.setOnClickListener {

            tip_view_news_test.visibility = View.VISIBLE

            val height = tip_view_news_test.height
            LogHelper.e("gone height:" + height)
            val animator = ObjectAnimator.ofFloat(root_layout_test, "translationY", 0f, -height.toFloat())
            //animator.interpolator = LinearInterpolator()
            animator.duration = 800
            animator.start()

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    tip_view_news_test.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })

//            Handler().postDelayed({
//                tip_view_news_test.visibility = View.GONE
//            }, 200)
        }
    }
}