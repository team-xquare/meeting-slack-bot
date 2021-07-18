package sender.notification

import model.Meeting
import sender.MessageSender
import sender.exception.MessageSendFailedException
import view.block.buildDeleteMeetingNotifyBlock

class DeleteMeetingMessageSender : MessageSender(), DeleteMeetingSender {
    override fun sendNotification(meeting: Meeting) {
        val response = methods.chatPostMessage { message -> message
            .channel(System.getenv("SLACK_NOTIFICATION_CHANNEL"))
            .attachments(buildDeleteMeetingNotifyBlock(meeting))
        }

        if (!response.isOk)
            throw MessageSendFailedException(response.error)
    }
}