package no.nordicsemi.android.nrftemp;

import no.nordicsemi.android.nrftemp.ble.TemperatureManager;
import no.nordicsemi.android.nrftemp.database.DatabaseHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScanNowBroadcastReceiver extends BroadcastReceiver {
	public static final String ACTION = "no.nordicsemi.android.nrftemp.SCAN";

	/** Each 5 minutes the bluetooth will scan for that amount of milliseconds for BLE sensors */
	private static final long PERIOD = 10000l; // ms

	@Override
	public void onReceive(Context context, Intent intent) {
		final DatabaseHelper databaseHelper = new DatabaseHelper(context);
		final TemperatureManager temperatureManager = new TemperatureManager(context, databaseHelper);
		if (!temperatureManager.isEnabled())
			return;

		temperatureManager.startScan(PERIOD);
	}
}
