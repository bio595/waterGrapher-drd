package mikelittle.waterGrapher.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		EditText usernameInput = (EditText) findViewById(R.id.username);
		EditText passwordInput = (EditText) findViewById(R.id.password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private class LoginTask extends AsyncTask<String, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
		
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
		}
		
	}

}
