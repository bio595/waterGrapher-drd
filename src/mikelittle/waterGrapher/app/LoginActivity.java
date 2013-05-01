package mikelittle.waterGrapher.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final EditText usernameInput = (EditText) findViewById(R.id.username);
		final EditText passwordInput = (EditText) findViewById(R.id.password);
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View button) {
				LoginTask loginTask =  new LoginTask();
				loginTask.execute(new String[]{usernameInput.getText().toString(), passwordInput.getText().toString()});
			}
		});
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
			//Return the success of the login
		}
		
	}

}
