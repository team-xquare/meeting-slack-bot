package sender.notification

import com.slack.api.model.view.ViewState

interface NotificationSender {
    fun sendNotification(value: Map<String, Map<String, ViewState.Value>>)
}