package com.lkkdesign.changlong.utils;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * FileName: CommonsMathUtil
 * Author:   huangyaoyu
 * Date:     2019/6/15 21:06
 * Description:
 * History:
 */

public class LeastSquaresMathUtil {
    /**
     * 一元线性拟合 y = a + b*x
     *
     * @param x
     * @param y
     * reuslt[0] = a result[1] = b result[2] 相关系数 result[3] 决定系数
     * result[4] 点数量(长度) result[5] 自由度
     */
    public static double[] lineFitting(Double x[], Double y[]) {
        int size = x.length;
        Double xmean = 0.0;
        Double ymean = 0.0;
        Double xNum = 0.0;
        Double yNum = 0.0;
        Double xyNum = 0.0;
        Double xNum2 = 0.0;
        Double yNum2 = 0.0;
        Double rss = 0.0;
        Double tss = 0.0;
        double result[] = new double[6];

        for (int i = 0; i < size; i++) {
            xmean += x[i];
            ymean += y[i];
            xNum2 += x[i] * x[i];
            yNum2 += y[i] * y[i];
            xyNum += x[i] * y[i];
        }
        xNum = xmean;
        yNum = ymean;
        xmean /= size;
        ymean /= size;

        Double sumx2 = 0.0d;
        Double sumxy = 0.0d;
        for (int i = 0; i < size; i++) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean);
            sumxy += (y[i] - ymean) * (x[i] - xmean);
        }

        Double b = sumxy / sumx2;
        Double a = ymean - b * xmean;

        result[0] = a;
        result[1] = b;
        System.out.println("a = " + a + ", b=" + b);

        Double correlation = (xyNum - xNum * yNum / size)
                / Math.sqrt((xNum2 - xNum * xNum / size) * (yNum2 - yNum * yNum / size));

        System.out.println("相关系数：" + correlation);
        result[2] = correlation;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - (a + b * x[i])) * (y[i] - (a + b * x[i]));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        Double r2 = 1 - (rss / (size - 1 - 1)) / (tss / (size - 1));

        result[3] = r2;
        System.out.println("决定系数：" + r2);

        result[4] = (double) x.length;
        result[5] = (double) x.length - 1 - 1;

        return result;
    }

    /**
     * 多元线性拟合 y = a + b*x1 + c*x2
     *
     * @param x
     * @param y
     *            result[0] = a result[b] = b . . . result[len - 4] 点数 result[len -
     *            3] 自由度 result[len - 2] 残差平方和 result[len - 1] 确定系数
     */
    public static double[] lineFitting2(double x[][], double y[]) {
        double[] a = new double[x.length + 1];
        double[] v = new double[2]; // 这里的2为m
        double[] dt = new double[4];

        line2sqt(x, y, 2, 11, a, dt, v);

        int i;

        System.out.println("残差平方和：" + dt[0]);

        double temp = a[a.length - 1];
        // 更换输出位置，把常数放到第一位
        for (i = a.length - 1; i > 0; i--) {
            a[i] = a[i - 1];
        }

        a[0] = temp;

        double[] result = new double[x.length + 5];

        for (i = 0; i <= x.length; i++) {
            result[i] = a[i];
        }

        result[x.length + 1] = y.length;
        result[x.length + 2] = y.length - x.length;
        result[x.length + 3] = dt[0];
        result[x.length + 4] = getLine2R(x, y, a, x.length);

        System.out.println("校正确定系数：" + result[x.length + 4]);

        return result;
    }

    /**
     * 多项式拟合 y = a + b*x1 + c*x1^2…… result[0] = a result[1] = b . . . result[n + 1]
     * 点数 result[n + 2] 自由度 result[n + 3] 确定系数
     *
     * @param n
     *            几级
     * @return
     */
    public static double[] dxsFitting(double x[], double y[], int n) {
        double result[] = new double[n + 4];

        WeightedObservedPoints obs = new WeightedObservedPoints();

        for (int i = 0; i < x.length; i++) {
            obs.add(x[i], y[i]);
        }

        // Instantiate a third-degree polynomial fitterm.
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n);

        // Retrieve fitted parameters (coefficients of the polynomial function).
        final double[] coeff = fitter.fit(obs.toList());

        for (int i = 0; i < coeff.length; i++) {
            result[i] = coeff[i];
        }

        double s = getDxsR(x, y, coeff, 5);

        System.out.println("确定系数:" + s);

        result[n + 1] = x.length;
        result[n + 2] = x.length - n - 1;
        result[n + 3] = s;

        return result;
    }

    /**
     * 指数拟合 y = b*exp(ax) result[0] = a result[1] = b result[2] 数据点数 result[3] 自由度
     * result[4] 确定系数
     *
     * @param x
     * @param y
     * @return
     */
    public static double[] expFitting(double x[], double y[]) {
        int size = x.length;
        double xmean = 0.0;
        double ymean = 0.0;
        double rss = 0;
        double tss = 0;
        double result[] = new double[5];

        for (int i = 0; i < size; i++) {
            xmean += x[i];
            y[i] = Math.log(y[i]);
            ymean += y[i];
        }
        xmean /= size;
        ymean /= size;

        double sumx2 = 0.0f;
        double sumxy = 0.0f;
        for (int i = 0; i < size; i++) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean);
            sumxy += (y[i] - ymean) * (x[i] - xmean);
        }

        double b = sumxy / sumx2;
        double a = ymean - b * xmean;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - (a + b * x[i])) * (y[i] - (a + b * x[i]));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / (size - 1 - 1)) / (tss / (size - 1));

        System.out.println("决定系数" + r2);

        a = Math.exp(a);

        System.out.println("a = " + a + ";b= " + b);

        result[0] = a;
        result[1] = b;
        result[2] = x.length;
        result[3] = x.length - 2;
        result[4] = r2;

        return result;
    }

    /**
     * 对数拟合 y = ln(a * x + b) result[0] = a result[1] = b result[2] 数据点数 result[3]
     * 自由度 result[4] 确定系数
     *
     * @param x
     * @param y
     */
    public static double[] logFitting(double x[], double y[]) {
        int size = x.length;
        double xmean = 0.0;
        double ymean = 0.0;
        double rss = 0;
        double tss = 0;
        double result[] = new double[5];

        for (int i = 0; i < size; i++) {
            xmean += x[i];
            y[i] = Math.exp(y[i]);
            ymean += y[i];
        }
        xmean /= size;
        ymean /= size;

        double sumx2 = 0.0f;
        double sumxy = 0.0f;
        for (int i = 0; i < size; i++) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean);
            sumxy += (y[i] - ymean) * (x[i] - xmean);
        }

        double b = sumxy / sumx2;
        double a = ymean - b * xmean;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - (b + a * x[i])) * (y[i] - (b + a * x[i]));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / (size - 1 - 1)) / (tss / (size - 1));

        System.out.println("决定系数" + r2);

        System.out.println("a = " + a + ";b= " + b);

        result[0] = a;
        result[1] = b;
        result[2] = x.length;
        result[3] = x.length - 2;
        result[4] = r2;

        return result;

    }

    /**
     * 参考网址：http://wenku.baidu.com/link?url=T-xy9CUR_hVlCpA5o6xjqWb-Z1o5jraGEg-OHEtDgFLEynf6HedV68wMwC5u7RTs7PN5kiYq3qjyiddMveDFywLiuXai11MisNrNvv4KjAW&qq-pf-to=pcqq.c2c
     * 峰拟合 y = y(max) * exp[-(x - x(max))^2/S]
     *
     * @param x
     * @param y
     *            result[0] = x(max) result[1] = y(max) result[2] = S result[3] 点数
     *            result[4] 自由度 result[5] 确定系数
     */
    public static double[] peakFitting(double x[], double y[]) {
        int size = x.length;
        double maxX = 0;
        double maxY = 0;
        double minY = Integer.MAX_VALUE;
        double result[] = new double[6];
        double[][] left = new double[x.length][3];
        double[][] right = new double[x.length][1];

        for (int i = 0; i < size; i++) {
            if (y[i] > maxY) {
                maxX = x[i];
                maxY = y[i];
            }

            if (y[i] < minY) {
                minY = y[i];
            }

            for (int j = 0; j < 3; j++) {
                left[i][j] = getPeakValue(j, x[i]);
            }

            right[i][0] = Math.log(y[i]);
        }

        RealMatrix leftMatrix = MatrixUtils.createRealMatrix(left);
        RealMatrix rightMatrix = MatrixUtils.createRealMatrix(right);
        RealMatrix leftMatrix1 = leftMatrix.transpose();
        RealMatrix m = leftMatrix1.multiply(leftMatrix);

        RealMatrix tMatrix = new LUDecomposition(m).getSolver().getInverse().multiply(leftMatrix1)
                .multiply(rightMatrix);

        result[0] = maxX;
        result[1] = maxY;
        result[2] = -1.0 / tMatrix.getEntry(2, 0);

        System.out.println(result[0] + " " + result[1] + " " + result[2]);
        double aaaa = getPearR(x, y, result, 2);
        System.out.println("确定系数:" + aaaa);

        result[3] = x.length;
        result[4] = x.length - 1 - 2;
        result[5] = aaaa;

        return result;
    }

    /**
     * 用户自定义拟合
     *
     * @param x
     * @param y
     * @param sf
     *            算法 <input type="checkbox" value="0" />常数
     *            <input type="checkbox" value="1" />x
     *            <input type="checkbox" value="2" />x^2
     *            <input type="checkbox" value="3" />x^3
     *            <input type="checkbox" value="4" />exp(x)
     *            <input type="checkbox" value="5" />ln(x)
     *            <input type="checkbox" value="6" />sin(x)
     *            <input type="checkbox" value="7" />cos(x)
     *
     *            result[0] = 第一项系数 result[1] = 第二项系数 . . . result[n] = 第N项系数
     *            result[sf.length] 点数 result[sf.length+1] 自由度 result[sf.length+2]
     *            确定系数
     * @return
     */
    public static double[] userDefineFitting(double x[], double y[], int sf[]) {
        double[][] left = new double[sf.length][sf.length];
        double[] right = new double[sf.length];
        double[] result = new double[sf.length + 3];

        result[sf.length] = x.length;
        boolean containZero = false;

        for (int i = 0; i < sf.length; i++) {
            double yValue = 0;

            // 数值中包括0
            if (sf[i] == 0) {
                containZero = true;
            }

            for (int j = 0; j < sf.length; j++) {
                double xValue = 0;

                // 计算X的的司格马
                for (int k = 0; k < x.length; k++) {
                    xValue += getUserDefineValue(sf[i], x[k]) * getUserDefineValue(sf[j], x[k]);
                }

                left[i][j] = xValue;
            }

            // 计算Y的的司格马
            for (int k = 0; k < x.length; k++) {
                yValue += y[k] * getUserDefineValue(sf[i], x[k]);
            }

            right[i] = yValue;
        }

        // 计算自由度
        result[sf.length + 1] = x.length - sf.length - 1;

        // 计算自由度
        if (containZero) {
            result[sf.length + 1] = x.length - sf.length;
        }

        // 矩阵求解
        RealMatrix leftMatrix = MatrixUtils.createRealMatrix(left);
        DecompositionSolver solver = new LUDecomposition(leftMatrix).getSolver();
        RealVector constants = new ArrayRealVector(right, false);

        RealVector solution = solver.solve(constants);

        System.out.println(solution);

        // 获得系数值
        for (int i = 0; i < solution.getDimension(); i++) {
            result[i] = solution.getEntry(i);
        }

        double rss = 0, tss = 0, ymean = 0;

        for (int i = 0; i < x.length; i++) {
            ymean += y[i];
        }

        ymean /= x.length;

        for (int i = 0; i < x.length; i++) {
            rss += (y[i] - getUserDefineValueByX(sf, x[i], solution))
                    * (y[i] - getUserDefineValueByX(sf, x[i], solution));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / result[sf.length + 1]) / (tss / (x.length - 1));

        System.out.println("决定系数" + r2);

        result[sf.length + 2] = r2;

        return result;
    }

    /**
     * 用户自定义函数，传入X值获得Y值
     *
     * @param n
     * @param x
     * @param solution
     * @return
     */
    private static double getUserDefineValueByX(int[] n, double x, RealVector solution) {
        double value = 0;

        for (int i = 0; i < n.length; i++) {
            value += getUserDefineValue(n[i], x) * solution.getEntry(i);
        }

        return value;
    }

    /**
     * 获得用户自定义的值， <input type="checkbox" value="0" />常数
     * <input type="checkbox" value="1" />x <input type="checkbox" value="2" />x^2
     * <input type="checkbox" value="3" />x^3
     * <input type="checkbox" value="4" />exp(x)
     * <input type="checkbox" value="5" />ln(x)
     * <input type="checkbox" value="6" />sin(x)
     * <input type="checkbox" value="7" />cos(x)
     *
     * @param i
     * @param x
     * @return
     */
    private static double getUserDefineValue(int i, double x) {
        // 常数
        if (i == 0) {
            return 1;
        } else if (i == 1) {
            // x
            return x;
        } else if (i == 2) {
            // x^2
            return x * x;
        } else if (i == 3) {
            // x^3
            return x * x * x;
        } else if (i == 4) {
            // exp(x)
            return Math.pow(Math.E, x);
        } else if (i == 5) {
            // ln(x)
            return Math.log(x);
        } else if (i == 6) {
            // sin(x)
            return Math.sin(x);
        } else if (i == 7) {
            // cos(x)
            return Math.cos(x);
        }

        return 0;
    }

    /**
     * 获得决定系数
     *
     * @param coeff
     * @return
     */
    private static double getPearR(double x[], double y[], double coeff[], int n) {
        int size = x.length;
        double ymean = 0.0;
        double rss = 0;
        double tss = 0;

        for (int i = 0; i < size; i++) {
            ymean += y[i];
        }

        ymean /= size;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - getPeakValueByX(x[i], coeff)) * (y[i] - getPeakValueByX(x[i], coeff));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / (size - n - 1)) / (tss / (size - 1));

        return r2;
    }

    /**
     * 通过X获得Y的值 y = y(max) * exp[-(x - x(max))^2/S]
     *
     * @param x
     * @param result
     *            result[0] = x(max) result[1] = y(max) result[2] = S
     * @return
     */
    private static double getPeakValueByX(double x, double result[]) {
        double b = Math.exp(-Math.pow((x - result[0]), 2) / result[2]) * result[1];

        return b;
    }

    private static double getPeakValue(int i, double x) {
        // 常数
        if (i == 0) {
            return 1;
        } else if (i == 1) {
            return x;
        } else if (i == 2) {
            // x^2
            return x * x;
        }

        return 0;
    }

    /**
     * 获得决定系数
     *
     * @param coeff
     * @return
     */
    private static double getDxsR(double x[], double y[], double coeff[], int n) {
        int size = x.length;
        double ymean = 0.0;
        double rss = 0;
        double tss = 0;

        for (int i = 0; i < size; i++) {
            ymean += y[i];
        }

        ymean /= size;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - getDxsValueByX(x[i], coeff)) * (y[i] - getDxsValueByX(x[i], coeff));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / (size - n - 1)) / (tss / (size - 1));

        return r2;
    }

    /**
     * 通过X获得Y的值
     *
     * @param x
     * @param coeff
     * @return
     */
    private static double getDxsValueByX(double x, double coeff[]) {
        int size = coeff.length;
        double result = coeff[0];

        for (int i = 1; i < size; i++) {
            result += coeff[i] * Math.pow(x, i);
        }

        return result;
    }

    /**
     * 多元线性回归分析
     *
     * @param  x
     *            每一列存放m个自变量的观察值
     * @param y
     *            存放随即变量y的n个观察值
     * @param m
     *            自变量的个数
     * @param n
     *            观察数据的组数
     * @param a
     *            返回回归系数a0,...,am
     * @param dt
     *            dt[0]偏差平方和q,dt[1] 平均标准偏差s dt[2]返回复相关系数r dt[3]返回回归平方和u
     * @param v
     *            返回m个自变量的偏相关系数
     */
    private static void line2sqt(double[][] x, double[] y, int m, int n, double[] a, double[] dt, double[] v) {
        int i, j, k, mm;
        double q, e, u, p, yy, s, r, pp;
        double[] b = new double[(m + 1) * (m + 1)];
        mm = m + 1;
        b[mm * mm - 1] = n;
        for (j = 0; j <= m - 1; j++) {
            p = 0.0;
            for (i = 0; i <= n - 1; i++)
                p = p + x[j][i];
            b[m * mm + j] = p;
            b[j * mm + m] = p;
        }
        for (i = 0; i <= m - 1; i++)
            for (j = i; j <= m - 1; j++) {
                p = 0.0;
                for (k = 0; k <= n - 1; k++)
                    p = p + x[i][k] * x[j][k];
                b[j * mm + i] = p;
                b[i * mm + j] = p;
            }
        a[m] = 0.0;
        for (i = 0; i <= n - 1; i++)
            a[m] = a[m] + y[i];
        for (i = 0; i <= m - 1; i++) {
            a[i] = 0.0;
            for (j = 0; j <= n - 1; j++)
                a[i] = a[i] + x[i][j] * y[j];
        }

        chlk(b, mm, 1, a);

        yy = 0.0;
        for (i = 0; i <= n - 1; i++)
            yy = yy + y[i] / n;
        q = 0.0;
        e = 0.0;
        u = 0.0;
        for (i = 0; i <= n - 1; i++) {
            p = a[m];
            for (j = 0; j <= m - 1; j++)
                p = p + a[j] * x[j][i];
            q = q + (y[i] - p) * (y[i] - p);
            e = e + (y[i] - yy) * (y[i] - yy);
            u = u + (yy - p) * (yy - p);
        }
        s = Math.sqrt(q / n);
        r = Math.sqrt(1.0 - q / e);
        for (j = 0; j <= m - 1; j++) {
            p = 0.0;
            for (i = 0; i <= n - 1; i++) {
                pp = a[m];
                for (k = 0; k <= m - 1; k++)
                    if (k != j)
                        pp = pp + a[k] * x[k][i];
                p = p + (y[i] - pp) * (y[i] - pp);
            }
            v[j] = Math.sqrt(1.0 - q / p);
        }
        dt[0] = q;
        dt[1] = s;
        dt[2] = r;
        dt[3] = u;
    }

    private static int chlk(double[] a, int n, int m, double[] d) {
        int i, j, k, u, v;
        if ((a[0] + 1.0 == 1.0) || (a[0] < 0.0)) {
            System.out.println("fail\n");
            return (-2);
        }
        a[0] = Math.sqrt(a[0]);
        for (j = 1; j <= n - 1; j++)
            a[j] = a[j] / a[0];
        for (i = 1; i <= n - 1; i++) {
            u = i * n + i;
            for (j = 1; j <= i; j++) {
                v = (j - 1) * n + i;
                a[u] = a[u] - a[v] * a[v];
            }
            if ((a[u] + 1.0 == 1.0) || (a[u] < 0.0)) {
                System.out.println("fail\n");
                return (-2);
            }
            a[u] = Math.sqrt(a[u]);
            if (i != (n - 1)) {
                for (j = i + 1; j <= n - 1; j++) {
                    v = i * n + j;
                    for (k = 1; k <= i; k++)
                        a[v] = a[v] - a[(k - 1) * n + i] * a[(k - 1) * n + j];
                    a[v] = a[v] / a[u];
                }
            }
        }
        for (j = 0; j <= m - 1; j++) {
            d[j] = d[j] / a[0];
            for (i = 1; i <= n - 1; i++) {
                u = i * n + i;
                v = i * m + j;
                for (k = 1; k <= i; k++)
                    d[v] = d[v] - a[(k - 1) * n + i] * d[(k - 1) * m + j];
                d[v] = d[v] / a[u];
            }
        }
        for (j = 0; j <= m - 1; j++) {
            u = (n - 1) * m + j;
            d[u] = d[u] / a[n * n - 1];
            for (k = n - 1; k >= 1; k--) {
                u = (k - 1) * m + j;
                for (i = k; i <= n - 1; i++) {
                    v = (k - 1) * n + i;
                    d[u] = d[u] - a[v] * d[i * m + j];
                }
                v = (k - 1) * n + k - 1;
                d[u] = d[u] / a[v];
            }
        }
        return (2);
    }

    /**
     * 获得相关系数
     *
     * @return
     */
    private static double getLine2R(double x[][], double y[], double a[], int n) {
        int size = x[0].length;
        double ymean = 0.0;
        double rss = 0;
        double tss = 0;

        for (int i = 0; i < size; i++) {
            ymean += y[i];
        }

        ymean /= size;

        for (int i = 0; i < size; i++) {
            rss += (y[i] - getLine2ValueByX(x, a, i)) * (y[i] - getLine2ValueByX(x, a, i));
            tss += (y[i] - ymean) * (y[i] - ymean);
        }

        double r2 = 1 - (rss / (size - n - 1)) / (tss / (size - 1));

        return r2;
    }

    /**
     * 通过X获得Y的值
     *
     * @param x
     * @return
     */
    private static double getLine2ValueByX(double x[][], double a[], int n) {
        int size = a.length;
        double result = a[0];

        for (int i = 1; i < size; i++) {
            result += x[i - 1][n] * a[i];
        }

        return result;
    }
}