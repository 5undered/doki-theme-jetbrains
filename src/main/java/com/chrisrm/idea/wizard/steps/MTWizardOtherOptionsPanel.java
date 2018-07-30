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

/*
 * Created by JFormDesigner on Tue Jul 24 22:51:19 IDT 2018
 */

package com.chrisrm.idea.wizard.steps;

import com.chrisrm.idea.MTConfig;
import com.chrisrm.idea.config.ui.ArrowsStyles;
import com.chrisrm.idea.config.ui.IndicatorStyles;
import com.intellij.ide.customize.AbstractCustomizeWizardStep;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.labels.LinkLabel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

/**
 * @author Elior Boukhobza
 */
public class MTWizardOtherOptionsPanel extends AbstractCustomizeWizardStep {
  private SpinnerModel highlightSpinnerModel;
  private SpinnerModel tabsHeightSpinnerModel;
  private SpinnerModel customSidebarHeightModel;
  private final MTConfig config;

  public MTWizardOtherOptionsPanel() {
    config = MTConfig.getInstance();

    initComponents();
    init();
  }

  private void init() {
    final int highlightThickness = valueInRange(config.getHighlightThickness(), MTConfig.MIN_HIGHLIGHT_THICKNESS,
        MTConfig.MAX_HIGHLIGHT_THICKNESS);
    final int tabsHeight = valueInRange(config.getTabsHeight(), MTConfig.MIN_TABS_HEIGHT, MTConfig.MAX_TABS_HEIGHT);
    final int customSidebarHeight = valueInRange(config.getCustomSidebarHeight(), MTConfig.MIN_SIDEBAR_HEIGHT, MTConfig.MAX_SIDEBAR_HEIGHT);

    highlightSpinnerModel = new SpinnerNumberModel(highlightThickness, MTConfig.MIN_HIGHLIGHT_THICKNESS,
        MTConfig.MAX_HIGHLIGHT_THICKNESS, 1);
    highlightSpinner.setModel(highlightSpinnerModel);
    tabsHeightSpinnerModel = new SpinnerNumberModel(tabsHeight, MTConfig.MIN_TABS_HEIGHT, MTConfig.MAX_TABS_HEIGHT, 1);
    tabHeightSpinner.setModel(tabsHeightSpinnerModel);
    customSidebarHeightModel = new SpinnerNumberModel(customSidebarHeight, MTConfig.MIN_SIDEBAR_HEIGHT, MTConfig.MAX_SIDEBAR_HEIGHT, 2);
    sidebarHeightSpinner.setModel(customSidebarHeightModel);

    // mono
    monochromeIconsCheckbox.setSelected(config.isMonochromeIcons());

    // folder dec
    folderDecoratorsCheckbox.setSelected(config.isDecoratedFolders());

    // compact stat
    compactStatusCheckbox.setSelected(config.isCompactStatusBar());

    // compact drop
    compactDropdownCheckbox.setSelected(config.isCompactDropdowns());

    // compact table
    compactTableCheckbox.setSelected(config.isCompactTables());

    // file colors
    fileColorsCheckbox.setSelected(config.isFileStatusColorsEnabled());

    // project view dec
    projectViewDecoratorsCheckbox.setSelected(config.isUseProjectViewDecorators());

    // title bar
    titleBarCheckbox.setSelected(config.isDarkTitleBar());

    // arrow styles
    arrowsStyleComboBox.setSelectedItem(config.getArrowsStyle());

    // indicator
    indicatorStyleComboBox.setSelectedItem(config.getIndicatorStyle());
  }

  @Override
  protected String getTitle() {
    return "Other Settings";
  }

  @Override
  protected String getHTMLHeader() {
    return "<html><body><h2>Other useful settings</h2></body></html>";
  }

  @Nullable
  @Override
  protected String getHTMLFooter() {
    return "You can find all the options in the settings at Appearance | Material Theme";
  }

