package sender.exception

import java.lang.RuntimeException

class MeetingNotFoundException : RuntimeException("Meeting is not found") {
}