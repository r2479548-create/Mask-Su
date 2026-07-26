package oi.masksu.com.core.base

import android.app.job.JobService
import android.content.Context
import oi.masksu.com.core.patch

abstract class BaseJobService : JobService() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.patch())
    }
}
