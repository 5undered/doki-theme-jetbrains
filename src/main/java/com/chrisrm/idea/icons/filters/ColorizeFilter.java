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

package com.chrisrm.idea.icons.filters;

import java.awt.*;
import java.awt.image.RGBImageFilter;

public class ColorizeFilter extends RGBImageFilter {
  private final Color primaryColor;

  public ColorizeFilter(final Color primaryColor) {
    this.primaryColor = primaryColor;
  }

  @Override
  public int filterRGB(final int x, final int y, final int pARGB) {
    final float[] myBase = Color.RGBtoHSB(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), null);
    // Get color components
    final int r = pARGB >> 16 & 0xFF;
    final int g = pARGB >> 8 & 0xFF;
    final int b = pARGB & 0xFF;
    final float[] hsb = new float[3];
    Color.RGBtoHSB(r, g, b, hsb);
    final int color = Color.HSBtoRGB(myBase[0], myBase[1] * hsb[1], myBase[2] * hsb[2]);
    return (pARGB & 0xFF000000) | color & 0x00FFFFFF;
  }
}
