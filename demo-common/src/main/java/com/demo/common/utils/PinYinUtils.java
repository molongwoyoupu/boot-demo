package com.demo.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

/**
 * 拼音工具类
 *
 * @author molong
 * @date 2021/9/6
 */
public class PinYinUtils {
    /**
     * @param china (字符串 汉字)
     * @return 汉字转拼音 其它字符不变
     */
    public static String getPinyin(String china){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        StringBuilder result = new StringBuilder();
        try {
            for (char ti : arrays) {
                //匹配是否是中文
                if (Character.toString(ti).matches("[\\u4e00-\\u9fa5]")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti, format);
                    result.append(temp[0]);
                } else {
                    result.append(ti);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * 获取字符串中第一个字符的英文大写形式
     * 如果结果是非英文字符则返回#
     * @param china (字符串 )
     * @return 一个大写字符
     */
    public static String getFirstUpperCase(String china) {
        if (StringUtils.isEmpty(china)) {
            return "#";
        }
        String pinyin = getPinyin(china);
        char first = pinyin.toUpperCase().charAt(0);
        if(Character.isLetter(first) || Character.isDigit(first)){
            return String.valueOf(first);
        }else{
            return "#";
        }
    }

    public static void main(String[] args) {
        System.out.println(getFirstUpperCase("啥开发局"));
    }
}

