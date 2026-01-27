package io.github.oin.titanpocketkeyboard

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.InputDevice
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.preference.PreferenceManager
import java.util.Locale
import android.inputmethodservice.InputMethodService as AndroidInputMethodService

/**
 * @return true if it is suitable to provide suggestions or text transforms in the given editor.
 */
fun canUseSuggestions(editorInfo: EditorInfo): Boolean {
	if(editorInfo.inputType == InputType.TYPE_NULL) {
		return false
	}

	return when(editorInfo.inputType and InputType.TYPE_MASK_VARIATION) {
		InputType.TYPE_TEXT_VARIATION_PASSWORD -> false
		InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> false
		InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD -> false
		InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> false
		InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS -> false
		InputType.TYPE_TEXT_VARIATION_URI -> false
		else -> true
	}
}

/**
 * @return A KeyEvent made from the given one, but with the given key code, meta state, action and source.
 */
fun makeKeyEvent(original: KeyEvent, code: Int, metaState: Int, action: Int, source: Int): KeyEvent {
	return KeyEvent(original.downTime, original.eventTime, action, code, original.repeatCount, metaState, KeyCharacterMap.VIRTUAL_KEYBOARD, code, 0, source)
}

val templates = hashMapOf(
	"fr" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('`', '^', 'æ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', '`', '^', '¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('^', '¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('^', 'œ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('`', '^', '¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_Y to arrayOf('¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_C to arrayOf('ç', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"fr-ext" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('`', '^', '´', '¨', 'æ', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', '`', '^', '¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('^', '´', '¨', '`', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('^', '´', 'œ', '¨', '~', '`', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('`', '^', '´', '¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_Y to arrayOf('¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_C to arrayOf('ç', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"cz" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_C to arrayOf('ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_D to arrayOf('´', 'ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', 'ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_N to arrayOf('ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_R to arrayOf('ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_S to arrayOf('ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_T to arrayOf('´', 'ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('´', '°', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_Y to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_Z to arrayOf('ˇ', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"es" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"de" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('¨', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_S to arrayOf('ß', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"pt" to hashMapOf(
		KeyEvent.KEYCODE_A to arrayOf('´', '^', '`', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', '^', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('´', '^', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('´', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_C to arrayOf('ç', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"order1" to hashMapOf( // áàâäã
		KeyEvent.KEYCODE_A to arrayOf('´', '`', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('´', '`', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('´', '`', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('´', '`', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('´', '`', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	),
	"order2" to hashMapOf( // àáâäã
		KeyEvent.KEYCODE_A to arrayOf('`', '´', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_E to arrayOf('`', '´', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_I to arrayOf('`', '´', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_O to arrayOf('`', '´', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_U to arrayOf('`', '´', '^', '¨', '~', MPSUBST_BYPASS),
		KeyEvent.KEYCODE_SPACE to arrayOf(MPSUBST_STR_DOTSPACE)
	)
)

class InputMethodService : AndroidInputMethodService() {
	private lateinit var vibrator: Vibrator
	private val shift = Modifier()
	private val alt = Modifier()
	private val sym = SimpleModifier()
	private var lastShift = false
	private var lastAlt = false
	private var lastSym = false

	private var autoCapitalize = false

	private val multipress = MultipressController(arrayOf(
		templates["fr-ext"]!!,
		hashMapOf(
			KeyEvent.KEYCODE_Q to arrayOf(MPSUBST_TOGGLE_ALT, '°', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_W to arrayOf(MPSUBST_TOGGLE_ALT, '&', '↑', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_E to arrayOf(MPSUBST_TOGGLE_ALT, '€', '∃', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_R to arrayOf(MPSUBST_TOGGLE_ALT, '®', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_T to arrayOf(MPSUBST_TOGGLE_ALT, '[', '{', '<', '≤', '†', '™', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_Y to arrayOf(MPSUBST_TOGGLE_ALT, ']', '}', '>', '≥', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_U to arrayOf(MPSUBST_TOGGLE_ALT, '—', '–', '∪', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_I to arrayOf(MPSUBST_TOGGLE_ALT, '|', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_O to arrayOf(MPSUBST_TOGGLE_ALT, '\\', 'œ', 'º', '÷', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_P to arrayOf(MPSUBST_TOGGLE_ALT, ';', '¶', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_A to arrayOf(MPSUBST_TOGGLE_ALT, 'æ', 'ª', '←', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_S to arrayOf(MPSUBST_TOGGLE_ALT, 'ß', '§', '↓', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_D to arrayOf(MPSUBST_TOGGLE_ALT, '∂', '→', '⇒', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_F to arrayOf(MPSUBST_TOGGLE_ALT, MPSUBST_CIRCUMFLEX, MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_G to arrayOf(MPSUBST_TOGGLE_ALT, '•', '·', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_H to arrayOf(MPSUBST_TOGGLE_ALT, '²', '♯', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_J to arrayOf(MPSUBST_TOGGLE_ALT, '=', '≠', '≈', '±', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_K to arrayOf(MPSUBST_TOGGLE_ALT, '%', '‰', '‱', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_L to arrayOf(MPSUBST_TOGGLE_ALT, MPSUBST_BACKTICK, MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_Z to arrayOf(MPSUBST_TOGGLE_ALT, '¡', '‽', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_X to arrayOf(MPSUBST_TOGGLE_ALT, '×', 'χ', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_C to arrayOf(MPSUBST_TOGGLE_ALT, 'ç', '©', '¢', '⊂', '⊄', '⊃', '⊅', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_V to arrayOf(MPSUBST_TOGGLE_ALT, '∀', '√', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_B to arrayOf(MPSUBST_TOGGLE_ALT, '…', 'ß', '∫', '♭', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_N to arrayOf(MPSUBST_TOGGLE_ALT, '~', '¬', '∩', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_M to arrayOf(MPSUBST_TOGGLE_ALT, '$', '€', '£', '¿', MPSUBST_TOGGLE_SHIFT, MPSUBST_BYPASS),
			KeyEvent.KEYCODE_SPACE to arrayOf('\t', '⇥', MPSUBST_BYPASS)
		)
	))

	override fun onCreate() {
		super.onCreate()
		vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

		val preferences = PreferenceManager.getDefaultSharedPreferences(this)
		preferences.registerOnSharedPreferenceChangeListener { preferences, key ->
			updateFromPreferences()
		}
		updateFromPreferences()
	}

	override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
		super.onStartInput(attribute, restarting)

		updateFromPreferences()

		if(!sym.get()) {
			updateAutoCapitalization()
		}
	}

	override fun onUpdateSelection(
		oldSelStart: Int,
		oldSelEnd: Int,
		newSelStart: Int,
		newSelEnd: Int,
		candidatesStart: Int,
		candidatesEnd: Int
	) {
		if(!sym.get()) {
			updateAutoCapitalization()
		}

		super.onUpdateSelection(
			oldSelStart,
			oldSelEnd,
			newSelStart,
			newSelEnd,
			candidatesStart,
			candidatesEnd
		)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		// Update modifier states
		if(!event.isLongPress && event.repeatCount == 0) {
			when(event.keyCode) {
				KeyEvent.KEYCODE_ALT_RIGHT -> {
					alt.onKeyDown()
					updateStatusIconIfNeeded(true)
				}
				KeyEvent.KEYCODE_SHIFT_LEFT -> {
					shift.onKeyDown()
					updateStatusIconIfNeeded(true)
				}
				KeyEvent.KEYCODE_SYM -> {
					sym.onKeyDown()
					onSymPossiblyChanged(true)
					updateStatusIconIfNeeded(true)
				}
			}
		}

		if(event.isCtrlPressed) {
			return super.onKeyDown(keyCode, event)
		}

		// Use special behavior when the SYM modifier is enabled
		if(sym.get()) {
			return onSymKey(event, true)
		}

		// Apply multipress substitution
		if(event.isPrintingKey || event.keyCode == KeyEvent.KEYCODE_SPACE) {
			val char = multipress.process(event, enhancedMetaState(event))
			if(char != MPSUBST_BYPASS) {
				if(char != MPSUBST_NOTHING) {
					currentInputConnection?.deleteSurroundingText(1, 0)
					updateAutoCapitalization()
					when(char) {
						MPSUBST_STR_DOTSPACE -> currentInputConnection?.commitText(". ", 2)
						else -> sendCharacter(char.toString())
					}

					consumeModifierNext()
				}
				vibrate()
				return true
			}
		}

		// Use default behavior for backspace and delete, with a few tweaks
		if(event.keyCode == KeyEvent.KEYCODE_DEL || event.keyCode == KeyEvent.KEYCODE_FORWARD_DEL) {
			multipress.reset()

			consumeModifierNext()

			return super.onKeyDown(keyCode, event)
		}

		// Ignore all long presses after this point
		if(event.isLongPress || event.repeatCount > 0) {
			return true
		}

		// Print something if it is a simple printing key press
		if((event.isPrintingKey || event.keyCode == KeyEvent.KEYCODE_SPACE || (event.keyCode == KeyEvent.KEYCODE_ENTER && shift.get()))) {
			val str = event.getUnicodeChar(enhancedMetaState(event)).toChar().toString()
			currentInputConnection?.commitText(str, 1)

			consumeModifierNext()
			return true
		}

		if(event.keyCode == KeyEvent.KEYCODE_ENTER) {
			consumeModifierNext()
		}

		return super.onKeyDown(keyCode, event)
	}

	override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
		// Update modifier states
		when(event.keyCode) {
			KeyEvent.KEYCODE_ALT_RIGHT -> {
				alt.onKeyUp()
				updateStatusIconIfNeeded(true)
			}
			KeyEvent.KEYCODE_SHIFT_LEFT -> {
				shift.onKeyUp()
				updateStatusIconIfNeeded(true)
			}
			KeyEvent.KEYCODE_SYM -> {
				sym.onKeyUp()
				onSymPossiblyChanged(false)
				updateStatusIconIfNeeded(true)
			}
		}

		// Use special behavior when the SYM modifier is enabled
		if(sym.get()) {
			return onSymKey(event, false)
		}

		return super.onKeyUp(keyCode, event)
	}

	/**
	 * Handle a key down event when the SYM modifier is enabled.
	 */
	private fun onSymKey(event: KeyEvent, pressed: Boolean): Boolean {

		// The SPACE key is equivalent to hitting Shift
		if(event.keyCode == KeyEvent.KEYCODE_SPACE) {
			if(pressed) {
				shift.onKeyDown()
			} else {
				shift.onKeyUp()
			}
			updateStatusIconIfNeeded(true)
			return true
		}

		// Some keys simulate a D-Pad
		val dpadKey = when(event.keyCode) {
			// WASD to directional keys
			KeyEvent.KEYCODE_W -> KeyEvent.KEYCODE_DPAD_UP
			KeyEvent.KEYCODE_A -> KeyEvent.KEYCODE_DPAD_LEFT
			KeyEvent.KEYCODE_S -> KeyEvent.KEYCODE_DPAD_DOWN
			KeyEvent.KEYCODE_D -> KeyEvent.KEYCODE_DPAD_RIGHT
			// HJKL to directional keys
			KeyEvent.KEYCODE_H -> KeyEvent.KEYCODE_DPAD_LEFT
			KeyEvent.KEYCODE_J -> KeyEvent.KEYCODE_DPAD_DOWN
			KeyEvent.KEYCODE_K -> KeyEvent.KEYCODE_DPAD_UP
			KeyEvent.KEYCODE_L -> KeyEvent.KEYCODE_DPAD_RIGHT
			//
			else -> 0
		}
		if(dpadKey != 0) {
			sendDPadKey(dpadKey, event, pressed)
			return true
		}

		// Some keys simulate keyboard keys
		val kbdKey = when(event.keyCode) {
			KeyEvent.KEYCODE_Y -> KeyEvent.KEYCODE_MOVE_HOME
			KeyEvent.KEYCODE_U -> KeyEvent.KEYCODE_PAGE_DOWN
			KeyEvent.KEYCODE_I -> KeyEvent.KEYCODE_PAGE_UP
			KeyEvent.KEYCODE_O -> KeyEvent.KEYCODE_MOVE_END
			KeyEvent.KEYCODE_P -> KeyEvent.KEYCODE_ESCAPE
			KeyEvent.KEYCODE_X -> KeyEvent.KEYCODE_CUT
			KeyEvent.KEYCODE_C -> KeyEvent.KEYCODE_COPY
			KeyEvent.KEYCODE_V -> KeyEvent.KEYCODE_PASTE
			KeyEvent.KEYCODE_Z -> KeyEvent.KEYCODE_TAB
			KeyEvent.KEYCODE_Q -> KeyEvent.KEYCODE_TAB
			else -> 0
		}
		if(kbdKey != 0) {
			sendKey(kbdKey, event, pressed)
			return true
		}

		// Some keys type text
		if(event.repeatCount == 0 && !event.isLongPress && pressed) {
			val str = when(event.keyCode) {
				KeyEvent.KEYCODE_B -> "$"
				KeyEvent.KEYCODE_N -> "="
				KeyEvent.KEYCODE_E -> "€"
				KeyEvent.KEYCODE_M -> "%"
				else -> null
			}
			if(str != null) {
				sendCharacter(str)
			}
		}

		// Other non-printing keys pass through
		if(!event.isPrintingKey) {
			return if(pressed) {
				super.onKeyDown(event.keyCode, event)
			} else {
				super.onKeyUp(event.keyCode, event)
			}
		}

		// The rest is ignored

		return true
	}

	/**
	 * Send a D-Pad key press or release.
	 */
	private fun sendDPadKey(code: Int, original: KeyEvent, pressed: Boolean) {
		currentInputConnection?.sendKeyEvent(makeKeyEvent(original, code, enhancedMetaState(original), if(pressed) KeyEvent.ACTION_DOWN else KeyEvent.ACTION_UP, InputDevice.SOURCE_DPAD))
	}

	/**
	 * Send a key press or release.
	 */
	private fun sendKey(code: Int, original: KeyEvent, pressed: Boolean) {
		currentInputConnection?.sendKeyEvent(makeKeyEvent(original, code, enhancedMetaState(original), if(pressed) KeyEvent.ACTION_DOWN else KeyEvent.ACTION_UP, InputDevice.SOURCE_DPAD))
	}

	/**
	 * Send a character, possibly uppercased depending on the Shift modifier.
	 */
	private fun sendCharacter(str: String, strict: Boolean = false) {
		var text = str
		if(!strict && shift.get()) {
			text = text.uppercase(Locale.getDefault())
		}
		currentInputConnection?.commitText(text, 1)
	}

	/**
	 * Make the device vibrate.
	 */
	private fun vibrate() {
		vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
	}

	/**
	 * Update the icon in the status bar according to modifier states.
	 */
	private fun updateStatusIconIfNeeded(force: Boolean = false) {
		val shiftState = shift.get()
		val altState = alt.get()
		val symState = sym.get()
		if(force || symState != lastSym || altState != lastAlt || shiftState != lastShift) {
			if(sym.get()) {
				if(shift.get()) {
					showStatusIcon(R.drawable.symshift)
				} else {
					showStatusIcon(R.drawable.sym)
				}
			} else if(alt.get()) {
				showStatusIcon(R.drawable.alt)
			} else if(shift.get()) {
				if(shift.isLocked()) {
					showStatusIcon(R.drawable.shiftlock)
				} else {
					showStatusIcon(R.drawable.shift)
				}
			} else {
				hideStatusIcon()
			}
		}
		lastShift = shiftState
		lastAlt = altState
		lastSym = symState
	}

	/**
	 * Update the Shift modifier state for auto-capitalization.
	 */
	private fun updateAutoCapitalization() {
		if(!autoCapitalize) {
			return
		}
		if(currentInputEditorInfo == null || currentInputConnection == null) {
			return
		}

		if(currentInputConnection.getCursorCapsMode(TextUtils.CAP_MODE_SENTENCES) > 0 && canUseSuggestions(currentInputEditorInfo)) {
			shift.activateForNext()
			updateStatusIconIfNeeded()
		}
	}

	/**
	 * Inform modifiers that the "next" key press has been consumed.
	 */
	private fun consumeModifierNext() {
		shift.nextDidConsume()
		alt.nextDidConsume()
		updateStatusIconIfNeeded()
	}

	/**
	 * @return The metaState of the given event, enhanced with our own modifiers.
	 */
	private fun enhancedMetaState(original: KeyEvent): Int {
		var metaState = original.metaState
		if(shift.get()) {
			metaState = metaState or KeyEvent.META_SHIFT_ON
		}
		if(alt.get()) {
			metaState = metaState or KeyEvent.META_ALT_ON
		}
		return metaState
	}

	/**
	 * Handle what happens when the SYM modifier has possibly changed.
	 */
	private fun onSymPossiblyChanged(pressed: Boolean) {
		if(sym.get() && !lastSym) {
			if(shift.get() && !shift.isHeld()) {
				shift.reset()
			}
		} else if(!sym.get() && lastSym) {
			updateAutoCapitalization()
		}
	}

	/**
	 * Update values from the preferences.
	 */
	private fun updateFromPreferences() {
		val preferences = PreferenceManager.getDefaultSharedPreferences(this)

		autoCapitalize = preferences.getBoolean("AutoCapitalize", true)

		val lockThreshold = preferences.getInt("ModifierLockThreshold", 250)
		shift.lockThreshold = lockThreshold
		alt.lockThreshold = lockThreshold
		sym.lockThreshold = lockThreshold

		val nextThreshold = preferences.getInt("ModifierNextThreshold", 350)
		shift.nextThreshold = nextThreshold
		alt.nextThreshold = nextThreshold

		multipress.multipressThreshold = preferences.getInt("MultipressThreshold", 750)
		multipress.ignoreFirstLevel = !preferences.getBoolean("UseFirstLevel", true)
		multipress.ignoreDotSpace = !preferences.getBoolean("DotSpace", true)
		multipress.ignoreConsonantsOnFirstLevel = preferences.getBoolean("FirstLevelOnlyVowels", false)

		val templateId = preferences.getString("FirstLevelTemplate", "fr")
		if(templates.containsKey(templateId)) {
			multipress.substitutions[0] = templates[templateId]!!
		}
	}
}
