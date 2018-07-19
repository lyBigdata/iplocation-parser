package com.tiger.iplocation;

/**
 * com.tiger.iplocation.Util
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:10:00
 */
public class Util {
    /**
     * write specfield bytes to a byte array start from offset
     *
     * @param b
     * @param offset
     * @param v
     * @param bytes
     */
    public static void write(byte[] b, int offset, long v, int bytes) {
        for (int i = 0; i < bytes; i++) {
            b[offset++] = (byte) ((v >>> (8 * i)) & MAX_TOKEN);
        }
    }

    /**
     * write a int to a byte array
     *
     * @param b
     * @param offset
     * @param v
     */
    public static void writeIntLong(byte[] b, int offset, long v) {
        b[offset++] = (byte) ((v >> 0) & MAX_TOKEN);
        b[offset++] = (byte) ((v >> 8) & MAX_TOKEN);
        b[offset++] = (byte) ((v >> 16) & MAX_TOKEN);
        b[offset] = (byte) ((v >> 24) & MAX_TOKEN);
    }

    /**
     * get a int from a byte array start from the specifiled offset
     *
     * @param b
     * @param offset
     */
    public static long getIntLong(byte[] b, int offset) {
        return (
                ((b[offset++] & 0x000000FFL)) |
                        ((b[offset++] << 8) & 0x0000FF00L) |
                        ((b[offset++] << 16) & 0x00FF0000L) |
                        ((b[offset] << 24) & 0xFF000000L)
        );
    }

    /**
     * string ip to long ip
     *
     * @param ip
     * @return long
     */
    public static long ip2long(String ip) {
        String[] arr = ip.split("\\.");
        if (arr.length != 4) {
            return 0;
        }
        int p1 = ((Integer.valueOf(arr[0]) << 24) & 0xFF000000);
        int p2 = ((Integer.valueOf(arr[1]) << 16) & 0x00FF0000);
        int p3 = ((Integer.valueOf(arr[2]) << 8) & 0x0000FF00);
        int p4 = ((Integer.valueOf(arr[3]) << 0) & 0x000000FF);

        return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
    }

    /**
     * int to ip string
     *
     * @param ip
     * @return string
     */
    public static String long2ip(long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append((ip >> 24) & MAX_TOKEN).append('.')
                .append((ip >> 16) & MAX_TOKEN).append('.')
                .append((ip >> 8) & MAX_TOKEN).append('.')
                .append((ip >> 0) & MAX_TOKEN);

        return sb.toString();
    }

    /**
     * check the validate of the specifeld ip address
     *
     * @param ip
     * @return boolean
     */
    public static boolean isIpAddress(String ip) {
        String[] arr = ip.split("\\.");
        if (arr.length != 4) {
            return false;
        }

        for (String p : arr) {
            if (p.length() > 3) {
                return false;
            }
            int val = Integer.valueOf(p);
            if (val > MAX_TOKEN) {
                return false;
            }
        }

        return true;
    }

    // 16进制的0xFF
    public final static int MAX_TOKEN = 255;
}
