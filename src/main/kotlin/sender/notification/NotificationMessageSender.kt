package sender.notification

import com.slack.api.model.view.ViewState
import model.Meeting
import sender.MessageSender
import sender.exception.MessageSendFailedException
import view.block.buildNotifyScheduleBlock

class NotificationMessageSender : MessageSender(), NotificationSender {
    override fun sendNotification(value: Map<String, Map<String, ViewState.Value>>) {
        val meeting = Meeting(
            agenda = value["meeting-agenda-input-block"]?.get("meeting-agenda")?.value ?: "",
            attender = value["meeting-attender-input-block"]?.get("attender-picker")?.selectedUsers ?: listOf(),
            description = value["meeting-block"]?.get("meeting-description")?.value ?: "",
            date = value["meeting-date-input-block"]?.get("date-picker")?.selectedDate ?: "",
            hour = value["meeting-time-hour-input-block"]?.get("hour-picker")?.selectedOption?.text?.text ?: "",
            minute = value["meeting-time-minute-input-block"]?.get("minute-picker")?.selectedOption?.text?.text ?: ""
        )

        val response = methods.chatPostMessage { message -> message
            .channel("#bot-testing")
            .attachments(buildNotifyScheduleBlock(meeting))
        }

        if (!response.isOk)
            throw MessageSendFailedException(response.error)
    }
}