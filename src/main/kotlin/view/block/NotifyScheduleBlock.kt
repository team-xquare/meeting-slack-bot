package view.block

import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.view.ViewState.Value
import model.Meeting

fun buildNotifyScheduleBlock(meeting: Meeting): MutableList<LayoutBlock> = asBlocks(
    header { header -> header
        .text(plainText("새 회의 일정이 추가되었습니다."))
    },
    section { section -> section
        .blockId("notify-agenda")
        .text(markdownText("회의 안건: ${meeting.agenda}"))
    },
    section { section -> section
        .blockId("notify-attender")
        .text(markdownText("회의 참석자: ${meeting.attender.joinToString(" ") { attender -> "<@$attender>" }}"))
    },
    section { section -> section
        .blockId("notify-date")
        .text(markdownText("회의 날짜: ${meeting.date}"))
    },
    section { section -> section
        .blockId("notify-time")
        .text(markdownText("회의 시간: ${meeting.hour} ${meeting.minute}"))
    },
    section { section -> section
        .blockId("notify-description")
        .text(markdownText("회의 내용: ${meeting.description}"))
    }
)