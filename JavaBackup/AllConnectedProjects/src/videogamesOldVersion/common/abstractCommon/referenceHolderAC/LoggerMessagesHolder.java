package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import common.mainTools.LoggerMessages;

public interface LoggerMessagesHolder extends Serializable {

	public LoggerMessages getLog();

	public default LoggerMessagesHolder setLogSafe(LoggerMessages log) {
		return setLog(LoggerMessages.loggerOrDefault(log));
	}

	public LoggerMessagesHolder setLog(LoggerMessages log);
}