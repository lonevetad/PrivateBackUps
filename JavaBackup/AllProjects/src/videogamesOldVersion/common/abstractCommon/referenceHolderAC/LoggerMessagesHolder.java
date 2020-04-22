package videogamesOldVersion.common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import tools.LoggerMessages;

public interface LoggerMessagesHolder extends Serializable {

	public LoggerMessages getLog();

	public default LoggerMessagesHolder setLogSafe(LoggerMessages log) {
		return setLog(LoggerMessages.loggerOrDefault(log));
	}

	public LoggerMessagesHolder setLog(LoggerMessages log);
}