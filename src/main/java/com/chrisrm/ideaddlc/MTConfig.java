/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.ideaddlc;

import com.chrisrm.ideaddlc.config.BeforeConfigNotifier;
import com.chrisrm.ideaddlc.config.ConfigNotifier;
import com.chrisrm.ideaddlc.config.ui.ArrowsStyles;
import com.chrisrm.ideaddlc.config.ui.IndicatorStyles;
import com.chrisrm.ideaddlc.config.ui.MTForm;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.acari.DDLC.DDLCThemeFacade;
import io.acari.DDLC.DDLCThemes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@State(
    name = "MaterialDDLCThemeConfig",
    storages = @Storage("material_theme.xml")
)
public class MTConfig implements PersistentStateComponent<MTConfig>, Cloneable {
  public static final String DEFAULT_BG =
      "https://github.com/cyclic-reference/jetbrains-theme/master/src/main/resources/themes/Doki_Doki_Literature_Club.png";
  public static final String ACCENT_COLOR = "80CBC4";
  public static final int MAX_HIGHLIGHT_THICKNESS = 5;
  public static final int MIN_HIGHLIGHT_THICKNESS = 1;
  public static final int MAX_INDICATOR_THICKNESS = 5;
  public static final int MIN_INDICATOR_THICKNESS = 1;
  public static final int MAX_TABS_HEIGHT = 60;
  public static final int MIN_TABS_HEIGHT = 18;
  public static final int MAX_TREE_INDENT = 40;
  public static final int MIN_TREE_INDENT = 0;
  public static final int MAX_SIDEBAR_HEIGHT = 36;
  public static final int MIN_SIDEBAR_HEIGHT = 18;
  public static final int MIN_FONT_SIZE = 6;
  public static final int MAX_FONT_SIZE = 24;
  public static final String WE_USING_DDLC_BOIS = "WE USING DDLC BOIS";
  public static final String WE_AINT_USING_DDLC_BOIS = "WE_AINT USING DDLC BOIS";

  // They are public so they can be serialized
  public String version;

  public String selectedTheme = DDLCThemes.MONIKA.getName();
  public String highlightColor = ACCENT_COLOR;
  public boolean highlightColorEnabled = false;
  public Integer highlightThickness = 2;
  public boolean isContrastMode = false;
  public boolean isMaterialDesign = true;
  public boolean isStyledDirectories = false;
  public boolean isCustomTreeIndentEnabled = false;
  public Integer rightTreeIndent = 10;
  public Integer leftTreeIndent = 6;

  public String accentColor = ACCENT_COLOR;

  public boolean useMaterialIcons = true;
  public boolean useProjectViewDecorators = true;
  public boolean hideFileIcons = false;
  public boolean compactSidebar = false;
  public boolean statusBarTheme = true;

  public Integer tabsHeight = 42;
  public boolean isMaterialTheme = true;
  public boolean themedScrollbars = true;
  public boolean isCompactStatusBar = false;
  public boolean isCompactTables = false;

  public boolean upperCaseTabs = false;
  public int customSidebarHeight = 18;
  public boolean accentScrollbars = true;
  public boolean darkTitleBar = false;
  public ArrowsStyles arrowsStyle = ArrowsStyles.MATERIAL;
  public IndicatorStyles indicatorStyle = IndicatorStyles.BORDER;
  @Deprecated
  public boolean useMaterialFont = true;
  public boolean useMaterialFont2 = false;

  public int tabOpacity = 50;
  public boolean compactDropdowns = false;
  public boolean monochromeIcons = false;
  public boolean upperCaseButtons = true;
  public String accentTitleBarColor = ACCENT_COLOR;
  public boolean isDecoratedFolders = true;
  public boolean fileIcons = true;
  public int treeFontSize = 12;
  public Integer settingsSelectedTab = 0;
  public boolean fileStatusColorsEnabled = true;
  public String userId = new UID().toString();
  public boolean allowDataCollection = false;
  public boolean treeFontSizeEnabled = false;
  public boolean isHighContrast = false;
  public Integer indicatorThickness = 2;
  public boolean overrideAccentColor = false;
  public boolean isWizardShown = false;
  public boolean isTabsShadow = true;

