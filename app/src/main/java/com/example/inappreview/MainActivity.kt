package com.example.inappreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inappreview.databinding.ActivityMainBinding
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var reviewInfo: ReviewInfo? = null
    private lateinit var manager: ReviewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        activateReviewInfo()

        binding.inAppReviewButton.setOnClickListener {
            startReviewFlow()
        }
    }

    private fun activateReviewInfo() {
        manager = ReviewManagerFactory.create(this)

        // it will return a task review info
        // this should be called at the correct time
        val managerInfoTask: Task<ReviewInfo> = manager.requestReviewFlow()

        managerInfoTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfo = task.result
            } else {
                Toast.makeText(this, "Review failed to start", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startReviewFlow() {
        if (reviewInfo != null) {
            val flow: Task<Void> = manager.launchReviewFlow(this, reviewInfo!!)
            flow.addOnCompleteListener { task ->
                Toast.makeText(this, "Rating is completed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}