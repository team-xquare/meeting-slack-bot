package util.concat

fun mergeReason(first: Set<String>, second: List<String>): List<String> {
    return first.zip(second) { name, reason -> "<@$name> - $reason" }
}

fun convertToMention(userList: Set<String>): String {
    return userList.joinToString(" ") { attender -> "<@$attender>" }
}

fun convertToMention(userList: List<String>): String {
    return userList.joinToString(" ") { attender -> "<@$attender>" }
}