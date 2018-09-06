package com.ndsc.myClickCal.util;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP工具包
 * @author Michael
 * @since 2018-8-28
 */
public class HttpUtil {

    //获取用户真实IP
    public static String getIp(HttpServletRequest request)
    {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("http_client_ip");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        // 如果是多级代理，那么取第一个ip为客户ip
//        if (ip != null && ip.indexOf(",") != -1) {
//            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
//        }
//        return ip;
        //由于同一个公司的外网ip是同一个，不利于计算访问量，所有用sessionid来代替
        return request.getSession().getId();
    }
}
