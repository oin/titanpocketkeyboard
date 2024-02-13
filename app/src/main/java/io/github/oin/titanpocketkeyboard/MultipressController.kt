package io.github.oin.titanpocketkeyboard

import android.view.KeyEvent

/**
 * A special value meaning that nothing should occur, not even sending or substituting a character.
 */
const val MPSUBST_NOTHING = '\uFFFF'
/**
 * A special value meaning that the substitution should be the default character, with the currently active modifiers.
 */
const val MPSUBST_BYPASS = '\u0000'
/**
 * A special value meaning that the substitution should be the default character, without any modifier.
 */
const val MPSUBST_NOMETA = '\uFFF4'
/**
 * A special value meaning that the substitution should be the default character, but as if only the Shift modifier was active.
 */
const val MPSUBST_SHIFT = '\uFFF0'
/**
 * A special value meaning that the substitution should be the default character, but as if the Shift modifier was toggled from its current state.
 */
const val MPSUBST_TOGGLE_SHIFT = '\uFFF2'
/**
 * A special value meaning that the substitution should be the default character, but as if the Alt modifier was active.
 */
const val MPSUBST_ALT = '\uFFF3'
/**
 * A special value meaning that the substitution should be the default character, but as if the Alt modifier was toggled from its current state.
 */
const val MPSUBST_TOGGLE_ALT = '\uFFF5'
/**
 * A special value meaning that the substitution should be a backtick character.
 */
const val MPSUBST_BACKTICK = '\uFFF7'
/**
 * A special value meaning that the substitution should be a circumflex accent character.
 */
const val MPSUBST_CIRCUMFLEX = '\uFFF8'
/**
 * A special value meaning that the substitution should be the string ". ".
 */
const val MPSUBST_STR_DOTSPACE = '\uFFF6'

/**
 * A controller for the Multipress functionality
 */
class MultipressController(val substitutions: Array<HashMap<Int, Array<Char>>>) {
	/**
	 * The maximum time for a subsequent press to be interpreted as a multipress, in milliseconds.
	 */
	var multipressThreshold = 750
	/**
	 * Whether to ignore the first level of multipresses.
	 */
	var ignoreFirstLevel = false
	/**
	 * Whether to ignore `MPSUBST_STR_DOTSPACE`.
	 */
	var ignoreDotSpace = false

	private var last: Int = 0
	private var lastTime: Long = 0
	private var count: Int = 1
	private var longPressCount: Int = 0
	private var lastSubstitution: Char = MPSUBST_BYPASS

	/**
	 * Reset the state to default.
	 */
	fun reset() {
		last = 0
		lastTime = 0
		count = 0
		longPressCount = 0
		lastSubstitution = MPSUBST_BYPASS
	}

	/**
	 * Process the given key event, with the given meta state.
	 * @return A character for the substitution, or `MPSUBST_NOTHING` if no action should occur, or `MPSUBST_STR_*` if a specific string must be used.
	 */
	fun process(e: KeyEvent, metaState: Int): Char {
		val keyCode = e.keyCode
		val t = System.currentTimeMillis()
		if(last == keyCode && t - lastTime < multipressThreshold) {
			lastTime = t

			if(e.repeatCount == 1) {
				++longPressCount
				count = 0
			} else if(e.repeatCount > 1) {
				return MPSUBST_BYPASS
			}

			if(longPressCount >= substitutions.size) {
				longPressCount = 0
			}

			val map = substitutions[longPressCount]
			var substitution: Char
			if(keyCode in map) {
				val subst = map[keyCode]!!

				val index = if(count < subst.size) count else 0
				substitution = subst[index]

				substitution = when(substitution) {
					MPSUBST_BYPASS -> e.getUnicodeChar(metaState).toChar()
					MPSUBST_NOMETA -> e.getUnicodeChar(0).toChar()
					MPSUBST_SHIFT -> e.getUnicodeChar(KeyEvent.META_SHIFT_ON).toChar()
					MPSUBST_ALT -> e.getUnicodeChar(KeyEvent.META_ALT_ON).toChar()
					MPSUBST_TOGGLE_SHIFT -> {
						val mstate = if((metaState and KeyEvent.META_SHIFT_MASK) != 0) {
							0
						} else {
							KeyEvent.META_SHIFT_ON
						}
						e.getUnicodeChar(mstate).toChar()
					}
					MPSUBST_TOGGLE_ALT -> {
						val mstate = if((metaState and KeyEvent.META_ALT_MASK) != 0) {
							0
						} else {
							KeyEvent.META_ALT_ON
						}
						e.getUnicodeChar(mstate).toChar()
					}
					MPSUBST_BACKTICK -> '`'
					MPSUBST_CIRCUMFLEX -> '^'
					in arrayOf('`', '´', '^', '¨', '~') -> KeyEvent.getDeadChar(substitution.code, e.unicodeChar).toChar()
					else -> substitution
				}

				++count
				if(count >= subst.size) {
					count = 0
				}

				if(substitution != MPSUBST_STR_DOTSPACE && ignoreFirstLevel && longPressCount == 0) {
					return MPSUBST_BYPASS
				}
				if(substitution == MPSUBST_STR_DOTSPACE && ignoreDotSpace) {
					return MPSUBST_BYPASS
				}

				if(lastSubstitution == substitution) {
					return MPSUBST_NOTHING
				}

				lastSubstitution = substitution
				return substitution
			}
		} else {
			count = 0
			longPressCount = 0
			lastSubstitution = MPSUBST_BYPASS
		}

		last = keyCode
		lastTime = t
		return MPSUBST_BYPASS
	}
}
