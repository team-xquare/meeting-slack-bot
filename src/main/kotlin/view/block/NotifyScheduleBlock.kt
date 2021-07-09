package view.block

import com.slack.api.model.block.Blocks.asBlocks
import com.slack.api.model.block.Blocks.section
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.view.ViewState.Value

fun buildNotifyScheduleBlock(data: Map<String, Map<String, Value>>): MutableList<LayoutBlock> = asBlocks(
    section { section -> section
        .blockId("notify-block")
        .text(markdownText("회의 안건: ${data["meeting-agenda-input-block"]?.get("meeting-agenda")}"))
    }
)