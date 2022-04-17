package com.example.mydish.model.notitifcation

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Timber.d("do work function is called")

        return Result.success()
    }


}