package com.example.rural_tutoring.util;

/**
 * 地理位置工具类
 * 用于计算两点之间的距离
 */
public class GeoUtil {
    
    private static final double EARTH_RADIUS = 6371.0; // 地球半径，单位：公里
    
    /**
     * 计算两个坐标点之间的距离（使用Haversine公式）
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 距离，单位：公里
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将角度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);
        
        // 计算差值
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;
        
        // Haversine公式
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
    
    /**
     * 判断两个坐标点是否在指定距离内
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @param maxDistance 最大距离，单位：公里
     * @return 是否在指定距离内
     */
    public static boolean isWithinDistance(double lat1, double lon1, double lat2, double lon2, double maxDistance) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= maxDistance;
    }
} 