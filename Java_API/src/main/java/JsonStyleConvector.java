package main.java;

import java.sql.*;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonStyleConvector {

    public static void main(String[] args) {
//        final String url = "jdbc:mysql://bd91kfdkf5spw0xzmzqv-mysql.services.clever-cloud.com:3306/bd91kfdkf5spw0xzmzqv";
//        final String username = "udwuiyqcaqjflruo";
//        final String password = "KKsTC3HXAALuBQp4CrUz";
        final String url = "jdbc:mysql://localhost:3306/localdb";
        final String username = "zhmysql";
        final String password = "bios2AlieZ!";
        StringBuffer result = new StringBuffer();
        result.append("{\n\t\"time_requested\":");
        result.append(new Date() + ",\n");
        result.append("\"list\": ");
        for(int i = 1; i <= 3; i++){
            String sql = "SELECT DISTINCT sensor.location, readings.date_read, readings.temperature, readings.ambient_light, readings.b_pressure\n" +
                    "FROM readings\n" +
                    "INNER JOIN sensor\n" +
                    "ON readings.sensor_id = sensor.sensor_id\n" +
                    "GROUP BY readings.sensor_id\n" +
                    "ORDER BY readings.date_read DESC";
            try {
                Connection conn = DriverManager.getConnection(url,username,password);
                Statement stet = conn.createStatement();
                ResultSet rs = stet.executeQuery(sql);
                result.append(toJsonString(rs));
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result.append("\n}");
        System.out.println(result.toString());
    }


    static String toJsonString(ResultSet rs) {
        JSONArray array = new JSONArray();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                JSONObject jsonObj = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = rs.getString(columnName);
                    jsonObj.put(columnName, value);
                }
                array.put(jsonObj);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return array.toString();
    }
}
