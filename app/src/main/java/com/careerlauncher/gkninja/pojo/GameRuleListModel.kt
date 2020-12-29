package com.careerlauncher.gkninja.pojo

import android.os.Parcel
import android.os.Parcelable

class GameRuleListModel  {
    var iconURL: String?
    var title: String?

    constructor(iconURL: String?, title: String?) {
        this.iconURL = iconURL
        this.title = title
    }
}