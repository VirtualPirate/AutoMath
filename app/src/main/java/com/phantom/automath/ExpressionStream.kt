package com.phantom.automath

import java.util.*
import kotlin.collections.HashSet

class Subtitute(var name: Char, var value: Double) {
	override fun toString(): String {
		return "(name =$name, value = $value)"
	}

	override fun equals(other: Any?): Boolean {
		val this_ = this
		return this_.name == name && this_.value == value
	}

	fun equals(ch: Char): Boolean {
		return this.name == ch
	}
}

class ExpressionStream(var expression: String) {
	var subtitute_set: HashSet<Subtitute>
	lateinit var subtitute_list: Array<Subtitute>
	fun addSubtitute(name: Char, value: Double) {
		subtitute_set.add(Subtitute(name, value))
	}

	fun simplify_output(): String {
		val object_array = subtitute_set.toArray()
		subtitute_list =
			Arrays.copyOf(object_array, object_array.size, Array<Subtitute>::class.java)
		// subtitute_list = (Subtitute[])subtitute_set.toArray();
		return invoke_simplify_stream()
	}

	external fun invoke_simplify_stream(): String

	init {
		subtitute_set = HashSet()
	}

	companion object{
		external fun isValidExpression(expression: String): Boolean
	}

}