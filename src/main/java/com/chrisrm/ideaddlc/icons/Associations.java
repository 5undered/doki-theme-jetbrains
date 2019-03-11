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

package com.chrisrm.ideaddlc.icons;

import com.chrisrm.ideaddlc.utils.StaticPatcher;
import com.google.common.annotations.VisibleForTesting;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of association parsed from the XML
 */
public final class Associations implements Serializable {

  private List<Association> associations = new ArrayList<>();

  public List<Association> getAssociations() {
    return associations;
  }

  public void setAssociations(final List<Association> associations) {
    this.associations = associations;
  }

  /**
   * Find the Association for the given FileInfo
   *
   * @param file
   */
  @VisibleForTesting
  @Nullable
  public Association findAssociationForFile(final FileInfo file) {
    Association result = null;
    for (final Association association : associations) {
      if (association.matches(file)) {
        result = association;
        break;
      }
    }

    if (result != null && result.getName().equals("Images")) {
      try {
        // Icon viewer plugin
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId("ch.dasoft.iconviewer"));
        final IdeaPluginDescriptor plugin2 = PluginManager.getPlugin(PluginId.getId("ch.dasoft.iconviewer.fork"));

        if (plugin != null || plugin2 != null) {
          return null;
        }
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

    return result;
  }
}
