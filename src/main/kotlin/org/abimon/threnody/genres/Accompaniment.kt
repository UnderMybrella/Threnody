package org.abimon.threnody.genres

import org.abimon.threnody.*
import org.parboiled.BaseParser
import org.parboiled.Rule

object Accompaniment: ICommand {

    override fun Syntax(parser: BaseParser<Any>): Rule =
            parser.makeCommand {
                Sequence(
                        clearTmpStack("Accompaniment"),
                        "accompany",
                        pushTmpAction(parser, "Accompaniment", this@Accompaniment),
                        Whitespace(),
                        Parameter("Accompaniment"),
                        Whitespace(),
                        "with role",
                        Whitespace(),
                        Parameter("Accompaniment"),
                        pushTmpStack(parser, "Accompaniment")
                )
            }

    override fun execute(params: Array<Any>) {
//        if(env.has(permission))
//            env.channel.queueMessageAndWait("Accompanying ${env.guild.findUser("${params[0]}")?.display ?: "No one"} with ${env.guild.findRole("${params[1]}")?.name ?: "No Role"}")
    }
}