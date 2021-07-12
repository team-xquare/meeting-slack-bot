package sender

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient

open class MessageSender {
    private val slack: Slack = Slack.getInstance()
    val methods: MethodsClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))
}