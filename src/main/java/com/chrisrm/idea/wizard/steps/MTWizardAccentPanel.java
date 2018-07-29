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
 * Created by JFormDesigner on Mon Jul 23 21:25:27 IDT 2018
 */

package com.chrisrm.idea.wizard.steps;

import com.chrisrm.idea.MTConfig;
import com.chrisrm.idea.MTThemeManager;
import com.intellij.ide.customize.AbstractCustomizeWizardStep;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.OnOffButton;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Elior Boukhobza
 */
public class MTWizardAccentPanel extends AbstractCustomizeWizardStep {

  private final MTConfig config;

  public MTWizardAccentPanel() {
    config = MTConfig.getInstance();
    initComponents();
    accentColorChooser.setSelectedColor(ColorUtil.fromHex(config.getAccentColor()));
  }

  @Override
  protected String getTitle() {
    return "Accent";
  }

  @Override
  protected String getHTMLHeader() {
    return "<html><body><h2>Accent Color</h2></body></html>";
  }

  private void accentThemeCheckboxActionPerformed(final ActionEvent e) {
    config.setOverrideAccentColor(accentThemeCheckbox.isSelected());
  }

  private void accentColorChooserActionPerformed(final ActionEvent e) {
    // TODO add your code here
    config.setAccentColor(ColorUtil.toHex(Objects.requireNonNull(accentColorChooser.getSelectedColor())));
    MTThemeManager.getInstance().applyAccents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.MTWizardBundle");
    scrollPane = new JBScrollPane();
    content = new JPanel();
    accentColorPanel = new JPanel();
    accentColorLabel = new JLabel();
    accentColorChooser = new ColorPanel();
    accentDescPanel = new JPanel();
    accentDesc = new JTextArea();
    accentOptionsPanel = new JPanel();
    accentThemeCheckbox = new JCheckBox();
    separator1 = new JSeparator();
    previewLabel = new JLabel();
    previewPanel = new JPanel();
    textField1 = new JTextField();
    comboBox1 = new JComboBox();
    spinner1 = new JSpinner();
    checkBox1 = new JCheckBox();
    radioButton1 = new JRadioButton();
    onOffButton1 = new OnOffButton();
    progressBar1 = new JProgressBar();
    slider1 = new JSlider();

    //======== this ========
    setLayout(new BorderLayout());

    //======== scrollPane ========
    {
      scrollPane.setBorder(null);

      //======== content ========
      {
        content.setMinimumSize(null);
        content.setPreferredSize(null);
        content.setMaximumSize(new Dimension(600, 32767));
        content.setBorder(null);
        content.setLayout(new VerticalLayout());

        //======== accentColorPanel ========
        {
          accentColorPanel.setFont(accentColorPanel.getFont().deriveFont(accentColorPanel.getFont().getSize() + 3f));
          accentColorPanel.setPreferredSize(null);
          accentColorPanel.setMinimumSize(null);
          accentColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

          //---- accentColorLabel ----
          accentColorLabel.setText(bundle.getString("MTWizardAccentPanel.accentColorLabel.text"));
          accentColorLabel.setLabelFor(accentColorChooser);
          accentColorLabel.setIcon(new ImageIcon(getClass().getResource("/icons/actions/mt/customAccent.png")));
          accentColorLabel.setFont(accentColorLabel.getFont().deriveFont(accentColorLabel.getFont().getStyle() | Font.BOLD,
              accentColorLabel.getFont().getSize() + 2f));
          accentColorPanel.add(accentColorLabel);

          //---- accentColorChooser ----
          accentColorChooser.setToolTipText("Select an accent color");
          accentColorChooser.addActionListener(e -> accentColorChooserActionPerformed(e));
          accentColorPanel.add(accentColorChooser);
        }
        content.add(accentColorPanel);

        //======== accentDescPanel ========
        {
          accentDescPanel.setMinimumSize(null);
          accentDescPanel.setPreferredSize(null);
          accentDescPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

          //---- accentDesc ----
          accentDesc.setText(bundle.getString("MTWizardAccentPanel.accentDesc.text"));
          accentDesc.setFont(UIManager.getFont("Label.font"));
          accentDesc.setBackground(UIManager.getColor("Panel.background"));
          accentDesc.setEditable(false);
          accentDesc.setBorder(null);
          accentDesc.setLineWrap(true);
          accentDesc.setColumns(1);
          accentDesc.setRows(2);
          accentDesc.setTabSize(10);
          accentDesc.setPreferredSize(new Dimension(500, 40));
          accentDesc.setMinimumSize(null);
          accentDescPanel.add(accentDesc);
        }
        content.add(accentDescPanel);

        //======== accentOptionsPanel ========
        {
          accentOptionsPanel.setFont(UIManager.getFont("Label.font"));
          accentOptionsPanel.setPreferredSize(null);
          accentOptionsPanel.setMinimumSize(null);
          accentOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

          //---- accentThemeCheckbox ----
          accentThemeCheckbox.setText(bundle.getString("MTWizardAccentPanel.accentThemeCheckbox.text"));
          accentThemeCheckbox.setFont(accentThemeCheckbox.getFont().deriveFont(accentThemeCheckbox.getFont().getSize() - 1f));
          accentThemeCheckbox.setToolTipText("When selected, selecting a theme will override the accent color with the one from the theme");
          accentThemeCheckbox.addActionListener(e -> accentThemeCheckboxActionPerformed(e));
          accentOptionsPanel.add(accentThemeCheckbox);
        }
        content.add(accentOptionsPanel);

        //---- separator1 ----
        separator1.setPreferredSize(null);
        separator1.setMinimumSize(null);
        content.add(separator1);

        //---- previewLabel ----
        previewLabel.setText(bundle.getString("MTWizardAccentPanel.previewLabel.text"));
        previewLabel.setAlignmentX(0.5F);
        previewLabel.setPreferredSize(null);
        previewLabel.setMinimumSize(null);
        content.add(previewLabel);

        //======== previewPanel ========
        {
          previewPanel.setMinimumSize(null);
          previewPanel.setPreferredSize(null);
          previewPanel.setLayout(new FlowLayout());
          previewPanel.add(textField1);
          previewPanel.add(comboBox1);
          previewPanel.add(spinner1);

          //---- checkBox1 ----
          checkBox1.setText("text");
          previewPanel.add(checkBox1);

          //---- radioButton1 ----
          radioButton1.setText("text");
          previewPanel.add(radioButton1);

          //---- onOffButton1 ----
          onOffButton1.setText("text");
          onOffButton1.setMinimumSize(new Dimension(52, 20));
          onOffButton1.setPreferredSize(new Dimension(52, 20));
          previewPanel.add(onOffButton1);
          previewPanel.add(progressBar1);
        }
        content.add(previewPanel);

        //---- slider1 ----
        slider1.setPreferredSize(null);
        slider1.setMinimumSize(null);
        content.add(slider1);
      }
      scrollPane.setViewportView(content);
    }
    add(scrollPane, BorderLayout.CENTER);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JBScrollPane scrollPane;
  private JPanel content;
  private JPanel accentColorPanel;
  private JLabel accentColorLabel;
  private ColorPanel accentColorChooser;
  private JPanel accentDescPanel;
  private JTextArea accentDesc;
  private JPanel accentOptionsPanel;
  private JCheckBox accentThemeCheckbox;
  private JSeparator separator1;
  private JLabel previewLabel;
  private JPanel previewPanel;
  private JTextField textField1;
  private JComboBox comboBox1;
  private JSpinner spinner1;
  private JCheckBox checkBox1;
  private JRadioButton radioButton1;
  private OnOffButton onOffButton1;
  private JProgressBar progressBar1;
  private JSlider slider1;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
