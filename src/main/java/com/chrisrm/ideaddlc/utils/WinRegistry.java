/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
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

package com.chrisrm.ideaddlc.utils;

import com.sun.jna.platform.win32.Advapi32Util;

import java.awt.*;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * Modifies the Windows Registry for the Accent Color
 */
public enum WinRegistry {
  WIN_REGISTRY;

  private static final String DWM_PATH = "Software\\Microsoft\\Windows\\DWM";
  private static final String KEY = "AccentColor";

  private static void writeStringValue(final int value) {
    Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, DWM_PATH, KEY, value);
  }

  public static void writeTitleColor(final Color backgroundColor) {
    writeStringValue(MTUiUtils.colorToDword(backgroundColor));
  }

}
