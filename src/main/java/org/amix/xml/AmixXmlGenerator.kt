package org.amix.xml

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

import java.lang.reflect.InvocationTargetException
import org.amix.components.Components
import org.amix.config.Config
import org.amix.converter.AttributeConverter
import org.amix.ext.newLine
import org.amix.ext.newLineBroken
import org.amix.internal.Utils.comment

/*
 * Class that generates XML from the received data.
 */

class AmixXmlGenerator(
  private val onGenerateCode: (String, Config) -> Unit,
  val onError: (String) -> Unit,
  private val useComments: Boolean = false,
  private val useStyle: Boolean = false,
  private val useVerticalRoot: Boolean = false,
) {
  companion object {
    private const val TAG = "AmixXmlGenerator"
  }

  private var orientation = "portrait"
  private var style = "defaultStyle"
  private var isConfigEnable = false

  private var components = Components(
    useComments = useComments,
    useStyle = useStyle,
    useVerticalRoot = useVerticalRoot
  )

  init {
    components.Root()
  }

  public fun closeBlockComponent() {
    if (isConfigEnable) {
      isConfigEnable = false
      return
    }

    if (components.closingTagLayoutList.isNotEmpty()) {
      val tags = components.closingTagLayoutList.last().split(":")

      if (tags.size >= 2) {
        val closingTagGui = tags[0]
        val closingTagXml = tags[1]

        if (useComments)
          components.xmlCodeList.newLineBroken(comment("Closing $closingTagGui Layout"))

        if (closingTagXml.equals("/>")) {
          var previousAttribute: String = components.xmlCodeList.last()
          if (previousAttribute.contains("\n"))
            previousAttribute = previousAttribute.replace("\n", "")

          components.xmlCodeList.removeAt(components.xmlCodeList.size - 1)
          components.xmlCodeList.newLineBroken(previousAttribute + closingTagXml)
        }
        components.indentLevel--
        components.closingTagLayoutList.removeAt(components.closingTagLayoutList.size - 1)
      } else {
        onError("Error: invalid tag format  tag of closing.")
      }
    } else {
      onError("Error: No layout to close in closeBlockComponent.")
    }
  }

  public fun closeBlockLayout() {
    if (components.closingTagLayoutList.isNotEmpty()) {
      val tags = components.closingTagLayoutList.last().split(":")

      if (tags.size >= 2) {
        val closingTagGui = tags[0]
        val closingTagXml = tags[1]

        if (useComments)
          components.xmlCodeList.newLineBroken(comment("Closing $closingTagGui Layout"))

        components.xmlCodeList.newLineBroken("${components.indent}$closingTagXml" + "\n")
        components.indentLevel--
        components.closingTagLayoutList.removeAt(components.closingTagLayoutList.size - 1)
      } else {
        onError("Error: invalid tag format  tag of closing.")
      }
    } else {
      onError("Error: No layout to close in closeBlockLayout.")
    }
  }

  public fun runMethod(methodName: String) {
    try {
      val method = Components::class.java.getDeclaredMethod(methodName)
      method.invoke(components)
    } catch (e: InvocationTargetException) {
      onError("runMethod: " + e.toString())
    }
  }

  public fun runMethodWithParameters(methodName: String, vararg args: Any?) {
    try {
      val parameterTypes = args.map { it?.javaClass }.toTypedArray()
      val method = this::class.java.getDeclaredMethod(methodName, *parameterTypes)
      method.invoke(this, *args)
    } catch (e: NoSuchMethodException) {

      onError(e.toString())
    } catch (e: InvocationTargetException) {
      val originalException = e.cause
      onError(e.toString())
    } catch (e: IllegalAccessException) {
      onError(e.toString())
    }
  }

  public fun addAttribute(methodName: String, key: String, value: String) {
    var containsCloseTag = false
    var containsSingleCloseTag = false
    var attribute = ""

    if (methodName.equals(Config.getName())) {
      when (key) {
        "orientation" -> orientation = value
        "style" -> style = value
      }

      components.config = Config(orientation = orientation, style = style)
      isConfigEnable = true
      return
    }

    if (components.xmlCodeList.get((components.xmlCodeList.size - 1)).contains("/>")) {
      containsCloseTag = true
      attribute = components.xmlCodeList.last().replace("/>", "").replace("\n", "")
      components.xmlCodeList.removeAt(components.xmlCodeList.size - 1)
      components.xmlCodeList.newLineBroken(attribute)
      components.indentLevel++
    }

    if (components.xmlCodeList.get((components.xmlCodeList.size - 1)).contains(">")) {
      containsSingleCloseTag = true
      components.xmlCodeList.removeAt(components.xmlCodeList.size - 1)
    }

    components.indentLevel++
    val attributeConverted = AttributeConverter.convert(key)
    if (!key.equals("id")) {
      components.xmlCodeList.newLineBroken(
        components.indent + attributeConverted + "=" + "\"$value\""
      )
    } else {
      components.xmlCodeList.newLineBroken(
        components.indent + attributeConverted + "=" + "\"@+id/$value\""
      )
    }
    components.indentLevel--

    if (containsCloseTag) {
      components.closingTagLayoutList.newLine("$methodName:/>")
      closeBlockComponent()
    }

    if (containsSingleCloseTag) {
      components.xmlCodeList.newLineBroken(">")
    }
  }

  public fun newLog(log: String) {
    if (useComments) components.xmlCodeList.newLine(log)
  }

  public fun buildXML(): String {
    var codes: StringBuilder = StringBuilder()
    components.xmlCodeList.forEach { codes.append(it) }
    return codes.toString()
  }

  public fun finish() {
    components.indentLevel--
    components.indentLevel--
    if (useComments) components.xmlCodeList.newLineBroken(comment("Closing Root Layout"))
    components.xmlCodeList.newLineBroken("</LinearLayout>")
    if (useComments) components.xmlCodeList.newLine("\n" + comment("End."))
    onGenerateCode(buildXML(), components.config)
  }
}
