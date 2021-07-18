package view.modal

import com.mongodb.client.FindIterable
import com.slack.api.model.block.Blocks.asBlocks
import com.slack.api.model.block.Blocks.input
import com.slack.api.model.block.composition.BlockCompositions.option
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements.staticSelect
import com.slack.api.model.view.View
import com.slack.api.model.view.Views.*
import model.Meeting

fun buildDeleteModal(meetings: FindIterable<Meeting>): View = view { view -> view
    .callbackId("delete-meetings")
    .type("modal")
    .type("modal")
    .notifyOnClose(true)
    .title(viewTitle { it.type("plain_text").text("회의 일정 삭제").emoji(true) })
    .submit(viewSubmit { it.type("plain_text").text("삭제") })
    .close(viewClose { it.type("plain_text").text("취소") })
    .blocks(
        asBlocks(
            input { input -> input
                .blockId("delete-meeting-input-block")
                .label(
                    plainText("취소할 회의 일정 선택")
                )
                .element(
                    staticSelect { it
                        .actionId("select-meeting")
                        .placeholder(plainText("삭제할 회의 일정을 선택해주세요"))
                        .options(
                            meetings.map { meeting -> option { option -> option
                                .text(plainText("안건: ${meeting.agenda}, 시간: ${meeting.time}"))
                                .value(meeting.meetingId)
                            } }.toList()
                        )
                    }
                )
            }
        )
    )
}