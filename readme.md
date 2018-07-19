## iplocation-parser(地域解析库)

####  字段说明
该库主要是通过IP解析地域信息。解析的地域信息如下：

| 字段|类型|说明|
|---- |---- |--- |
|geo_code|string|地域编码|
|country|string|国家|
|province|string|省份|
|region|string|区域|
|city|string|城市|
|city_level|string|城市等级|
|isp|string|运营商|


**备注**：
* geo_code与国家统计局的地区代码保持一致，对于国内的是准确的，国外的不准确。

#### 实例
```java
 String ipFile = Parser.class.getResource("/ip2region.db").getFile();
 Parser parser = new Parser(ipFile);
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
```

其中ipFile是ip段到地域信息的映射关系表.需要单独加载。而类`DbGenerator`用来生成上述的ip2region.db文件。

备注：本实现参考了https://github.com/lionsoul2014/ip2region,并进行了优化，能够返回对象及geo_code。