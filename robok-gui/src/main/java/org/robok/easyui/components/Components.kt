package org.robok.easyui.components

import org.robok.easyui.config.Config
import org.robok.easyui.internal.DefaultValues
import org.robok.easyui.internal.Utils.comment
import org.robok.easyui.internal.newLine
import org.robok.easyui.internal.newLineBroken

class Components(
    private val codeComments: Boolean = false,
    private val verticalRoot: Boolean = false,
) {

    var indentLevel = 0
    val indent: String
        get() = "\t".repeat(indentLevel)

    val xmlCodeList: MutableList<String> = mutableListOf()
    val closingTagLayoutList: MutableList<String> = mutableListOf()
    var config: Config = Config(orientation = "portrait", style = "defaultStyle")

    fun rootView() {
        if (codeComments) xmlCodeList.newLineBroken(comment("Opening Root Layout"))
        xmlCodeList.newLineBroken("""<?xml version="1.0" encoding="utf-8"?>""")
        xmlCodeList.newLineBroken("<LinearLayout")
        indentLevel++
        xmlCodeList.newLineBroken(DefaultValues.XMLNS(indent))
        xmlCodeList.newLineBroken("${indent}${DefaultValues.LAYOUT_HEIGHT}")
        xmlCodeList.newLineBroken("${indent}${DefaultValues.LAYOUT_WIDTH}")
        if (verticalRoot) xmlCodeList.newLineBroken("${indent}\tandroid:orientation=\"vertical\"")
        xmlCodeList.newLine("${indent}\tandroid:id=\"@+id/root_view\"")
        xmlCodeList.newLineBroken(">")
        indentLevel++
    }

    fun Column() {
        if (codeComments) xmlCodeList.newLineBroken(comment("Opening Column Layout"))
        xmlCodeList.newLineBroken("${indent}<LinearLayout")
        indentLevel++
        xmlCodeList.newLineBroken("${indent}\tandroid:orientation=\"vertical\"")
        xmlCodeList.newLineBroken(">")
        indentLevel++
        closingTagLayoutList.newLine("Column:</LinearLayout>")
    }

    fun Row() {
        if (codeComments) xmlCodeList.newLineBroken(comment("Opening Row Layout"))
        xmlCodeList.newLineBroken("${indent}<LinearLayout")
        indentLevel++
        xmlCodeList.newLineBroken("${indent}\tandroid:orientation=\"horizontal\"")
        xmlCodeList.newLineBroken(">")
        indentLevel++
        closingTagLayoutList.newLine("Row:</LinearLayout>")
    }

    fun Text() {
        if (codeComments) xmlCodeList.newLineBroken(comment("Text Component"))
        xmlCodeList.newLineBroken("${indent}<TextView")
        indentLevel++
        xmlCodeList.newLineBroken(
            "${indent}android:background=\"@drawable/" +
                config.convertStyleToFileName(config.style + "Text") +
                "\""
        )
        closingTagLayoutList.newLine("Text:/>")
    }

    fun Button() {
        if (codeComments) xmlCodeList.newLineBroken(comment("Button Component"))
        xmlCodeList.newLineBroken("${indent}<Button")
        indentLevel++
        xmlCodeList.newLineBroken(
            "${indent}android:background=\"@drawable/" +
                config.convertStyleToFileName(config.style + "Button") +
                "\""
        )
        closingTagLayoutList.newLine("Button:/>")
    }

    /*
     * for test
     */
    fun MaterialButton() {
        if (codeComments) xmlCodeList.newLineBroken(comment("MaterialButton Component"))
        xmlCodeList.newLineBroken("${indent}<com.google.android.material.button.MaterialButton")
        indentLevel++
        closingTagLayoutList.newLine("com.google.android.material.button.MaterialButton:/>")
    }

    fun config() {
        /* its fake method*/
    }
}
