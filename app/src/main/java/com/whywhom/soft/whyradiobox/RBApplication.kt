package com.whywhom.soft.whyradiobox

import android.app.Application
import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.*
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.whywhom.soft.whyradiobox.download.DownloadTracker
import com.whywhom.soft.whyradiobox.utils.Connectivity
import java.io.File
import java.io.IOException


/**
 * Created by wuhaoyong on 2020-01-10.
 */
open class RBApplication: Application() {
    val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"
    protected var userAgent: String? = null
    private val TAG = "RBApplication"
    private val DOWNLOAD_ACTION_FILE = "actions"
    private val DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions"
    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    private var downloadManager: DownloadManager? = null
    private var databaseProvider: DatabaseProvider? = null
    private var downloadDirectory: File? = null
    private var downloadCache: Cache? = null
    private var downloadTracker: DownloadTracker? = null
    private var downloadNotificationHelper: DownloadNotificationHelper? = null
    companion object {
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        userAgent = Util.getUserAgent(this, "RBApplication")
        registerNetworkCallback()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun registerNetworkCallback(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Connectivity.registerNetworkCallback(this)
        }
    }

    fun getDownloadManager(): DownloadManager {
        initDownloadManager()
        return downloadManager!!
    }

    @Synchronized
    private fun initDownloadManager() {
        if (downloadManager == null) {
            val downloadIndex = DefaultDownloadIndex(getDatabaseProvider())
            upgradeActionFile(
                DOWNLOAD_ACTION_FILE,
                downloadIndex,  /* addNewDownloadsAsCompleted= */
                false
            )
            upgradeActionFile(
                DOWNLOAD_TRACKER_ACTION_FILE,
                downloadIndex,  /* addNewDownloadsAsCompleted= */
                true
            )
            val downloaderConstructorHelper =
                DownloaderConstructorHelper(getDownloadCache(), buildHttpDataSourceFactory())
            downloadManager = DownloadManager(
                this, downloadIndex, DefaultDownloaderFactory(downloaderConstructorHelper)
            )
            downloadTracker =
                DownloadTracker( /* context= */this, buildDataSourceFactory(), downloadManager)
        }
    }
    @Synchronized
    protected fun getDownloadCache(): Cache {
        if (downloadCache == null) {
            val downloadContentDirectory = File(
                getDownloadDirectory(),
                DOWNLOAD_CONTENT_DIRECTORY
            )
            downloadCache =
                SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), getDatabaseProvider())
        }
        return downloadCache!!
    }
    private fun upgradeActionFile(
        fileName: String, downloadIndex: DefaultDownloadIndex, addNewDownloadsAsCompleted: Boolean
    ) {
        try {
            ActionFileUpgradeUtil.upgradeAndDelete(
                File(getDownloadDirectory(), fileName),  /* downloadIdProvider= */
                null,
                downloadIndex,  /* deleteOnFailure= */
                true,
                addNewDownloadsAsCompleted
            )
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Failed to upgrade action file: $fileName", e
            )
        }
    }

    private fun getDatabaseProvider(): DatabaseProvider {
        if (databaseProvider == null) {
            databaseProvider = ExoDatabaseProvider(this)
        }
        return databaseProvider!!
    }

    private fun getDownloadDirectory(): File? {
        if (downloadDirectory == null) {
            downloadDirectory = getExternalFilesDir(null)
            if (downloadDirectory == null) {
                downloadDirectory = filesDir
            }
        }
        return downloadDirectory
    }
    open fun buildHttpDataSourceFactory(): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent)
    }
    open fun buildDataSourceFactory(): DataSource.Factory? {
        val upstreamFactory = DefaultDataSourceFactory(this, buildHttpDataSourceFactory())
        return buildReadOnlyCacheDataSource(
            upstreamFactory,
            getDownloadCache()
        )
    }
    protected open fun buildReadOnlyCacheDataSource(
        upstreamFactory: DataSource.Factory?, cache: Cache?
    ): CacheDataSourceFactory? {
        return CacheDataSourceFactory(
            cache,
            upstreamFactory,
            FileDataSource.Factory(),  /* cacheWriteDataSinkFactory= */
            null,
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,  /* eventListener= */
            null
        )
    }
    open fun getDownloadNotificationHelper(): DownloadNotificationHelper {
        if (downloadNotificationHelper == null) {
            downloadNotificationHelper = DownloadNotificationHelper(
                this,
                DOWNLOAD_NOTIFICATION_CHANNEL_ID
            )
        }
        return downloadNotificationHelper!!
    }
}