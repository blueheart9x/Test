/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Admin
 */
public class Client {

    private static final Logger logger = Logger.getLogger(Client.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Post
     *
     * @param urlRequest
     * @param paramMap
     * @return
     */
    public static String executePost(String urlRequest, Map paramMap) {
        try {
            String result = null;
            mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

            String strJson = mapper.writeValueAsString(paramMap);
            //--
            String data = strJson;
            HttpURLConnection conn = null;
            try {
                //Create connection
                URL url = new URL(urlRequest);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", MediaType.APPLICATION_JSON);
                conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                try ( //Send request
                        DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.writeBytes(data);
                }
                //Get Response
                InputStream is = conn.getInputStream();
                StringBuilder response;
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                    response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                } // or StringBuffer if not Java 5+ success
                result = response.toString();
            } catch (IOException e) {
                System.err.println(e);
                logger.error(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return result;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String executePost(String urlRequest, Map paramMap, String viIdentity, String bearerToken) {
        try {
            String result = null;
            mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

            String strJson = mapper.writeValueAsString(paramMap);
            //--
            String data = strJson;
            HttpURLConnection conn = null;
            try {
                //Create connection
                URL url = new URL(urlRequest);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", MediaType.APPLICATION_JSON);
                conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
                conn.setRequestProperty("vi-identity", viIdentity);
                conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                try ( //Send request
                        DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.writeBytes(data);
                }
                //Get Response
                InputStream is = conn.getInputStream();
                StringBuilder response;
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                    response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                } // or StringBuffer if not Java 5+ success
                result = response.toString();
            } catch (IOException e) {
                System.err.println(e);
                logger.error(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return result;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String executeGet(String urlRequest) {
        String result = null;
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(urlRequest);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            //Get Response  
            int responseCode = conn.getResponseCode();
            InputStream is = null;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED
                    || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                is = conn.getInputStream();
                if ("gzip".equals(conn.getContentEncoding())) {
                    is = new GZIPInputStream(is);
                }
            } else {
                is = conn.getErrorStream();
            }

            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
            }
            result = response.toString();
        } catch (IOException e) {
            System.err.println(e);
            logger.error(e);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * Get url request with bear authentication
     *
     * @param urlRequest
     * @param viIdentity
     * @param bearerToken
     * @return
     */
    public static String executeGet(String urlRequest, String viIdentity, String bearerToken) {
        String result = null;
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(urlRequest);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("vi-identity", viIdentity);
            conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            //Get Response  
            int responseCode = conn.getResponseCode();
            InputStream is = null;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED
                    || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                is = conn.getInputStream();
                if ("gzip".equals(conn.getContentEncoding())) {
                    is = new GZIPInputStream(is);
                }
            } else {
                is = conn.getErrorStream();
            }

            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
            }
            result = response.toString();
        } catch (IOException e) {
            System.err.println(e);
            logger.error(e);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }
    
    public static String executeDelete(String urlRequest, Map paramMap, String viIdentity, String bearerToken) {
        try {
            String result = null;
            mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

            String strJson = mapper.writeValueAsString(paramMap);
            //--
            String data = strJson;
            HttpURLConnection conn = null;
            try {
                //Create connection
                URL url = new URL(urlRequest);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("accept", MediaType.APPLICATION_JSON);
                conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
                conn.setRequestProperty("vi-identity", viIdentity);
                conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                try ( //Send request
                        DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.writeBytes(data);
                }
                //Get Response
                InputStream is = conn.getInputStream();
                StringBuilder response;
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                    response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                } // or StringBuffer if not Java 5+ success
                result = response.toString();
            } catch (IOException e) {
                System.err.println(e);
                logger.error(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return result;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String viIdenty = "72037453-bdfe-43c3-bc31-5afb210e5e26";
        String bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MjAzNzQ1My1iZGZlLTQzYzMtYmMzMS01YWZiMjEwZTVlMjYiLCJqdGkiOiI0OWY1OGY2ZC04N2UwLTQ0YWYtYjFiMy1mNWE3MGM5ZDAwMmUiLCJpYXQiOjE1ODUwMTQ1MTcsImlzcyI6Ii52bi5jb20uZWxjb20ifQ.ImGZtxXQ9bRgsfwYQqni8OZakylR6Wgnnq3zZrsW6N8";
        String urlRequest = "http://localhost:8080/interviewService/service/v1.0/letter?companyId=19&interviewId=167&type=ADMIN&jobId=&stt=&status=0,1&startDate=2020-04-01&endDate=2020-04-30&page=1&rowsPerPage=10";
        String result = executeGet(urlRequest, viIdenty, bearerToken);
        System.out.println("result: " + result);

        //Post
        urlRequest = "http://api.ale.vn/getinfo?name=interested";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "interested");
        result = executePost(urlRequest, paramMap);
        System.out.println("result: " + result);
        
        //Post test
        urlRequest = "http://localhost:8080/interviewService/service/v1.0/rating_template";
        Map<String, Object> paramMap1 = new HashMap<>();
        paramMap1.put("companyId", 1);
        paramMap1.put("name", "aaa bbb tét");
        result = executePost(urlRequest, paramMap1, viIdenty, bearerToken);
        System.out.println("result: " + result);
        
        //Delete
        urlRequest = "http://localhost:8080/interviewService/service/v1.0/rating_template/11";
        result = executeDelete(urlRequest, null, viIdenty, bearerToken);
        System.out.println("result: " + result);
    }
}
