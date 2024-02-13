package io.github.oin.titanpocketkeyboard

/**
 * A virtual modifier that can be used in the following ways:
 *  - hold to activate, release to deactivate
 *  - press to activate until the next printing key press
 *  - double press to lock until the next press of this modifier
 */
class Modifier {
	/**
	 * The maximum time of a double press needed to lock a modifier, in milliseconds.
	 */
	var lockThreshold = 250

	/**
	 * The minimum time of a long press needed to apply the modifier until it is released, in milliseconds.
	 */
	var nextThreshold = 350

	private var held = false
	private var lock = false
	private var next = false
	private var preventNext = false
	private var lastTime: Long = 0

	fun reset() {
		held = false
		lock = false
		next = false
		preventNext = false
		lastTime = 0
	}

	fun get() : Boolean {
		return lock || held || next
	}
	fun isLocked(): Boolean {
		return lock
	}
	fun isHeld(): Boolean {
		return held
	}

	fun onKeyDown() {
		held = true

		val t = System.currentTimeMillis()
		if(t - lastTime < lockThreshold) {
			lock = !lock
			preventNext = true
		} else {
			preventNext = lock || next
			lock = false
		}
		lastTime = t
	}
	fun onKeyUp() {
		val t = System.currentTimeMillis()
		next = !lock && t - lastTime < nextThreshold && !preventNext
		preventNext = false
		held = false
	}

	fun activateForNext() {
		next = true
	}
	fun nextDidConsume() {
		next = false
		preventNext = true
	}
}

/**
 * A virtual modifier that can be used in the following ways:
 *  - hold to activate, release to deactivate
 *  - press to lock until the next press of this modifier
 */
class SimpleModifier {
	var lockThreshold = 350

	private var held = false
	private var lock = false
	private var lastTime: Long = 0

	fun reset() {
		held = false
		lock = false
		lastTime = 0
	}

	fun get() : Boolean {
		return lock || held
	}
	fun isLocked(): Boolean {
		return lock
	}
	fun isHeld(): Boolean {
		return held
	}

	fun onKeyDown() {
		held = true

		lock = !lock
		val t = System.currentTimeMillis()
		lastTime = t
	}
	fun onKeyUp() {
		val t = System.currentTimeMillis()
		if(t - lastTime > lockThreshold) {
			lock = false
		}
		held = false
	}
}