  public MTConfig() {
  }

  @Override
  public Object clone() {
    return XmlSerializerUtil.createCopy(this);
  }

  /**
   * Get instance of the config from the ServiceManager
   *
   * @return the MTConfig instance
   */
  public static MTConfig getInstance() {
    return ServiceManager.getService(MTConfig.class);
  }

  public Map asProperties() {
    return getNativeProperties();
  }

  public JSONObject asJson() throws JSONException {
    return getNativePropertiesAsJson();
  }

  public void copyFrom(final MTConfig configCopy) {
    XmlSerializerUtil.copyBean(configCopy, this);
  }

  //region Tabs Shadow
  public void setIsTabsShadow(final boolean isTabsShadow) {
    this.isTabsShadow = isTabsShadow;
  }

  public boolean isTabsShadow() {
    return isTabsShadow;
  }

  public boolean isTabsShadowChanged(final boolean tabsShadow) {
    return isTabsShadow != tabsShadow;
  }
  //endregion



  /**
   * Convenience method to reset settings
   */
  public void resetSettings() {
    selectedTheme = DDLCThemes.MONIKA.getName();
    highlightColor = ACCENT_COLOR;
    highlightColorEnabled = false;
    highlightThickness = 2;
    isContrastMode = false;
    isMaterialDesign = true;
    isStyledDirectories = false;
    isCustomTreeIndentEnabled = false;
    rightTreeIndent = 6;
    leftTreeIndent = 6;

    accentColor = ACCENT_COLOR;

    useMaterialIcons = true;
    useProjectViewDecorators = true;
    hideFileIcons = false;
    compactSidebar = false;
    statusBarTheme = true;

    tabsHeight = 42;
    isMaterialTheme = true;
    themedScrollbars = true;
    isCompactStatusBar = false;
    isCompactTables = false;

    upperCaseTabs = false;
    customSidebarHeight = 18;
    accentScrollbars = true;
    darkTitleBar = false;
    arrowsStyle = ArrowsStyles.MATERIAL;
    indicatorStyle = IndicatorStyles.BORDER;
    indicatorThickness = 2;
    useMaterialFont2 = false;
    tabOpacity = 50;
    compactDropdowns = false;
    monochromeIcons = false;
    upperCaseButtons = true;
    accentTitleBarColor = ACCENT_COLOR;
    isDecoratedFolders = true;
    fileIcons = true;
    treeFontSize = 12;
    treeFontSizeEnabled = false;
    fileStatusColorsEnabled = true;
    isHighContrast = false;
    overrideAccentColor = false;
    isTabsShadow = true;
  }

  public boolean needsRestart(final MTForm form) {
    boolean modified = isMaterialDesignChanged(form.getIsMaterialDesign());
    modified = modified || isUseMaterialFontChanged(form.getUseMaterialFont());
    modified = modified || treeFontSizeChanged(form.getTreeFontSize());
    modified = modified || isTreeFontSizeEnabledChanged(form.isTreeFontSizeEnabled());
    modified = modified || isThemedScrollbarsChanged(form.isThemedScrollbars());
    modified = modified || isMaterialIconsChanged(form.isUseMaterialIcons());
    modified = modified || isMaterialThemeChanged(form.getIsMaterialTheme());
    modified = modified || isAccentScrollbarsChanged(form.isAccentScrollbars());

    return modified;
  }

  /**
   * Return the selected theme by eventually loads it if not loaded yet
   *
   * @return
   */
  public DDLCThemeFacade getSelectedTheme() {
    final DDLCThemeFacade themeFor = DDLCThemes.getThemeFor(selectedTheme);
    return ObjectUtils.notNull(themeFor, DDLCThemes.MONIKA);
  }

  public void setSelectedTheme(final DDLCThemeFacade selectedTheme) {
    this.selectedTheme = selectedTheme.getThemeId();
  }

