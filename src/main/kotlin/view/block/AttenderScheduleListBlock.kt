package view.block

import com.mongodb.client.MongoIterable
import com.slack.api.model.Attachment
import com.slack.api.model.Attachments.attachment
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import handler.dto.GetScheduleDto
import util.concat.convertToMention
import util.concat.mergeReason

fun buildAttenderScheduleListBlock(schedules: MongoIterable<GetScheduleDto>): List<Attachment> = listOf(
    attachment { attachment -> attachment
        .id(1)
        .color("#93cfdb")
        .blocks(
            schedules.map { schedule ->
                section { section -> section
                    .blockId("schedule-section-${schedule.time}")
                    .text(
                        markdownText(
                            "안건: ${schedule.agenda}\n" +
                                    "날짜: ${schedule.date}\n" +
                                    "시간: ${schedule.time}\n" +
                                    "회의 참석자: ${convertToMention(schedule.approves)}}\n" +
                                    "회의 불참자: ${convertToMention(schedule.denys)}\n" +
                                    "*불참자 사유*\n${
                                        mergeReason(
                                            schedule.denyReason.keys, schedule.denyReason.values.toList()
                                        ).joinToString { reason -> reason + "\n" }
                                    }"
                        )
                    )
                }
            }.toList()
        )
    }
)