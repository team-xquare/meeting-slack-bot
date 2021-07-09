import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import handler.AmsHandler

fun main() {
    val app = App()
    val amsHandler = AmsHandler()

    app.command("/ams", amsHandler::addSchedule)
    app.viewClosed("add-schedule") { _, ctx ->
        ctx.ack()
    }

    SocketModeApp(app).start()
}