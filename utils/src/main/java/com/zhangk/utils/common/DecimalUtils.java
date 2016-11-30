package com.zhangk.utils.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zhangkui on 16/6/7.
 */
public class DecimalUtils {
    private final static DecimalFormat DF_2 = new DecimalFormat("#,##0.00");
    private final static DecimalFormat D_2 = new DecimalFormat("0.00");
    private final static DecimalFormat DF_0 = new DecimalFormat("#,###");
    private final static DecimalFormat DF_1 = new DecimalFormat("#,###.################################");

    public static final RoundingMode ROUNDING_MODE_FOR_FORMAT_UTIL =RoundingMode.HALF_UP;

    /**
     * 带逗号,四舍五入保留两位小数
     * @param num
     * @return
     */
    public static String formatTo2DecimalPlaces(BigDecimal num){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        DF_2.setRoundingMode(ROUNDING_MODE_FOR_FORMAT_UTIL);
        return DF_2.format(num);
    }

    /**
     * 不带逗号,四舍五入保留两位小数
     * @param num
     * @return
     */
    public static String formatTo2Decimal(BigDecimal num){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        D_2.setRoundingMode(ROUNDING_MODE_FOR_FORMAT_UTIL);
        return D_2.format(num);
    }

    /**
     * 取整四舍五入取值
     * @param num
     * @return
     */
    public static String formatToNoZero(BigDecimal num){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        DF_1.setRoundingMode(ROUNDING_MODE_FOR_FORMAT_UTIL);
        return DF_1.format(num);
    }

    /**
     * 四舍五入取整,带逗号
     * @param num
     * @return
     */
    public static String formatTo0DecimalPlaces(BigDecimal num){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        DF_0.setRoundingMode(ROUNDING_MODE_FOR_FORMAT_UTIL);
        return DF_0.format(num);
    }

    public static String formatToPatternDecimalPlaces(BigDecimal num,String pattern){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        return formatToInputDecimalPlaces(num,findDecimalPlaces(pattern));
    }

    public static String formatToInputDecimalPlaces(BigDecimal num, int places){
        if(num==null){
            num=BigDecimal.ZERO;
        }
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(places);
        df.setMinimumFractionDigits(places);
        df.setRoundingMode(ROUNDING_MODE_FOR_FORMAT_UTIL);
        return df.format(num);
    }

    public static String readValue(String value){
        return value.replaceAll(",", "");
    }

    public static int findDecimalPlaces(String pattern){
        int decimalPlaces=0;
        if(pattern!=null && !pattern.contains("1.0")){
            int idx=pattern.indexOf(".");
            while(idx < pattern.length()&&pattern.charAt(idx)!='1'){
                idx++;
                decimalPlaces++;
            }
        }
        return decimalPlaces;
    }
}
