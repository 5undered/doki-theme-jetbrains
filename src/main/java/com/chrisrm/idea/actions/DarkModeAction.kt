package com.chrisrm.idea.actions

import com.chrisrm.idea.MTConfig
import com.chrisrm.idea.MTThemes
import com.chrisrm.idea.actions.themes.literature.club.JustMonikaThemeAction
import com.chrisrm.idea.actions.themes.literature.club.SayoriThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

/**
 * Forged in the flames of battle by alex.
 */
class DarkModeAction : ToggleAction("SPOILERS!!!") {
    override fun isSelected(e: AnActionEvent?) = DarkMode.isOn()


    override fun setSelected(e: AnActionEvent, state: Boolean) {
        DarkMode.toggle()
        when (MTConfig.getInstance().getSelectedTheme()) {
            MTThemes.MONIKA -> JustMonikaThemeAction.setSelected(e, state)
            MTThemes.SAYORI -> SayoriThemeAction.setSelected(e, state)
//            MTThemes.NATSUKI -> NatsukiThemeAction.setSelected(e, state)
//            MTThemes.YURI -> YuriThemeAction.setSelected(e, state)
        }
    }


}