  public boolean isMateriaThemeActive(){
    return !this.selectedTheme.equals(WE_USING_DDLC_BOIS);
  }

  /**
   * Get the state of MTConfig
   */
  @Nullable
  @Override
  public MTConfig getState() {
    return this;
  }

  /**
   * Load the state from XML
   *
   * @param state the MTConfig instance
   */
  @Override
  public void loadState(@NotNull final MTConfig state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  /**
   * Fire an event to the application bus that the settings have changed
   *
   * @param form
   */
  public void fireBeforeChanged(final MTForm form) {
    ApplicationManager.getApplication().getMessageBus()
                      .syncPublisher(BeforeConfigNotifier.BEFORE_CONFIG_TOPIC)
                      .beforeConfigChanged(this, form);
  }

  /**
   * Fire an event to the application bus that the settings have changed
   */
  public void fireChanged() {
    ApplicationManager.getApplication().getMessageBus()
                      .syncPublisher(ConfigNotifier.CONFIG_TOPIC)
                      .configChanged(this);
  }

  @NotNull
  private Map getNativeProperties() {
    final HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", selectedTheme);
    hashMap.put("userId", userId);
    hashMap.put("highlightColor", highlightColor);
    hashMap.put("highlightThickness", highlightThickness);
    hashMap.put("isContrastMode", isContrastMode);
    hashMap.put("isMaterialDesign", isMaterialDesign);
    hashMap.put("isStyledDirectories", isStyledDirectories);
    hashMap.put("isCustomTreeIndentEnabled", isCustomTreeIndentEnabled);
    hashMap.put("rightTreeIndent", rightTreeIndent);
    hashMap.put("leftTreeIndent", leftTreeIndent);
    hashMap.put("accentColor", accentColor);
    hashMap.put("useMaterialIcons", useMaterialIcons);
    hashMap.put("useProjectViewDecorators", useProjectViewDecorators);
    hashMap.put("hideFileIcons", hideFileIcons);
    hashMap.put("compactSidebar", compactSidebar);
    hashMap.put("statusBarTheme", statusBarTheme);
    hashMap.put("tabsHeight", tabsHeight);
    hashMap.put("isMaterialTheme", isMaterialTheme);
    hashMap.put("themedScrollbars", themedScrollbars);
    hashMap.put("isCompactStatusBar", isCompactStatusBar);
    hashMap.put("isCompactTables", isCompactTables);
    hashMap.put("upperCaseTabs", upperCaseTabs);
    hashMap.put("customSidebarHeight", customSidebarHeight);
    hashMap.put("accentScrollbars", accentScrollbars);
    hashMap.put("darkTitleBar", darkTitleBar);
    hashMap.put("arrowsStyle", arrowsStyle);
    hashMap.put("indicatorStyles", indicatorStyle);
    hashMap.put("indicatorThickness", indicatorThickness);
    hashMap.put("useMaterialFont", useMaterialFont2);
    hashMap.put("tabOpacity", tabOpacity);
    hashMap.put("compactDropdowns", compactDropdowns);
    hashMap.put("monochromeIcons", monochromeIcons);
    hashMap.put("upperCaseButtons", upperCaseButtons);
    hashMap.put("fileIcons", fileIcons);
    hashMap.put("isDecoratedFolders", isDecoratedFolders);
    hashMap.put("treeFontSizeEnabled", treeFontSizeEnabled);
    hashMap.put("treeFontSize", treeFontSize);
    hashMap.put("fileStatusColorsEnabled", fileStatusColorsEnabled);
    hashMap.put("isHighContrast", isHighContrast);
    hashMap.put("overrideAccentColor", overrideAccentColor);
    hashMap.put("isTabsShadow", isTabsShadow);

    return hashMap;
  }

  private JSONObject getNativePropertiesAsJson() throws JSONException {
    final JSONObject hashMap = new JSONObject();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", selectedTheme);
    hashMap.put("userId", userId);
    hashMap.put("highlightColor", highlightColor);
    hashMap.put("highlightThickness", highlightThickness);
    hashMap.put("isContrastMode", isContrastMode);
    hashMap.put("isMaterialDesign", isMaterialDesign);
    hashMap.put("isStyledDirectories", isStyledDirectories);
    hashMap.put("isCustomTreeIndentEnabled", isCustomTreeIndentEnabled);
    hashMap.put("rightTreeIndent", rightTreeIndent);
    hashMap.put("leftTreeIndent", leftTreeIndent);
    hashMap.put("accentColor", accentColor);
    hashMap.put("useMaterialIcons", useMaterialIcons);
    hashMap.put("useProjectViewDecorators", useProjectViewDecorators);
    hashMap.put("hideFileIcons", hideFileIcons);
    hashMap.put("compactSidebar", compactSidebar);
    hashMap.put("statusBarTheme", statusBarTheme);
    hashMap.put("tabsHeight", tabsHeight);
    hashMap.put("isMaterialTheme", isMaterialTheme);
    hashMap.put("themedScrollbars", themedScrollbars);
    hashMap.put("isCompactStatusBar", isCompactStatusBar);
    hashMap.put("isCompactTables", isCompactTables);
    hashMap.put("upperCaseTabs", upperCaseTabs);
    hashMap.put("customSidebarHeight", customSidebarHeight);
    hashMap.put("accentScrollbars", accentScrollbars);
    hashMap.put("darkTitleBar", darkTitleBar);
    hashMap.put("arrowsStyle", arrowsStyle);
    hashMap.put("indicatorStyles", indicatorStyle);
    hashMap.put("indicatorThickness", indicatorThickness);
    hashMap.put("useMaterialFont", useMaterialFont2);
    hashMap.put("tabOpacity", tabOpacity);
    hashMap.put("compactDropdowns", compactDropdowns);
    hashMap.put("monochromeIcons", monochromeIcons);
    hashMap.put("upperCaseButtons", upperCaseButtons);
    hashMap.put("fileIcons", fileIcons);
    hashMap.put("isDecoratedFolders", isDecoratedFolders);
    hashMap.put("treeFontSizeEnabled", treeFontSizeEnabled);
    hashMap.put("treeFontSize", treeFontSize);
    hashMap.put("fileStatusColorsEnabled", fileStatusColorsEnabled);
    hashMap.put("isHighContrast", isHighContrast);
    hashMap.put("overrideAccentColor", overrideAccentColor);
    hashMap.put("isTabsShadow", isTabsShadow);

    return hashMap;
  }

  public String getVersion() {
    return version;
  }

  /**
   * Quick doc
   *
   * @param version
   */
  public void setVersion(final String version) {
    this.version = version;
  }

  public boolean isSelectedThemeChanged(final DDLCThemeFacade theme) {
    return !selectedTheme.equals(theme.getName());
  }

  public String getDefaultBackground() {
    return DEFAULT_BG;
  }

  //region Tabs Highlight

  /**
   * Get the set highlight color
   *
   * @return the highlight color
   */
  public Color getHighlightColor() {
    return ColorUtil.fromHex(highlightColor);
  }

  /**
   * Set a new highlight color
   *
   * @param color the new hightlight color
   */
  public void setHighlightColor(@NotNull final Color color) {
    highlightColor = ColorUtil.toHex(color);
  }

  /**
   * Checks whether the new highlightColor is different from the previous one
   *
   * @param color new highlight color
   * @return true if changed
   */
  public boolean isHighlightColorChanged(@NotNull final Color color) {
    final Color current = getHighlightColor();
    return !Objects.equals(current, color);
  }

  /**
   * Return whether custom highlight is enabled
   *
   * @return true if enabled
   */
  public boolean isHighlightColorEnabled() {
    return highlightColorEnabled;
  }

  /**
   * Enable/Disable custom highlight
   *
   * @param enabled state
   */
  public void setHighlightColorEnabled(final boolean enabled) {
    highlightColorEnabled = enabled;
  }

  /**
   * Checks whether the highlight color enabled state has changed
   *
   * @param enabled new enabled state
   * @return true if changed
   */
  public boolean isHighlightColorEnabledChanged(final boolean enabled) {
    return highlightColorEnabled != enabled;
  }
  //endregion

  //region Tab highlight thickness

  /**
   * Get user's highlight thickness
   *
   * @return highlight thickness
   */
  public int getHighlightThickness() {
    return highlightThickness;
  }

  /**
   * Set highlight thickness
   *
   * @param thickness thickness value
   */
  public void setHighlightThickness(final int thickness) {
    if (thickness < MIN_HIGHLIGHT_THICKNESS || thickness > MAX_HIGHLIGHT_THICKNESS) {
      return;
    }
    highlightThickness = thickness;
  }

  /**
   * Checks whether the highlight thickness has changed
   *
   * @param thickness new thickness
   * @return true if changed
   */
  public boolean isHighlightThicknessChanged(final int thickness) {
    return highlightThickness != thickness;
  }
  //endregion

  //region Contrast mode

  /**
   * Checks whether we are in contrast mode
   *
   * @return true if contrast mode
   */
  public boolean getIsContrastMode() {
    return isContrastMode;
  }

  /**
   * Enable/disable contrast mode
   *
   * @param isContrastMode contrast mode value
   */
  public void setIsContrastMode(final boolean isContrastMode) {
    this.isContrastMode = isContrastMode;
  }

  public boolean isContrastModeChanged(final boolean isContrastMode) {
    return this.isContrastMode != isContrastMode;
  }
  //endregion

  //region Material Design Components

  public boolean getIsMaterialDesign() {
    return isMaterialDesign;
  }

  public void setIsMaterialDesign(final boolean materialDesign) {
    isMaterialDesign = materialDesign;
  }

  public boolean isMaterialDesignChanged(final boolean isMaterialDesign) {
    return this.isMaterialDesign != isMaterialDesign;
  }
  //endregion

  //region Styled Directories

  public boolean getIsStyledDirectories() {
    return isStyledDirectories;
  }

  public void setIsStyledDirectories(final boolean isStyledDirectories) {
    this.isStyledDirectories = isStyledDirectories;
  }

  public boolean isStyledDirectoriesChanged(final boolean isStyledDirectories) {
    return this.isStyledDirectories != isStyledDirectories;
  }
  //endregion

  //region Accent Color

  public String getAccentColor() {
    return accentColor;
  }

  public void setAccentColor(final String accentColor) {
    this.accentColor = accentColor;
  }

  public boolean isAccentColorChanged(final Color customAccentColor) {
    return !Objects.equals(accentColor, ColorUtil.toHex(customAccentColor));
  }
  //endregion

  //region Override Accent Color
  public boolean isOverrideAccentColor() {
    return overrideAccentColor;
  }

  public void setOverrideAccentColor(final boolean overrideAccentColor) {
    this.overrideAccentColor = overrideAccentColor;
  }

  public boolean isOverrideAccentColorChanged(final boolean overrideAccents) {
    return overrideAccentColor != overrideAccents;
  }
  //endregion

  //region Material Icons

  public boolean isUseMaterialIcons() {
    return useMaterialIcons;
  }

  public void setUseMaterialIcons(final boolean useMaterialIcons) {
    this.useMaterialIcons = useMaterialIcons;
  }

  public boolean isMaterialIconsChanged(final boolean useMaterialIcons) {
    return this.useMaterialIcons != useMaterialIcons;
  }
  //endregion

  //region Project View Decorators

  public boolean isUseProjectViewDecorators() {
    return useProjectViewDecorators;
  }

  public void setUseProjectViewDecorators(final boolean useProjectViewDecorators) {
    this.useProjectViewDecorators = useProjectViewDecorators;
  }

  public boolean isUseProjectViewDecoratorsChanged(final boolean useProjectViewDecorators) {
    return this.useProjectViewDecorators != useProjectViewDecorators;
  }
  //endregion

  //region Hide File Icons

  public boolean getHideFileIcons() {
    return hideFileIcons;
  }

  public void setHideFileIcons(final boolean hideFileIcons) {
    this.hideFileIcons = hideFileIcons;
  }

  public boolean isHideFileIconsChanged(final boolean hideFileIcons) {
    return this.hideFileIcons != hideFileIcons;
  }
  //endregion

  //region File Icons
  public boolean isFileIcons() {
    return fileIcons;
  }

  public void setFileIcons(final boolean fileIcons) {
    this.fileIcons = fileIcons;
  }

  public boolean isFileIconsChanged(final boolean fileIcons) {
    return this.fileIcons != fileIcons;
  }
  //endregion

  //region Compact Sidebar

  public boolean isCompactSidebar() {
    return compactSidebar;
  }

  public void setCompactSidebar(final boolean compactSidebar) {
    this.compactSidebar = compactSidebar;
  }

  public boolean isCompactSidebarChanged(final boolean compactSidebar) {
    return this.compactSidebar != compactSidebar;
  }

  public int getCustomSidebarHeight() {
    return customSidebarHeight;
  }

  public boolean customSidebarHeightChanged(final Integer customSidebarHeight) {
    return this.customSidebarHeight != customSidebarHeight;
  }

  public void setCustomSidebarHeight(final Integer customSidebarHeight) {
    if (customSidebarHeight < MIN_SIDEBAR_HEIGHT || customSidebarHeight > MAX_SIDEBAR_HEIGHT) {
      return;
    }
    this.customSidebarHeight = customSidebarHeight;
  }
  //endregion

  //region Statusbar indicator

  public boolean isStatusBarTheme() {
    return statusBarTheme;
  }

  public void setIsStatusBarTheme(final boolean isStatusBarTheme) {
    statusBarTheme = isStatusBarTheme;
  }

  public boolean isStatusBarThemeChanged(final boolean statusBarTheme) {
    return this.statusBarTheme != statusBarTheme;
  }
  //endregion

  //region Tabs Height

  public int getTabsHeight() {
    return tabsHeight;
  }

  public void setTabsHeight(final Integer tabsHeight) {
    this.tabsHeight = tabsHeight;
  }

  public boolean isTabsHeightChanged(final Integer tabsHeight) {
    return !Objects.equals(this.tabsHeight, tabsHeight);
  }
  //endregion

  //region Material Theme

  public boolean isMaterialTheme() {
    return isMaterialTheme;
  }

  public void setIsMaterialTheme(final boolean isMaterialTheme) {
    this.isMaterialTheme = isMaterialTheme;
  }

  public boolean isMaterialThemeChanged(final boolean isMaterialTheme) {
    return this.isMaterialTheme != isMaterialTheme;
  }
  //endregion

  //region Custom Tree Indents

  public int getRightTreeIndent() {
    return rightTreeIndent;
  }

  public void setRightTreeIndent(final Integer rightTreeIndent) {
    this.rightTreeIndent = rightTreeIndent;
  }

  public int getLeftTreeIndent() {
    return leftTreeIndent;
  }

  public void setLeftTreeIndent(final Integer leftTreeIndent) {
    this.leftTreeIndent = leftTreeIndent;
  }

  public boolean isCustomTreeIndent() {
    return isCustomTreeIndentEnabled;
  }

  public boolean rightTreeIndentChanged(final int rightTreeIndent) {
    return this.rightTreeIndent != rightTreeIndent;
  }

  public boolean leftTreeIndentChanged(final int leftTreeIndent) {
    return this.leftTreeIndent != leftTreeIndent;
  }

  public void setIsCustomTreeIndent(final boolean isCustomTreeIndent) {
    isCustomTreeIndentEnabled = isCustomTreeIndent;
  }

  public boolean isCustomTreeIndentChanged(final boolean customTreeIndentEnabled) {
    return isCustomTreeIndentEnabled != customTreeIndentEnabled;
  }
  //endregion

  //region Themed Scrollbars

  public boolean isThemedScrollbars() {
    return themedScrollbars;
  }

  public void setThemedScrollbars(final boolean themedScrollbars) {
    this.themedScrollbars = themedScrollbars;
  }

  public boolean isThemedScrollbarsChanged(final boolean themedScrollbars) {
    return this.themedScrollbars != themedScrollbars;
  }

  public boolean isAccentScrollbars() {
    return accentScrollbars;
  }

  public void setAccentScrollbars(final boolean accentScrollbars) {
    this.accentScrollbars = accentScrollbars;
  }

  public boolean isAccentScrollbarsChanged(final boolean accentScrollbars) {
    return this.accentScrollbars != accentScrollbars;
  }
  //endregion

  //region Compact Status Bar

  public boolean isCompactStatusBar() {
    return isCompactStatusBar;
  }

  public void setIsCompactStatusBar(final boolean isCompactStatusBar) {
    this.isCompactStatusBar = isCompactStatusBar;
  }

  public boolean isCompactStatusBarChanged(final boolean compactStatusBar) {
    return isCompactStatusBar != compactStatusBar;
  }
  //endregion

  //region Compact Tables

  public boolean isCompactTables() {
    return isCompactTables;
  }

  public void setIsCompactTables(final boolean isCompactTables) {
    this.isCompactTables = isCompactTables;
  }

  public boolean isCompactTablesChanged(final boolean compactTables) {
    return isCompactTables != compactTables;
  }
  //endregion

  //region Uppercase tabs

  public boolean isUpperCaseTabs() {
    return upperCaseTabs;
  }

  public void setIsUpperCaseTabs(final boolean isUpperCaseTabs) {
    upperCaseTabs = isUpperCaseTabs;
  }

  public boolean isUpperCaseTabsChanged(final boolean upperCaseTabs) {
    return this.upperCaseTabs != upperCaseTabs;
  }
  // endregion

  //region Dark titlebar

  public boolean isDarkTitleBar() {
    return darkTitleBar;
  }

  public void setDarkTitleBar(final boolean darkTitleBar) {
    this.darkTitleBar = darkTitleBar;
  }

  public boolean isDarkTitleBarChanged(final boolean darkTitleBar) {
    return this.darkTitleBar != darkTitleBar;
  }
  //endregion

  // region arrows styles

  public ArrowsStyles getArrowsStyle() {
    return arrowsStyle;
  }

  public void setArrowsStyle(final ArrowsStyles arrowsStyle) {
    this.arrowsStyle = arrowsStyle;
  }

  public boolean isArrowsStyleChanged(final ArrowsStyles arrowsStyle) {
    return this.arrowsStyle != arrowsStyle;
  }
  // endregion

  // region indicator styles
  public IndicatorStyles getIndicatorStyle() {
    return indicatorStyle;
  }

  public void setIndicatorStyle(final IndicatorStyles indicatorStyle) {
    this.indicatorStyle = indicatorStyle;
  }

  public boolean isIndicatorStyleChanged(final IndicatorStyles indicatorStyle) {
    return this.indicatorStyle != indicatorStyle;
  }
  // endregion

  // region indicator thickness

  public Integer getIndicatorThickness() {
    return indicatorThickness;
  }

  public void setIndicatorThickness(final int indicatorThickness) {
    this.indicatorThickness = indicatorThickness;
  }

  public boolean isIndicatorThicknessChanged(final int indicatorThickness) {
    return this.indicatorThickness != indicatorThickness;
  }
  // endregion

  // region use material fonts

  public void setUseMaterialFont(final boolean useMaterialFont) {
    useMaterialFont2 = useMaterialFont;
  }

  public boolean isUseMaterialFont() {
    return useMaterialFont2;
  }

  public boolean isUseMaterialFontChanged(final boolean useMaterialFont) {
    return useMaterialFont2 != useMaterialFont;
  }
  //endregion

  //region Tab Opacity

  public int getTabOpacity() {
    return tabOpacity;
  }

  public void setTabOpacity(final int tabOpacity) {
    this.tabOpacity = tabOpacity;
  }

  public boolean isTabOpacityChanged(final int tabOpacity) {
    return this.tabOpacity != tabOpacity;
  }

  //endregion

  //region Compact dropdowns

  public boolean isCompactDropdowns() {
    return compactDropdowns;
  }

  public void setCompactDropdowns(final boolean compactDropdowns) {
    this.compactDropdowns = compactDropdowns;
  }

  public boolean isCompactDropdownsChanged(final boolean isCompactDropdowns) {
    return compactDropdowns != isCompactDropdowns;
  }
  //endregion

  //region Monochrome Icons

  public boolean isMonochromeIcons() {
    return monochromeIcons;
  }

  public void setMonochromeIcons(final boolean monochromeIcons) {
    this.monochromeIcons = monochromeIcons;
  }

  public boolean isMonochromeIconsChanged(final boolean isMonochromeIcons) {
    return monochromeIcons != isMonochromeIcons;
  }

  //endregion

  //region UpperCase Buttons

  public boolean isUpperCaseButtons() {
    return upperCaseButtons;
  }

  public void setUpperCaseButtons(final boolean upperCaseButtons) {
    this.upperCaseButtons = upperCaseButtons;
  }

  public boolean isUpperCaseButtonsChanged(final boolean isUppercaseButtons) {
    return upperCaseButtons != isUppercaseButtons;
  }
  //endregion

  // region Decorated Folders
  public void setIsDecoratedFolders(final boolean isDecoratedFolders) {
    this.isDecoratedFolders = isDecoratedFolders;
  }

  public boolean isDecoratedFolders() {
    return isDecoratedFolders;
  }

  public boolean isDecoratedFoldersChanged(final boolean decoratedFolders) {
    return isDecoratedFolders != decoratedFolders;
  }
  //endregion

  // region Tree Font Size
  public int getTreeFontSize() {
    return treeFontSize;
  }

  public void setTreeFontSize(final int treeFontSize) {
    this.treeFontSize = treeFontSize;
  }

  public boolean treeFontSizeChanged(final Integer treeFontSize) {
    return this.treeFontSize != treeFontSize;
  }

  public boolean isTreeFontSizeEnabled() {
    return treeFontSizeEnabled;
  }

  public void setTreeFontSizeEnabled(final boolean treeFontSizeEnabled) {
    this.treeFontSizeEnabled = treeFontSizeEnabled;
  }

  public boolean isTreeFontSizeEnabledChanged(final boolean treeFontSizeEnabled) {
    return this.treeFontSizeEnabled != treeFontSizeEnabled;
  }
  // endregion

  //region File Status Colors
  public boolean isFileStatusColorsEnabled() {
    return fileStatusColorsEnabled;
  }

  public void setFileStatusColorsEnabled(final boolean enabled) {
    fileStatusColorsEnabled = enabled;
  }

  public boolean isFileStatusColorsEnabledChanged(final boolean fileStatusColors) {
    return fileStatusColorsEnabled != fileStatusColors;
  }
  //endregion

  //region High Contrast
  public void setIsHighContrast(final boolean isHighContrast) {
    this.isHighContrast = isHighContrast;
  }

  public boolean getIsHighContrast() {
    return isHighContrast;
  }

  public boolean isHighContrastChanged(final boolean isHighContrast) {
    return this.isHighContrast != isHighContrast;
  }

  //endregion

  //region Settings Selected Tab
  public void setSettingsSelectedTab(final Integer settingsSelectedTab) {
    this.settingsSelectedTab = settingsSelectedTab;
  }

  public Integer getSettingsSelectedTab() {
    return settingsSelectedTab;
  }
  //endregion

  public String getUserId() {
    return userId;
  }

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  public boolean isDisallowDataCollection() {
    return !allowDataCollection;
  }

  public void setAllowDataCollection(final boolean allowDataCollection) {
    this.allowDataCollection = allowDataCollection;
  }

  public boolean getIsWizardShown() {
    return isWizardShown;
  }

  public void setIsWizardShown(final boolean isWizardShown) {
    this.isWizardShown = isWizardShown;
  }
}
