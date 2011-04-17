package com.android.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ChatMain extends Activity
{
	private static final int ADD_FRIEND_ID = Menu.FIRST;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		
		final ListView contactList = (ListView) findViewById(R.id.contactList);
		contactList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				launchActivity(ChatSession.class);				
			}			
		});
	}
	
	private void launchActivity(Class className)
	{
		Intent intent = new Intent(this, className);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_FRIEND_ID, 0, R.string.add_friend_title);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		
		switch (item.getItemId())
		{
		case ADD_FRIEND_ID:
			launchActivity(FriendAddition.class);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	
	

}