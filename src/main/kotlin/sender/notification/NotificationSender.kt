package sender.notification

import sender.dto.SenderMeeting

interface NotificationSender {
    fun sendNotification(senderMeeting: SenderMeeting)
}