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
 *
 */

package com.chrisrm.ideaddlc;

import com.intellij.ide.plugins.PluginManagerConfigurable;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrameProvider;
import com.intellij.ui.CaptionPanel;
import com.intellij.ui.ScrollingUtil;
import com.intellij.util.ui.JBSwingUtilities;
import io.acari.DDLC.LegacySupportUtility;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.jetbrains.annotations.NonNls;

@SuppressWarnings({
    "CallToSuspiciousStringMethod",
    "HardCodedStringLiteral",
    "DuplicateStringLiteralInspection"})
public final class MTHackComponent implements BaseComponent {

  static {
    hackTitleLabel();
    hackSpeedSearch();
    hackSearchTextField();
    hackPluginManagerNew();
    hackIntelliJFailures();
    hackNewScreenHardcodedColor();
  }

  /**
   * Fix fatal error introduced by intellij
   */
  private static void hackIntelliJFailures() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(JBSwingUtilities.class));
      final CtClass ctClass2 = cp.get("com.intellij.util.IJSwingUtilities");
      final CtMethod method = ctClass2.getDeclaredMethod("updateComponentTreeUI");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if ("decorateWindowHeader".equals(m.getMethodName())) {
            m.replace("{ }");
          }
        }
      });
      ctClass2.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void hackSearchTextField() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(ScrollingUtil.class));
      final CtClass ctClass2 = cp.get("com.intellij.ui.SearchTextField");
      final CtMethod method = ctClass2.getDeclaredMethod("customSetupUIAndTextField");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final FieldAccess f) throws CannotCompileException {
          if ("isMac".equals(f.getFieldName())) {
            f.replace("{ $_ = false; }");
          }
        }
      });
      ctClass2.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void hackNewScreenHardcodedColor() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(FlatWelcomeFrameProvider.class));
      final CtClass ctClass2 = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame");
      final CtMethod method = ctClass2.getDeclaredMethod("getActionLinkSelectionColor");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final NewExpr e) throws CannotCompileException {
          final String bgColor = "javax.swing.UIManager.getColor(\"MenuItem.selectionBackground\")";
          e.replace(String.format("{ $_ = %s; $proceed($$); }", bgColor));
        }
      });
      ctClass2.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("OverlyComplexAnonymousInnerClass")
  private static void hackPluginManagerNew() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(PluginManagerConfigurable.class));

      // 1: Hack Plugin Groups color
      final CtClass ctClass = cp.get("com.intellij.ide.plugins.newui.PluginsGroupComponent");

      final CtMethod addGroup = ctClass.getDeclaredMethod("addGroup", new CtClass[]{
          cp.get("com.intellij.ide.plugins.newui.PluginsGroup"),
          cp.get("java.util.List"),
          cp.get("int")
      });
      addGroup.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if ("setForeground".equals(m.getMethodName())) {
            final String fgColor = "javax.swing.UIManager.getColor(\"List.foreground\")";

            m.replace(String.format("{ $1 = %s; $_ = $proceed($$); }", fgColor));
          }
        }

        @Override
        public void edit(final NewExpr e) throws CannotCompileException {
          if (e.getClassName().contains("OpaquePanel")) {
            final String bgColor = "javax.swing.UIManager.getColor(\"List.background\")";

            e.replace(String.format("{ $2 = %s; $_ = $proceed($$); }", bgColor));
          }
        }
      });
      ctClass.toClass();

      // 2. Hack plugin tags color
      final CtClass ctClass2 = cp.get("com.intellij.ide.plugins.newui.TagComponent");
      final CtMethod method = ctClass2.getDeclaredMethod("paintComponent");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if ("setColor".equals(m.getMethodName())) {
            final String bgColor = "javax.swing.UIManager.getColor(\"Button.mt.background\")";

            m.replace(String.format("{ $1 = %s; $proceed($$); }", bgColor));
          }
        }
      });

      ctClass2.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * "I don't know who you are.
   * I don't know what you want.
   * If you are looking for classes that are closed to modifications I can tell you I don't have have access right now,
   * but what I do have are a very particular set of skills.
   * Skills I have acquired over a very long career.
   * Skills that make me a nightmare for people like you.
   * If you let me configure your class right now that'll be the end of it.
   * I will not look for you, I will not pursue you, but if you don't,
   * I will look for you, I will find you and I will change your classes.
   */
  private static void hackTitleLabel() {
    // Hack method
    try {
      @NonNls final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(CaptionPanel.class));
      final CtClass ctClass = cp.get("com.intellij.ui.TitlePanel");
      final CtConstructor declaredConstructor = ctClass.getDeclaredConstructor(new CtClass[]{
          cp.get("javax.swing.Icon"),
          cp.get("javax.swing.Icon")});

      LegacySupportUtility.INSTANCE.orRunLegacy(
          "com.intellij.ide.ui.lafs.darcula.ui.DarculaOptionButtonUI",
          () -> declaredConstructor.instrument(new ExprEditor() {
            @Override
            public void edit(final MethodCall m) throws CannotCompileException {
              final String s = m.getMethodName();
              if ("setHorizontalAlignment".equals(s)) {
                // Set title at the left
                m.replace("{ $1 = javax.swing.SwingConstants.LEFT; $_ = $proceed($$); }");
              } else if ("setBorder".equals(s)) {
                // Bigger heading
                m.replace("{ $_ = $proceed($$); myLabel.setFont(myLabel.getFont().deriveFont(1, com.intellij.util.ui.JBUI.scale(16.0f))); }");
              }
            }
          }),
          () -> declaredConstructor.instrument(new ExprEditor() {
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
          })
      );

      final CtMethod getPreferredSize = ctClass.getDeclaredMethod("getPreferredSize");
      getPreferredSize.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if ("headerHeight".equals(m.getMethodName())) {
            // Set title at the left
            m.replace("{ $_ = 40; }");
          }
        }
      });

      ctClass.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fix Speed Search (typing into dialogs) color
   */
  private static void hackSpeedSearch() {
    // Hack method
    try {
      @NonNls final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(ToolWindowImpl.class));
      final CtClass ctClass = cp.get("com.intellij.ui.SpeedSearchBase$SearchPopup");
      final CtConstructor declaredConstructor = ctClass.getDeclaredConstructors()[0];
      declaredConstructor.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if ("setBackground".equals(m.getMethodName())) {
            final String bgColor = "com.intellij.util.ui.UIUtil.getToolTipBackground().brighter();";
            m.replace(String.format("{ $1 = %s; $proceed($$); }", bgColor));
          } else if ("setBorder".equals(m.getMethodName())) {
            final String borderColor = "null";
            m.replace(String.format("{ $1 = %s; $proceed($$); }", borderColor));
          }
        }
      });

      ctClass.toClass();
    } catch (final CannotCompileException | NotFoundException e) {
      e.printStackTrace();
    }
  }
}
