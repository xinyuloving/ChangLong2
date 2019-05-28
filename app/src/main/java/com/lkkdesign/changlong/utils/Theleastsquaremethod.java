package com.lkkdesign.changlong.utils;

import java.text.DecimalFormat;

/**
 * 最小二乘法 y=ax+b
 *
 * @author huangyaoyu
 *
 */
public class Theleastsquaremethod {

    private static double a;

    private static double b;

    private static int num;

    /**
     * 训练
     *
     * @param x
     * @param y
     */
    public static void train(double x[], double y[]) {
        num = x.length < y.length ? x.length : y.length;
        calCoefficientes(x, y);
    }

    /**
     * a=(NΣxy-ΣxΣy)/(NΣx^2-(Σx)^2) b=y(平均)-a*x（平均）
     *
     * @param x
     * @param y
     * @return
     */
    public static void calCoefficientes(double x[], double y[]) {
        double xy = 0.0, xT = 0.0, yT = 0.0, xS = 0.0;
        for (int i = 0; i < num; i++) {
            xy += x[i] * y[i];
            xT += x[i];
            yT += y[i];
            xS += Math.pow(x[i], 2.0);
        }
        a = (num * xy - xT * yT) / (num * xS - Math.pow(xT, 2.0));
        b = yT / num - a * xT / num;
    }

    /**
     * 预测
     *
     * @param xValue
     * @return
     */
    public static double predict(double xValue) {
        System.out.println("线性回归系数a=" + a);
        System.out.println("线性回归系数b=" + b);
        System.out.println("线性方程C="+a+"K"+ b);
        return a * xValue + b;
    }

    public static void main(String args[]) {
        double[] x = {0.0904,0.166,0.2453,0.3327,0.3969,0.4987,0.7102,0.8803,1.2546};
        double[] y = {0,0.2,0.4,0.6,0.8,1,1.5,2,3};
        Theleastsquaremethod.train(x, y);
        DecimalFormat df=new DecimalFormat("#,##0.0000");//格式化设置，保留四个小数
        System.out.println("K的值：240");
        System.out.println("经线性回归后的C值："+df.format(Theleastsquaremethod.predict(240.0)));
    }

}