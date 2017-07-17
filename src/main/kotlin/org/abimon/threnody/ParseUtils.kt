package org.abimon.threnody

import org.parboiled.Action
import org.parboiled.BaseParser
import org.parboiled.Parboiled
import org.parboiled.Rule
import org.parboiled.errors.ErrorUtils
import org.parboiled.parserunners.ReportingParseRunner
import java.util.*

fun pushAction(parser: BaseParser<Any>, value: Any? = null): Action<Any> = Action { parser.push(value ?: parser.match()) }

val tmpStack = HashMap<String, LinkedList<Any>>()
var tmp: Any? = null
var param: Any? = null

fun clearState(): Action<Any> = Action { tmpStack.clear(); tmp = null; param = null; return@Action true }

fun clearTmpStack(cmd: String): Action<Any> = Action { tmpStack.remove(cmd); return@Action true }
fun pushTmpAction(parser: BaseParser<Any>, cmd: String, value: Any? = null): Action<Any> = Action { if (!tmpStack.containsKey(cmd)) tmpStack[cmd] = LinkedList(); tmpStack[cmd]!!.push(value ?: parser.match()); true }
fun pushTmpFromStack(parser: BaseParser<Any>, cmd: String): Action<Any> = Action { if (!tmpStack.containsKey(cmd)) tmpStack[cmd] = LinkedList(); if(!it.valueStack.isEmpty) tmpStack[cmd]!!.push(parser.pop()); true }
fun pushTmpStack(parser: BaseParser<Any>, cmd: String): Action<Any> = Action { parser.push(tmpStack.remove(cmd)?.reversed() ?: LinkedList<Any>()) }
fun operateOnTmpStack(parser: BaseParser<Any>, cmd: String, operate: (Any) -> Unit): Action<Any> = Action { if(tmpStack.containsKey(cmd)) tmpStack[cmd]!!.forEach(operate); true }
fun pushStackToTmp(parser: BaseParser<Any>, cmd: String): Action<Any> = Action { pushTmpAction(parser, cmd, parser.pop() ?: return@Action true).run(it) }

fun pushToStack(parser: BaseParser<Any>): Action<Any> = Action { parser.push(parser.match()) }

fun copyTmp(from: String, to: String): Action<Any> = Action { if (!tmpStack.containsKey(to)) tmpStack[to] = LinkedList(); (tmpStack[from] ?: return@Action true).reversed().forEach { tmpStack[to]!!.push(it) }; tmpStack[from]!!.clear(); true }

fun popTmpFromStack(parser: BaseParser<Any>): Action<Any> = Action { tmp = parser.pop(); return@Action true }
fun pushTmpToStack(parser: BaseParser<Any>): Action<Any> = Action { parser.push(tmp ?: return@Action true) }

fun popParamFromStack(parser: BaseParser<Any>): Action<Any> = Action { param = if(it.valueStack.isEmpty) null else parser.pop(); return@Action true }
fun pushParamToStack(parser: BaseParser<Any>): Action<Any> = Action { parser.push(param ?: return@Action true) }
fun pushParamToTmp(parser: BaseParser<Any>, cmd: String): Action<Any> = Action { pushTmpAction(parser, cmd, param ?: return@Action true).run(it) }

fun clearTmpStack(): Action<Any> = Action { tmp = null; return@Action true }
fun clearParam(): Action<Any> = Action { param = null; return@Action true }

fun BaseParser<*>.Whitespace(): Rule = OneOrMore(AnyOf(charArrayOf(' ', '\t')))
fun BaseParser<*>.Digit(): Rule = AnyOf("1234567890")
fun BaseParser<*>.UserMention(): Rule = Sequence("<@", Optional('!'), OneOrMore(Digit()), ">")
fun BaseParser<Any>.makeCommand(make: BaseParser<Any>.() -> Rule): Rule = make(this)
fun BaseParser<Any>.Parameter(key: String): Rule = Sequence(
        '"',
        OneOrMore(ParameterMatcher),
        pushTmpAction(this, key),
        '"'
)

val voice: MusicalParser = Parboiled.createParser(MusicalParser::class.java)
fun parse(song: String) {
    val runner = ReportingParseRunner<Any>(voice.Song("\n"))
    for (line in song.split('\n')) {
        val result = runner.run(line)
        if (!result.parseErrors.isEmpty())
            println(ErrorUtils.printParseError(result.parseErrors[0]))
        else {
            result.valueStack.reversed().forEach { value ->
                if (value is ArrayList<*>) (value[0] as ICommand).execute(value.subList(1, value.size).toTypedArray())
            }
        }
    }
}