package sender.notification

import sender.MessageSender
import sender.dto.DenyMeeting
import sender.exception.MessageSendFailedException

class DenyMessageSender : DenySender, MessageSender() {
    override fun sendNotification(meeting: DenyMeeting) {
        val response = methods.chatPostEphemeral { message -> message
            .user(meeting.userId)
            .channel(meeting.channel)
            .text("✅ 회의 참여 거부가 성공적으로 완료되었습니다.")
        }

        if (!response.isOk)
            throw MessageSendFailedException(response.error)
    }
}