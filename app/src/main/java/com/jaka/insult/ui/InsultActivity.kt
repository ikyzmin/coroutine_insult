package com.jaka.insult.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jaka.insult.R
import com.jaka.insult.presenters.InsultPresenter
import com.jaka.insult.views.InsultView
import kotlinx.android.synthetic.main.a_insult.*
import org.koin.android.ext.android.inject

class InsultActivity : AppCompatActivity(), InsultView {
    override fun showInsult(insult: String) {
        insult_text.text = insult
        insult_text.visibility=View.VISIBLE
        get_insult_button.isEnabled = true
        insult_progress.visibility= View.GONE
    }

    private val firstPresenter: InsultPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_insult)
        firstPresenter.attach(this)
        get_insult_button.setOnClickListener {
            firstPresenter.insult()
            get_insult_button.isEnabled = false
            insult_progress.visibility= View.VISIBLE
            insult_text.visibility=View.GONE
        }
    }
}