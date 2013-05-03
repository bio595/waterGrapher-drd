package mikelittle.waterGrapher.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

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
				findViewById(R.id.badLoginCredentialsContainer).setVisibility(View.GONE);
				if (isNetworkAvailable()) {

					LoginTask loginTask = new LoginTask();
					loginTask.execute(new String[] {
							usernameInput.getText().toString(),
							passwordInput.getText().toString() });
				} else {
					// Display a network not available alert
					findViewById(R.id.networkErrorContainer).setVisibility(View.VISIBLE);
				}
			}
		});
		
		if (isNetworkAvailable()) {
			findViewById(R.id.networkErrorContainer).setVisibility(View.GONE);
		}else{
			findViewById(R.id.networkErrorContainer).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private class LoginTask extends AsyncTask<String, Void, Boolean> {

		
		
		private int statusCode;
		private ProgressDialog progDialog;
		private Cookie sessionCookie;
		
		@Override
		protected void onPreExecute() {
			progDialog = new ProgressDialog(LoginActivity.this);
			progDialog.setMessage("Attempting to log in...");
			progDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {

			DefaultHttpClient client = createHttpClient();

			HttpPost postRequest = new HttpPost(
					"http://watergrapher.herokuapp.com/login");
			// Put data in the request
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));

			try {
				postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(postRequest);
				

				statusCode = response.getStatusLine().getStatusCode();

				switch (statusCode) {
				case 200: // We successfully logged in
					sessionCookie = client.getCookieStore().getCookies().get(0);
					return true;
				case 401: // Login credentials were incorrect
					//Indicate this to the user somehow
					return false;
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			
			if (progDialog.isShowing()) {
	            progDialog.dismiss();
	        }
			
			if(success){
				Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("sessionCookie", new SerializableCookie(sessionCookie));
				intent.putExtra("bundle", b);
				startActivity(intent);
			}else{
				if(statusCode == 401){
					findViewById(R.id.badLoginCredentialsContainer).setVisibility(View.VISIBLE);
				}else{
					//Some error happened, "There was an error signing in, try again later"
				}
			}
		}

		private DefaultHttpClient createHttpClient() {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);

			return new DefaultHttpClient(conMgr, params);
		}

	}

}
