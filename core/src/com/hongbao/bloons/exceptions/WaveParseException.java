package com.hongbao.bloons.exceptions;

public class WaveParseException extends RuntimeException {

	private final String fileName;
	private final int lineNumber;
	private final String offendingToken;

	public WaveParseException(String message) {
		super(message);
		this.fileName = null;
		this.lineNumber = -1;
		this.offendingToken = null;
	}

	public WaveParseException(String message, String fileName, int lineNumber) {
		super(formatMessage(message, fileName, lineNumber, null));
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.offendingToken = null;
	}

	public WaveParseException(String message, String fileName, int lineNumber, String offendingToken) {
		super(formatMessage(message, fileName, lineNumber, offendingToken));
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.offendingToken = offendingToken;
	}

	private static String formatMessage(String message, String fileName, int lineNumber, String offendingToken) {
		StringBuilder sb = new StringBuilder();
		if (message != null) {
			sb.append(message);
		}
		if (lineNumber > 0) {
			sb.append(" at line ").append(lineNumber);
		}
		if (fileName != null && !fileName.isEmpty()) {
			sb.append(" in ").append(fileName);
		}
		return sb.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getOffendingToken() {
		return offendingToken;
	}

}
