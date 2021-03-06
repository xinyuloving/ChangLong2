package com.lkkdesign.changlong.utils;

import java.text.DecimalFormat;

/**
 * 作者：HuangYaoYu
 * 功能：最小二乘法 线性回归（Least squares）
 *
 * y = a x + b b = sum( y ) / n - a * sum( x ) / n a = ( n * sum( xy ) - sum( x
 * ) * sum( y ) ) / ( n * sum( x^2 ) - sum(x) ^ 2 )
 *
 */
public class LeastSquares {

    /**
     * 作者：HuangYaoYu
     * 功能：main()测试线性回归的最小二乘法java实现函数
     */
    public static void main(String[] args) {
        Double[] x ={0.0904,0.166,0.2453,0.3327,0.3969,0.4987,0.7102,0.8803,1.2546};
        Double[] y ={0d,0.2,0.4,0.6,0.8,1d,1.5,2d,3d};
        System.out.println("经线性回归后的y值：\t" + estimate(x, y, 240d));
    }

    /**
     * 作者：HuangYaoYu
     * 功能：返回估计的y值
     */
    public static String estimate(Double[] x, Double[] y, Double input) {
        Double a = getA(x, y);
        Double b = getB(x, y);
        DecimalFormat df=new DecimalFormat("#,##0.0000");//格式化设置，保留四个小数
        System.out.println("y=" + df.format(a) + "x" + "+" + df.format(b));
//		System.out.println("线性回归系数a值：\t" + a + "\n" + "线性回归系数b值：\t" + b);
        System.out.println("线性回归系数a值：\t" + df.format(a) + "\n" + "线性回归系数b值：\t" + df.format(b));
        return df.format(a * input + b);
    }

    /**
     * 作者：HuangYaoYu
     * 功能：返回x的系数a 公式：a = ( n sum( xy ) - sum( x ) sum( y ) ) / ( n
     * sum( * x^2 ) - sum(x) ^ 2 )
     */
    public static Double getA(Double[] x, Double[] y) {
        int n = x.length;
        return (Double) ((n * pSum(x, y) - sum(x) * sum(y)) / (n * sqSum(x) - Math.pow(sum(x), 2)));
    }

    /**
     * 作者：HuangYaoYu
     * 功能：返回常量系数系数b 公式：b = sum( y ) / n - a sum( x ) / n
     */
    public static Double getB(Double[] x, Double[] y) {
        int n = x.length;
        Double a = getA(x, y);
        return sum(y) / n - a * sum(x) / n;
    }

    /**
     * 作者：HuangYaoYu
     * 功能：求和
     */
    private static Double sum(Double[] ds) {
        Double s = 0d;
        for (Double d : ds) {
            s = s + d;
        }
        return s;
    }

    /**
     * 作者：HuangYaoYu
     * 功能：求平方和
     */
    private static Double sqSum(Double[] ds) {
        Double s = 0d;
        for (Double d : ds) {
            s = (Double) (s + Math.pow(d, 2));
        }
        return s;
    }

    /**
     * 作者：HuangYaoYu
     * 功能：返回对应项相乘后的和
     */
    private static Double pSum(Double[] x, Double[] y) {
        Double s = 0d;
        for (int i = 0; i < x.length; i++) {
            s = s + x[i] * y[i];
        }
        return s;
    }

}
