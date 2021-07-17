package view.block

import com.slack.api.model.Attachment
import com.slack.api.model.Attachments.attachment
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements.*
import sender.dto.SenderMeeting
import util.concat.convertToMention

fun buildNotifyScheduleBlock(senderMeeting: SenderMeeting): List<Attachment> = listOf(
    attachment { attachment -> attachment
        .id(1)
        .color("#93cfdb")
        .blocks(
            asBlocks(
                header { header -> header
                    .text(plainText("새 회의 일정 추가:"))
                },
                section { section -> section
                    .blockId("notify-information")
                    .text(markdownText(
                        "회의 안건: ${senderMeeting.agenda}\n" +
                                "회의 참석자: ${convertToMention(senderMeeting.attender)}\n" +
                                "회의 날짜: ${senderMeeting.date}\n" +
                                "회의 시간: ${senderMeeting.hour} ${senderMeeting.minute}"))
                    .accessory(
                        imageElement { it
                            .imageUrl("https://api.slack.com/img/blocks/bkb_template_images/notifications.png")
                            .altText("calendar thumbnail")
                        }
                    )
                },
                divider(),
                header { it.text(plainText("회의 내용")) },
                section { section -> section
                    .blockId("notify-description")
                    .text(markdownText(senderMeeting.description))
                },
                divider(),
                actions { actions -> actions
                    .elements(
                        asElements(
                            button { it
                                .actionId("approve-meeting")
                                .style("primary")
                                .text(plainText{ pt -> pt.emoji(true).text("참여")})
                                .value(senderMeeting.id)
                            },
                            button { it
                                .actionId("deny-meeting")
                                .style("danger")
                                .text(plainText{ pt -> pt.emoji(true).text("거부")})
                                .value(senderMeeting.id)
                            }
                        )
                    )
                }
            )
        )
    }
)