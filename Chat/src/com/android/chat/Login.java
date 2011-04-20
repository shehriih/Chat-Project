package com.android.chat;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity
{
	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		final Button loginButton = (Button) findViewById(R.id.submit_credentials);
		loginButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				final EditText emailEditText = (EditText) findViewById(R.id.email_value);
				final EditText passwordEditText = (EditText) findViewById(R.id.password_value);

				String email = emailEditText.getText().toString();
				String password = passwordEditText.getText().toString();

				java.lang.System
						.setProperty("java.net.preferIPv4Stack", "true");
				java.lang.System.setProperty("java.net.preferIPv6Addresses",
						"false");
				XMPPConnection xmpp = new XMPPConnection("gmail.com");

				try
				{
					xmpp.connect();
					xmpp.login(email, password);
					launchActivity(ChatMain.class);
				}
				catch (Exception e)
				{

					Toast.makeText(Login.this, "Failed to connect", Toast.LENGTH_LONG).show();
					Log.v(TAG, "Failed to connect to " + xmpp.getHost());
					e.printStackTrace();
				}				
			}
		});
	}

	private void launchActivity(Class className)
	{
		Intent intent = new Intent(this, className);
		startActivity(intent);
	}

}
