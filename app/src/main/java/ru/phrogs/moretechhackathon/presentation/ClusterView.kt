package ru.phrogs.moretechhackathon.presentation

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import ru.phrogs.moretechhackathon.R

class ClusterView(context: Context) : LinearLayout(context) {

    private val amountText by lazy { findViewById<TextView>(R.id.countText) }

    init {
        inflate(context, R.layout.clusterview, this)
    }

    fun setData(pointsAmount: Int) {
        amountText.text = pointsAmount.toString()
    }

}