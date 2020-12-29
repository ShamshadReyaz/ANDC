package com.careerlauncher.gkninja.view.home.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseFragment
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.pojo.GameRuleListModel
import com.careerlauncher.gkninja.pojo.LevelsListModel
import com.careerlauncher.gkninja.pojo.RankModel
import com.careerlauncher.gkninja.pojo.sorter.SortByPoints
import com.careerlauncher.gkninja.utils.AppConstants
import com.careerlauncher.gkninja.view.home.HomePresenter
import com.careerlauncher.gkninja.view.home.HomeView
import com.careerlauncher.gkninja.view.home.adapterrecycler.GameRuleAdapter
import com.careerlauncher.gkninja.view.home.adapterrecycler.LevelsAdapter
import com.careerlauncher.gkninja.view.home.adapterrecycler.RankAdapter
import com.careerlauncher.gkninja.view.quizscreen.QuizActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Arman Reyaz on 12/19/2020.
 */
class DashboardFragment : BaseFragment(),HomeView {

    lateinit var dialog: BottomSheetDialog
    lateinit var pointsScored: String
    var pullToRefresh:SwipeRefreshLayout?=null
    var ultimateNinja:TextView?=null
    var played:TextView?=null
    var pointsEarned:TextView?=null
    var gameRules:TextView?=null
    var rankTypes:RadioGroup?=null
    var rankList:RecyclerView?=null
    var mHomePresenter:HomePresenter?=null
    lateinit var ultimateNinjaJSON:JSONObject
    var rankAdapter:RankAdapter?=null
    var rankListModel:MutableList<RankModel>?=null
    var gameRuleListModel:MutableList<GameRuleListModel>?=null
    var levelListModel:MutableList<LevelsListModel>?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.fragment_dashboard,container,false)
        pullToRefresh=view.findViewById(R.id.pullToRefresh)
        ultimateNinja=view.findViewById(R.id.ultimateNinja)
        played=view.findViewById(R.id.played)
        pointsEarned=view.findViewById(R.id.points)
        gameRules=view.findViewById(R.id.gameRules)
        rankTypes=view.findViewById(R.id.rankTypes)
        rankList=view.findViewById(R.id.ranklist)
        mHomePresenter=HomePresenter(this)
        mHomePresenter?.getRankData()
        rankTypes?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { rankTypes, checkedId ->
            val radioButton:RadioButton =   view.findViewById(checkedId)
            setUpRankListView(radioButton.text.toString())
        })

        pullToRefresh?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                mHomePresenter?.getRankData()
        })

        ultimateNinja?.setOnClickListener {
            showBottomDialog("Game Levels")
        }
        gameRules?.setOnClickListener {
            showBottomDialog("Game Rules")
        }
        return view
    }
    override fun initVariables() {
        rankListModel=ArrayList()
        gameRuleListModel=ArrayList()
        levelListModel=ArrayList()

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
            if(pullToRefresh!=null && pullToRefresh!!.isRefreshing){
                pullToRefresh!!.isRefreshing==false
            }
        gameRuleListModel?.clear()
        DataManager(activity as Context).saveStringInPreference(PrefKeys.HOME_PAGE_JSON_DATA, jsonObject.toString())
        try {
            played!!.text =
                "Played: " + jsonObject!!.getJSONObject("GK Ninza Game").getString("played")
            pointsEarned?.setText(
                "Points: " + jsonObject!!.getJSONObject("GK Ninza Game").getString("points")
            )
            pointsScored = jsonObject!!.getJSONObject("GK Ninza Game").getString("points")
            ultimateNinja!!.text = jsonObject!!.getJSONObject("GK Ninza Game").getString("level")
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
            ultimateNinjaJSON =
                jsonObject!!.getJSONObject("GK Ninza Game").getJSONObject("allLevels")
            setUpRankListView("WEEKLY")
        } catch (e: JSONException) {
            e.printStackTrace()
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
            if (dialog != null) dialog.cancel()
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
            if (dialog != null) dialog.cancel()
            showSnackBar("Something goes wrong! Please try again")
            e.printStackTrace()
        }

    }

    override fun setUpData(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    fun setUpRankListView(type:String){
        try {
            rankListModel?.clear()
            val jsonObject =
                JSONObject(DataManager(activity as Context).getStringFromPreference(PrefKeys.HOME_PAGE_JSON_DATA))
            var mainJSONObj: JSONObject? = null
            mainJSONObj = jsonObject.getJSONObject("Gk Ninza Leaderboard Rankings")
            if (mainJSONObj != null) {
                if (type == "WEEKLY") {
                    val weekAboveArray = mainJSONObj.getJSONArray("weekAbove")
                    val weekBelowArray = mainJSONObj.getJSONArray("weekBelow")
                    for (i in 0 until weekAboveArray.length()) {
                        val obj = weekAboveArray.getJSONObject(i)
                        val a = RankModel(
                            obj.getString("ranking"),
                            obj.getString("genderImage"),
                            obj.getString("username"),
                            obj.getString("coins"),
                            obj.getString("rankState"),
                            obj.getString("status")
                        )
                        rankListModel?.add(a)
                    }
                    val arr =
                        mainJSONObj.getString("myWeeklyRanking").split("/".toRegex()).toTypedArray()
                    val b = DataManager(activity as Context).getStringFromPreference(PrefKeys.USER_NAME)?.let {
                        RankModel(
                            arr[0],
                            mainJSONObj.getString("myGenderImage"),
                            it,
                            mainJSONObj.getString("myWeeklyCoins"),
                            "",
                            ""
                        )
                    }
                    if (b != null) {
                        rankListModel?.add(b)
                    }
                    for (i in 0 until weekBelowArray.length()) {
                        val obj = weekBelowArray.getJSONObject(i)
                        val c = RankModel(
                            obj.getString("ranking"),
                            obj.getString("genderImage"),
                            obj.getString("username"),
                            obj.getString("coins"),
                            obj.getString("rankState"), obj.getString("status")
                        )
                        rankListModel?.add(c)
                    }
                } else {
                    val monthAboveArray = mainJSONObj.getJSONArray("monthAbove")
                    val monthBelowArray = mainJSONObj.getJSONArray("monthBelow")
                    for (i in 0 until monthAboveArray.length()) {
                        val obj = monthAboveArray.getJSONObject(i)
                        val a = RankModel(
                            obj.getString("ranking"),
                            obj.getString("genderImage"),
                            obj.getString("username"),
                            obj.getString("coins"),
                            obj.getString("rankState"), obj.getString("status")
                        )
                        rankListModel?.add(a)
                    }
                    val arr2 =
                        mainJSONObj.getString("myMonthlyRanking").split("/".toRegex())
                            .toTypedArray()
                    val d = DataManager(activity as Context).getStringFromPreference(PrefKeys.USER_NAME)?.let {
                        RankModel(
                            arr2[0],
                            mainJSONObj.getString("myGenderImage"),
                            it,
                            mainJSONObj.getString("myMonthlyCoins"),
                            "", ""
                        )
                    }
                    if (d != null) {
                        rankListModel?.add(d)
                    }
                    for (i in 0 until monthBelowArray.length()) {
                        val obj = monthBelowArray.getJSONObject(i)
                        val c = RankModel(
                            obj.getString("ranking"),
                            obj.getString("genderImage"),
                            obj.getString("username"),
                            obj.getString("coins"),
                            obj.getString("rankState"), obj.getString("status")
                        )
                        rankListModel?.add(c)
                    }
                }
                //rankAdapter?.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(activity)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                rankList?.layoutManager = layoutManager
                rankAdapter= RankAdapter(activity as Context, rankListModel as ArrayList<RankModel>)
                rankList?.adapter = rankAdapter

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun showSnackBar(message: String?, view: View?) {
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
            } else {
                levelListModel?.clear()
                val data: MutableList<String> =
                    java.util.ArrayList()
                val data2: MutableList<String> =
                    java.util.ArrayList()
                val keys = ultimateNinjaJSON.keys()
                while (keys.hasNext()) {
                    data.add(keys.next())
                }
                for (i in data.indices) {
                    data2.add(ultimateNinjaJSON.getString(data[i]))
                }
                for (i in data.indices) {
                    val model = LevelsListModel(data2[i], data[i])
                    levelListModel?.add(model)
                }
                Collections.sort(levelListModel, SortByPoints())
                val mLevelsAdapter =
                    levelListModel?.let { LevelsAdapter(activity as Context, it, pointsScored) }
                qNoList.layoutManager = LinearLayoutManager(activity)
                qNoList.adapter = mLevelsAdapter
            }
            close.setOnClickListener { dialog.cancel() }
            dialog.setContentView(dialogView)
           // dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
           // dialog.window?.findViewById<>(R.id.des)
            dialog.show()
        } catch (e: Exception) {
        }
    }


}