package com.careerlauncher.gkninja.pojo

import java.util.*

class QuizModel {
    var questionId = 0
    var seqId: String? = null
    var jsonData: String? = null
    var sectionName: String? = null
    var questioNumber: String? = null
    var question: String? = null
    var questionHindi: String? = null
    var topicName: String? = null
    var markedAnswer = ""
    var instruction = ""
    var instructionHindi = ""
    var type = ""
    var solution = ""
    var correctAnswer = ""
    var attemptedAnswer = ""
    var answerRemark = ""
    var answerStatus = ""
    var answerId = ""
    var bookMarkQuestionId = ""
    var submitQuestionId = ""
    var timeQuestionId = ""
    var timeTaken = ""
    var isSkipped = false
    var isMarked = false
    var isQuestionTrue = false
    var isAnswered = false
    var isRead = false
    var isBookmarked = false

    var correctOptionId = 0
    var options: ArrayList<OptionsModel>? = null
}