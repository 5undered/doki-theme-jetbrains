package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerLevel.*
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.DokiThemes

class StickersToggleAction : ToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.currentStickerLevel == ON

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      if(state){
        ThemeConfig.instance.stickerLevel = ON.name
        DokiThemes.currentTheme.ifPresent {
          StickerService.instance.activateForTheme(it)
        }
      } else {
        ThemeConfig.instance.stickerLevel = OFF.name
        StickerService.instance.remove()
      }
  }
}