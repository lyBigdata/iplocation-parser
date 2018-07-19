package com.tiger.iplocation;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * com.tiger.iplocation.DbMaker
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:15:00
 */
public class DbMaker {
    private DbConfig dbConfig;
    private String ipSrcFile;
    private LinkedList<IndexBlock> indexPool;
    private LinkedList<HeaderBlock> headerPool;
    private HashMap<String, DataBlock> regionPtrPool = null;

    public DbMaker(DbConfig config, String srcFile)
            throws IOException {
        this.dbConfig = config;
        this.ipSrcFile = srcFile;
        this.regionPtrPool = new HashMap();
        File file = new File(srcFile);
        if (!file.exists()) {
            throw new IOException("Error: Invalid file path " + srcFile);
        }
    }

    private void initDbFile(RandomAccessFile paramRandomAccessFile)
            throws IOException {
        paramRandomAccessFile.seek(0L);
        paramRandomAccessFile.write(new byte[8]);
        paramRandomAccessFile.write(new byte[this.dbConfig.getTotalHeaderSize()]);
        this.headerPool = new LinkedList();
        this.indexPool = new LinkedList();
    }

    public void make(String fileName)
            throws IOException {
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        initDbFile(file);
        readIPRegionFile(this.ipSrcFile, file);

        //当前文件偏移量
        long offset = file.getFilePointer();
        int blockLength = IndexBlock.getIndexBlockLength();

        addIndexBlock(file, offset, blockLength);

        long l3 = file.getFilePointer();

        // 文件头8个字符表示数据总长度和索引总长度
        file.seek(0L);
        byte[] arrayOfByte = new byte[8];
        Util.writeIntLong(arrayOfByte, 0, offset);
        Util.writeIntLong(arrayOfByte, 4, l3 - blockLength);
        file.write(arrayOfByte);

        addHeaderBlock(file);
        file.close();
    }

    /**
     * 读取原始的地域信息及起始ip，结束ip信息，并生成数据文件和索引文件
     * @param fileName
     * @param file
     * @throws IOException
     */
    private void readIPRegionFile(String fileName, RandomAccessFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            int i = 0;
            int j = line.indexOf('|', i + 1);
            if (j == -1) {
                continue;
            }
            String startIP = line.substring(i, j);
            i = j + 1;
            j = line.indexOf('|', i + 1);
            if (j == -1) {
                continue;
            }
            String endIP = line.substring(i, j);
            i = j + 1;
            String region = line.substring(i); // 地域信息
            addDataBlock(file, startIP, endIP, region);
        }
        reader.close();
    }

    /**
     * 1. 向数据文件增加地域信息。
     * 2. 向地域池增加文件偏移量及地域信息的对应关系
     * 3. 向索引池增加起始ip，结束ip，地域信息及文件偏移量信息。
     * @param file
     * @param startIP
     * @param endIP
     * @param region
     */
    private void addDataBlock(RandomAccessFile file, String startIP, String endIP, String region) {
        try {
            byte[] arrayOfByte = region.getBytes("UTF-8");
            int i;
            if (this.regionPtrPool.containsKey(region)) {
                DataBlock block = this.regionPtrPool.get(region);
                i = block.getDataPtr();
            } else {
                i = (int) file.getFilePointer();
                file.write(arrayOfByte);
                this.regionPtrPool.put(region, new DataBlock(region, i));
            }
            IndexBlock indexBlock = new IndexBlock(Util.ip2long(startIP), Util.ip2long(endIP), i, arrayOfByte.length);
            this.indexPool.add(indexBlock);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向数据文件增加索引信息
     * @param file
     * @param filePointer
     * @param indexLength
     * @throws IOException
     */
    private void addIndexBlock(RandomAccessFile file,long filePointer,int indexLength) throws IOException {
        IndexBlock indexBlock = this.indexPool.getFirst();
        long ipAddress = indexBlock.getStartIp();
        this.headerPool.add(new HeaderBlock(ipAddress, (int) filePointer));
        int m = 0;
        int n = this.dbConfig.getIndexBlockSize() / indexLength - 1;
        Iterator iterator = this.indexPool.iterator();
        while (iterator.hasNext()) {
            indexBlock = (IndexBlock) iterator.next();
            m++;
            if (m >= n) {
                int offset = (int) file.getFilePointer();
                HeaderBlock block = new HeaderBlock(indexBlock.getStartIp(), offset);
                this.headerPool.add(block);
                m = 0;
            }
            //  向文件写入内容后，file.getFilePointer会变化，getFilePointer始终指向file当前可写入的位置
            file.write(indexBlock.getBytes());
        }
        if (m > 0) {
            indexBlock = this.indexPool.getLast();
            HeaderBlock block = new HeaderBlock(indexBlock.getStartIp(), (int) file.getFilePointer() - IndexBlock.getIndexBlockLength());
            this.headerPool.add(block);
        }
    }

    /**
     * 向数据文件增加头文件信息
     * @param file
     * @throws IOException
     */
    private void addHeaderBlock(RandomAccessFile file) throws IOException {
        Iterator iter = this.headerPool.iterator();
        while (iter.hasNext()) {
            HeaderBlock block = (HeaderBlock) iter.next();
            file.write(block.getBytes());
        }
    }
}
