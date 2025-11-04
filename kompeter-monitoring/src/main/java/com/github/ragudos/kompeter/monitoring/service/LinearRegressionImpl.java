/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public class LinearRegressionImpl implements ILinearRegression {

    public static void main(final String[] args) {
        final LinearRegressionImpl LR = new LinearRegressionImpl();
        final List<Double> numberX = new ArrayList<>();
        numberX.add(1.00);
        numberX.add(2.00);
        numberX.add(3.00);

        final List<Double> numberY = new ArrayList<>();
        numberY.add(10.00);
        numberY.add(20.00);
        numberY.add(30.00);

        System.out
                .println("Linear Regression Final Boss(New Y Value): " + LR.LinearRegression(numberX, numberY, 10.00));
        System.out.println("Slope: " + LR.Slope(numberX, numberY));
        System.out.println("Y-intercept: " + LR.YIntercept(numberX, numberY));
        System.out.println("Pearson: " + LR.Pearson(numberX, numberY));
        System.out.println("Standard Deviation of X: " + LR.StandardDeviationX(numberX));
        System.out.println("Standard Deviation of Y: " + LR.StandardDeviationY(numberY));
        System.out.println("Sum of all Y-YMean Squared: " + LR.AllYMinYMeanSquared(numberY));
        System.out.println("Sum of all X-XMean Squared:" + LR.AllXMinXMeanSquared(numberX));
        System.out.println("Sum of all X-XMean*Y-YMean: " + LR.AllXXMeanYYMean(numberX, numberY));
        System.out.println("Y-YMean Squared: " + LR.YMinYMeanSquared(numberY, numberY.get(2)));
        System.out.println("X-XMean Squared: " + LR.XMinXMeanSquared(numberX, numberX.get(2)));
        System.out.println("X-XMean*Y-YMean: " + LR.XXMeanYYMean(numberX, numberY, numberX.get(2), numberY.get(2)));
        System.out.println("Y-YMean: " + LR.YMinYMean(numberY, numberY.get(2)));
        System.out.println("X-XMean: " + LR.XMinXMean(numberX, numberX.get(2)));
        System.out.println("XMean: " + LR.XMean(numberX));
        System.out.println("YMean: " + LR.YMean(numberY));
    }

    @Override
    public Double AllXMinXMeanSquared(final List<Double> XValues) {
        Double result = 0.00;
        for (int i = 0; i < XValues.size(); i++) {
            result += XMinXMeanSquared(XValues, XValues.get(i));
        }

        return result;
    }

    @Override
    public Double AllXXMeanYYMean(final List<Double> XValues, final List<Double> YValues) {
        Double result = 0.00;
        for (int i = 0; i < XValues.size() && i < YValues.size(); i++) {
            result += XXMeanYYMean(XValues, YValues, XValues.get(i), YValues.get(i));
        }

        return result;
    }

    @Override
    public Double AllYMinYMeanSquared(final List<Double> YValues) {
        Double result = 0.00;
        for (int i = 0; i < YValues.size(); i++) {
            result += YMinYMeanSquared(YValues, YValues.get(i));
        }

        return result;
    }

    @Override
    public Double LinearRegression(final List<Double> XValues, final List<Double> YValues, final Double XValue) {
        return YIntercept(XValues, YValues) + (Slope(XValues, YValues) * XValue);
    }

    @Override
    public Double Pearson(final List<Double> XValues, final List<Double> YValues) {
        final Double allXXMeanYYMean = AllXXMeanYYMean(XValues, YValues);
        final Double allXXMeanSquared = AllXMinXMeanSquared(XValues);
        final Double allYYMeanSquared = AllYMinYMeanSquared(YValues);
        final Double multipliedAllXXAllYYSquared = (allXXMeanSquared * allYYMeanSquared);
        final Double sqrtXXSquaredYYSquared = Math.sqrt(multipliedAllXXAllYYSquared);
        final Double finalResult = (allXXMeanYYMean / sqrtXXSquaredYYSquared);
        return finalResult;
    }

    @Override
    public Double Slope(final List<Double> XValues, final List<Double> YValues) {
        final Double pearson = Pearson(XValues, YValues);
        final Double SdYDivSdX = StandardDeviationY(YValues) / StandardDeviationX(XValues);

        return pearson * SdYDivSdX;
    }

    // @Override
    public Double StandardDeviationX(final List<Double> XValues) {
        final Double sumDivNMin1 = AllXMinXMeanSquared(XValues) / (XValues.size() - 1);

        return Math.sqrt(sumDivNMin1);
    }

    @Override
    public Double StandardDeviationY(final List<Double> YValues) {
        final Double sumDivNMin1 = AllYMinYMeanSquared(YValues) / (YValues.size() - 1);

        return Math.sqrt(sumDivNMin1);
    }

    @Override
    public Double XMean(final List<Double> XValues) {
        Double sumOfX = 0.00;
        for (int i = 0; i < XValues.size(); i++) {
            sumOfX += XValues.get(i);
        }
        final Double XMean = sumOfX / XValues.size();
        return XMean;
    }

    @Override
    public Double XMinXMean(final List<Double> XValues, final Double XValue) {
        return XValue - XMean(XValues);
    }

    @Override
    public Double XMinXMeanSquared(final List<Double> XValues, final Double XValue) {
        return XMinXMean(XValues, XValue) * XMinXMean(XValues, XValue);
    }

    @Override
    public Double XXMeanYYMean(final List<Double> XValues, final List<Double> YValues, final Double XValue,
            final Double YValue) {
        return XMinXMean(XValues, XValue) * YMinYMean(YValues, YValue);
    }

    @Override
    public Double YIntercept(final List<Double> XValues, final List<Double> YValues) {
        return YMean(YValues) - (Slope(XValues, YValues) * XMean(XValues));
    }

    @Override
    public Double YMean(final List<Double> YValues) {
        Double sumOfY = 0.00;
        for (int i = 0; i < YValues.size(); i++) {
            sumOfY += YValues.get(i);
        }
        final Double YMean = (double) sumOfY / YValues.size();
        return YMean;
    }

    @Override
    public Double YMinYMean(final List<Double> YValues, final Double YValue) {
        return YValue - YMean(YValues);
    }

    @Override
    public Double YMinYMeanSquared(final List<Double> YValues, final Double YValue) {
        return YMinYMean(YValues, YValue) * YMinYMean(YValues, YValue);
    }
}
