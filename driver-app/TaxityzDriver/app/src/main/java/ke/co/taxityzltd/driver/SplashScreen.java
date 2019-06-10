package ke.co.taxityzltd.driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {
	ProgressBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen_activity);
bar=(ProgressBar)findViewById(R.id.progress);
		
	}
	
	public void onStart() {
		super.onStart();
		bar.setProgress(0);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					
						Thread.sleep(7000);
						 Intent ii=new Intent(getBaseContext(),Login.class);
		                    startActivity(ii);
						finish();
					
				}
				catch (Throwable t) {
				}
			}
		}).start();
	}

	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			bar.incrementProgressBy(2);
		}
	};
}