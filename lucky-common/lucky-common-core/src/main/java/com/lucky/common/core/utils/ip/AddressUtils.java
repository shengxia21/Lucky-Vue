package com.lucky.common.core.utils.ip;

import com.fasterxml.jackson.databind.JsonNode;
import com.lucky.common.core.config.LuckyConfig;
import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.utils.JsonUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
@Slf4j
public class AddressUtils {

    // IP地址查询
    public static final String IP_URL = "https://whois.pconline.com.cn/ipJson.jsp";
    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (LuckyConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JsonNode obj = JsonUtils.parseObject(rspStr);
                String region = obj.get("pro").asText();
                String city = obj.get("city").asText();
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }

}
