package sender.notification

import sender.dto.SenderMeeting
import sender.MessageSender
import sender.exception.MessageSendFailedException
import view.block.buildNotifyScheduleBlock

class NotificationMessageSender : MessageSender(), NotificationSender {
    override fun sendNotification(senderMeeting: SenderMeeting) {

        val response = methods.chatPostMessage { message -> message
            .channel(System.getenv("SLACK_NOTIFICATION_CHANNEL"))
            .attachments(buildNotifyScheduleBlock(senderMeeting))
        }

        if (!response.isOk)
            throw MessageSendFailedException(response.error)
    }
}