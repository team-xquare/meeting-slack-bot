package sender.notification

import model.Meeting

interface DeleteMeetingSender {
    fun sendNotification(meeting: Meeting)
}