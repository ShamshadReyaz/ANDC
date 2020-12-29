package com.careerlauncher.gkninja.pojo.sorter

import com.careerlauncher.gkninja.pojo.LevelsListModel
import java.util.*

class SortByPoints : Comparator<LevelsListModel> {
    // Used for sorting in ascending order of
    // roll number
    override fun compare(t1: LevelsListModel, t2: LevelsListModel): Int {
        return t1.points.replace("Points", "").trim { it <= ' ' }
            .toInt() - t2.points.replace("Points", "").trim { it <= ' ' }.toInt()
    }
}