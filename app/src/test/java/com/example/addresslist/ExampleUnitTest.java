package com.example.addresslist;

import com.example.addresslist.db.DBHelper;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSelectByCompany() {
        List lists = DBHelper.getInstance(null).selectByCompanySize();
        for (int i = 0; i < lists.size(); i++) {
            Map map = (Map) lists.get(i);
            String company = (String) map.get("company");
            int c_size = (int) map.get("c_size");
            System.out.println("公司名: "+company +"人数:"+c_size);
        }
    }
}