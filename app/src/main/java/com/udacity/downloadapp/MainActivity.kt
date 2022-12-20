package com.udacity.downloadapp

import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.udacity.R
import com.udacity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var linkToDownload: String
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var binding: ActivityMainBinding
    private lateinit var noti: NotificationManager
    private var selectedFile: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        createChannel("channelId", "download notification")


        binding.contentMainView.customButton.setOnClickListener {
//            download()
            when (selectedFile) {
                0 -> Snackbar.make(
                    binding.root, "Nothing selected", Snackbar.LENGTH_SHORT
                ).show()
                1 -> download(Glide_Link)
                2 -> download(URL_Project)
                3 -> download(Retrofit_Link)
            }

        }
//        var animator = ObjectAnimator.ofArgb(
//            binding.contentMainView.customButton,
//            "backgroundColor", Color.BLACK, Color.RED
//        )
//        animator.setDuration(500)
//        animator.repeatCount = 1
//        animator.repeatMode = ObjectAnimator.REVERSE
////        animator.disableViewDuringAnimation(colorizeButton)
//        animator.start()

        noti =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            if (query != null) {
                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL or DownloadManager.STATUS_FAILED)
            }

            val c = downloadManager.query(query)
            if (c.moveToFirst()) {
                var p = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val s = c.getInt(p)
                when (s) {
                    DownloadManager.STATUS_SUCCESSFUL -> noti.sendNoti(
                        linkToDownload,
                        this@MainActivity,
                        "Successful"
                    )
                    DownloadManager.STATUS_FAILED -> noti.sendNoti(
                        linkToDownload,
                        this@MainActivity,
                        "Failed"
                    )
                }


            }

        }
    }

    private fun download(link: String) {
        linkToDownload = link
        val request =
            DownloadManager.Request(Uri.parse(link))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description)).setRequiresCharging(false)
                .setAllowedOverMetered(true).setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    link
                )
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//        binding.contentMainView.customButton.setBackgroundColor(Color.YELLOW)
    }

    companion object {
        private const val Glide_Link = "https://github.com/bumptech/glide"
        private const val Retrofit_Link = "https://github.com/square/retrofit"
        private const val URL_Project =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonChecked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                binding.contentMainView.glideButton.id -> {
                    if (checked) {
                        selectedFile = 1
                    }
                }
                binding.contentMainView.projectButton.id -> {
                    if (checked) {
                        selectedFile = 2
                    }
                }
                binding.contentMainView.retrofitButton.id -> {
                    if (checked) {
                        selectedFile = 3
                    }
                }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}
