package view.block

import com.slack.api.model.Attachments.attachment
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements
import model.Meeting
import util.concat.convertToMention

fun buildDeleteMeetingNotifyBlock(meeting: Meeting) = listOf(
    attachment { attachment -> attachment
        .id(1)
        .color("#93cfdb")
        .blocks(
            asBlocks(
                header { it.text(plainText("회의 일정 취소: "))},
                section { section -> section
                    .blockId("deleted-meeting")
                    .text(markdownText(
                        "회의 안건: ${meeting.agenda}\n" +
                                "회의 참석자: ${convertToMention(meeting.attenders)}\n" +
                                "회의 날짜: ${meeting.date}\n" +
                                "회의 시간: ${meeting.time}"
                    ))
                    .accessory(
                        BlockElements.imageElement {
                            it
                                .imageUrl("https://api.slack.com/img/blocks/bkb_template_images/notifications.png")
                                .altText("calendar thumbnail")
                        }
                    )
                }
            )
        )
    }
)