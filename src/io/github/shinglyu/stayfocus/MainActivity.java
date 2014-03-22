package io.github.shinglyu.stayfocus;

import java.util.Timer;
import java.util.TimerTask;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class MainActivity extends Activity implements SensorEventListener {


	private SensorManager mSensorManager;
	private Sensor mSensor;
	private double[] gravity = new double[3];
	private double[] linear_acceleration = new double[3];
	private int timeout=5;
	private int remainTime=5;
	private double motionThreshold=1.0; //change this by experiment
	private Timer timer = new Timer("inactive", true);
	public void ringring(){
		//toast("Wake up!");
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}
	private void toast(String text){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	

	
	public void onSensorChanged(SensorEvent event){
		// In this example, alpha is calculated as t / (t + dT),
		// where t is the low-pass filter's time-constant and
		// dT is the event delivery rate.

		final double alpha = 0.8;

		// Isolate the force of gravity with the low-pass filter.
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		// Remove the gravity contribution with the high-pass filter.
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];
		linear_acceleration[2] = event.values[2] - gravity[2];

		TextView textView = null;
		textView = (TextView) findViewById(R.id.Xvalue);
		textView.setText(String.valueOf(linear_acceleration[0]));
		textView = (TextView) findViewById(R.id.Yvalue);
		textView.setText(String.valueOf(linear_acceleration[1]));
		textView = (TextView) findViewById(R.id.Zvalue);
		textView.setText(String.valueOf(linear_acceleration[2]));
		for (double acce :linear_acceleration){
			if (Math.abs(acce) > motionThreshold){
				timer.cancel();
				timer = new Timer("inactive", true);
				timer.schedule(new AlertTask(getApplicationContext()), timeout*60*1000);
			}
		}
   

	}
	public void onCheckedChanged(View view) {
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {
			toast("Alert Started");
			// The toggle is enabled
			gravity[0] = 0;
			gravity[1] = 0;
			gravity[2] = 0;
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
			//start timer
			//timer.schedule(alarm, remainTime);
		} else {
			toast("Alert Stopped");
			// The toggle is disabled
			mSensorManager.unregisterListener(this);
			timer.cancel();
			timer.purge();
			timer = new Timer("inactive", true);

		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EditText timeoutView = (EditText)findViewById(R.id.timeoutEdit);
		timeoutView.setText(String.valueOf(timeout));
		timeoutView.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				try{
					timeout = Integer.parseInt(s.toString());

				}
				catch (NumberFormatException ex){
				}
				
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}
		}); 


		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	//ondestory cancle all timer


}
