/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
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

package com.chrisrm.ideaddlc.tabs;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.MTThemeManager;
import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.chrisrm.ideaddlc.utils.MTAccents;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.impl.DefaultEditorTabsPainter;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.intellij.ui.tabs.impl.JBEditorTabsPainter;
import com.intellij.ui.tabs.impl.ShapeTransform;
import com.intellij.util.Consumer;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.UIUtil;
import io.acari.DDLC.DDLCConfig;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Optional;


/**
 * Patch the Tabs Component to get the Material Design style
 *
 * @author Dennis.Ushakov
 */
public final class MTTabsPainterPatcherComponent implements BaseComponent {

  private final MTConfig config;
  private final Field pathField;
  private final Field fillPathField;
  private final Field labelPathField;

  public MTTabsPainterPatcherComponent() throws ClassNotFoundException, NoSuchFieldException {
    config = MTConfig.getInstance();

    // Get the shapeinfo class because it is protected
    final Class<?> clazz = Class.forName("com.intellij.ui.tabs.impl.JBTabsImpl$ShapeInfo");
    // Retrieve private fields of ShapeInfo class
    pathField = clazz.getField("path");
    fillPathField = clazz.getField("fillPath");
    labelPathField = clazz.getField("labelPath");
  }

  @Override
  public void disposeComponent() {

  }

  @NotNull
  @Override
  public String getComponentName() {
    return "DDLCMTTabsPainterPatcherComponent";
  }

  private boolean ddlcActive=false;
  private boolean initalized=false;
  private FileEditor fileEditor = null;

