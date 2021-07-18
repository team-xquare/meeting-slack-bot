import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import handler.AttenderHandler
import handler.MeetingHandler
import model.Meeting
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import sender.notification.DeleteMeetingMessageSender
import sender.notification.DenyMessageSender
import sender.notification.NotificationMessageSender

fun main() {
    val app = App()
    val notifySender = NotificationMessageSender()
    val denySender = DenyMessageSender()
    val deleteSender = DeleteMeetingMessageSender()
    val channel = System.getenv("SLACK_NOTIFICATION_CHANNEL")

    val col = KMongo.createClient(System.getenv("MONGO_URL")).getDatabase(System.getenv("MONGO_NAME")).getCollection<Meeting>()
    val meetingHandler = MeetingHandler(notifySender, denySender, deleteSender, col, channel)
    val attenderHandler = AttenderHandler(col)

//  Commands
    app.command("/meeting", meetingHandler::addSchedule)
    app.command("/delmeeting", meetingHandler::deleteMeeting)
    app.command("/mtattender", attenderHandler::getSchedule)

    // Modal
    app.viewSubmission("add-schedule", meetingHandler::handleViewSchedule)
    app.viewClosed("add-schedule") { _, ctx ->
        ctx.ack()
    }
    app.viewSubmission("delete-meetings", meetingHandler::handleDeleteMeetingView)
    app.viewClosed("delete-meetings") { _, ctx ->
        ctx.ack()
    }

    app.viewSubmission("deny-schedule", meetingHandler::handleDenyView)
    app.viewClosed("deny-schedule") { _, ctx ->
        ctx.ack()
    }

    // Actions
    app.blockAction("approve-meeting", meetingHandler::handleApproveAction)
    app.blockAction("deny-meeting", meetingHandler::handleDenyAction)

    SocketModeApp(app).start()
}