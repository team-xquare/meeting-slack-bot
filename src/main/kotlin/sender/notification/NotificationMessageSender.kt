package sender.notification

import com.slack.api.model.view.ViewState
import sender.dto.Meeting
import sender.MessageSender
import sender.exception.MessageSendFailedException
import view.block.buildNotifyScheduleBlock

class NotificationMessageSender : MessageSender(), NotificationSender {
    override fun sendNotification(meeting: Meeting) {

        val response = methods.chatPostMessage { message -> message
            .channel("#bot-testing")
            .attachments(buildNotifyScheduleBlock(meeting))
        }

        if (!response.isOk)
            throw MessageSendFailedException(response.error)
    }
}