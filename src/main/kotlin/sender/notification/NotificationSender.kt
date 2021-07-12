package sender.notification

import sender.dto.Meeting

interface NotificationSender {
    fun sendNotification(meeting: Meeting)
}