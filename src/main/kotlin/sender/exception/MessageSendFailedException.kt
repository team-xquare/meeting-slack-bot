package sender.exception

import java.lang.RuntimeException

class MessageSendFailedException(message: String) : RuntimeException(message) {
}