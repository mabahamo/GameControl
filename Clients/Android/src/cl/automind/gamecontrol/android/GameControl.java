package cl.automind.gamecontrol.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.DhcpInfo;
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
	private final int PORT = 9999;
	private final int DISCOVERY_PORT = 9998;
	private final String SERVER = "192.168.1.114";

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
			sock = new Socket(SERVER, PORT);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			debug("conexi—n exitosa");
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
	
	public void debug(Exception ex){
		if (!DEBUG){
			return;
		}
		debug(ex.getMessage());
	}

	private void enableWifi() {
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		debug("conectando a servidor");
	}
	
	/**
	 * Construye la direcci—n de broadcast de acuerdo a la direcci—n obtenida y a la m‡scara de la red
	 * @return
	 * @throws IOException
	 */
	private InetAddress getBroadcastAddress() throws IOException {
	    WifiManager wifi = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
	    DhcpInfo dhcp = wifi.getDhcpInfo();
	    // handle null somehow

	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	}
	
	/**
	 * Env’a un mensaje buscando el servidor local y espera por una respuesta
	 */
	private void searchLocalServer() {
		try {
			String data = "HOWDY!";
			DatagramSocket socket = new DatagramSocket(PORT);
			socket.setBroadcast(true);
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
			    getBroadcastAddress(), DISCOVERY_PORT);
			socket.send(packet);
	
			byte[] buf = new byte[1024];
			DatagramPacket msg = new DatagramPacket(buf, buf.length);
			socket.receive(msg);
		} catch(Exception ex){
			debug(ex);
		}
	}
	

}