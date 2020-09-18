package com.whywhom.soft.whyradiobox.services

import android.app.Notification
import android.content.Context
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.util.NotificationUtil
import com.google.android.exoplayer2.util.Util
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.RBApplication
import com.whywhom.soft.whyradiobox.utils.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID

class MediaDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.exo_download_notification_channel_name,  /* channelDescriptionResourceId= */
    0
) {
    override fun getDownloadManager(): DownloadManager {
        // This will only happen once, because getDownloadManager is guaranteed to be called only once
        // in the life cycle of the process.
        val application: RBApplication = application as RBApplication
        val downloadManager: DownloadManager = application.getDownloadManager()
        val downloadNotificationHelper: DownloadNotificationHelper =
            application.getDownloadNotificationHelper()
        downloadManager.addListener(
            TerminalStateNotificationHelper(
                this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1
            )
        )
        return downloadManager
    }

    override fun getScheduler(): PlatformScheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
    }

    override fun getForegroundNotification(downloads: List<Download>): Notification {
        return (application as RBApplication)
            .getDownloadNotificationHelper()
            .buildProgressNotification(
                R.drawable.ic_download,  /* contentIntent= */null,  /* message= */null, downloads
            )
    }

    private class TerminalStateNotificationHelper(
        context: Context, notificationHelper: DownloadNotificationHelper, firstNotificationId: Int
    ) :
        DownloadManager.Listener {
        private val context: Context
        private val notificationHelper: DownloadNotificationHelper
        private var nextNotificationId: Int
        override fun onDownloadChanged(manager: DownloadManager, download: Download) {
            val notification: Notification
            notification = if (download.state == Download.STATE_COMPLETED) {
                notificationHelper.buildDownloadCompletedNotification(
                    R.drawable.ic_download_done,  /* contentIntent= */
                    null,
                    Util.fromUtf8Bytes(download.request.data)
                )
            } else if (download.state == Download.STATE_FAILED) {
                notificationHelper.buildDownloadFailedNotification(
                    R.drawable.ic_download_done,  /* contentIntent= */
                    null,
                    Util.fromUtf8Bytes(download.request.data)
                )
            } else {
                return
            }
            NotificationUtil.setNotification(context, nextNotificationId++, notification)
        }

        init {
            this.context = context.applicationContext
            this.notificationHelper = notificationHelper
            nextNotificationId = firstNotificationId
        }
    }

    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }
}
