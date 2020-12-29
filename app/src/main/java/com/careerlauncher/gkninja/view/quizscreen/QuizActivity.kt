package com.careerlauncher.gkninja.view.quizscreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseActivity
import com.careerlauncher.gkninja.dialogs.EndQuizConfirmationDialog
import com.careerlauncher.gkninja.dialogs.StreakCompletedDialog
import com.careerlauncher.gkninja.dialogs.WrongAnswerDialog
import com.careerlauncher.gkninja.interfaces.DialogListener
import com.careerlauncher.gkninja.interfaces.RecyclerItemClickListener
import com.careerlauncher.gkninja.pojo.FailureResponse
import com.careerlauncher.gkninja.pojo.OptionsModel
import com.careerlauncher.gkninja.pojo.QuizModel
import com.careerlauncher.gkninja.utils.AppConstants
import com.careerlauncher.gkninja.view.home.HomePresenter
import com.careerlauncher.gkninja.view.home.HomeView
import com.careerlauncher.gkninja.view.quizscreen.adapter.OptionAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class QuizActivity : BaseActivity(), HomeView {

    private var PERMISSION_REQUEST = 1

    var tvQuestionNo: TextView? = null
    private var timer: TextView? = null
    var tvQuestion: TextView? = null
    var rvOptions: RecyclerView? = null
    var closeTest: TextView? = null
    var btn_next: TextView? = null
    var played: TextView? = null
    var points: TextView? = null
    var currentPoint: TextView? = null
    var labelLayout: LinearLayout? = null
    var progressBarCircle: ProgressBar? = null

    private var mPresenter: HomePresenter? = null
    private var selectedOptionId = -1
    private var selectedOptionPosition = -1
    private var mQuestionList: ArrayList<QuizModel>? = null
    private var mOptionsList: ArrayList<OptionsModel>? = null
    private var mOptionsAdapter_GK: OptionAdapter? = null

    private var mPosition = 0
    private var wrongAnswerDialog: WrongAnswerDialog? = null
    private var streakCompletedDialog: StreakCompletedDialog? = null
    private var endQuizConfirmationDialog: EndQuizConfirmationDialog? = null
    private var streakCount = 0
    private var questJSON: JSONObject? = null
    private var questionCount = 1
    private var qsetId = ""
    private var questionId = ""
    private var correctOption = ""
    private var sequenceNumber = 1
    private var quizType = ""
    private var testStatus = ""
    private var countDown: CountDown? = null
    private var status = 0
    private var currentScore = 0
    private var streakCompleted = false
    private var questionResultType = ""
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gk_ninja_quiz_2)
        mPresenter = HomePresenter(this)
        tvQuestion = findViewById(R.id.tv_question)
        tvQuestionNo = findViewById(R.id.tv_question_no)
        timer = findViewById(R.id.timer)
        rvOptions = findViewById(R.id.rv_options)
        closeTest = findViewById(R.id.tv_close_test)
        btn_next = findViewById(R.id.btn_next)
        played = findViewById(R.id.played)
        points = findViewById(R.id.points)
        currentPoint = findViewById(R.id.currentPoint)
        labelLayout = findViewById(R.id.labelLayout)
        progressBarCircle = findViewById(R.id.progressBarCircle)
        try {
            questJSON = JSONObject(intent.extras!!.getString(AppConstants.KEY_JSON_OBJECT))
            //ultimateNinja.setText(getIntent().getExtras().getString("LEVEL"));
            played!!.text = intent.extras!!.getString("GAMES")
            points!!.text = intent.extras!!.getString("POINT")
            questionId = questJSON!!.getJSONObject("formQuestionObj").getString("quesId")
            correctOption = questJSON!!.getJSONObject("formQuestionObj").getString("correctOption")
            qsetId = questJSON!!.getJSONObject("formQuestionObj").getString("qsetId")
            // sequenceNumber = questJSON.getJSONObject("formParameterObj").getString("seqNo");
            quizType = questJSON!!.getJSONObject("formParameterObj").getString("typ")
            testStatus = questJSON!!.getJSONObject("formParameterObj").getString("test")
            tvQuestionNo!!.text = "Question #$questionCount"
            tvQuestion!!.text = HtmlCompat.fromHtml(
                questJSON!!.getJSONObject("formQuestionObj").getString("questionText"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            val optionArray =
                questJSON!!.getJSONObject("formQuestionObj").getJSONArray("questionOption")
            for (i in 0 until optionArray.length()) {
                if (optionArray.getJSONObject(i).getString("optionText") == "null") {
                } else {
                    val option = OptionsModel()
                    option.option = optionArray.getJSONObject(i).getString("optionText")
                    val j = i + 1
                    option.optionId = j
                    mOptionsList!!.add(option)
                }
            }
            rvOptions!!.layoutManager = LinearLayoutManager(this)
            rvOptions!!.adapter = mOptionsAdapter_GK
            mp = MediaPlayer.create(this, R.raw.clock)
            countDown = CountDown(
                "30".toInt() * 1000,
                1000
            )
            countDown?.start()
            mp?.start()
            mp?.setLooping(true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        btn_next?.setOnClickListener {
            nextOnClickListen()
        }

        closeTest?.setOnClickListener {
            closeClickListen()
        }
    }

    override fun initVariables() {
        mQuestionList = ArrayList()
        mOptionsList = ArrayList()
        mOptionsAdapter_GK =
            OptionAdapter(this, mOptionsList!!, object : RecyclerItemClickListener {
                override fun onRecyclerItemClicked(position: Int) {
                    try {
                        for (i in mOptionsList!!.indices) {
                            mOptionsList!![i].isSelected = false
                        }
                        selectedOptionPosition = position
                        val corrAnswer: String? = mOptionsList!![position].optionStringId
                        mOptionsList!![position].isSelected = true
                        mOptionsAdapter_GK!!.notifyDataSetChanged()
                        selectedOptionId = mOptionsList!![position].optionId
                        btn_next!!.visibility = View.VISIBLE
                    } catch (e: ArrayIndexOutOfBoundsException) {
                    }
                }
            })
        endQuizConfirmationDialog = EndQuizConfirmationDialog(this, object : DialogListener {
            override fun onPositiveButtonClick() {
                if (countDown != null) {
                    countDown?.cancel()
                }
                finish()
            }

            override fun onNegativeButtonClick() {}
        })
        wrongAnswerDialog = WrongAnswerDialog(this, object : DialogListener {
            override fun onPositiveButtonClick() {
                questionCount = 1
                streakCount = 0
                sequenceNumber = 1
                refreshQuiz()
            }

            override fun onNegativeButtonClick() {
                finish()
            }
        })

        streakCompletedDialog = StreakCompletedDialog(this, object : DialogListener {
            override fun onPositiveButtonClick() {
                questionCount = 1
                streakCount = 0
                refreshQuiz()
            }

            override fun onNegativeButtonClick() {
                finish()
            }
        })
    }

    override fun setListeners() {
    }

    override val resourceId: Int
        get() = R.layout.activity_gk_ninja_quiz_2

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

    override fun onBackPressed() {
        endQuizConfirmationDialog!!.show()
        endQuizConfirmationDialog!!.setData("Close Test!", "Are you sure you want to stop playing?")
        endQuizConfirmationDialog!!.setPositiveButtonText("CLOSE")
        endQuizConfirmationDialog!!.setNegativeButtonText("CANCEL")

    }

    override fun setUpRankData(jsonObject: JSONObject?) {
        try {
            if (!(this as Activity).isFinishing) {
                if (streakCompleted == true) {
                    //      streakCompletedDialog.show();
                    showStreakComplGameDialog(currentScore.toString())
                }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    override fun setUpGameQuestion(jsonObject: JSONObject?) {
        btn_next!!.visibility = View.GONE

        try {
            selectedOptionId = -1
            questJSON = jsonObject!!.getJSONObject("data").getJSONObject("formData")
            //ultimateNinja.setText(jsonObject.getJSONObject("data").getString("gameLevel"));
            played!!.text = jsonObject!!.getJSONObject("data").getString("games")
            points!!.text = jsonObject!!.getJSONObject("data").getString("points")
            questionId = questJSON?.getJSONObject("formQuestionObj")!!.getString("quesId")
            correctOption = questJSON?.getJSONObject("formQuestionObj")!!.getString("correctOption")
            qsetId = questJSON?.getJSONObject("formQuestionObj")!!.getString("qsetId")
            //    sequenceNumber = questJSON.getJSONObject("formParameterObj").getString("seqNo");
            quizType = questJSON?.getJSONObject("formParameterObj")!!.getString("typ")
            testStatus = questJSON?.getJSONObject("formParameterObj")!!.getString("test")
            tvQuestionNo!!.text = "Question #$questionCount"
            tvQuestion!!.text = HtmlCompat.fromHtml(
                questJSON?.getJSONObject("formQuestionObj")!!.getString("questionText"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            val optionArray =
                questJSON?.getJSONObject("formQuestionObj")!!.getJSONArray("questionOption")
            mOptionsList!!.clear()
            for (i in 0 until optionArray.length()) {
                val j = i + 1
                val option = OptionsModel()
                option.option = optionArray.getJSONObject(i).getString("optionText")
                option.optionId = j
                mOptionsList!!.add(option)
            }
            mOptionsAdapter_GK!!.notifyDataSetChanged()
            countDown = CountDown(
                "30".toInt() * 1000,
                1000
            )
            countDown?.start()
            mp!!.start()
            mp!!.isLooping = true
            Log.e("CURRENT_SCORE", "" + currentScore)
        } catch (e: JSONException) {
            btn_next!!.text = "Something goes wrong! Please re-open the quiz."
            btn_next!!.isEnabled = false
            btn_next!!.setBackgroundResource(R.drawable.rectangle_background_gray)
            e.printStackTrace()
        }
    }

    override fun setUpData(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun showSpecificError(failureResponse: FailureResponse?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.release()
    }

    fun nextOnClickListen() {
        if (mp != null && mp!!.isPlaying) {
            mp!!.stop()
            mp = MediaPlayer.create(this, R.raw.clock)
        }


        try {
            if (countDown != null) {
                countDown?.cancel()
            }
            Log.e("CORECT ANSWER", "" + correctOption)
            if (selectedOptionId == correctOption.toInt()) {
                currentScore = currentScore + 10
                currentPoint!!.text = "" + currentScore
                questionResultType = "WIN"
                mOptionsList!![selectedOptionPosition].isNeutral = false
                mOptionsList!![selectedOptionPosition].isAttemptedAnswer = true
                mOptionsList!![selectedOptionPosition].isCorrectAnswer = true
                Handler(Looper.getMainLooper()).postDelayed({
                    streakCount++
                    questionCount++
                    sequenceNumber++
                    if (streakCount >= 15) {
                        streakCompleted = true
                        submitQuiz("1")
                    } else {
                        getNextQues()
                    }
                }, 1000)
            } else {
                questionResultType = "LOOSE"
                val i = correctOption.toInt() - 1
                mOptionsList!![selectedOptionPosition].isNeutral = false
                mOptionsList!![i].isNeutral = false
                mOptionsList!![selectedOptionPosition].isAttemptedAnswer = true
                mOptionsList!![selectedOptionPosition].isCorrectAnswer = false
                mOptionsList!![i].isCorrectAnswer = true
                mOptionsList!![i].isAttemptedAnswer = true
                mOptionsList!![i].isSelected = true
                Handler(Looper.getMainLooper()).postDelayed({
                    var ans: String? = ""
                    var optNumber = ""
                    val i = correctOption.toInt() - 1
                    if (correctOption == "1") {
                        optNumber = "A"
                        ans = mOptionsList!![i].option
                    } else if (correctOption == "2") {
                        optNumber = "B"
                        ans = mOptionsList!![i].option
                    } else if (correctOption == "3") {
                        optNumber = "C"
                        ans = mOptionsList!![i].option
                    } else if (correctOption == "4") {
                        optNumber = "D"
                        ans = mOptionsList!![i].option
                    }
                    var success = ""
                    success = if (sequenceNumber > 1) "1" else "0"
                    submitQuiz(success)
                    if (ans != null) {
                        showBottomGameOverDialog(
                            "Wrong Answer",
                            "Wrong Answer", currentScore.toString(), streakCount.toString(),
                            optNumber,
                            ans,
                            tvQuestion!!.text.toString()
                        )
                    }
                    //  wrongAnswerDialog.setCanceledOnTouchOutside(false);
                    currentScore = 0
                }, 1000)
            }
            mOptionsAdapter_GK!!.notifyDataSetChanged()
        } catch (e: Exception) {
        }

    }

    fun closeClickListen() {
        endQuizConfirmationDialog?.show()
        endQuizConfirmationDialog?.setData("Close Test!", "Are you sure you want to stop playing?")
        endQuizConfirmationDialog?.setPositiveButtonText("CLOSE")
        endQuizConfirmationDialog?.setNegativeButtonText("CANCEL")
    }

    inner class CountDown(millisInFuture: Int, countDownInterval: Long) :
        CountDownTimer(millisInFuture.toLong(), countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val text = String.format(
                "%02d",
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                )
            )
            timer?.setText(text)
            // mp.start();
            progressBarCircle?.setProgress((millisUntilFinished / 1000).toInt())
        }

        override fun onFinish() {
            timer?.setText("Time Up")
            if (mp != null && mp!!.isPlaying()) {
                mp?.stop()
                mp = MediaPlayer.create(baseContext, R.raw.clock)
            }
            try {
                if (selectedOptionId == correctOption.toInt()) {
                    currentScore = currentScore + 10
                    currentPoint?.setText("" + currentScore)
                    questionResultType = "WIN"
                    mOptionsList?.get(selectedOptionPosition)?.isNeutral = false
                    mOptionsList?.get(selectedOptionPosition)?.isAttemptedAnswer = true
                    mOptionsList?.get(selectedOptionPosition)?.isCorrectAnswer = true
                    Handler(Looper.getMainLooper())
                        .postDelayed({
                            streakCount++
                            questionCount++
                            sequenceNumber++
                            if (streakCount >= 15) {
                                streakCompleted = true
                                submitQuiz("1")
                            } else {
                                getNextQues()
                            }
                        }, 1000)
                } else {
                    val i: Int = correctOption.toInt() - 1
                    questionResultType = "LOOSE"
                    if (selectedOptionId == -1) {
                    } else {
                        mOptionsList?.get(selectedOptionPosition)?.isNeutral = false
                        mOptionsList?.get(selectedOptionPosition)?.isAttemptedAnswer = true
                        mOptionsList?.get(selectedOptionPosition)?.isCorrectAnswer = false
                    }
                    mOptionsList?.get(i)?.isNeutral = false
                    mOptionsList?.get(i)?.isCorrectAnswer = true
                    mOptionsList?.get(i)?.isAttemptedAnswer = true
                    mOptionsList?.get(i)?.isSelected = true

                    if (endQuizConfirmationDialog?.isShowing == true) {
                    } else {
                        if (!isFinishing) {
                            //wrongAnswerDialog.show();
                            var ans = ""
                            var optNumber = ""
                            if (correctOption == "1") {
                                optNumber = "A"
                                ans = mOptionsList?.get(i)!!.option
                            } else if (correctOption == "2") {
                                optNumber = "B"
                                ans = mOptionsList?.get(i)!!.option
                            } else if (correctOption == "3") {
                                optNumber = "C"
                                ans = mOptionsList?.get(i)!!.option
                            } else if (correctOption == "4") {
                                optNumber = "D"
                                ans = mOptionsList?.get(i)!!.option
                            }
                            var success = ""
                            success = if (selectedOptionId == -1) "-1" else {
                                if (sequenceNumber > 1) "1" else "0"
                            }
                            submitQuiz(success)
                            showBottomGameOverDialog(
                                "Time Up",
                                "Time Up!", currentScore.toString(), streakCount.toString(),
                                optNumber,
                                ans,
                                tvQuestion?.getText().toString()
                            )
                            // wrongAnswerDialog.setCanceledOnTouchOutside(false);
                        }
                    }
                    currentScore = 0
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun refreshQuiz() {
        if (intent.extras!!.getString("RANK_NAME")!!.contains("GK")) {
            mPresenter?.getGKQuizQuestion("new", "2", "", "")
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Legal")) {
            mPresenter?.getLegalQuestion("new", "2", "", "")
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Vocab")) {
            mPresenter?.getVoabQuestion("new", "2", "", "")
        } else {
            mPresenter?.getFactsQuestion("new", "2", "", "")
        }
    }

    private fun getNextQues() {
        if (intent.extras!!.getString("RANK_NAME")!!.contains("GK")) {
            mPresenter?.getGKQuizQuestion("continue", quizType, sequenceNumber.toString(), qsetId)
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Legal")) {
            mPresenter?.getLegalQuestion("continue", quizType, sequenceNumber.toString(), qsetId)
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Vocab")) {
            mPresenter?.getVoabQuestion("continue", quizType, sequenceNumber.toString(), qsetId)
        } else {
            mPresenter?.getFactsQuestion("continue", quizType, sequenceNumber.toString(), qsetId)
        }
    }


    private fun submitQuiz(sucess: String) {
        if (intent.extras!!.getString("RANK_NAME")!!.contains("GK")) {
            mPresenter?.submitGKQuizQuestion(sucess, sequenceNumber.toString(), qsetId)
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Legal")) {
            mPresenter?.submitLegalQuizQuestion(sucess, sequenceNumber.toString(), qsetId)
        } else if (intent.extras!!.getString("RANK_NAME")!!.contains("Fact")) {
            mPresenter?.submitFactsQuizQuestion(sucess, sequenceNumber.toString(), qsetId)
        } else {
            mPresenter?.submitVocabQuestion(sucess, sequenceNumber.toString(), qsetId)
        }
    }

    private fun showBottomGameOverDialog(
        title: String,
        labelVal: String,
        points: String,
        streak: String,
        optioNUmber: String,
        answer: String,
        question: String
    ) {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_wrong_answers_2, null)
            val dialog = BottomSheetDialog(this)
            val label = dialogView.findViewById<TextView>(R.id.reportErrorLabel)
            val parentLayout =
                dialogView.findViewById<RelativeLayout>(R.id.parentLayout)
            val tvQuestion = dialogView.findViewById<TextView>(R.id.tv_question)
            val tvOptionNumber =
                dialogView.findViewById<TextView>(R.id.tv_option_number)
            val tvAnswer = dialogView.findViewById<TextView>(R.id.tv_answer)
            val tvLabelGameOver =
                dialogView.findViewById<TextView>(R.id.tv_label_game_over)
            val subTitle = dialogView.findViewById<TextView>(R.id.subTitle)
            val pointsScored = dialogView.findViewById<TextView>(R.id.points)
            val streakText = dialogView.findViewById<TextView>(R.id.streakText)
            val tvLabelCorrectOption =
                dialogView.findViewById<TextView>(R.id.tv_label_correct_option)
            val btnRestart =
                dialogView.findViewById<Button>(R.id.btn_restart)
            val btnShare =
                dialogView.findViewById<Button>(R.id.btn_share)
            val btnClose = dialogView.findViewById<TextView>(R.id.btn_close)
            val screenshotLayout =
                dialogView.findViewById<LinearLayout>(R.id.shareScreenShotlayout)
            tvLabelGameOver.text = title
            subTitle.text = labelVal
            pointsScored.text = points
            streakText.text = "Your streak lasted $streak questions."
            tvOptionNumber.text = optioNUmber
            tvAnswer.text = answer
            tvQuestion.text = question
            btnClose.setOnClickListener { finish() }
            btnShare.setOnClickListener {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission(screenshotLayout, points)
                } else {
                    try {
                        val bitmap = getScreenShot(screenshotLayout)
                        val uri = getLocalBitmapUri(bitmap)
                        prepareShareIntent(uri, points)
                    } catch (e: java.lang.Exception) {
                    }
                }
            }
            btnRestart.setOnClickListener {
                questionCount = 1
                streakCount = 0
                sequenceNumber = 1
                refreshQuiz()
                dialog.cancel()
            }
            dialog.setContentView(dialogView)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window
                ?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showStreakComplGameDialog(points: String) {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_streak_complete, null)
            val dialog = BottomSheetDialog(this)
            val pointsScored = dialogView.findViewById<TextView>(R.id.points)
            val parentlayout =
                dialogView.findViewById<RelativeLayout>(R.id.parentLayout)
            val btnRestart =
                dialogView.findViewById<Button>(R.id.btn_restart)
            val btnShare =
                dialogView.findViewById<Button>(R.id.btn_share)
            val btnClose = dialogView.findViewById<TextView>(R.id.btn_close)
            val shareScreenlayout =
                dialogView.findViewById<LinearLayout>(R.id.shareScreenShotlayout)
            pointsScored.text = points
            btnClose.setOnClickListener { finish() }
            btnShare.setOnClickListener {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission(shareScreenlayout, points)
                } else {
                    try {
                        val bitmap = getScreenShot(shareScreenlayout)
                        val uri = getLocalBitmapUri(bitmap)
                        prepareShareIntent(uri, points)
                    } catch (e: java.lang.Exception) {
                    }
                }
            }
            btnRestart.setOnClickListener {
                questionCount = 1
                streakCount = 0
                sequenceNumber = 1
                refreshQuiz()
                dialog.cancel()
            }
            dialog.setContentView(dialogView)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window
                ?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog.show()
        } catch (e: java.lang.Exception) {
        }
    }

    fun getScreenShot(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun prepareShareIntent(
        bmpUri: Uri?,
        points: String
    ) {
        val shareIntent = Intent()
        shareIntent.setPackage("com.whatsapp")
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey! I have scored " + points + " on CL GK Ninja. You also download and play. Link: " + AppConstants.PLAY_STORE_URL
        )
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share Opportunity"))
    }

    private fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            try {
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            bmpUri = FileProvider.getUriForFile(
                this,
                this
                    .getPackageName() + ".provider", file
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    private fun checkPermission(view: View, points: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST
            )
        } else {
            try {
                val bitmap = getScreenShot(view)
                val uri = getLocalBitmapUri(bitmap)
                prepareShareIntent(uri, points)
            } catch (e: java.lang.Exception) {
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                // permission denied. show an explanation stating the importance of this permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSION_REQUEST
                )
            }
        }
    }

}