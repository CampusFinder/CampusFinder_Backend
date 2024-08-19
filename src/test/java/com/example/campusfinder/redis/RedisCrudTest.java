package com.example.campusfinder.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * packageName    : com.example.campusfinder.redis
 * fileName       : RedisCrudTest
 * author         : tlswl
 * date           : 2024-08-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-19        tlswl       최초 생성
 */
@SpringBootTest
public class RedisCrudTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisCrudOperations(){
        String key ="testKey";
        String value="testValue";

        //데이터 저장
        redisTemplate.opsForValue().set(key,value);

        //데이터 조회
        String fetchedValue=(String) redisTemplate.opsForValue().get(key);
        Assertions.assertEquals(value, fetchedValue,"Redis에서 가져온 값이 일치하지 않음.");

        // 3. 데이터 업데이트 (Update)
        String newValue = "newValue";
        redisTemplate.opsForValue().set(key, newValue);
        String updatedValue = (String) redisTemplate.opsForValue().get(key);
        Assertions.assertEquals(newValue, updatedValue, "업데이트된 값이 일치하지 않습니다.");

        // 4. 데이터 삭제 (Delete)
        redisTemplate.delete(key);
        String deletedValue = (String) redisTemplate.opsForValue().get(key);
        Assertions.assertNull(deletedValue, "데이터가 삭제되지 않았습니다.");
    }
}
