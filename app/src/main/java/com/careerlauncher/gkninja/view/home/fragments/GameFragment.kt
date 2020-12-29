package com.careerlauncher.gkninja.view.home.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.adapter.BannerPagerAdapter
import com.careerlauncher.gkninja.base.BaseFragment
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.pojo.GameRuleListModel
import com.careerlauncher.gkninja.pojo.LevelsListModel
import com.careerlauncher.gkninja.pojo.sorter.SortByPoints
import com.careerlauncher.gkninja.utils.AppConstants
import com.careerlauncher.gkninja.utils.AutoScrollViewPager
import com.careerlauncher.gkninja.view.home.HomePresenter
import com.careerlauncher.gkninja.view.home.HomeView
import com.careerlauncher.gkninja.view.home.adapterrecycler.GameRuleAdapter
import com.careerlauncher.gkninja.view.home.adapterrecycler.LevelsAdapter
import com.careerlauncher.gkninja.view.quizscreen.QuizActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by Arman Reyaz on 12/19/2020.
 */
class GameFragment : BaseFragment(),HomeView {

    var vpBanner: AutoScrollViewPager? = null
    var dotsIndicator: WormDotsIndicator? = null
    var title: TextView? = null
    var description: TextView? = null
    var startQuiz: TextView? = null
    var mHomePresenter:HomePresenter?=null
    var dialog:BottomSheetDialog?=null
    var gameRuleListModel:MutableList<GameRuleListModel>?=null
    lateinit var ultimateNinjaJSON:JSONObject

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    var handler: Handler? = null
    var vpRunnable: Runnable? = null
    var NUM_PAGES = 4

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.fragment_game,container,false)
        vpBanner=view.findViewById(R.id.vp_banner)
        dotsIndicator=view.findViewById(R.id.dots_indicator)
        title=view.findViewById(R.id.title)
        description=view.findViewById(R.id.description)
        startQuiz=view.findViewById(R.id.startQuiz)
        mHomePresenter= HomePresenter(this)
        val bannerList: MutableList<Int> = ArrayList()
        bannerList.add(R.drawable.nin_ban_1)
        bannerList.add(R.drawable.nin_ban_2)
        bannerList.add(R.drawable.nin_ban_3)
        bannerList.add(R.drawable.nin_ban_4)
        val pagerAdapter = BannerPagerAdapter(bannerList)

        vpBanner!!.startAutoScroll()
        vpBanner!!.setInterval(3000)
        vpBanner!!.setCycle(true)
        vpBanner!!.setStopScrollWhenTouch(true)

        vpBanner!!.setAdapter(pagerAdapter)
        dotsIndicator!!.setViewPager(vpBanner)

        startQuiz?.setOnClickListener {
            mHomePresenter?.getRankData()
        }

        return view
    }
    override fun initVariables() {
        gameRuleListModel=ArrayList()
    }

    override fun setListeners() {
    }

    override fun setUpBottomNavigationView() {
        TODO("Not yet implemented")
    }

    override fun showLoginSignUp() {
        TODO("Not yet implemented")
    }

    override fun showErrorMessage() {
        TODO("Not yet implemented")
    }

    override fun registerAppSubmit(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun getAppVersion(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun handleDashboard() {
        TODO("Not yet implemented")
    }

    override fun setUpRankData(jsonObject: JSONObject?) {
        gameRuleListModel?.clear()
        DataManager(activity as Context).saveStringInPreference(PrefKeys.HOME_PAGE_JSON_DATA, jsonObject.toString())
        try {
            val ruleICONArray =
                jsonObject!!.getJSONObject("GK Ninza Game").getJSONObject("gameRules")
                    .getJSONArray("gameBodyIconURLs")
            val ruleTextArray =
                jsonObject!!.getJSONObject("GK Ninza Game").getJSONObject("gameRules")
                    .getJSONArray("gameBody")
            for (i in 0 until ruleICONArray.length()) {
                val model =
                    GameRuleListModel(ruleICONArray.getString(i), ruleTextArray.getString(i))
                gameRuleListModel?.add(model)
            }
            showBottomDialog("Rules")
            } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
    fun showBottomDialog(type: String) {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_list_pop_up, null)
            dialog = BottomSheetDialog(activity as Context)
            val label = dialogView.findViewById<TextView>(R.id.reportErrorLabel)
            val startQuiz = dialogView.findViewById<TextView>(R.id.startQuiz)
            val close =
                dialogView.findViewById<ImageView>(R.id.close)
            label.text = type
            val qNoList: RecyclerView = dialogView.findViewById(R.id.qNoList)
            if (type.contains("Rules")) {
                startQuiz.visibility = View.VISIBLE
                val adapter = gameRuleListModel?.let { GameRuleAdapter(activity as Context, it) }
                qNoList.layoutManager = LinearLayoutManager(activity)
                qNoList.adapter = adapter
                startQuiz.setOnClickListener { mHomePresenter?.getGKQuizQuestion("new", "2", "1", "") }
            }
            close.setOnClickListener { dialog!!.cancel() }
            dialog!!.setContentView(dialogView)
            // dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
            // dialog.window?.findViewById<>(R.id.des)
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    override fun setUpGameQuestion(jsonObject: JSONObject?) {
        val questJSON: JSONObject?
        var level: String? = ""
        var games: String? = ""
        var point: String? = ""
        try {
            level = jsonObject!!.getJSONObject("data").getString("gameLevel")
            games = jsonObject!!.getJSONObject("data").getString("games")
            point = jsonObject!!.getJSONObject("data").getString("points")
            questJSON = jsonObject!!.getJSONObject("data").getJSONObject("formData")
            if (dialog != null) dialog!!.cancel()
            if (questJSON != null) {
                 val intent = Intent(activity, QuizActivity::class.java)
                 intent.putExtra(AppConstants.KEY_JSON_OBJECT, questJSON.toString())
                 intent.putExtra("LEVEL", level)
                 intent.putExtra("GAMES", games)
                 intent.putExtra("POINT", point)
                 intent.putExtra("RANK_NAME", "GK Ninja")
                 startActivity(intent)
            }
        } catch (e: JSONException) {
            if (dialog != null) dialog!!.cancel()
            showSnackBar("Something goes wrong! Please try again")
            e.printStackTrace()
        }

    }

    override fun setUpData(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun showSnackBar(message: String?, view: View?) {
    }
}