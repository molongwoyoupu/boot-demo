package com.demo.common.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 统计的工具类
 *
 * @author molong
 * @date 2021/9/6
 */
public class StatisticUtils {
    public static Integer NUMBER_ONE = 1;
    public static Integer NUMBER_ZERO = 0;
    public static Integer NUMBER_TWO = 2;
    public static Integer NUMBER_THREE = 3;
    public static Integer NUMBER_SIX = 6;
    public static Integer NUMBER_NINE = 9;
    public static Integer NUMBER_TEN = 10;
    public static Integer NUMBER_60 = 60;
    /**
     * 分钟数转小时数
     *
     * @param minutes 分钟数
     * @return 小时数
     */
    public static BigDecimal getHoursByMinutes(int minutes) {
        //判断是否整除
        int last = minutes % NUMBER_60;
        if (last == 0) {
            //整除
            return new BigDecimal(minutes).divide(new BigDecimal(NUMBER_60), RoundingMode.HALF_UP);
        } else {
            //不整除,保留一位小数
            return new BigDecimal(minutes).divide(new BigDecimal(NUMBER_60),
                    NUMBER_ONE, RoundingMode.HALF_UP);
        }
    }

    /**
     * 获取比率(保留一位小数)
     *
     * @param upNum   数量
     * @param downNum 被除以数量
     * @return 两数比率
     */
    public static BigDecimal getRate(int upNum, int downNum) {
        //保留一位小数
        return getRateWithScale(upNum, downNum, NUMBER_ONE);
    }

    /**
     * 获取比率
     *
     * @param upNum   数量
     * @param downNum 被除以数量
     * @param scale   保留小数位数
     * @return 两数比率
     */
    public static BigDecimal getRateWithScale(int upNum, int downNum, int scale) {
        //数量为0,比率为0%
        if (upNum == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        //被除以数量为0,比率为100%
        if (downNum == 0) {
            return new BigDecimal(100).setScale(scale, RoundingMode.HALF_UP);
        }
        return new BigDecimal(upNum).multiply(new BigDecimal(100))
                .divide(new BigDecimal(downNum), scale, RoundingMode.HALF_UP);
    }

    /**
     * 比较获取增长或下降趋势(保留一位小数)
     *
     * @param current 当前的值
     * @param last    之前的值
     * @return 增长或下降趋势
     */
    public static Pair<Integer, BigDecimal> getTrendRate(int current, int last) {
        //趋势 0-上升 1-下降 -1-持平
        int trend;
        //变化的比率
        BigDecimal rate;
        if (current > last) {
            trend = 0;
            rate = getRate(current - last, last);
        } else if (current == last) {
            trend = -1;
            rate = getRate(current - last, last);
        } else {
            trend = 1;
            rate = getRate(last - current, last);
        }
        return Pair.of(trend, rate);
    }

    /**
     * 比较获取增长或下降趋势
     *
     * @param current 当前的值
     * @param last    之前的值
     * @param scale   保留小数位位数
     * @return 增长或下降趋势
     */
    public static Pair<Integer, BigDecimal> getTrendRate(int current, int last, int scale) {
        //趋势 0-上升 1-下降 -1-持平
        int trend;
        //变化的比率
        BigDecimal rate;
        if (current > last) {
            trend = 0;
            rate = getRateWithScale(current - last, last, scale);
        } else if (current == last) {
            trend = -1;
            rate = getRateWithScale(current - last, last, scale);
        } else {
            trend = 1;
            rate = getRateWithScale(last - current, last, scale);
        }
        return Pair.of(trend, rate);
    }

    /**
     * 比较获取增长或下降趋势和变化数量(保留一位小数)
     *
     * @param current 当前的值
     * @param last    之前的值
     * @return 增长或下降趋势和变化数量
     */
    public static Pair<Integer, Pair<Integer, BigDecimal>> getTrendRateAndNum(int current, int last) {
        //趋势 0-上升 1-下降 -1-持平
        int trend;
        //变化的比率
        BigDecimal rate;
        //变化的数量
        int changeNum;
        if (current > last) {
            trend = 0;
            changeNum = current - last;
        } else if (current == last) {
            trend = -1;
            changeNum = current - last;
        } else {
            trend = 1;
            changeNum = last - current;
        }
        rate = getRate(changeNum, last);
        return Pair.of(trend, Pair.of(changeNum, rate));
    }
}
