package com.zhangk.utils.common;


/**
 * Created by zhangkui on 2016/11/22.
 */
public class IRR {

    public static double irr(double[] income) {
        return getIRR(income, 0.1D);
    }


    public static double getIRR(final double[] cashFlows, final double estimatedResult) {
        double result = Double.NaN;
        if (cashFlows != null && cashFlows.length > 0) {
// check if business startup costs is not zero:
            if (cashFlows[0] != 0.0) {
                final double noOfCashFlows = cashFlows.length;
                double sumCashFlows = 0.0;
// check if at least 1 positive and 1 negative cash flow exists:
                int noOfNegativeCashFlows = 0;
                int noOfPositiveCashFlows = 0;
                for (int i = 0; i < noOfCashFlows; i++) {
                    sumCashFlows += cashFlows[i];
                    if (cashFlows[i] > 0) {
                        noOfPositiveCashFlows++;
                    } else if (cashFlows[i] < 0) {
                        noOfNegativeCashFlows++;
                    }
                }
// at least 1 negative and 1 positive cash flow available?
                if (noOfNegativeCashFlows > 0 && noOfPositiveCashFlows > 0) {
// set estimated result:
                    double irrGuess = 0.1; // default: 10%
                    if (!Double.isNaN(estimatedResult)) {
                        irrGuess = estimatedResult;
                        if (irrGuess <= 0.0)
                            irrGuess = 0.5;
                    }
// initialize first IRR with estimated result:
                    double irr = 0.0;
                    if (sumCashFlows < 0) { // sum of cash flows negative?
                        irr = -irrGuess;
                    } else { // sum of cash flows not negative
                        irr = irrGuess;
                    }
// iteration:
// the smaller the distance, the smaller the interpolation
// error
                    final double minDistance = 1E-15;
// business startup costs
                    final double cashFlowStart = cashFlows[0];
                    final int maxIteration = 100;
                    boolean wasHi = false;
                    double cashValue = 0.0;
                    for (int i = 0; i <= maxIteration; i++) {
// calculate cash value with current irr:
                        cashValue = cashFlowStart; // init with startup costs
// for each cash flow
                        for (int j = 1; j < noOfCashFlows; j++) {
                            cashValue += cashFlows[j] / Math.pow(1.0 + irr, j);
                        }
// cash value is nearly zero
                        if (Math.abs(cashValue) < 0.01) {
                            result = irr;
                            break;
                        }
// adjust irr for next iteration:
// cash value > 0 => next irr > current irr
                        if (cashValue > 0.0) {
                            if (wasHi) {
                                irrGuess /= 2;
                            }
                            irr += irrGuess;
                            if (wasHi) {
                                irrGuess -= minDistance;
                                wasHi = false;
                            }
                        } else {// cash value < 0 => next irr < current irr
                            irrGuess /= 2;
                            irr -= irrGuess;
                            wasHi = true;
                        }
// estimated result too small to continue => end
// calculation
                        if (irrGuess <= minDistance) {
                            result = irr;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}
