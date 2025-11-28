package com.lucky.common.utils;

/**
 * 脱敏工具类
 *
 * @author lucky
 */
public class DesensitizedUtil {

    /**
     * 密码的全部字符都用*代替，比如：******
     *
     * @param password 密码
     * @return 脱敏后的密码
     */
    public static String password(String password) {
        if (StringUtils.isBlank(password)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * 车牌中间用*代替，如果是错误的车牌，不处理
     *
     * @param carLicense 完整的车牌号
     * @return 脱敏后的车牌
     */
    public static String carLicense(String carLicense) {
        if (StringUtils.isBlank(carLicense)) {
            return StringUtils.EMPTY;
        }
        // 普通车牌
        if (carLicense.length() == 7) {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        } else if (carLicense.length() == 8) {
            // 新能源车牌
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }

    /**
     * AI大模型的 API 密钥，显示前5位和后3位字符，其他全部用*代替
     *
     * @param apiKey AI大模型的 API 密钥
     * @return 脱敏后的 API 密钥
     */
    public static String aiApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            return StringUtils.EMPTY;
        }
        if (apiKey.length() < 10) {
            return apiKey;
        }
        return StringUtils.hide(apiKey, 5, apiKey.length() - 3);
    }

}
