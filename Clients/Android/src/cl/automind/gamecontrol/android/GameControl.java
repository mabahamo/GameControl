package cl.automind.gamecontrol.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class GameControl extends Activity implements SensorEventListener {
	SensorManager sensorManager;
	Sensor sensor;
	TextView angle, pitch, debug;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	WifiManager wifiManager;
	final boolean DEBUG = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		angle = (TextView) findViewById(R.id.angle);
		pitch = (TextView) findViewById(R.id.pitch);
		debug = (TextView) findViewById(R.id.debug);
		enableWifi();
		try {
			sock = new Socket("192.168.15.12", 9999);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			debug("conexi�n exitosa");
		} catch (Exception ex) {
			debug(ex.getMessage());
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		float[] data = arg0.values;
		angle.setText("" + data[2]);
		pitch.setText("" + data[1]);
		if (out != null) {
			out.println(angle.getText() + ";" + pitch.getText() );
		}
	}

	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		finish();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		finish();
	}

	public void debug(String message) {
		if (!DEBUG) {
			return;
		}
		debug.setText(message);
	}

	private void enableWifi() {
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		debug("conectando a servidor");
		
	}

}