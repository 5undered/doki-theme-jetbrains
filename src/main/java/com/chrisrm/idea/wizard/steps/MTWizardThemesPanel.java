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
 * Created by JFormDesigner on Fri Jun 29 18:52:29 IDT 2018
 */

package com.chrisrm.idea.wizard.steps;

import com.chrisrm.idea.MTConfig;
import com.chrisrm.idea.MTThemeFacade;
import com.chrisrm.idea.MTThemeManager;
import com.chrisrm.idea.MTThemes;
import com.intellij.ide.customize.AbstractCustomizeWizardStep;
import com.intellij.ui.components.JBScrollPane;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * @author Elior Boukhobza
 */
public class MTWizardThemesPanel extends AbstractCustomizeWizardStep {
  public MTWizardThemesPanel() {
    initComponents();
  }

  @Override
  protected String getTitle() {
    return "Themes";
  }

  @Override
  protected String getHTMLHeader() {
    return "<html><body><h2>Select a UI theme</h2>&nbsp;</body></html>";
  }

  @Nullable
  @Override
  protected String getHTMLFooter() {
    return "You can also select a Custom Theme or External Themes from the Material Theme Settings.";
  }

  private void selectTheme(final MTThemeFacade theme) {
    MTConfig.getInstance().setSelectedTheme(theme);
    MTThemeManager.getInstance().activate(theme, true);
  }

