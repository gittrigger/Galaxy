package com.havenskys.galaxy;

import com.havenskys.galaxy.IRemoteServiceCallback;

interface IRemoteService {
	void registerCallback(IRemoteServiceCallback cb);
	void unregisterCallback(IRemoteServiceCallback cb);
}