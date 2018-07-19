package com.tiger.iplocation;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * com.tiger.iplocation.GeoIpTest
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 14:26:00
 */
public class GeoIpTest {

    @Test
    public void testGeoip() throws IOException {
        String ipFile = Parser.class.getResource("/ip2region.db").getFile();
        Parser parser = new Parser(ipFile);
//        String ip = "58.16.228.173";
        String ip = "223.102.129.78";
        //二分查找方法查找对应关系
        DataBlock block = parser.binarySearch(ip);
        System.out.println(block.getRegion());
        //使用b+树的方法来查找对应关系
        block = parser.btreeSearch(ip);
        System.out.println(block.getRegion());
        //ipFile全部加载到内存，使用二分查找
        block = parser.memorySearch(ip);
        System.out.println(block.getRegion());
        Location location = block.getLocation();
        System.out.println(location.getProvince());

    }

    @Test
    public void testConvertIpAndLong() throws URISyntaxException {
        String ip = "58.16.228.173";
        long address = Util.ip2long(ip);
        String actual = Util.long2ip(address);
        assertThat(actual, is(ip));
    }

    @Test
    public void testProtobuf() throws IOException {
        Location region = Location.newBuilder()
                .setCity("北京市")
                .setGeoCode("11561101")
                .setCountry("中国").setProvince("北京").setRegion("华北地区")
                .setCityLevel("一线城市").build();

        String expr = region.toString();
        DatumReader<Location> reader = new SpecificDatumReader<Location>(Location.class);
        Decoder decoder = DecoderFactory.get().jsonDecoder(Location.SCHEMA$, expr);
        Location location = reader.read(null, decoder);
        System.out.println(location);
    }
}
