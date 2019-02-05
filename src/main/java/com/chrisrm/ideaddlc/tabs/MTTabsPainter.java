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

package com.chrisrm.ideaddlc.tabs;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.tabs.impl.DefaultEditorTabsPainter;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.intellij.ui.tabs.impl.ShapeTransform;

import java.awt.*;

public class MTTabsPainter extends DefaultEditorTabsPainter {
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
    final MTConfig config = MTConfig.getInstance();
    final MTThemeable mtTheme = config.getSelectedTheme().getTheme();
    return mtTheme.getBackgroundColor();
  }

  @SuppressWarnings("FeatureEnvy")
  public static Color getContrastColor() {
    final MTConfig config = MTConfig.getInstance();
    final MTThemeable mtTheme = config.getSelectedTheme().getTheme();
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
