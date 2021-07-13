package view.block

import com.mongodb.client.MongoIterable
import com.slack.api.model.Attachment
import com.slack.api.model.Attachments.attachment
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import handler.dto.GetScheduleDto

fun buildAttenderScheduleListBlock(schedules: MongoIterable<GetScheduleDto>): List<Attachment> = listOf(
    attachment { attachment -> attachment
        .id(1)
        .color("#93cfdb")
        .blocks(
                schedules.map { schedule ->
                    section { section -> section
                        .blockId("schedule-section")
                        .text(
                            markdownText(
                            "날짜: ${schedule.date}\n" +
                                    "시간: ${schedule.time}\n" +
                                "회의 참석자: ${schedule.approves.joinToString(" ") { attender -> "<@$attender>" }}\n" +
                                "회의 불참자: ${schedule.denys.joinToString(" ") { deny -> "<@$deny>"}}"
                            )
                        )
                    }
                }.toList()
        )
    }
)