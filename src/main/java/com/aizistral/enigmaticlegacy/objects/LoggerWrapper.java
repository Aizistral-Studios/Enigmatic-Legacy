package com.aizistral.enigmaticlegacy.objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LoggerWrapper {
	private final Logger logger;
	private final Marker markWarn = MarkerManager.getMarker("WARNING");
	private final Marker markInfo = MarkerManager.getMarker("INFO");
	private final Marker markLog = MarkerManager.getMarker("LOG");
	private final Marker markDebug = MarkerManager.getMarker("DEBUG");
	private final Marker markFatal = MarkerManager.getMarker("FATAL");
	private final Marker markError = MarkerManager.getMarker("ERROR");

	public LoggerWrapper(String name) {
		this.logger = LogManager.getLogger(name);
	}

	public Logger getInternal() {
		return this.logger;
	}

	public void info(String line) {
		this.logger.info(this.markInfo, line);
	}

	public void log(String line) {
		this.logger.info(this.markLog, line);
	}

	public void debug(String line) {
		this.logger.debug(this.markDebug, line);
	}

	public void warn(String line) {
		this.logger.warn(this.markWarn, line);
	}

	public void fatal(String line) {
		this.logger.fatal(this.markFatal, line);
	}

	public void error(String line) {
		this.logger.error(this.markError, line);
	}

	public void catching(Throwable ex) {
		this.logger.catching(ex);
	}

}
