package com.taoge.biz.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taoge.biz.es.enums.MerchantShopFacilitiesEnum;
import com.taoge.biz.es.enums.MerchantShopSceneTimeEnum;
import com.taoge.biz.es.enums.MerchantShopTagOpenTimeEnum;
import com.taoge.biz.es.merchant.MerchantShopEO;
import com.taoge.biz.es.merchant.MerchantShopProductEO;
import com.taoge.biz.es.merchant.MerchantShopTagEO;
import org.elasticsearch.common.geo.GeoPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateRandomDataUtil {

    private static final Random random = new Random();
    private static final String[] provinces = new String[]{"北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "台湾", "香港", "澳门"};
    private static final String[] productNames = new String[]{"双椒鸡丁", "西芹炒木耳", "青椒火腿炒蛋", "鱼香肉丝", "干锅土豆片", "白灼菜心", "宫保鸡丁", "蒜蓉粉丝", "玉子豆腐虾仁蒸蛋", "荷塘小炒", "白灼茼蒿", "奥尔良烤翅", "孜然土豆午餐肉", "孜然鸡翅土豆条", "木须鸡蛋", "荷兰豆炒牛柳", "西兰花炒鸡胸肉", "菌菇炒火腿", "茄汁豆腐抱蛋", "芦笋炒虾仁", "红烧排骨", "菠萝咕肉", "蒜苔炒肉丝", "山药炒木耳珍珠糯米丸子", "金钱蛋", "红烧肉", "四季豆炒肉丝", "虾滑藕夹", "红枣糯米"};
    private static final List<String> provinceList = Arrays.asList(provinces);
    private static final List<String> productNameList = Arrays.asList(productNames);
    private static final List<String> locationList = new ArrayList<>();
    static {
        locationList.add("116.433612 39.91112");
        locationList.add("116.432785 39.911729");
        locationList.add("116.434932 39.915689");
        locationList.add("116.432992 39.92013");
    }

    public static MerchantShopEO generatorMerchantShopEO() {
        MerchantShopEO eo = new MerchantShopEO();
        eo.setId(getRandomLong(1000));
        eo.setMerchantId(getRandomLong(1000));
        eo.setCategoryId(getRandomLong(1000));
        eo.setShopName(getRandomString(10));
        eo.setAverageConsume(getRandomPrice());
        eo.setSalesVolume(getRandomLong(1000));
        eo.setScore(getRandomScore());
        eo.setOpenTime(getRandomOpenTimeTags(MerchantShopTagOpenTimeEnum.getTagValues()));
        eo.setScene(getRandomOpenTimeTags(MerchantShopSceneTimeEnum.getTagNames()));
        eo.setFacilities(getRandomFacilitiesTags());
        eo.setProductList(getRandomProductList());
        eo.setProductNameList(getProductNameList(eo.getProductList()));
        eo.setLocation(generateGeoPoint());
        return eo;
    }

    private static GeoPoint generateGeoPoint() {
        int index = random.nextInt(locationList.size());
        String locationStr = locationList.get(index);
        String[] location = locationStr.split(" ");
        GeoPoint geoPoint = new GeoPoint(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
        return geoPoint;
    }

    private static List<String> getProductNameList(List<MerchantShopProductEO> productList) {
        List<String> productNameList = productList.stream().map(MerchantShopProductEO::getProductName).collect(Collectors.toList());
        return productNameList;
    }

    private static List<String> getRandomOpenTimeTags(List<String> tagValues) {
        // 从tag枚举列表中，随机出几个tag
        // 数量随机，标签不能重复
        List<String> tags = new ArrayList<>();
        int length = random.nextInt(tagValues.size());
        while (length > 0) {
            int index = random.nextInt(tagValues.size());
            String tag = tagValues.get(index);
            tags.add(tag);
            tagValues.remove(tag);
            length--;
        }
        return tags;
    }

    private static List<MerchantShopTagEO> getRandomFacilitiesTags() {
        List<MerchantShopFacilitiesEnum> list = new ArrayList<>(Arrays.asList(MerchantShopFacilitiesEnum.values()));
        List<MerchantShopTagEO> tags = new ArrayList<>();
        int length = random.nextInt(list.size());
        while (length > 0) {
            int index = random.nextInt(list.size());
            MerchantShopFacilitiesEnum e = list.get(index);
            MerchantShopTagEO eo = new MerchantShopTagEO();
            eo.setTagName(e.getTagName());
            eo.setTabValue(e.getValue());
            tags.add(eo);
            list.remove(e);
            length--;
        }
        return tags;
    }

    private static Date generateRandomDate() {
        long millis = System.currentTimeMillis() - (long) (random.nextFloat() * 1000000000);
        return new Date(millis);
    }

    private static BigDecimal generateRandomBigDecimal() {
        return BigDecimal.valueOf(random.nextDouble() * 1000);
    }

    private static Integer generateRandomInteger() {
        return random.nextInt(10000);
    }

    private static Boolean generateRandomBoolean() {
        return random.nextBoolean();
    }

    private static Byte generateRandomByte() {
        return (byte) random.nextInt(4);
    }

    private static Long getRandomLong() {
        return (long) random.nextInt(1000) + 1; // id在1~1000之间
    }

    private static Long getRandomLong(int max) {
        return (long) random.nextInt(max) + 1; // id在1~1000之间
    }

    private static Integer getRandomInt(int max) {
        return random.nextInt(max); // id在1~1000之间
    }

    private static String getRandomTelephone() {
        return "1" + getRandomNumber(10);
    }

    private static String getRandomNumber(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private static String getRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private static BigDecimal getRandomPrice() {
        BigDecimal price = BigDecimal.valueOf(random.nextDouble() * 100);
        price = price.multiply(BigDecimal.ONE).setScale(2, RoundingMode.HALF_UP);
        return price;
    }

    private static double getRandomScore() {
        BigDecimal price = BigDecimal.valueOf(random.nextDouble());
        price = price.multiply(BigDecimal.ONE).setScale(1, RoundingMode.HALF_UP);
        return 4 + price.doubleValue();
    }

    private static String getRandomProvince() {
        int index = random.nextInt(provinceList.size());
        return provinceList.get(index);
    }

    public static List<MerchantShopProductEO> getRandomProductList() {
        List<MerchantShopProductEO> list = new ArrayList<>();
        ArrayList<String> newProductNameList = new ArrayList<>(productNameList);
        int length = random.nextInt(5);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(newProductNameList.size());
            String productName = newProductNameList.get(index);
            MerchantShopProductEO eo = new MerchantShopProductEO();
            eo.setId(getRandomLong(1000));
            eo.setProductName(productName);
            list.add(eo);
            newProductNameList.remove(productName);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(generatorMerchantShopEO(), SerializerFeature.PrettyFormat));
    }

}
