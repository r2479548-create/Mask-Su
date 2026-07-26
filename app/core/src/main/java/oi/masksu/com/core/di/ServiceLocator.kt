package oi.masksu.com.core.di

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import androidx.room.Room
import oi.masksu.com.core.AppContext
import oi.masksu.com.core.Const
import oi.masksu.com.core.data.SuLogDatabase
import oi.masksu.com.core.data.magiskdb.PolicyDao
import oi.masksu.com.core.data.magiskdb.SettingsDao
import oi.masksu.com.core.data.magiskdb.StringDao
import oi.masksu.com.core.ktx.deviceProtectedContext
import oi.masksu.com.core.repository.LogRepository
import oi.masksu.com.core.repository.NetworkService
import io.noties.markwon.Markwon
import io.noties.markwon.utils.NoCopySpannableFactory

@SuppressLint("StaticFieldLeak")
object ServiceLocator {

    val deContext by lazy { AppContext.deviceProtectedContext }
    val timeoutPrefs by lazy { deContext.getSharedPreferences("su_timeout", 0) }

    // Preferences
    val settingsPrefs by lazy { AppContext.getSharedPreferences("settings", Context.MODE_PRIVATE) }

    // Database
    val policyDB = PolicyDao()
    val settingsDB = SettingsDao()
    val stringDB = StringDao()
    val sulogDB by lazy { createSuLogDatabase(deContext).suLogDao() }
    val logRepo by lazy { LogRepository(sulogDB) }

    // Networking
    val okhttp by lazy { createOkHttpClient(AppContext) }
    val retrofit by lazy { createRetrofit(okhttp) }
    val markwon by lazy { createMarkwon(AppContext) }
    val networkService by lazy {
        NetworkService(
            createApiService(retrofit, Const.Url.INVALID_URL),
            createApiService(retrofit, Const.Url.GITHUB_API_URL),
        )
    }
}

private fun createSuLogDatabase(context: Context) =
    Room.databaseBuilder(context, SuLogDatabase::class.java, "sulogs.db")
        .addMigrations(SuLogDatabase.MIGRATION_1_2)
        .fallbackToDestructiveMigration(true)
        .build()

private fun createMarkwon(context: Context) =
    Markwon.builder(context).textSetter { textView, spanned, bufferType, onComplete ->
        textView.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setSpannableFactory(NoCopySpannableFactory.getInstance())
            setText(spanned, bufferType)
            onComplete.run()
        }
    }.build()
