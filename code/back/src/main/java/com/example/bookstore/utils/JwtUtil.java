package com.example.bookstore.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author zhangmingcheng
 */
@Configuration
public class JwtUtil {

    private static long EXPIRATION_TIME = 3600000 * 24; // 24 hour
//private static long EXPIRATION_TIME = 10000 * 10;
    private static String SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjY34DFDSSSd";// 秘钥

    private static final String USER_ID = "id";


    /**
     * 生成jwtToken
     *
     * @param id
     * @return
     */
    public static String generateToken(String id) {
        HashMap<String, Object> map = new HashMap<>();
        // you can put any data in the map
        map.put(USER_ID, id);
        String token = Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        return token;
    }

    /**
     * 校验jwtToken
     *
     * @param token
     * @return
     */
    public static String validateToken(String token) {

        //AssertUtils.assertNotNull(token, ResultType.AGAIN_LOGIN, "Missing token");

        if(StringUtils.isBlank(token)){
            System.out.println("Missing token");
        }

        try {
            Map<String, Object> body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String id = (String) (body.get(USER_ID));

            if(StringUtils.isBlank(id)){
                System.out.println("Wrong token");
                return "";
            }


            return id;
        }catch (Exception e){
            System.out.println("Wrong token without id");
            return "";
        }


    }


    public static void main(String[] args) {
        String id = "hahaha15";

        String token = generateToken(id);
        System.out.println(token);

        //token = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjY4NzZhYjFmYjk0MmZkNGYyN2Zm";
        id = validateToken(token);
        System.out.println(id);
//        HashMap<String, Object> map = new HashMap<>();
//        // you can put any data in the map
//        map.put("name", id);
//        token = Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
//
//         String result = "";
//        try {
//
//            result = validateToken(token);
//            System.out.println(result);
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }

    }
}