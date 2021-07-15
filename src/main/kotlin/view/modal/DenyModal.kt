package view.modal

import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements.plainTextInput
import com.slack.api.model.view.View
import com.slack.api.model.view.Views.*

fun buildDenyModal(value: String): View = view { view -> view
    .callbackId("deny-schedule")
    .privateMetadata(value)
    .type("modal")
    .notifyOnClose(true)
    .title(viewTitle { it.type("plain_text").text("회의 참가 거부 신청").emoji(true) })
    .submit(viewSubmit { it.type("plain_text").text("확인") })
    .close(viewClose { it.type("plain_text").text("취소") })
    .blocks(
        asBlocks(
            input { input -> input
                .blockId("meeting-deny-reason-input-block")
                .label(
                    plainText("참가 거부 사유")
                )
                .element(
                    plainTextInput { it
                        .actionId("meeting-deny-reason")
                        .placeholder(plainText("참가 거부 사유를 기입해주세요."))
                        .maxLength(50)
                        .multiline(true)
                    }
                )
            }
        )
    )
}