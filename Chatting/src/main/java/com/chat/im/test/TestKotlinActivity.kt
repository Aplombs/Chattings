package com.chat.im.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import com.chat.im.R
import com.chat.im.R.drawable.icon_failed
import com.chat.im.test.fragment.BeautyFragment
import com.chat.im.test.fragment.JokeFragment
import com.chat.im.test.fragment.LikeFragment
import com.chat.im.test.fragment.GifFragment
import kotlinx.android.synthetic.main.activity_test_kotlin.*
import kotlinx.android.synthetic.main.title_bar.*


/**
 * test kotlin
 * @author kfzx-tanglitao
 */
class TestKotlinActivity : AppCompatActivity() {

    val images: Array<Int> = arrayOf(icon_failed, icon_failed, icon_failed, icon_failed)
    val titles: Array<String> = arrayOf("段子", "趣图", "美女", "关注")
    val fragments: Array<Fragment> = arrayOf(JokeFragment(), GifFragment(), BeautyFragment(), LikeFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_kotlin)

        init()
    }

    fun init() {
        title_bar_title.text = "朋友圈"
        title_bar_add.visibility = View.GONE
        ll_return_top.visibility = View.VISIBLE

        viewpager_test_kotlin.adapter = FriendPagerAdapter(supportFragmentManager)
        //tab_layout__test_kotlin.tabMode=TabLayout.MODE_FIXED
        //设置自定义tab样式
        //tab_layout__test_kotlin.getTabAt(0)!!.customView = null
        tab_layout__test_kotlin.setupWithViewPager(viewpager_test_kotlin)
    }

    inner class FriendPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getPageTitle(position: Int): CharSequence? {
            val image = resources.getDrawable(images[position])
            image.setBounds(0, 0, 50, 50)
            // Replace blank spaces with image icon
            val sb = SpannableString("   " + titles[position])
            val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return sb
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}