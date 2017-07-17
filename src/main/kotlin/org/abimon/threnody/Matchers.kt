package org.abimon.threnody

import org.parboiled.MatcherContext
import org.parboiled.matchers.AnyMatcher

object LineMatcher: AnyMatcher() {
    override fun match(context: MatcherContext<*>): Boolean {
        when(context.currentChar) {
            '\n' -> return false
            else -> return super.match(context)
        }
    }
}

object ParameterMatcher: AnyMatcher() {
    override fun match(context: MatcherContext<*>): Boolean {
        when(context.currentChar) {
            '"' -> return false
            else -> return super.match(context)
        }
    }
}