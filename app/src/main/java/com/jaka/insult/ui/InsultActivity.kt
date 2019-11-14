package com.jaka.insult.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jaka.domain.model.InsultModel
import com.jaka.domain.model.InsultStatus
import com.jaka.insult.R
import com.jaka.insult.presenters.InsultPresenter
import com.jaka.insult.views.InsultView
import kotlinx.android.synthetic.main.a_insult.*
import org.koin.android.ext.android.inject

class InsultActivity : AppCompatActivity(), InsultView {
    override fun showInsult(insultModel: InsultModel) {
        when (insultModel.status) {
            InsultStatus.IN_PROGRESS -> {
                insult_text.text = ""
                insult_text.visibility = View.GONE
                insult_progress.visibility = View.VISIBLE
                get_insult_button.isEnabled = false
            }
            InsultStatus.COMPLETED -> {
                insult_progress.visibility = View.GONE
                insult_text.text = insultModel.insult.insult
                insult_text.visibility = View.VISIBLE
                get_insult_button.isEnabled = true
            }
            InsultStatus.ERROR->{
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
                insult_progress.visibility = View.GONE
                insult_text.visibility = View.GONE
                get_insult_button.isEnabled = true
            }
        }
    }

    private val firstPresenter: InsultPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_insult)
        firstPresenter.attach(this)
        get_insult_button.setOnClickListener {
            firstPresenter.insult()
        }
    }
}