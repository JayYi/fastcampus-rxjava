package com.maryang.fastrxjava.ui.repo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maryang.fastrxjava.R
import kotlinx.android.synthetic.main.activity_github_repo.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk21.listeners.onClick

class GithubRepoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repo)

        star.onClick {

        }
    }

    companion object {
        private const val KEY_ID = "KEY_ID"

        fun start(context: Context, id: Long) {
            context.startActivity(
                context.intentFor<GithubRepoActivity>(
                    KEY_ID to id
                )
            )
        }
    }

}