/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.util.List;

/**
 * @author Hanz Mapua
 */
public interface LinearRegression {
    Double AllXMinXMeanSquared(List<Double> XValues);

    Double AllXXMeanYYMean(List<Double> XValues, List<Double> YValues);

    Double AllYMinYMeanSquared(List<Double> YValues);

    Double LinearRegression(List<Double> XValues, List<Double> YValues, Double XValue);

    Double Pearson(List<Double> XValues, List<Double> YValues);

    Double Slope(List<Double> XValues, List<Double> YValues);

    Double StandardDeviationX(List<Double> XValues);

    Double StandardDeviationY(List<Double> YValues);

    Double XMean(List<Double> XValues);

    Double XMinXMean(List<Double> XValues, Double XValue);

    Double XMinXMeanSquared(List<Double> XValues, Double XValue);

    Double XXMeanYYMean(List<Double> XValues, List<Double> YValues, Double XValue, Double YValue);

    Double YIntercept(List<Double> XValues, List<Double> YValues);

    Double YMean(List<Double> YValues);

    Double YMinYMean(List<Double> YValues, Double YValue);

    Double YMinYMeanSquared(List<Double> YValues, Double YValue);
}