  private void oceanicButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.OCEANIC);
  }

  private void darkerButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.DARKER);
  }

  private void palenightButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.PALENIGHT);
  }

  private void lighterButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.LIGHTER);
  }

  private void deepoceanButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.DEEPOCEAN);
  }

  private void monokaiButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.MONOKAI);
  }

  private void arcdarkButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.ARC_DARK);
  }

  private void onedarkButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.ONE_DARK);
  }

  private void onelightButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.ONE_LIGHT);
  }

  private void solarizedDarkButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.SOLARIZED_DARK);
  }

  private void solarizedLightButtonActionPerformed(final ActionEvent e) {
    selectTheme(MTThemes.SOLARIZED_LIGHT);
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.MTWizardBundle");
    scrollPane = new JBScrollPane();
    grid = new JPanel();
    oceanicPanel = new JPanel();
    oceanicButton = new JRadioButton();
    oceanicLabel = new JLabel();
    darkerLayout = new JPanel();
    darkerButton = new JRadioButton();
    darkerLabel = new JLabel();
    palenightPanel = new JPanel();
    palenightButton = new JRadioButton();
    palenightLabel = new JLabel();
    lighterPanel = new JPanel();
    lighterButton = new JRadioButton();
    lighterLabel = new JLabel();
    deepoceanPanel = new JPanel();
    deepoceanButton = new JRadioButton();
    deepoceanLabel = new JLabel();
    monokaiPanel = new JPanel();
    monokaiButton = new JRadioButton();
    monokaiLabel = new JLabel();
    arcdarkPanel = new JPanel();
    arcdarkButton = new JRadioButton();
    arcdarkLabel = new JLabel();
    onedarkPanel = new JPanel();
    onedarkButton = new JRadioButton();
    onedarkLabel = new JLabel();
    onelightPanel = new JPanel();
    onelightButton = new JRadioButton();
    onelightLabel = new JLabel();
    solarizeddarkPanel = new JPanel();
    solarizedDarkButton = new JRadioButton();
    solarizedDarkLabel = new JLabel();
    solarizedlightPanel = new JPanel();
    solarizedLightButton = new JRadioButton();
    solarizedLightLabel = new JLabel();

    //======== this ========
    setLayout(new BorderLayout());

    //======== scrollPane ========
    {
      scrollPane.setBorder(null);

      //======== grid ========
      {
        grid.setMaximumSize(new Dimension(2147483647, 200));
        grid.setLayout(new MigLayout(
            "flowy,insets 0,align left top",
            // columns
            "[left]" +
                "[grow,fill]",
            // rows
            "[grow,top]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

        //======== oceanicPanel ========
        {
          oceanicPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          oceanicPanel.setLayout(new BoxLayout(oceanicPanel, BoxLayout.Y_AXIS));

          //---- oceanicButton ----
          oceanicButton.setText("Oceanic");
          oceanicButton.setHorizontalAlignment(SwingConstants.LEFT);
          oceanicButton.setActionCommand(bundle.getString("MTWizardThemesPanel.oceanicButton.actionCommand"));
          oceanicButton.addActionListener(e -> oceanicButtonActionPerformed(e));
          oceanicPanel.add(oceanicButton);

          //---- oceanicLabel ----
          oceanicLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/oceanic.png")));
          oceanicPanel.add(oceanicLabel);
        }
        grid.add(oceanicPanel, "cell 0 0");

        //======== darkerLayout ========
        {
          darkerLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
          darkerLayout.setLayout(new BoxLayout(darkerLayout, BoxLayout.Y_AXIS));

          //---- darkerButton ----
          darkerButton.setText(bundle.getString("MTWizardThemesPanel.darkerButton.text"));
          darkerButton.setHorizontalAlignment(SwingConstants.LEFT);
          darkerButton.addActionListener(e -> darkerButtonActionPerformed(e));
          darkerLayout.add(darkerButton);

          //---- darkerLabel ----
          darkerLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/darker.png")));
          darkerLayout.add(darkerLabel);
        }
        grid.add(darkerLayout, "cell 1 0,align center center,grow 0 0");

        //======== palenightPanel ========
        {
          palenightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          palenightPanel.setLayout(new BoxLayout(palenightPanel, BoxLayout.Y_AXIS));

          //---- palenightButton ----
          palenightButton.setText(bundle.getString("MTWizardThemesPanel.palenightButton.text"));
          palenightButton.addActionListener(e -> palenightButtonActionPerformed(e));
          palenightPanel.add(palenightButton);

          //---- palenightLabel ----
          palenightLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/palenight.png")));
          palenightPanel.add(palenightLabel);
        }
        grid.add(palenightPanel, "cell 0 1,align center center,grow 0 0");

        //======== lighterPanel ========
        {
          lighterPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          lighterPanel.setLayout(new BoxLayout(lighterPanel, BoxLayout.Y_AXIS));

          //---- lighterButton ----
          lighterButton.setText(bundle.getString("MTWizardThemesPanel.lighterButton.text"));
          lighterButton.addActionListener(e -> lighterButtonActionPerformed(e));
          lighterPanel.add(lighterButton);

          //---- lighterLabel ----
          lighterLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/lighter.png")));
          lighterPanel.add(lighterLabel);
        }
        grid.add(lighterPanel, "cell 1 1,align center center,grow 0 0");

        //======== deepoceanPanel ========
        {
          deepoceanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          deepoceanPanel.setLayout(new BoxLayout(deepoceanPanel, BoxLayout.Y_AXIS));

          //---- deepoceanButton ----
          deepoceanButton.setText(bundle.getString("MTWizardThemesPanel.deepoceanButton.text"));
          deepoceanButton.addActionListener(e -> deepoceanButtonActionPerformed(e));
          deepoceanPanel.add(deepoceanButton);

          //---- deepoceanLabel ----
          deepoceanLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/deepocean.png")));
          deepoceanPanel.add(deepoceanLabel);
        }
        grid.add(deepoceanPanel, "cell 0 2");

        //======== monokaiPanel ========
        {
          monokaiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          monokaiPanel.setLayout(new BoxLayout(monokaiPanel, BoxLayout.Y_AXIS));

          //---- monokaiButton ----
          monokaiButton.setText(bundle.getString("MTWizardThemesPanel.monokaiButton.text"));
          monokaiButton.addActionListener(e -> monokaiButtonActionPerformed(e));
          monokaiPanel.add(monokaiButton);

          //---- monokaiLabel ----
          monokaiLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/monokai.png")));
          monokaiPanel.add(monokaiLabel);
        }
        grid.add(monokaiPanel, "cell 1 2,align center center,grow 0 0");

        //======== arcdarkPanel ========
        {
          arcdarkPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          arcdarkPanel.setLayout(new BoxLayout(arcdarkPanel, BoxLayout.Y_AXIS));

          //---- arcdarkButton ----
          arcdarkButton.setText(bundle.getString("MTWizardThemesPanel.arcdarkButton.text"));
          arcdarkButton.addActionListener(e -> arcdarkButtonActionPerformed(e));
          arcdarkPanel.add(arcdarkButton);

          //---- arcdarkLabel ----
          arcdarkLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/arcdark.png")));
          arcdarkPanel.add(arcdarkLabel);
        }
        grid.add(arcdarkPanel, "cell 0 3,align center center,grow 0 0");

        //======== onedarkPanel ========
        {
          onedarkPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          onedarkPanel.setLayout(new BoxLayout(onedarkPanel, BoxLayout.Y_AXIS));

          //---- onedarkButton ----
          onedarkButton.setText(bundle.getString("MTWizardThemesPanel.onedarkButton.text"));
          onedarkButton.addActionListener(e -> onedarkButtonActionPerformed(e));
          onedarkPanel.add(onedarkButton);

          //---- onedarkLabel ----
          onedarkLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/onedark.png")));
          onedarkPanel.add(onedarkLabel);
        }
        grid.add(onedarkPanel, "cell 1 3,align center center,grow 0 0");

        //======== onelightPanel ========
        {
          onelightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          onelightPanel.setLayout(new BoxLayout(onelightPanel, BoxLayout.Y_AXIS));

          //---- onelightButton ----
          onelightButton.setText(bundle.getString("MTWizardThemesPanel.onelightButton.text"));
          onelightButton.addActionListener(e -> onelightButtonActionPerformed(e));
          onelightPanel.add(onelightButton);

          //---- onelightLabel ----
          onelightLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/onelight.png")));
          onelightPanel.add(onelightLabel);
        }
        grid.add(onelightPanel, "cell 0 4");

        //======== solarizeddarkPanel ========
        {
          solarizeddarkPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          solarizeddarkPanel.setLayout(new BoxLayout(solarizeddarkPanel, BoxLayout.Y_AXIS));

          //---- solarizedDarkButton ----
          solarizedDarkButton.setText(bundle.getString("MTWizardThemesPanel.solarizedDarkButton.text"));
          solarizedDarkButton.setHorizontalAlignment(SwingConstants.LEFT);
          solarizedDarkButton.addActionListener(e -> solarizedDarkButtonActionPerformed(e));
          solarizeddarkPanel.add(solarizedDarkButton);

          //---- solarizedDarkLabel ----
          solarizedDarkLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/solarizeddark.png")));
          solarizeddarkPanel.add(solarizedDarkLabel);
        }
        grid.add(solarizeddarkPanel, "cell 1 4");

        //======== solarizedlightPanel ========
        {
          solarizedlightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          solarizedlightPanel.setLayout(new BoxLayout(solarizedlightPanel, BoxLayout.Y_AXIS));

          //---- solarizedLightButton ----
          solarizedLightButton.setText(bundle.getString("MTWizardThemesPanel.solarizedLightButton.text"));
          solarizedLightButton.addActionListener(e -> solarizedLightButtonActionPerformed(e));
          solarizedlightPanel.add(solarizedLightButton);

          //---- solarizedLightLabel ----
          solarizedLightLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/solarizedlight.png")));
          solarizedlightPanel.add(solarizedLightLabel);
        }
        grid.add(solarizedlightPanel, "cell 0 5,align center center,grow 0 0");
      }
      scrollPane.setViewportView(grid);
    }
    add(scrollPane, BorderLayout.CENTER);

    //---- selectedTheme ----
    final ButtonGroup selectedTheme = new ButtonGroup();
    selectedTheme.add(oceanicButton);
    selectedTheme.add(darkerButton);
    selectedTheme.add(palenightButton);
    selectedTheme.add(lighterButton);
    selectedTheme.add(deepoceanButton);
    selectedTheme.add(monokaiButton);
    selectedTheme.add(arcdarkButton);
    selectedTheme.add(onedarkButton);
    selectedTheme.add(onelightButton);
    selectedTheme.add(solarizedDarkButton);
    selectedTheme.add(solarizedLightButton);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JBScrollPane scrollPane;
  private JPanel grid;
  private JPanel oceanicPanel;
  private JRadioButton oceanicButton;
  private JLabel oceanicLabel;
  private JPanel darkerLayout;
  private JRadioButton darkerButton;
  private JLabel darkerLabel;
  private JPanel palenightPanel;
  private JRadioButton palenightButton;
  private JLabel palenightLabel;
  private JPanel lighterPanel;
  private JRadioButton lighterButton;
  private JLabel lighterLabel;
  private JPanel deepoceanPanel;
  private JRadioButton deepoceanButton;
  private JLabel deepoceanLabel;
  private JPanel monokaiPanel;
  private JRadioButton monokaiButton;
  private JLabel monokaiLabel;
  private JPanel arcdarkPanel;
  private JRadioButton arcdarkButton;
  private JLabel arcdarkLabel;
  private JPanel onedarkPanel;
  private JRadioButton onedarkButton;
  private JLabel onedarkLabel;
  private JPanel onelightPanel;
  private JRadioButton onelightButton;
  private JLabel onelightLabel;
  private JPanel solarizeddarkPanel;
  private JRadioButton solarizedDarkButton;
  private JLabel solarizedDarkLabel;
  private JPanel solarizedlightPanel;
  private JRadioButton solarizedLightButton;
  private JLabel solarizedLightLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

}
