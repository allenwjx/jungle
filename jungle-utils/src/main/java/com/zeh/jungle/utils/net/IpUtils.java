package com.zeh.jungle.utils.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ip地址转换工具类
 * 
 * @author allen
 * @version $Id: IpUtils.java, v 0.1 2016年3月1日 上午12:11:36 allen Exp $
 */
public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     */
    private IpUtils() {
    }

    /**
     * 字符串转换为数字
     *
     * @param ip ip地址
     * @return 0 ~ 4294967295的数字，如果ip地址格式错误，返回-1
     */
    public static long ipToLong(String ip) {
        if (ip == null || "".equals(ip.trim())) {
            return -1;
        }

        String[] segment = ip.trim().split("[.]");
        if (segment.length != 4) {
            return -1;
        }

        long[] ips = new long[4];
        for (int i = 0; i < segment.length; i++) {
            ips[i] = Integer.parseInt(segment[i]);
            if (ips[i] < 0 || ips[i] > 255) {
                return -1;
            }
        }
        return ipToLong(ips);
    }

    /**
     * ip字符串根据点号分隔的数组转换为数字
     *
     * @param ip long类型数组，长度为4
     * @return 数字格式的ip地址
     */
    public static long ipToLong(long[] ip) {
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 数字转换为字符串
     *
     * @param ip 数字格式的ip
     * @return 字符串格式的ip地址，例如127.0.0.1。如果超出范围(0 ~ 4294967295)，返回null。
     */
    public static String longToIp(long ip) {
        if (ip < 0 || ip > 4294967295L) {
            return null;
        }

        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf(ip >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(ip & 0x000000FF));

        return sb.toString();
    }

    public static InetAddress getInetAddress() {

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
        return null;

    }

    public static String getLocalHostIp() {
        InetAddress netAddress = getInetAddress();
        if (null == netAddress) {
            return "";
        }
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;
    }

    public static String getLocalHostName() {
        InetAddress netAddress = getInetAddress();
        if (null == netAddress) {
            return null;
        }
        String name = netAddress.getHostName(); //get the host address  
        return name;
    }

    public static String getHostIp(InetAddress netAddress) {
        if (null == netAddress) {
            return null;
        }
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;
    }

    public static String getHostName(InetAddress netAddress) {
        if (null == netAddress) {
            return null;
        }
        String name = netAddress.getHostName(); //get the host address  
        return name;
    }
}
