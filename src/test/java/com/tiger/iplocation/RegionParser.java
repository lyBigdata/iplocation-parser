package com.tiger.iplocation;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * com.tiger.iplocation.RegionParser
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-18 15:08:00
 */
public class RegionParser {

    String targetDir = "/home/iplocation";

    @Ignore
    public void executeIpMergeToGeoCode() throws IOException {
        String file = this.getClass().getResource("/ip.merge.txt").getFile();
        Map<String, List<String>> map = parseRegion(file);
        String areaFile = this.getClass().getResource("/area.txt").getFile();
        Map<String, List<String>> area = parseArea(areaFile);
        Set<String> set = area.keySet();
        List<String> existList = Lists.newArrayList();
        List<String> noList = Lists.newArrayList();
        Map<String, Location> relation = Maps.newHashMap();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String city = entry.getKey();

            if (set.contains(city)) {
                existList.add(city);
                List<String> info = area.get(city);
                Location location = Location.newBuilder().setGeoCode(info.get(4)).setCountry(entry.getValue().get(2))
                        .setProvince(info.get(0)).setRegion(info.get(1)).setCity(city).setIsp(entry.getValue().get(6))
                        .setCityLevel(info.get(3)).build();
                relation.put(city, location);
            } else if (set.contains(entry.getValue().get(2))) {
                existList.add(city);
                List<String> info = entry.getValue();
                Location location = Location.newBuilder().setGeoCode(area.get(info.get(2)).get(4)).setCountry(info.get(2))
                        .setProvince(info.get(4)).setCity(info.get(5)).setIsp(info.get(6)).build();
                if ("0".equalsIgnoreCase(info.get(5))) {
                    city = String.format("%s-%s", info.get(2), info.get(4));
                }
                relation.put(city, location);
            } else {
                noList.add(String.format("%s-%s", entry.getValue().get(2), city));
                List<String> info = entry.getValue();
                Location location = Location.newBuilder().setCountry(info.get(2)).setProvince(info.get(4)).setCity(info.get(5)).setIsp(info.get(6)).build();
                if ("0".equalsIgnoreCase(info.get(5))) {
                    city = String.format("%s-%s", info.get(2), info.get(4));
                }
                relation.put(city, location);
            }
        }
        generateIPRegion(file, relation);
        System.out.println(map.size());
    }

    private Map<String, List<String>> parseRegion(String fileName) throws IOException {
        final Splitter splitter = Splitter.on("|");
        Function<String, Pair<String, List<String>>> func = new Function<String, Pair<String, List<String>>>() {
            @Override
            public Pair<String, List<String>> apply(String line) {
                List<String> list = splitter.splitToList(line);
                String city = list.get(5);
                if ("0".equalsIgnoreCase(city)) {
                    city = String.format("%s-%s", list.get(2), list.get(4));
                }
                return Pair.of(city, list);
            }
        };
        return parseForFunc(fileName, func);
    }

    private Map<String, List<String>> parseArea(String fileName) throws IOException {
        final Splitter splitter = Splitter.on(",");
        Function<String, Pair<String, List<String>>> func = new Function<String, Pair<String, List<String>>>() {
            @Override
            public Pair<String, List<String>> apply(String line) {
                List<String> list = splitter.splitToList(line);
                return Pair.of(list.get(2), list);
            }
        };
        return parseForFunc(fileName, func);
    }


    private Map<String, List<String>> parseForFunc(String fileName, Function<String, Pair<String, List<String>>> func) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return Maps.newHashMapWithExpectedSize(0);
        }

        Map<String, List<String>> map = Maps.newHashMap();
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        while ((line = reader.readLine()) != null) {
            Pair<String, List<String>> pair = func.apply(line);
            if (pair == null || Strings.isNullOrEmpty(pair.getKey()) || pair.getValue().size() < 1) {
                continue;
            }
            map.put(pair.getKey(), pair.getValue());
        }
        reader.close();
        fileReader.close();
        return map;
    }


    private Map<String, String> getCityAlias() {
        Map<String, String> dictionary = Maps.newHashMap();
        dictionary.put("吐鲁番", "吐鲁番市");
        dictionary.put("昌都", "昌都市");
        dictionary.put("日喀则", "日喀则市");
        dictionary.put("伊犁", "伊犁哈萨克自治州");
        dictionary.put("阿勒泰", "阿勒泰地区");
        dictionary.put("阿坝", "阿坝藏族羌族自治州");
        dictionary.put("山南", "山南市");
        dictionary.put("博尔塔拉", "博尔塔拉蒙古自治州");
        dictionary.put("甘南", "甘南藏族自治州");
        dictionary.put("毕节", "毕节市");
        dictionary.put("喀什", "喀什地区");
        dictionary.put("甘孜", "甘孜藏族自治州");
        dictionary.put("临夏", "临夏回族自治州");
        dictionary.put("铜仁", "铜仁市");
        dictionary.put("襄阳", "襄阳市");
        dictionary.put("林芝", "林芝市");
        dictionary.put("塔城", "塔城地区");
        dictionary.put("大兴安岭", "大兴安岭地区");
        dictionary.put("锡林郭勒", "锡林郭勒盟");
        dictionary.put("湘西", "湘西土家族苗族自治州");
        dictionary.put("和田", "和田地区");
        dictionary.put("儋州", "儋州市");
        dictionary.put("红河", "红河哈尼族彝族自治州");
        dictionary.put("恩施", "恩施土家族苗族自治州");
        dictionary.put("怒江", "怒江傈僳族自治州");
        dictionary.put("德宏", "德宏傣族景颇族自治州");
        dictionary.put("那曲", "那曲市");
        dictionary.put("克孜勒苏", "克孜勒苏柯尔克孜自治州");
        dictionary.put("阿克苏", "阿克苏地区");
        dictionary.put("昌吉", "昌吉回族自治州");
        dictionary.put("延边", "延边朝鲜族自治州");
        dictionary.put("西双版纳", "西双版纳傣族自治州");
        dictionary.put("大理", "大理白族自治州");
        dictionary.put("凉山", "凉山彝族自治州");
        dictionary.put("文山", "文山壮族苗族自治州");
        dictionary.put("迪庆", "迪庆藏族自治州");
        dictionary.put("巴音郭楞", "巴音郭楞蒙古自治州");
        dictionary.put("哈密", "哈密市");

        return dictionary;
    }

    private void generateIPRegion(String fileName, Map<String, Location> map) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        String targetFile = String.format("%s/ip.merge.txt", targetDir);
        File newFile = new File(targetFile);
        if (newFile.exists()) {
            newFile.delete();
        }

        FileWriter writer = new FileWriter(targetFile);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            List<String> list = Splitter.on("|").splitToList(line);
            String city = list.get(5);
            if ("0".equalsIgnoreCase(city)) {
                city = String.format("%s-%s", list.get(2), list.get(4));
            }
            line = String.format("%s|%s|%s", list.get(0), list.get(1), map.get(city));
            writer.write(line + '\n');
        }
        writer.flush();
        writer.close();
        reader.close();

    }

    @Ignore
    public void executeAlias() throws IOException {
        String file = this.getClass().getResource("/ip.merge.txt").getFile();
        String target = String.format("%s/ipregion.txt", targetDir);
        FileWriter writer = new FileWriter(target);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Splitter splitter = Splitter.on("|");
        Map<String, String> map = getCityAlias();
        String line;
        while ((line = reader.readLine()) != null) {
            List<String> list = splitter.splitToList(line);
            String city = list.get(5);
            if (map.containsKey(city)) {
                line = line.replace(city, map.get(city));
            }

            writer.write(line + "\n");
        }
        writer.flush();
        writer.close();
        reader.close();

    }

    @Ignore
    public void testDiff() throws IOException {
        String file = this.getClass().getResource("/ip.merge.txt").getFile();
        Map<String, List<String>> map = parseRegion(file);
        String areaFile = this.getClass().getResource("/area.txt").getFile();
        Map<String, List<String>> area = parseArea(areaFile);
        Set<String> set = area.keySet();
        List<String> existList = Lists.newArrayList();
        List<String> noList = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String city = entry.getKey();
            if (set.contains(city)) {
                existList.add(city);
            } else if (set.contains(entry.getValue().get(0))) {
                existList.add(entry.getValue().get(0));
            }
        }
        for (Map.Entry<String, List<String>> entry : area.entrySet()) {
            if (!existList.contains(entry.getKey())) {
                noList.add(Joiner.on("|").join(entry.getValue()));
            }
        }
        System.out.println(noList.size());
    }
}
