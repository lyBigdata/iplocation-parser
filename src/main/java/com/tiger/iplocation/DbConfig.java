package com.tiger.iplocation;

/**
 * com.tiger.iplocation.DbConfig
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:12:00
 */
public class DbConfig {
    /**
     * total header data block size
     */
    private int totalHeaderSize;

    /**
     * max index data block size
     * u should always choice the fastest read block size
     */
    private int indexBlockSize;

    /**
     * construct method
     *
     * @param totalHeaderSize
     * @throws IllegalArgumentException
     */
    public DbConfig(int totalHeaderSize) throws IllegalArgumentException {
        if ((totalHeaderSize % 8) != 0) {
            throw new IllegalArgumentException("totalHeaderSize need to be times of 8");
        }

        this.totalHeaderSize = totalHeaderSize;
        this.indexBlockSize = 8192; //4 * 2048
    }

    public DbConfig() throws IllegalArgumentException {
        this(8 * 2048);
    }

    public int getTotalHeaderSize() {
        return totalHeaderSize;
    }

    public DbConfig setTotalHeaderSize(int totalHeaderSize) {
        this.totalHeaderSize = totalHeaderSize;
        return this;
    }

    public int getIndexBlockSize() {
        return indexBlockSize;
    }

    public DbConfig setIndexBlockSize(int dataBlockSize) {
        this.indexBlockSize = dataBlockSize;
        return this;
    }
}
