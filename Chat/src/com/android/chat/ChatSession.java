package com.android.chat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatSession extends Activity
{
	/** Called when the activity is first created. */
	private static final String TAG = "ChatSession";
	private String email = "";
	private ArrayAdapter<String> chatSessionListAdapter;
	final Handler mHandler = new Handler();

	/*
	 * final Runnable mUpdateResult = new Runnable() {
	 * 
	 * public void run() { //updateResultInUI(); }
	 * 
	 * 
	 * };
	 */

	private void updateResultInUI(String msg)
	{
		chatSessionListAdapter.add(msg);

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_session);
		
		

		
		Toast.makeText(this, email, Toast.LENGTH_LONG).show();

		new Thread(new Runnable()
		{

			public void run()
			{
				Bundle extras = getIntent().getExtras();
				final ListView chatSessionListUI = (ListView) findViewById(R.id.chatList);

				chatSessionListAdapter = new ArrayAdapter<String>(
						ChatSession.this, android.R.layout.simple_list_item_1);

				chatSessionListUI.setAdapter(chatSessionListAdapter);

				if (extras == null)
					return;

				email = extras.getString("email");

				MySharedData mysharedData = (MySharedData) getApplicationContext();
				XMPPConnection xmpp = mysharedData.getXmpp();
				ChatManager chatman = xmpp.getChatManager();

				//
				final Chat newChat = chatman.createChat(email, new MessageListener()
				{

					@Override
					public void processMessage(Chat chat, Message message)
					{
						try
						{
							// Log.v(TAG,"got:processMessage")
							chat.sendMessage(message.getBody());
						}
						catch (XMPPException e)
						{
							Log.v(TAG, "could not respond" + e);
						}

					}
				});

				// send message
				
				final EditText chatBox= (EditText)findViewById(R.id.chat_box);
				final Button chatButton=(Button)findViewById(R.id.send);
				
				final StringBuilder chatMsgToBeSend = new StringBuilder();
					chatButton.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							try
							{
								chatMsgToBeSend.replace(0, chatMsgToBeSend.length(), (chatBox.getText().toString()));	
								 newChat.sendMessage(chatMsgToBeSend.toString());
								 chatBox.setText("");
								 mHandler.post(new Runnable()
									{

										public void run()
										{
											updateResultInUI("me :"+chatMsgToBeSend.toString());
										}

									});
							}
							catch (XMPPException e)
							{
								// TODO: handle exception
							}
							
						}
					});
					
					
				

				
			

				// recive message
				PacketFilter filter = new AndFilter(new PacketTypeFilter(
						Message.class), new FromContainsFilter(email));

				// Collect messages
				PacketCollector collector = xmpp.createPacketCollector(filter);
				final StringBuilder msgBuilder = new StringBuilder();
				while (true)
				{
					Packet packet = collector.nextResult();
					if (packet instanceof Message)
					{
						Message msg = (Message) packet;

						Log.v(TAG, "Message From" + email + ":" + msg.getBody());
						// chatSessionListAdapter.add(email+":"+msg.getBody());
						msgBuilder.replace(0, msgBuilder.length(), email.split("@")[0] + ":"
								+ msg.getBody());

						mHandler.post(new Runnable()
						{

							public void run()
							{
								updateResultInUI(msgBuilder.toString());
							}

						});
					}
				}

			}
		}).start();

	}

}
