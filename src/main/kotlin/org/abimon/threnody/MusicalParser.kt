package org.abimon.threnody

import org.abimon.threnody.genres.Accompaniment
import org.abimon.threnody.genres.VocalLine
import org.abimon.threnody.genres.Whispering
import org.parboiled.BaseParser
import org.parboiled.Rule

open class MusicalParser: BaseParser<Any>() {
    open fun Verse(): Rule = FirstOf(VocalLine.Syntax(this), Accompaniment.Syntax(this), Whispering.Syntax(this))
    open fun Song(terminator: String): Rule = Sequence(ZeroOrMore(clearTmpStack(), Verse(), popTmpFromStack(this), terminator, pushTmpToStack(this)), Verse())
}