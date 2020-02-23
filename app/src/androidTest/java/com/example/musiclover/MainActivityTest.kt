package com.example.musiclover

import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainActivityTest {

    @get:Rule
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    var mActivity : MainActivity? = null


    @Before
    fun setUp() {
        mActivity = mActivityRule.activity
    }

    @Test
    fun testLaunch() {
        val view = mActivity?.recycler_view
        assertNotNull(view)
    }

    @Test
    fun testLaunchofAlbumDetailsActivityOnClick() {
        val view = mActivity?.recycler_view
        assertNotNull(view)
    }

    @After
    fun tearDown() {
        mActivity = null
    }
}