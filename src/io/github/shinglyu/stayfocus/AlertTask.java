package io.github.shinglyu.stayfocus;

import java.util.TimerTask;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.app.Service;
import android.content.Context;

public class AlertTask extends TimerTask{
	Context c=null;
	public AlertTask(Context applicationContext) {
		this.c= applicationContext;// TODO Auto-generated constructor stub
	}
		public void run(){
			//toast("Wake up!");
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			Ringtone r = RingtoneManager.getRingtone(this.c, notification);
			r.play();
			
			Vibrator vb = (Vibrator) c.getSystemService(Service.VIBRATOR_SERVICE);
			vb.vibrate(10000);
		}
}

