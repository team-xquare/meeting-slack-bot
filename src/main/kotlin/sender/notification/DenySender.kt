package sender.notification

import sender.dto.DenyMeeting

interface DenySender {
    fun sendNotification(meeting: DenyMeeting)
}