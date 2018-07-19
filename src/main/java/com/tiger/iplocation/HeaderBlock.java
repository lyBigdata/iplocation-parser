package com.tiger.iplocation;

/**
 * com.tiger.iplocation.HeaderBlock
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:11:00
 */
public class HeaderBlock {
    /**
     * index block start ip address
     */
    private long indexStartIp;

    /**
     * ip address
     */
    private int indexPtr;

    public HeaderBlock( long indexStartIp, int indexPtr )
    {
        this.indexStartIp = indexStartIp;
        this.indexPtr = indexPtr;
    }

    public long getIndexStartIp()
    {
        return indexStartIp;
    }

    public HeaderBlock setIndexStartIp(long indexStartIp)
    {
        this.indexStartIp = indexStartIp;
        return this;
    }

    public int getIndexPtr()
    {
        return indexPtr;
    }

    public HeaderBlock setIndexPtr(int indexPtr)
    {
        this.indexPtr = indexPtr;
        return this;
    }

    /**
     * get the bytes for db storage
     *
     * @return    byte[]
     */
    public byte[] getBytes()
    {
        /*
         * +------------+-----------+
         * | 4bytes        | 4bytes    |
         * +------------+-----------+
         *  start ip      index ptr
        */
        byte[] b = new byte[8];

        Util.writeIntLong(b, 0, getIndexStartIp());
        Util.writeIntLong(b, 4, getIndexPtr());

        return b;
    }
}
