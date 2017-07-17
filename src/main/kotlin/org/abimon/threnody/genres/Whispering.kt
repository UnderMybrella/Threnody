package org.abimon.threnody.genres

import org.abimon.threnody.*
import org.parboiled.BaseParser
import org.parboiled.Rule

object Whispering : ICommand {
    override fun Syntax(parser: BaseParser<Any>): Rule =
            parser.makeCommand {
                Sequence(
                        clearTmpStack("Whispering"),
                        "whisper",
                        pushTmpAction(parser, "Whispering", this@Whispering),
                        Whitespace(),
                        Parameter("Whispering"),
                        Optional(
                                Whitespace(),
                                "to",
                                Whitespace(),
                                Parameter("Whispering")
                        ),
                        pushTmpStack(parser, "Whispering")
                )
            }

    override fun execute(params: Array<Any>) {
//        if (env.has(permission)) {
//            if (params.size == 1)
//                env.author.orCreatePMChannel.queueMessageAndWait("${params[0]}")
//            else
//                (env.guild.findUser("${params[1]}") ?: env.author).orCreatePMChannel.queueMessageAndWait("${params[0]}")
//        }
    }
}