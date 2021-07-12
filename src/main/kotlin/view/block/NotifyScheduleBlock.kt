package view.block

import com.slack.api.model.Attachment
import com.slack.api.model.Attachments.attachment
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements.*
import model.Meeting

fun buildNotifyScheduleBlock(meeting: Meeting): List<Attachment> = listOf(
    attachment { attachment -> attachment
        .blocks(
            asBlocks(
                header { header -> header
                    .text(plainText("새 회의 일정 추가:"))
                },
                section { section -> section
                    .blockId("notify-information")
                    .text(markdownText(
                        "회의 안건: ${meeting.agenda}\n" +
                                "회의 참석자: ${meeting.attender.joinToString(" ") { attender -> "<@$attender>" }}\n" +
                                "회의 날짜: ${meeting.date}\n" +
                                "회의 시간: ${meeting.hour} ${meeting.minute}"))
                    .accessory(
                        imageElement { it
                            .imageUrl("https://api.slack.com/img/blocks/bkb_template_images/notifications.png")
                            .altText("calendar thumbnail")
                        }
                    )
                },
                divider(),
                section { section -> section
                    .blockId("notify-description")
                    .text(markdownText("회의 내용: ${meeting.description}"))
                },
                actions { actions -> actions
                    .elements(
                        asElements(
                            button { it
                                .actionId("approve-meeting")
                                .style("primary")
                                .text(plainText{ pt -> pt.emoji(true).text("참여")})
                            },
                            button { it
                                .actionId("deny-meeting")
                                .style("danger")
                                .text(plainText{ pt -> pt.emoji(true).text("거부")})
                            }
                        )
                    )
                }
            )
        )
    }
)