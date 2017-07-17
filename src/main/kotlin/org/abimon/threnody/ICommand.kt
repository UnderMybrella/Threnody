package org.abimon.threnody

import org.parboiled.BaseParser
import org.parboiled.Rule

interface ICommand {
    fun Syntax(parser: BaseParser<Any>): Rule
    fun execute(params: Array<Any>)
}