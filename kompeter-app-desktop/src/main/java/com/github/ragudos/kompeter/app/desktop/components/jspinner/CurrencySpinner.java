/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.jspinner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class CurrencySpinner extends JSpinner {
    public CurrencySpinner() {
        super(new CurrencySpinnerModel(new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal(Double.MAX_VALUE),
                new BigDecimal("0.25")));

        final NumberEditor editor = new NumberEditor(this);

        setEditor(editor);

        final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.of("tl", "ph"));

        if (format instanceof final DecimalFormat df) {
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
        }

        final NumberFormatter formatter = new NumberFormatter(format);

        formatter.setValueClass(BigDecimal.class);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(false);
        formatter.setOverwriteMode(false);

        editor.getTextField().setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    private static class CurrencySpinnerModel extends SpinnerNumberModel {
        private final BigDecimal max;
        private final BigDecimal min;
        private final BigDecimal stepSize;
        private BigDecimal value;

        public CurrencySpinnerModel(final BigDecimal value, final BigDecimal min, final BigDecimal max,
                final BigDecimal stepSize) {
            this.value = value;
            this.min = min;
            this.max = max;
            this.stepSize = stepSize;
        }

        @Override
        public Object getNextValue() {
            final BigDecimal next = value.add(stepSize);
            return (max != null && next.compareTo(max) > 0) ? null : next;
        }

        @Override
        public Object getPreviousValue() {
            final BigDecimal prev = value.subtract(stepSize);
            return (min != null && prev.compareTo(min) < 0) ? null : prev;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public void setValue(final Object newValue) {
            if (newValue == null)
                throw new IllegalArgumentException("null");
            final BigDecimal v = (newValue instanceof BigDecimal)
                    ? (BigDecimal) newValue
                    : new BigDecimal(newValue.toString());
            if (!v.equals(value)) {
                value = v;
                fireStateChanged();
            }
        }
    }
}
