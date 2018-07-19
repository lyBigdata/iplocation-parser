package com.tiger.iplocation;

/**
 * com.tiger.iplocation.IndexBlock
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:11:00
 */
public class IndexBlock {
    private static int LENGTH = 12;

    /**
     * start ip address
     */
    private long startIp;

    /**
     * end ip address
     */
    private long endIp;

    /**
     * data ptr
     */
    private int dataPtr;

    /**
     * data length
     */
    private int dataLen;

    public IndexBlock(long startIp, long endIp, int dataPtr, int dataLen) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.dataPtr = dataPtr;
        this.dataLen = dataLen;
    }

    public long getStartIp() {
        return startIp;
    }

    public long getEndIp() {
        return endIp;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    public int getDataLen() {
        return dataLen;
    }

    public static int getIndexBlockLength() {
        return LENGTH;
    }

    /**
     * get the bytes for storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        /*
         * +------------+-----------+-----------+
         * | 4bytes        | 4bytes    | 4bytes    |
         * +------------+-----------+-----------+
         *  start ip      end ip      data ptr + len
        */
        byte[] b = new byte[LENGTH];

        Util.writeIntLong(b, 0, getStartIp());
        Util.writeIntLong(b, 4, getEndIp());

        //write the data ptr and the length
        long mix = getDataPtr() | ((getDataLen() << 24) & 0xFF000000L);
        Util.writeIntLong(b, 8, mix);

        return b;
    }
}
