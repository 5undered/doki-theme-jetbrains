/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.chrisrm.idea;

import com.intellij.ide.navigationToolbar.NavBarIdeView;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.impl.ChameleonAction;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrameProvider;
import com.intellij.ui.CaptionPanel;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

public class MTHackComponent implements ApplicationComponent {
  public static final String TABS_HEIGHT = "MTTabsHeight";
  public static final String BOLD_TABS = "MTBoldTabs";
  public static final String BORDER_POPUP = "MTBorderPopup";

  static {
    hackTitleLabel();
    hackIdeaActionButton();
    hackBackgroundFrame();
    hackTabsGetHeight();
    hackToolWindowHeader();
    hackSpeedSearch();
    hackFlatWelcomeFrame();
    hackPopupBorder();
    hackDarculaTabsPainter();
  }

  public MTHackComponent() {
    PropertiesComponent.getInstance().setValue(TABS_HEIGHT, 25, 24);
    PropertiesComponent.getInstance().setValue(BOLD_TABS, false, true);
    PropertiesComponent.getInstance().setValue(BORDER_POPUP, true, false);
  }

  private static void hackPopupBorder() {
    try {
      final ClassPool cp = new ClassPool(true);
      final CtClass ctClass2 = cp.get("com.intellij.ui.PopupBorder$Factory");
      cp.insertClassPath(new ClassClassPath(TabInfo.class));
      final CtMethod method = ctClass2.getDeclaredMethod("create");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("getBorderColor")) {
            final String code = String.format("com.intellij.ide.util.PropertiesComponent.getInstance().getBoolean(\"%s\", true)",
                BORDER_POPUP);
            m.replace(String.format("{ $_ = %s ? javax.swing.UIManager.getColor(\"Separator.foreground\") : $proceed($$); }", code));
          }
        }
      });
      ctClass2.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private static void hackDarculaTabsPainter() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(JBEditorTabs.class));
      final CtClass ctClass = cp.get("com.intellij.ui.tabs.impl.DarculaEditorTabsPainter");

      final CtMethod defaultTabColor = ctClass.getDeclaredMethod("getDefaultTabColor");
      defaultTabColor.instrument(new ExprEditor() {
        @Override
        public void edit(final FieldAccess f) throws CannotCompileException {
          f.replace("{ $_ = javax.swing.UIManager.getColor(\"TabbedPane.selectHighlight\"); }");
        }
      });
      ctClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private static void hackBackgroundFrame() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(IdeBackgroundUtil.class));
      final CtClass ctClass = cp.get("com.intellij.openapi.wm.impl.IdePanePanel");

      final CtMethod paintBorder = ctClass.getDeclaredMethod("getBackground");
      paintBorder.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("getIdeBackgroundColor")) {
            m.replace("{ $_ = javax.swing.UIManager.getColor(\"Viewport.background\"); }");
          }
        }
      });
      ctClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * For better dialog titles (since I have no idea how to know when dialogs appear, I can't attach events so I'm directly hacking
   * the source code). I hate doing this.
   */
  private static void hackTitleLabel() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(CaptionPanel.class));
      final CtClass ctClass = cp.get("com.intellij.ui.TitlePanel");
      final CtConstructor declaredConstructor = ctClass.getDeclaredConstructor(new CtClass[]{
          cp.get("javax.swing.Icon"),
          cp.get("javax.swing" +
              ".Icon")});
      declaredConstructor.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          switch (m.getMethodName()) {
            case "empty":
              // Replace insets
              m.replace("{ $1 = 10; $2 = 10; $3 = 10; $4 = 10; $_ = $proceed($$); }");
              break;
            case "setHorizontalAlignment":
              // Set title at the left
              m.replace("{ $1 = javax.swing.SwingConstants.LEFT; $_ = $proceed($$); }");
              break;
            case "setBorder":
              // Bigger heading
              m.replace("{ $_ = $proceed($$); myLabel.setFont(myLabel.getFont().deriveFont(1, com.intellij.util.ui.JBUI.scale(16.0f))); }");
              break;
          }
        }
      });
      ctClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Change Look and feel of Action buttons
   */
  private static void hackIdeaActionButton() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(NavBarIdeView.class));
      cp.insertClassPath(new ClassClassPath(ChameleonAction.class));
      final CtClass ctClass = cp.get("com.intellij.ide.navigationToolbar.NavBarBorder");

      final CtMethod paintBorder = ctClass.getDeclaredMethod("paintBorder");
      paintBorder.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("setColor")) {
            m.replace("{ $1 = javax.swing.UIManager.getColor(\"Panel.background\"); $_ = $proceed($$); }");
          }
        }
      });
      ctClass.toClass();

      final CtClass comboBoxActionButtonClass = cp.get("com.intellij.openapi.actionSystem.ex.ComboBoxAction$ComboBoxButton");
      final CtMethod paint = comboBoxActionButtonClass.getDeclaredMethod("paint");
      paint.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          switch (m.getMethodName()) {
            case "isUnderDefaultMacTheme":
            case "isUnderWin10LookAndFeel":
              m.replace("{ $_ = false; }");
              break;
            case "isUnderDarcula":
              m.replace("{ $_ = true; }");
              break;
            case "drawRoundRect":
              m.replace("{ $2 = $4; $5 = 0; $6 = 0; $_ = $proceed($$); }");
              break;
            case "getGradientPaint":
              final String bgColor = "javax.swing.UIManager.getColor(\"control\")";

              m.replace(String.format("{ $3 = %s; $6 = %s; $_ = $proceed($$); }", bgColor, bgColor));
              break;
            case "setPaint":
              final String color = "javax.swing.UIManager.getColor(\"TextField.selectedSeparatorColor\")";

              m.replace("{ $1 = $1 instanceof com.intellij.ui.JBColor && myMouseInside ? " + color + " : $1; $_ = $proceed($$); }");
              break;
          }
        }
      });

      comboBoxActionButtonClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Hack ToolWindowHeight to not take TabsUtil.getHeight
   */
  private static void hackToolWindowHeader() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(ToolWindowImpl.class));
      final CtClass ctClass = cp.get("com.intellij.openapi.wm.impl.ToolWindowHeader");
      final CtMethod ctMethod = ctClass.getDeclaredMethod("getPreferredSize");
      ctMethod.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getClassName().equals("com.intellij.ui.tabs.TabsUtil") && m.getMethodName().equals("getTabsHeight")) {
            m.replace("{ $_ = com.intellij.util.ui.JBUI.scale(25); }");
          }
        }
      });

      ctClass.toClass();

      final CtClass ctClass1 = cp.get("com.intellij.ui.tabs.impl.JBEditorTabs");
      final CtMethod useBoldLabels = ctClass1.getDeclaredMethod("useBoldLabels");
      useBoldLabels.instrument(new ExprEditor() {
        @Override
        public void edit(final FieldAccess f) throws CannotCompileException {
          if (f.getFieldName().equals("isMac")) {
            f.replace("{ $_ = true; }");
          }
        }

        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("is")) {
            final String code = String.format("com.intellij.ide.util.PropertiesComponent.getInstance().getBoolean(\"%s\", false)",
                BOLD_TABS);
            m.replace(String.format("{ $_ = %s; }", code));
          }
        }
      });

      ctClass1.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private static void hackSpeedSearch() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(ToolWindowImpl.class));
      final CtClass ctClass = cp.get("com.intellij.ui.SpeedSearchBase$SearchPopup");
      final CtConstructor declaredConstructor = ctClass.getDeclaredConstructors()[0];
      declaredConstructor.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("setBackground")) {
            final String bgColor = "com.intellij.util.ui.UIUtil.getToolTipBackground().brighter();";
            m.replace(String.format("{ $1 = %s; $proceed($$); }", bgColor));
          } else if (m.getMethodName().equals("setBorder")) {
            final String borderColor = "null";
            m.replace(String.format("{ $1 = %s; $proceed($$); }", borderColor));
          }
        }
      });

      ctClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Hack TabsUtil,getHeight to override SDK
   */
  private static void hackTabsGetHeight() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(TabInfo.class));
      final CtClass ctClass = cp.get("com.intellij.ui.tabs.impl.TabLabel");
      final CtMethod ctMethod = ctClass.getDeclaredMethod("getPreferredSize");

      ctMethod.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getClassName().equals("com.intellij.ui.tabs.TabsUtil") && m.getMethodName().equals("getTabsHeight")) {
            final String code = String.format("com.intellij.ide.util.PropertiesComponent.getInstance().getInt(\"%s\", 25)", TABS_HEIGHT);
            m.replace(String.format("{ $_ = com.intellij.util.ui.JBUI.scale(%s); }", code));
          }
        }
      });
      ctClass.toClass();

      // Hack JBRunnerTabs
      final CtClass tabLabelClass = cp.get("com.intellij.execution.ui.layout.impl.JBRunnerTabs$MyTabLabel");
      final CtMethod ctMethod2 = tabLabelClass.getDeclaredMethod("getPreferredSize");

      ctMethod2.instrument(new ExprEditor() {
        @Override
        public void edit(final FieldAccess f) throws CannotCompileException {
          if (f.getFieldName().equals("height") && f.isReader()) {
            f.replace("{ $_ = com.intellij.util.ui.JBUI.scale(25); }");
          }
        }
      });
      tabLabelClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private static void hackFlatWelcomeFrame() {
    // Hack method
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(FlatWelcomeFrameProvider.class));
      final CtClass ctClass = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame");
      final CtMethod ctMethod = ctClass.getDeclaredMethod("getProjectsBackground");
      ctMethod.instrument(new ExprEditor() {
        @Override
        public void edit(final NewExpr e) throws CannotCompileException {
          final String bgColor = "javax.swing.UIManager.getColor(\"List.background\")";

          e.replace(String.format("{ $1 = %s; $2 = %s; $_ = $proceed($$); }", bgColor, bgColor));
        }
      });

      ctClass.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
