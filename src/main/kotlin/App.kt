import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp

fun main() {
    val app = App()

    SocketModeApp(app).start()
}