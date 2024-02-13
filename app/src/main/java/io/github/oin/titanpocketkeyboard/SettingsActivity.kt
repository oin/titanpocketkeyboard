package io.github.oin.titanpocketkeyboard

import android.content.DialogInterface
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SeekBarPreference

class SettingsActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.settings_activity)
		if (savedInstanceState == null) {
			supportFragmentManager
				.beginTransaction()
				.replace(R.id.settings, SettingsFragment())
				.commit()
		}
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
	}

	class SettingsFragment : PreferenceFragmentCompat() {
		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preferences, rootKey)
			val context = activity
			if(context != null) {
				findPreference<Preference>("Reset")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
					AlertDialog.Builder(context)
						.setTitle("Reset settings")
						.setMessage("Do you really want to reset all the settings to their default value?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
							val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
							editor.clear()
							editor.commit()
							setPreferencesFromResource(R.xml.preferences, rootKey)
						})
						.setNegativeButton(android.R.string.no, null)
						.show()
					true
				}
			}
		}
	}
}