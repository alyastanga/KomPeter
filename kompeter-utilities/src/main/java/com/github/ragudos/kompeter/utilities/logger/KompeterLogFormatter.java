/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class KompeterLogFormatter extends Formatter {
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withLocale(Locale.US)
                    .withZone(ZoneId.systemDefault());

    private void appendException(final StringBuilder sb, final Throwable err) {
        sb.append("\t");

        String simpleName = err.getClass().getCanonicalName();

        sb.append(simpleName).append(": ").append(err.getMessage());

        sb.append("\n");

        StackTraceElement[] trace = err.getStackTrace();

        for (StackTraceElement element : trace) {
            sb.append("\t\tat ").append(element).append("\n");
        }

        Set<Throwable> circularReferenceSet = Collections.newSetFromMap(new IdentityHashMap<>());
        Throwable[] suppressed = err.getSuppressed();

        if (suppressed.length != 0) {
            sb.append("\tSuppressed: ").append("\n");

            for (Throwable se : suppressed) {

                printEnclosedStackTrace(sb, se, trace, "\tSuppressed: ", "\t", circularReferenceSet);
            }
        }

        Throwable cause = err.getCause();

        if (cause != null) {
            sb.append("\tCaused by: ").append("\n");

            printEnclosedStackTrace(sb, cause, trace, "\tCaused by: ", "\t", circularReferenceSet);
        }
    }

    private int computeFramesInCommon(
            final StackTraceElement[] trace,
            final StackTraceElement[] enclosingTrace,
            final int m,
            final int n) {
        var copyM = m;
        var copyN = n;

        while (copyM >= 0 && copyN >= 0 && trace[copyM].equals(enclosingTrace[copyN])) {
            --copyM;
            --copyN;
        }

        return trace.length - copyM - 1;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(formateDateString(record.getMillis()))
                .append(" - ")
                .append(record.getSourceClassName())
                .append(".")
                .append(record.getSourceMethodName())
                .append(" - ")
                .append(record.getLevel().getName())
                .append(" - ")
                .append(formatMessage(record));

        stringBuilder.append("\n");

        Throwable errThrowable = record.getThrown();

        if (errThrowable != null) {
            appendException(stringBuilder, errThrowable);
        }

        return stringBuilder.toString();
    }

    private String formateDateString(final long ms) {
        return dateTimeFormatter.format(Instant.ofEpochMilli(ms));
    }

    private void printEnclosedStackTrace(
            final StringBuilder sb,
            final Throwable err,
            final StackTraceElement[] enclosingTrace,
            final String caption,
            final String prefix,
            final Set<Throwable> circularReferenceSet) {
        if (circularReferenceSet.contains(err)) {
            sb.append(prefix).append(caption).append("\tCIRCULAR REFERENCE ").append(this).append("\n");

            return;
        }

        circularReferenceSet.add(err);

        StackTraceElement[] trace = err.getStackTrace();

        int m = trace.length - 1;
        int n = enclosingTrace.length - 1;
        int framesInCommon = computeFramesInCommon(trace, enclosingTrace, m, n);

        sb.append(prefix).append(caption).append(err).append("\n");

        for (int i = 0; i <= m; ++i) {
            sb.append(prefix).append("\tat ").append(trace[i]).append("\n");
        }

        if (framesInCommon != 0) {
            sb.append(prefix).append("\t... ").append(framesInCommon).append(" more").append("\n");
        }

        Throwable[] suppressed = err.getSuppressed();

        if (suppressed.length != 0) {
            sb.append(prefix).append("Suppressed: ").append("\n");

            for (Throwable se : suppressed) {
                printEnclosedStackTrace(sb, se, trace, "\n" + caption, prefix + "\t", circularReferenceSet);
            }
        }

        Throwable cause = err.getCause();

        if (cause != null) {
            sb.append(prefix).append("Caused by: ").append("\n");

            printEnclosedStackTrace(
                    sb, cause, trace, "\tCaused by: ", prefix + "\t", circularReferenceSet);
        }
    }
}
