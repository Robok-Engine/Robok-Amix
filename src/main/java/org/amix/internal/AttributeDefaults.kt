package org.amix.internal

/*
 *  This file is part of Amix © 2024.
 *
 *  Amix is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Amix is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Amix.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * A Basic values used on XMLs Layouts.
 * @author Aquiles Trindade (trindadedev).
 */

internal object AttributeDefaults {
  private const val XMLNS_ANDROID = "\txmlns:android=\"http://schemas.android.com/apk/res/android\""
  private const val XMLNS_APP = "\txmlns:app=\"http://schemas.android.com/apk/res-auto\""
  private const val XMLNS_TOOLS = "\txmlns:tools=\"http://schemas.android.com/tools\""

  const val LAYOUT_HEIGHT = "\tandroid:layout_height=\"wrap_content\""
  const val LAYOUT_WIDTH = "\tandroid:layout_width=\"match_parent\""

  fun XMLNS(indent: String): String =
    "${indent}${XMLNS_ANDROID}\n${indent}${XMLNS_APP}\n${indent}${XMLNS_TOOLS}"

  const val DEFAULT_KEY = "id"
  const val DEFAULT_VALUE = "@+id/robok_no_id_view"
}
