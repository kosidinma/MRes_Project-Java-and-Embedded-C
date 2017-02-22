package no.nordicsemi.android.nrftemp;

import no.nordicsemi.android.nrftemp.fragment.SettingsFragment;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartPeriodicScanBroadcastReceiver extends BroadcastReceiver {
	public static final String ACTION = "no.nordicsemi.android.nrftemp.START";
	public static final int START_REQUEST = 23513115;

	/** The default length of interval between scanning for BLE temperature sensors */
	private static final long DELAY = 5 * 60 * 1000l; // ms

	@Override
	public void onReceive(Context context, Intent intent) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final boolean scanInBackgroundEnabled = preferences.getBoolean(SettingsFragment.SETTEINGS_SCAN_IN_BACKGROUND, true);
		// check whether background scanning is enabled. This must be done because this receiver receives also a BOOT_COMPLETED action. 
		if (!scanInBackgroundEnabled)
			return;

		// scanning is enabled, obtain scan interval
		final String delayString = preferences.getString(SettingsFragment.SETTEINGS_SCAN_INTERVAL, "" + DELAY);
		final long delay = Long.parseLong(delayString);

		final Intent i = new Intent(context, ScanNowBroadcastReceiver.class);
		final PendingIntent operation = PendingIntent.getBroadcast(context, START_REQUEST, i, 0);

		final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, delay, operation);
	}
}
