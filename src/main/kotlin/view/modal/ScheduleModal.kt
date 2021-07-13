package view.modal

import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.*
import com.slack.api.model.block.element.BlockElements.*
import com.slack.api.model.view.View
import com.slack.api.model.view.Views.*
import java.text.SimpleDateFormat
import java.util.*

fun buildScheduleModal(): View = view { view ->
    view
        .callbackId("add-schedule")
        .type("modal")
        .notifyOnClose(true)
        .title(viewTitle { it.type("plain_text").text("회의 일정 추가").emoji(true) })
        .submit(viewSubmit { it.type("plain_text").text("추가") })
        .close(viewClose { it.type("plain_text").text("취소") })
        .blocks(
            asBlocks(
                input { input ->
                    input
                        .blockId("meeting-agenda-input-block")
                        .label(
                            plainText("회의 안건")
                        )
                        .element(
                            plainTextInput {
                                it
                                    .actionId("meeting-agenda")
                                    .placeholder(plainText("회의 안건(주제)를 입력해주세요"))
                            }
                        )
                },
                input { input ->
                    input
                        .blockId("meeting-attender-input-block")
                        .label(
                            plainText("참석자")
                        )
                        .element(
                            multiUsersSelect {
                                it
                                    .actionId("attender-picker")
                                    .placeholder(plainText("회의 참석자를 선택해주세요"))
                            }
                        )
                },
                input { input ->
                    input
                        .blockId("meeting-date-input-block")
                        .label(
                            plainText("회의 날짜")
                        )
                        .element(
                            datePicker {
                                it
                                    .actionId("date-picker")
                                    .initialDate(SimpleDateFormat("yyyy-MM-dd").format(Date()))
                            }
                        )
                },
                input { input ->
                    input
                        .blockId("meeting-time-hour-input-block")
                        .label(
                            plainText("시")
                        )
                        .element(
                            staticSelect { select ->
                                select
                                    .actionId("hour-picker")
                                    .placeholder(plainText("회의할 시각의 시간을 선택해주세요"))
                                    .options(
                                        (0..23).map { number ->
                                            if (number - 12 < 0)
                                                option { option ->
                                                    option.value("$number").text(plainText("오전 ${if (number != 0) number else 12}시"))
                                                }
                                            else
                                                option { option ->
                                                    option.value("$number").text(plainText("오후 ${if (number - 12 != 0) number - 12 else 12}시"))
                                                }
                                        }
                                    )
                            }
                        )
                },
                input { input ->
                    input
                        .blockId("meeting-time-minute-input-block")
                        .label(
                            plainText("분")
                        )
                        .element(
                            staticSelect { select ->
                                select
                                    .actionId("minute-picker")
                                    .placeholder(plainText("회의할 시간의 분을 선택해주세요"))
                                    .options(
                                        (0..59).map { number ->
                                            option { option ->
                                                option.value("$number").text(plainText("${number}분"))
                                            }
                                        }
                                    )
                            }
                        )
                },
                input { input ->
                    input
                        .blockId("meeting-block")
                        .label(plainText("회의 내용"))
                        .element(
                            plainTextInput {
                                it
                                    .actionId("meeting-description")
                                    .placeholder(plainText("회의할 내용을 간단히 입력해주세요."))
                                    .multiline(true)
                            }
                        )
                }
            )
        )
}