  private void isCompactSidebarCheckboxActionPerformed(final ActionEvent e) {
    // TODO add your code here
  }

  private void tabHeightSpinnerStateChanged(final ChangeEvent e) {
    config.setTabsHeight((Integer) tabHeightSpinner.getModel().getValue());
  }

  private void highlightSpinnerStateChanged(final ChangeEvent e) {
    config.setHighlightThickness((Integer) highlightSpinner.getModel().getValue());
  }

  private void compactStatusCheckboxActionPerformed(final ActionEvent e) {
    config.setIsCompactStatusBar(compactStatusCheckbox.isSelected());
  }

  private void compactTableCheckboxActionPerformed(final ActionEvent e) {
    config.setIsCompactTables(compactTableCheckbox.isSelected());
  }

  private void compactDropdownCheckboxActionPerformed(final ActionEvent e) {
    config.setCompactDropdowns(compactDropdownCheckbox.isSelected());
  }

  private void sidebarHeightSpinnerStateChanged(final ChangeEvent e) {
    config.setCompactSidebar(true);
    config.setCustomSidebarHeight((Integer) sidebarHeightSpinner.getModel().getValue());
  }

  private void arrowsStyleComboBoxActionPerformed(final ActionEvent e) {
    config.setArrowsStyle((ArrowsStyles) arrowsStyleComboBox.getSelectedItem());
  }

  private void indicatorStyleComboBoxActionPerformed(final ActionEvent e) {
    config.setIndicatorStyle((IndicatorStyles) indicatorStyleComboBox.getSelectedItem());
  }

  private void monochromeIconsCheckboxActionPerformed(final ActionEvent e) {
    config.setMonochromeIcons(monochromeIconsCheckbox.isSelected());
  }

  private void folderDecoratorsCheckboxActionPerformed(final ActionEvent e) {
    config.setIsDecoratedFolders(folderDecoratorsCheckbox.isSelected());
  }

  private void fileColorsCheckboxActionPerformed(final ActionEvent e) {
    config.setFileStatusColorsEnabled(fileColorsCheckbox.isSelected());
  }

  private void projectViewDecoratorsCheckboxActionPerformed(final ActionEvent e) {
    config.setUseProjectViewDecorators(projectViewDecoratorsCheckbox.isSelected());
  }