  @Override
  public void initComponent() {
    final MessageBus bus = ApplicationManagerEx.getApplicationEx().getMessageBus();

    final MessageBusConnection connect = bus.connect();
    MTThemeManager.addMaterialThemeActivatedListener(areOtherThemesActive-> {
      if(!(areOtherThemesActive || initalized)){
        this.ddlcActive = true;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> initializeTabs(fileEditor1, this::patchPainter));
      } else if(initalized && areOtherThemesActive) {
        ddlcActive = false;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> {
              initializeTabs(fileEditor1, a ->
                  replacePainters(new DefaultEditorTabsPainter(a), new DefaultEditorTabsPainter(a), a));
              initalized = false;
            });
      }
    });
    connect.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
      @Override
      public void selectionChanged(@NotNull final FileEditorManagerEvent event) {
        final FileEditor editor = event.getNewEditor();
        if(ddlcActive){
          initializeTabs(editor, a->patchPainter(a));
        }
        fileEditor = editor;
      }
    });
  }

  private void initializeTabs(FileEditor editor, Consumer<JBEditorTabs> painterWorker) {
    if (editor != null) {
      this.initalized = true;
      Component component = editor.getComponent();
      while (component != null) {
        if (component instanceof JBEditorTabs) {
          painterWorker.consume((JBEditorTabs) component);
          return;
        }
        component = component.getParent();
      }
    }
  }

  /**
   * Patch tabsPainter
   */
  private void patchPainter(final JBEditorTabs component) {
    final JBEditorTabsPainter painter = ReflectionUtil.getField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDarkPainter");
    final Color accentColor = ObjectUtils.notNull(ColorUtil.fromHex(config.getAccentColor()), new ColorUIResource(0x80cbc4));

    if (painter instanceof MTTabsPainter) {
      return;
    }

    final MTTabsPainter tabsPainter = new MTTabsPainter(component);
    final JBEditorTabsPainter proxy = (MTTabsPainter) Enhancer.create(MTTabsPainter.class, (MethodInterceptor) (o, method, objects,
                                                                                                                methodProxy) -> {
      final Object result = method.invoke(tabsPainter, objects);

      // Custom props
      final boolean isColorEnabled = config.isHighlightColorEnabled();
      final Color borderColor = isColorEnabled ? config.getHighlightColor() : accentColor;
      final int borderThickness = config.getHighlightThickness();

      if ("paintSelectionAndBorder".equals(method.getName())) {
        paintSelectionAndBorder(objects, borderColor, borderThickness, tabsPainter);
      }

      return result;
    });

    this.replacePainters(proxy, proxy, component);
  }

  private void replacePainters(JBEditorTabsPainter defaultPainter, JBEditorTabsPainter darkPainter, JBEditorTabs component){
    ReflectionUtil.setField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDefaultPainter", defaultPainter);
    ReflectionUtil.setField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDarkPainter", darkPainter);
  }

  /**
   * Paint tab selected and highlight border
   *
   * @param objects
   * @param borderColor
   * @param borderThickness
   * @param tabsPainter
   */
  private void paintSelectionAndBorder(final Object[] objects,
                                       final Color borderColor,
                                       final int borderThickness,
                                       final MTTabsPainter tabsPainter)
      throws IllegalAccessException {

    // Retrieve arguments
    final Graphics2D g2d = (Graphics2D) objects[0];
    final Rectangle rect = (Rectangle) objects[1];
    final Object selectedShape = objects[2];
    final Insets insets = (Insets) objects[3];
    final Color tabColor = (Color) objects[4];

    final ShapeTransform path = (ShapeTransform) pathField.get(selectedShape);
    final ShapeTransform fillPath = (ShapeTransform) fillPathField.get(selectedShape);
    final ShapeTransform labelPath = (ShapeTransform) labelPathField.get(selectedShape);

    // Other properties needed for drawing
    final Insets i = path.transformInsets(insets);
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectHeight = rect.height;

    // The tabs component
    final JBEditorTabs tabsComponent = tabsPainter.getTabsComponent();

    // Position of tabs
    final JBTabsPosition position = tabsComponent.getTabsPosition();

    // color me
    tabsPainter.fillSelectionAndBorder(g2d, fillPath, tabColor, rectX, rectY, rectHeight);

    // shadow
    if (MTConfig.getInstance().isTabsShadow()) {
      if (position == JBTabsPosition.bottom) {
        drawTopShadow(tabsPainter, g2d, path, labelPath, rect);
      } else if (position == JBTabsPosition.top) {
        drawBottomShadow(tabsPainter, g2d, path, labelPath, rect);
      } else if (position == JBTabsPosition.left) {
        drawRightShadow(tabsPainter, g2d, path, labelPath, rect);
      } else if (position == JBTabsPosition.right) {
        drawLeftShadow(tabsPainter, g2d, path, labelPath, rect);
      }
    }

    // Finally paint the active tab highlighter
    g2d.setColor(borderColor);

    if (position == JBTabsPosition.bottom) {
      // Paint on top
      paintOnTop(borderThickness, g2d, rect);
    } else if (position == JBTabsPosition.top) {
      // Paint on bottom
      paintOnBottom(borderThickness, g2d, rect);
    } else if (position == JBTabsPosition.left) {
      paintOnRight(borderThickness, g2d, rect);
    } else if (position == JBTabsPosition.right) {
      paintOnLeft(borderThickness, g2d, rect);
    }
  }

  private static void drawBottomShadow(final MTTabsPainter tabsPainter,
                                final Graphics2D g2d,
                                final ShapeTransform path,
                                final ShapeTransform labelPath,
                                final Rectangle rect) {
    final int h = labelPath.getMaxY();
    final int w = path.getMaxX();

    final Color bg = MTTabsPainter.getContrastColor().darker();
    g2d.setColor(bg);
    g2d.drawLine(0, h + 1, w, h + 1);

    // draw the drop-shadow
    final Color mid = ColorUtil.toAlpha(bg, 75);
    g2d.setColor(mid);
    g2d.drawLine(0, h + 2, w, h + 2);

    // draw the drop-shadow
    final Color mid2 = ColorUtil.toAlpha(bg, 50);
    g2d.setColor(mid2);
    g2d.drawLine(0, h + 3, w, h + 3);
    g2d.drawLine(0, h + 4, w, h + 4);


    final Color edge = ColorUtil.toAlpha(bg, 25);
    g2d.setColor(edge);
    g2d.drawLine(0, h + 5, w, h + 5);
  }

  private static void drawTopShadow(final MTTabsPainter tabsPainter,
                             final Graphics2D g2d,
                             final ShapeTransform path,
                             final ShapeTransform labelPath, final Rectangle rect) {
    final int w = path.getMaxX();
    final int h = rect.y;

    final Color bg = MTTabsPainter.getContrastColor().darker();
    g2d.setColor(bg);
    g2d.drawLine(0, h - 1, w, h - 1);

    // draw the drop-shadow
    final Color mid = ColorUtil.toAlpha(bg, 75);
    g2d.setColor(mid);
    g2d.drawLine(0, h - 2, w, h - 2);

    // draw the drop-shadow
    final Color mid2 = ColorUtil.toAlpha(bg, 50);
    g2d.setColor(mid2);
    g2d.drawLine(0, h - 3, w, h - 3);
    g2d.drawLine(0, h - 4, w, h - 4);


    final Color edge = ColorUtil.toAlpha(bg, 25);
    g2d.setColor(edge);
    g2d.drawLine(0, h - 5, w, h - 5);
  }

  private static void drawRightShadow(final MTTabsPainter tabsPainter,
                               final Graphics2D g2d,
                               final ShapeTransform path,
                               final ShapeTransform labelPath,
                               final Rectangle rect) {
    final int h = path.getMaxY();
    final int w = rect.width;

    final Color bg = MTTabsPainter.getContrastColor().darker();
    g2d.setColor(bg);
    g2d.drawLine(w + 1, 0, w + 1, h);

    // draw the drop-shadow
    final Color mid = ColorUtil.toAlpha(bg, 75);
    g2d.setColor(mid);
    g2d.drawLine(w + 2, 0, w + 2, h);

    // draw the drop-shadow
    final Color mid2 = ColorUtil.toAlpha(bg, 50);
    g2d.setColor(mid2);
    g2d.drawLine(w + 3, 0, w + 3, h);
    g2d.drawLine(w + 4, 0, w + 4, h);


    final Color edge = ColorUtil.toAlpha(bg, 25);
    g2d.setColor(edge);
    g2d.drawLine(w + 5, 0, w + 5, h);
  }

  private static void drawLeftShadow(final MTTabsPainter tabsPainter,
                              final Graphics2D g2d,
                              final ShapeTransform path,
                              final ShapeTransform labelPath,
                              final Rectangle rect) {
    final int h = labelPath.getMaxY();
    final int w = rect.x;


    final Color bg = MTTabsPainter.getContrastColor().darker();
    g2d.setColor(bg);
    g2d.drawLine(w - 1, 0, w - 1, h);

    // draw the drop-shadow
    final Color mid = ColorUtil.toAlpha(bg, 75);
    g2d.setColor(mid);
    g2d.drawLine(w - 2, 0, w - 2, h);

    // draw the drop-shadow
    final Color mid2 = ColorUtil.toAlpha(bg, 50);
    g2d.setColor(mid2);
    g2d.drawLine(w - 3, 0, w - 3, h);
    g2d.drawLine(w - 4, 0, w - 4, h);


    final Color edge = ColorUtil.toAlpha(bg, 25);
    g2d.setColor(edge);
    g2d.drawLine(w - 5, 0, w - 5, h);
  }

  private static void paintOnLeft(final int borderThickness, final Graphics2D g2d, final Rectangle rect) {
    g2d.fillRect(rect.x + rect.width - borderThickness + 1, rect.y, borderThickness, rect.height);
  }

  private static void paintOnRight(final int borderThickness, final Graphics2D g2d, final Rectangle rect) {
    g2d.fillRect(rect.x, rect.y, borderThickness, rect.height);
  }

  private static void paintOnBottom(final int borderThickness, final Graphics2D g2d, final Rectangle rect) {
    g2d.fillRect(rect.x, rect.y + rect.height - borderThickness + 1, rect.width, borderThickness);
    g2d.setColor(UIUtil.CONTRAST_BORDER_COLOR);
    g2d.drawLine(Math.max(0, rect.x - 1), rect.y, rect.x + rect.width, rect.y);
  }

  private static void paintOnTop(final int borderThickness, final Graphics2D g2d, final Rectangle rect) {
    g2d.fillRect(rect.x, rect.y - 1, rect.width, borderThickness);
  }

  public static class MTTabsPainter extends DefaultEditorTabsPainter {
    public MTTabsPainter() {
      super(null);
    }

    MTTabsPainter(final JBEditorTabs tabs) {
      super(tabs);
    }

    final void fillSelectionAndBorder(final Graphics2D g,
                                             final ShapeTransform selectedShape,
                                             final Color tabColor,
                                             final int x,
                                             final int y,
                                             final int height) {
      g.setColor(tabColor != null ? tabColor : getDefaultTabColor());
      g.fill(selectedShape.getShape());
    }

    @Override
    public final Color getBackgroundColor() {
      final DDLCConfig config = DDLCConfig.getInstance();
      final MTThemeable mtTheme = config.getSelectedTheme().getTheme();
      return mtTheme.getBackgroundColor();
    }

    public static final Color getContrastColor() {
      final MTConfig config = MTConfig.getInstance();
      final MTThemeable mtTheme = DDLCConfig.getInstance().getSelectedTheme().getTheme();
      return config.isContrastMode() ? mtTheme.getContrastColor() : mtTheme.getBackgroundColor();
    }

    final JBEditorTabs getTabsComponent() {
      return myTabs;
    }

    @Override
    protected final Color getDefaultTabColor() {
      if (myDefaultTabColor != null) {
        return myDefaultTabColor;
      }

      return getBackgroundColor();
    }

    @Override
    protected final Color getInactiveMaskColor() {
      final float opacity = (float) (MTConfig.getInstance().getTabOpacity() / 100.0);
      return ColorUtil.withAlpha(getContrastColor(), opacity);
    }
  }
}

