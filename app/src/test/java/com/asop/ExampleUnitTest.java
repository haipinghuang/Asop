package com.asop;

import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < 5; i++) {
            map.put("int" + i, "int" + i);
        }
    }
}