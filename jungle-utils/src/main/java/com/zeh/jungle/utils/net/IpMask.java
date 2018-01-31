package com.zeh.jungle.utils.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author allen
 * @version $Id: IpMask.java, v 0.1 2016年3月1日 上午12:13:30 allen Exp $
 */
public class IpMask {
    private Inet4Address i4addr;
    private byte         maskCtr;
    private int          addrInt;
    private int          maskInt;

    public IpMask(Inet4Address i4addr, byte mask) {
        this.i4addr = i4addr;
        this.maskCtr = mask;

        this.addrInt = addrToInt(i4addr);
        this.maskInt = ~((1 << (32 - maskCtr)) - 1);
    }

    /**
     * IPMask factory method.
     * 
     * @param addrSlashMask
     *            IP/Mask String in format "nnn.nnn.nnn.nnn/mask". If the
     *            "/mask" is omitted, "/32" (just the single address) is
     *            assumed.
     * @return a new IPMask
     * @throws UnknownHostException
     *             if address part cannot be parsed by InetAddress
     */
    public static IpMask getIPMask(String addrSlashMask) throws UnknownHostException {
        int pos = addrSlashMask.indexOf('/');
        String addr;
        byte maskCtr;
        if (pos == -1) {
            addr = addrSlashMask;
            maskCtr = 32;
        } else {
            addr = addrSlashMask.substring(0, pos);
            maskCtr = Byte.parseByte(addrSlashMask.substring(pos + 1));
        }
        return new IpMask((Inet4Address) InetAddress.getByName(addr), maskCtr);
    }

    /**
     * Test given IPv4 address against this IPMask object.
     * 
     * @param testAddr
     *            address to check.
     * @return true if address is in the IP Mask range, false if not.
     */
    public boolean matches(Inet4Address testAddr) {
        int testAddrInt = addrToInt(testAddr);
        int lowest = addrInt & maskInt;
        int highest = lowest + (~maskInt);
        return (testAddrInt >= lowest) && (testAddrInt <= highest);
        //      return ((addrInt & maskInt) == (testAddrInt & maskInt));
    }

    /**
     * Convenience method that converts String host to IPv4 address.
     * 
     * @param addr
     *            IP address to match in nnn.nnn.nnn.nnn format or hostname.
     * @return true if address is in the IP Mask range, false if not.
     * @throws UnknownHostException
     *             if the string cannot be decoded.
     */
    public boolean matches(String addr) throws UnknownHostException {
        return matches((Inet4Address) InetAddress.getByName(addr));
    }

    /**
     * Converts IPv4 address to integer representation.
     */
    private static int addrToInt(Inet4Address i4addr) {
        byte[] ba = i4addr.getAddress();

        return ((ba[0] << 24) & 0xFF000000) | ((ba[1] << 16) & 0xFF0000) | ((ba[2] << 8) & 0xFF00) | (ba[3] & 0xFF);
    }

    @Override
    public String toString() {
        return "IPMask(" + i4addr.getHostAddress() + "/" + maskCtr + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IpMask that = (IpMask) obj;
        return (this.addrInt == that.addrInt && this.maskInt == that.maskInt);
    }

    @Override
    public int hashCode() {
        return this.maskInt + this.addrInt;
    }
}