  private void titleBarCheckboxActionPerformed(final ActionEvent e) {
    config.setDarkTitleBar(titleBarCheckbox.isSelected());
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.MTWizardBundle");
    scrollPane = new JBScrollPane();
    content = new JPanel();
    tabsPanel = new JPanel();
    final JLabel tabHeight = new JLabel();
    tabHeightSpinner = new JSpinner();
    tabHeightDesc = new JTextArea();
    final JLabel thicknessLabel = new JLabel();
    highlightSpinner = new JSpinner();
    thicknessDesc = new JTextArea();
    iconsPanel = new JPanel();
    monochromeIconsCheckbox = new JCheckBox();
    monochromeIconsDesc = new JTextArea();
    folderDecoratorsCheckbox = new JCheckBox();
    folderDecoratorsDesc = new JTextArea();
    panelPanel = new JPanel();
    compactStatusCheckbox = new JCheckBox();
    compactStatusDesc = new JTextArea();
    compactTableCheckbox = new JCheckBox();
    compactTableDesc = new JTextArea();
    compactDropdownCheckbox = new JCheckBox();
    compactStatusDesc2 = new JTextArea();
    otherPanel = new JPanel();
    fileColorsCheckbox = new JCheckBox();
    fileColorsDesc = new JTextArea();
    projectViewDecoratorsCheckbox = new JCheckBox();
    projectViewDecoratorsDesc = new JTextArea();
    titleBarCheckbox = new JCheckBox();
    titleBarDesc = new JTextArea();
    titleBarDesc2 = new JTextArea();
    moreInfoLink = new LinkLabel();
    projectPanel = new JPanel();
    final JLabel sidebarHeight = new JLabel();
    sidebarHeightSpinner = new JSpinner();
    sidebarHeightDesc = new JTextArea();
    arrowsStyleLabel = new JLabel();
    arrowsStyleComboBox = new ComboBox<>();
    arrowsStyleDesc = new JTextArea();
    selectedIndicatorLabel = new JLabel();
    indicatorStyleComboBox = new ComboBox<>();
    arrowsStyleDesc2 = new JTextArea();

    //======== this ========
    setLayout(new BorderLayout());

    //======== scrollPane ========
    {
      scrollPane.setBorder(null);

      //======== content ========
      {
        content.setBorder(null);
        content.setLayout(new MigLayout(
            "fillx,novisualpadding,hidemode 3,align left top",
            // columns
            "[325,grow,fill]" +
                "[grow,fill]",
            // rows
            "[]" +
                "[]" +
                "[]"));

        //======== tabsPanel ========
        {
          tabsPanel.setBorder(new TitledBorder(bundle.getString("MTWizardOtherOptionsPanel.tabsPanel.border")));
          tabsPanel.setLayout(new MigLayout(
              "insets 0,hidemode 3",
              // columns
              "[grow,left]",
              // rows
              "[20,fill]0" +
                  "[]" +
                  "[]0" +
                  "[]"));

          //---- tabHeight ----
          tabHeight.setHorizontalTextPosition(SwingConstants.LEADING);
          tabHeight.setText(bundle.getString("MTWizardOtherOptionsPanel.tabHeight.text"));
          tabHeight.setToolTipText("Set a custom tab height (between 25 and 60)");
          tabsPanel.add(tabHeight, "pad 0 4 0 0,cell 0 0,aligny center,grow 100 0");

          //---- tabHeightSpinner ----
          tabHeightSpinner.addChangeListener(e -> tabHeightSpinnerStateChanged(e));
          tabsPanel.add(tabHeightSpinner, "cell 0 0,wmax 60");

          //---- tabHeightDesc ----
          tabHeightDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.tabHeightDesc.text"));
          tabHeightDesc.setFont(UIManager.getFont("Label.font"));
          tabHeightDesc.setBackground(UIManager.getColor("Panel.background"));
          tabHeightDesc.setEditable(false);
          tabHeightDesc.setEnabled(false);
          tabsPanel.add(tabHeightDesc, "pad 0 10 0 10,cell 0 1");

          //---- thicknessLabel ----
          thicknessLabel.setHorizontalTextPosition(SwingConstants.LEADING);
          thicknessLabel.setLabelFor(highlightSpinner);
          thicknessLabel.setText(bundle.getString("MTWizardOtherOptionsPanel.thicknessLabel.text"));
          thicknessLabel.setToolTipText("Specify the thickness of the indicator");
          tabsPanel.add(thicknessLabel, "pad 0 4 0 0,cell 0 2,aligny center,grow 100 0");

          //---- highlightSpinner ----
          highlightSpinner.addChangeListener(e -> highlightSpinnerStateChanged(e));
          tabsPanel.add(highlightSpinner, "cell 0 2,wmax 60");

          //---- thicknessDesc ----
          thicknessDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.thicknessDesc.text"));
          thicknessDesc.setFont(UIManager.getFont("Label.font"));
          thicknessDesc.setBackground(UIManager.getColor("Panel.background"));
          thicknessDesc.setEditable(false);
          thicknessDesc.setEnabled(false);
          tabsPanel.add(thicknessDesc, "pad 0 10 0 10,cell 0 3");
        }
        content.add(tabsPanel, "cell 0 0,aligny top,growy 0");

        //======== iconsPanel ========
        {
          iconsPanel.setBorder(new TitledBorder(bundle.getString("MTWizardOtherOptionsPanel.iconsPanel.border")));
          iconsPanel.setLayout(new MigLayout(
              "insets 0,hidemode 3",
              // columns
              "[left]",
              // rows
              "0[18,fill]0" +
                  "[]" +
                  "[]0" +
                  "[]"));

          //---- monochromeIconsCheckbox ----
          monochromeIconsCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.monochromeIconsCheckbox.text"));
          monochromeIconsCheckbox.addActionListener(e -> monochromeIconsCheckboxActionPerformed(e));
          iconsPanel.add(monochromeIconsCheckbox, "cell 0 0");

          //---- monochromeIconsDesc ----
          monochromeIconsDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.monochromeIconsDesc.text"));
          monochromeIconsDesc.setFont(UIManager.getFont("Label.font"));
          monochromeIconsDesc.setBackground(UIManager.getColor("Panel.background"));
          monochromeIconsDesc.setEditable(false);
          monochromeIconsDesc.setEnabled(false);
          iconsPanel.add(monochromeIconsDesc, "pad 0 10 0 10,cell 0 1");

          //---- folderDecoratorsCheckbox ----
          folderDecoratorsCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.folderDecoratorsCheckbox.text"));
          folderDecoratorsCheckbox.addActionListener(e -> folderDecoratorsCheckboxActionPerformed(e));
          iconsPanel.add(folderDecoratorsCheckbox, "cell 0 2");

          //---- folderDecoratorsDesc ----
          folderDecoratorsDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.folderDecoratorsDesc.text"));
          folderDecoratorsDesc.setFont(UIManager.getFont("Label.font"));
          folderDecoratorsDesc.setBackground(UIManager.getColor("Panel.background"));
          folderDecoratorsDesc.setEditable(false);
          folderDecoratorsDesc.setEnabled(false);
          iconsPanel.add(folderDecoratorsDesc, "pad 0 10 0 10,cell 0 3");
        }
        content.add(iconsPanel, "cell 1 0,aligny top,growy 0");

        //======== panelPanel ========
        {
          panelPanel.setBorder(new TitledBorder(bundle.getString("MTWizardOtherOptionsPanel.panelPanel.border")));
          panelPanel.setLayout(new MigLayout(
              "insets 0,hidemode 3",
              // columns
              "[grow,left]",
              // rows
              "0[18,fill]0" +
                  "[]" +
                  "[]0" +
                  "[]" +
                  "[]0" +
                  "[]"));

          //---- compactStatusCheckbox ----
          compactStatusCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.compactStatusCheckbox.text"));
          compactStatusCheckbox.addActionListener(e -> compactStatusCheckboxActionPerformed(e));
          panelPanel.add(compactStatusCheckbox, "cell 0 0");

          //---- compactStatusDesc ----
          compactStatusDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.compactStatusDesc.text"));
          compactStatusDesc.setFont(UIManager.getFont("Label.font"));
          compactStatusDesc.setBackground(UIManager.getColor("Panel.background"));
          compactStatusDesc.setEditable(false);
          compactStatusDesc.setEnabled(false);
          panelPanel.add(compactStatusDesc, "pad 0 10 0 10,cell 0 1");

          //---- compactTableCheckbox ----
          compactTableCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.compactTableCheckbox.text"));
          compactTableCheckbox.addActionListener(e -> compactTableCheckboxActionPerformed(e));
          panelPanel.add(compactTableCheckbox, "cell 0 2");

          //---- compactTableDesc ----
          compactTableDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.compactTableDesc.text"));
          compactTableDesc.setFont(UIManager.getFont("Label.font"));
          compactTableDesc.setBackground(UIManager.getColor("Panel.background"));
          compactTableDesc.setEditable(false);
          compactTableDesc.setEnabled(false);
          panelPanel.add(compactTableDesc, "pad 0 10 0 10,cell 0 3");

          //---- compactDropdownCheckbox ----
          compactDropdownCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.compactDropdownCheckbox.text"));
          compactDropdownCheckbox.addActionListener(e -> compactDropdownCheckboxActionPerformed(e));
          panelPanel.add(compactDropdownCheckbox, "cell 0 4");

          //---- compactStatusDesc2 ----
          compactStatusDesc2.setText(bundle.getString("MTWizardOtherOptionsPanel.compactStatusDesc2.text"));
          compactStatusDesc2.setFont(UIManager.getFont("Label.font"));
          compactStatusDesc2.setBackground(UIManager.getColor("Panel.background"));
          compactStatusDesc2.setEditable(false);
          compactStatusDesc2.setEnabled(false);
          panelPanel.add(compactStatusDesc2, "pad 0 10 0 10,cell 0 5");
        }
        content.add(panelPanel, "cell 0 1,aligny top,growy 0");

        //======== otherPanel ========
        {
          otherPanel.setBorder(new TitledBorder(bundle.getString("MTWizardOtherOptionsPanel.otherPanel.border")));
          otherPanel.setLayout(new MigLayout(
              "insets 0,hidemode 3",
              // columns
              "[grow,left]",
              // rows
              "0[18,fill]0" +
                  "[]" +
                  "[]0" +
                  "[]" +
                  "[]0" +
                  "[]rel" +
                  "[]"));

          //---- fileColorsCheckbox ----
          fileColorsCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.fileColorsCheckbox.text"));
          fileColorsCheckbox.addActionListener(e -> fileColorsCheckboxActionPerformed(e));
          otherPanel.add(fileColorsCheckbox, "cell 0 0");

          //---- fileColorsDesc ----
          fileColorsDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.fileColorsDesc.text"));
          fileColorsDesc.setFont(UIManager.getFont("Label.font"));
          fileColorsDesc.setBackground(UIManager.getColor("Panel.background"));
          fileColorsDesc.setEditable(false);
          fileColorsDesc.setEnabled(false);
          otherPanel.add(fileColorsDesc, "pad 0 10 0 10,cell 0 1");

          //---- projectViewDecoratorsCheckbox ----
          projectViewDecoratorsCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.projectViewDecoratorsCheckbox.text"));
          projectViewDecoratorsCheckbox.addActionListener(e -> projectViewDecoratorsCheckboxActionPerformed(e));
          otherPanel.add(projectViewDecoratorsCheckbox, "cell 0 2");

          //---- projectViewDecoratorsDesc ----
          projectViewDecoratorsDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.projectViewDecoratorsDesc.text"));
          projectViewDecoratorsDesc.setFont(UIManager.getFont("Label.font"));
          projectViewDecoratorsDesc.setBackground(UIManager.getColor("Panel.background"));
          projectViewDecoratorsDesc.setEditable(false);
          projectViewDecoratorsDesc.setEnabled(false);
          otherPanel.add(projectViewDecoratorsDesc, "pad 0 10 0 10,cell 0 3");

          //---- titleBarCheckbox ----
          titleBarCheckbox.setText(bundle.getString("MTWizardOtherOptionsPanel.titleBarCheckbox.text"));
          titleBarCheckbox.addActionListener(e -> titleBarCheckboxActionPerformed(e));
          otherPanel.add(titleBarCheckbox, "cell 0 4");

          //---- titleBarDesc ----
          titleBarDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.titleBarDesc.text"));
          titleBarDesc.setFont(UIManager.getFont("Label.font"));
          titleBarDesc.setBackground(UIManager.getColor("Panel.background"));
          titleBarDesc.setEditable(false);
          titleBarDesc.setEnabled(false);
          otherPanel.add(titleBarDesc, "pad 0 10 0 10,cell 0 5");

          //---- titleBarDesc2 ----
          titleBarDesc2.setText(bundle.getString("MTWizardOtherOptionsPanel.titleBarDesc2.text"));
          titleBarDesc2.setFont(UIManager.getFont("Label.font"));
          titleBarDesc2.setBackground(UIManager.getColor("Panel.background"));
          titleBarDesc2.setEditable(false);
          titleBarDesc2.setEnabled(false);
          otherPanel.add(titleBarDesc2, "pad 0 10 0 10,cell 0 6");

          //---- moreInfoLink ----
          moreInfoLink.setText("More information here");
          moreInfoLink.setForeground(UIManager.getColor("link.foreground"));
          otherPanel.add(moreInfoLink, "cell 0 6");
        }
        content.add(otherPanel, "cell 1 1,aligny top,growy 0");

        //======== projectPanel ========
        {
          projectPanel.setBorder(new TitledBorder(bundle.getString("MTWizardOtherOptionsPanel.projectPanel.border")));
          projectPanel.setLayout(new MigLayout(
              "insets 0,hidemode 3",
              // columns
              "[grow,left]",
              // rows
              "[20,fill]0" +
                  "[]0" +
                  "[]0" +
                  "[]0" +
                  "[]0" +
                  "[]"));

          //---- sidebarHeight ----
          sidebarHeight.setHorizontalTextPosition(SwingConstants.LEADING);
          sidebarHeight.setText(bundle.getString("MTWizardOtherOptionsPanel.sidebarHeight.text"));
          sidebarHeight.setToolTipText("Set a custom tab height (between 25 and 60)");
          projectPanel.add(sidebarHeight, "pad 0 4 0 0,cell 0 0,aligny center,grow 100 0");

          //---- sidebarHeightSpinner ----
          sidebarHeightSpinner.addChangeListener(e -> sidebarHeightSpinnerStateChanged(e));
          projectPanel.add(sidebarHeightSpinner, "cell 0 0,wmax 60");

          //---- sidebarHeightDesc ----
          sidebarHeightDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.sidebarHeightDesc.text"));
          sidebarHeightDesc.setFont(UIManager.getFont("Label.font"));
          sidebarHeightDesc.setBackground(UIManager.getColor("Panel.background"));
          sidebarHeightDesc.setEditable(false);
          sidebarHeightDesc.setEnabled(false);
          projectPanel.add(sidebarHeightDesc, "pad 0 10 0 10,cell 0 1");

          //---- arrowsStyleLabel ----
          arrowsStyleLabel.setText(bundle.getString("MTWizardOtherOptionsPanel.arrowsStyleLabel.text"));
          arrowsStyleLabel.setToolTipText("Change the style of the arrows in trees");
          projectPanel.add(arrowsStyleLabel, "pad 0 4 0 0,cell 0 2,aligny center,grow 100 0");

          //---- arrowsStyleComboBox ----
          arrowsStyleComboBox.setToolTipText("Change the style of the arrows in trees");
          arrowsStyleComboBox.addActionListener(e -> arrowsStyleComboBoxActionPerformed(e));
          projectPanel.add(arrowsStyleComboBox, "cell 0 2");

          //---- arrowsStyleDesc ----
          arrowsStyleDesc.setText(bundle.getString("MTWizardOtherOptionsPanel.arrowsStyleDesc.text"));
          arrowsStyleDesc.setFont(UIManager.getFont("Label.font"));
          arrowsStyleDesc.setBackground(UIManager.getColor("Panel.background"));
          arrowsStyleDesc.setEditable(false);
          arrowsStyleDesc.setEnabled(false);
          projectPanel.add(arrowsStyleDesc, "pad 0 10 0 10,cell 0 3");

          //---- selectedIndicatorLabel ----
          selectedIndicatorLabel.setText(bundle.getString("MTWizardOtherOptionsPanel.selectedIndicatorLabel.text"));
          selectedIndicatorLabel.setToolTipText("Choose a style for the selected item in trees");
          projectPanel.add(selectedIndicatorLabel, "pad 0 4 0 0,cell 0 4,growx");

          //---- indicatorStyleComboBox ----
          indicatorStyleComboBox.setToolTipText("Change the style of the arrows in trees");
          indicatorStyleComboBox.addActionListener(e -> indicatorStyleComboBoxActionPerformed(e));
          projectPanel.add(indicatorStyleComboBox, "cell 0 4");

          //---- arrowsStyleDesc2 ----
          arrowsStyleDesc2.setText(bundle.getString("MTWizardOtherOptionsPanel.arrowsStyleDesc2.text"));
          arrowsStyleDesc2.setFont(UIManager.getFont("Label.font"));
          arrowsStyleDesc2.setBackground(UIManager.getColor("Panel.background"));
          arrowsStyleDesc2.setEditable(false);
          arrowsStyleDesc2.setEnabled(false);
          projectPanel.add(arrowsStyleDesc2, "pad 0 10 0 10,cell 0 5");
        }
        content.add(projectPanel, "cell 0 2,aligny top,growy 0");
      }
      scrollPane.setViewportView(content);
    }
    add(scrollPane, BorderLayout.CENTER);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents

    // Arrows
    arrowsStyleComboBox.setModel(new DefaultComboBoxModel<>(ArrowsStyles.values()));
    arrowsStyleComboBox.setRenderer(new ListCellRendererWrapper<ArrowsStyles>() {
      @Override
      public void customize(final JList list, final ArrowsStyles value, final int index, final boolean selected, final boolean hasFocus) {
        final Icon baseIcon;
        if (value == null) {
          return;
        }
        baseIcon = value.getIcon();
        setIcon(baseIcon);
      }
    });

    // Indicator
    indicatorStyleComboBox.setModel(new DefaultComboBoxModel<>(IndicatorStyles.values()));

    moreInfoLink.setListener((aSource, aLinkData) -> {
      if (Desktop.isDesktopSupported()) {
        try {
          Desktop.getDesktop().browse(new URI("http://www.material-theme.com/docs/configuration/other-tweaks-settings/#themed-title-bar"));
        } catch (final IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
    }, null);

  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JBScrollPane scrollPane;
  private JPanel content;
  private JPanel tabsPanel;
  private JSpinner tabHeightSpinner;
  private JTextArea tabHeightDesc;
  private JSpinner highlightSpinner;
  private JTextArea thicknessDesc;
  private JPanel iconsPanel;
  private JCheckBox monochromeIconsCheckbox;
  private JTextArea monochromeIconsDesc;
  private JCheckBox folderDecoratorsCheckbox;
  private JTextArea folderDecoratorsDesc;
  private JPanel panelPanel;
  private JCheckBox compactStatusCheckbox;
  private JTextArea compactStatusDesc;
  private JCheckBox compactTableCheckbox;
  private JTextArea compactTableDesc;
  private JCheckBox compactDropdownCheckbox;
  private JTextArea compactStatusDesc2;
  private JPanel otherPanel;
  private JCheckBox fileColorsCheckbox;
  private JTextArea fileColorsDesc;
  private JCheckBox projectViewDecoratorsCheckbox;
  private JTextArea projectViewDecoratorsDesc;
  private JCheckBox titleBarCheckbox;
  private JTextArea titleBarDesc;
  private JTextArea titleBarDesc2;
  private LinkLabel moreInfoLink;
  private JPanel projectPanel;
  private JSpinner sidebarHeightSpinner;
  private JTextArea sidebarHeightDesc;
  private JLabel arrowsStyleLabel;
  private ComboBox<ArrowsStyles> arrowsStyleComboBox;
  private JTextArea arrowsStyleDesc;
  private JLabel selectedIndicatorLabel;
  private ComboBox<IndicatorStyles> indicatorStyleComboBox;
  private JTextArea arrowsStyleDesc2;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  private int valueInRange(final int value, final int min, final int max) {
    return Integer.min(max, Integer.max(value, min));
  }

}
