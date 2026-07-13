package com.testable.checkstyle.model;

/**
 * Immutable record of a single Checkstyle violation used for metric computation.
 */
public final class ViolationRecord {

    private final String fileName;
    private final int line;
    private final int column;
    private final String message;
    private final String source;
    private final String severity;

    public ViolationRecord(String fileName, int line, int column,
                           String message, String source, String severity) {
        this.fileName = fileName;
        this.line = line;
        this.column = column;
        this.message = message;
        this.source = source;
        this.severity = severity;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public String getSeverity() {
        return severity;
    }

    public boolean isError() {
        return "error".equalsIgnoreCase(severity);
    }

    public boolean isWarning() {
        return "warning".equalsIgnoreCase(severity);
    }

    public boolean isInfo() {
        return "info".equalsIgnoreCase(severity);
    }

    public boolean messageContains(String token) {
        return message != null && message.toLowerCase().contains(token.toLowerCase());
    }

    public boolean sourceContains(String token) {
        return source != null && source.toLowerCase().contains(token.toLowerCase());
    }
}
