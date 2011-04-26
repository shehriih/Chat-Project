package com.android.chat;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Application;

public class MySharedData extends Application
{
	private XMPPConnection xmpp;

	public XMPPConnection getXmpp()
	{
		return xmpp;
	}

	public void setXmpp(XMPPConnection xmpp)
	{
		this.xmpp = xmpp;
	}
}
