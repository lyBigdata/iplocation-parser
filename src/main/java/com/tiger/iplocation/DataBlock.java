package com.tiger.iplocation;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

/**
 * com.tiger.iplocation.DataBlock
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 11:11:00
 */
public class DataBlock {

    private final static DatumReader<Location> reader = new SpecificDatumReader(Location.class);

    private final static Location defaultLocation = Location.newBuilder().build();
    /**
     * region address
     */
    private String region;

    /**
     * region ptr in the db file
     */
    private int dataPtr;

    /**
     * construct method
     *
     * @param region  region string
     * @param dataPtr data ptr
     */
    public DataBlock(String region, int dataPtr) {
        this.region = region;
        this.dataPtr = dataPtr;
    }

    public DataBlock(String region) {
        this(region, 0);
    }


    public String getRegion() {
        return region;
    }

    public DataBlock setRegion(String region) {
        this.region = region;
        return this;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    public DataBlock setDataPtr(int dataPtr) {
        this.dataPtr = dataPtr;
        return this;
    }

    public Location getLocation() {
        try {
            Decoder decoder = DecoderFactory.get().jsonDecoder(Location.SCHEMA$, this.region);
            return reader.read(null, decoder);
        }catch (final Exception e){
            e.printStackTrace();
        }
        return defaultLocation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getRegion()).append('|').append(getDataPtr());
        return sb.toString();
    }
}
