package org.abimon.threnody.genres

import org.abimon.threnody.*
import org.parboiled.BaseParser
import org.parboiled.Rule

object VocalLine: ICommand {

    override fun Syntax(parser: BaseParser<Any>): Rule =
            parser.makeCommand {
                Sequence(
                        clearTmpStack("VocalLine"),
                        "say",
                        pushTmpAction(parser, "VocalLine", this@VocalLine),
                        Whitespace(),
                        Parameter("VocalLine"),
                        pushTmpStack(parser, "VocalLine")
                )
            }

    override fun execute(params: Array<Any>) {
//        if(env.has(permission))
//            env.channel.queueMessageAndWait("${params[0]}")
    }
}