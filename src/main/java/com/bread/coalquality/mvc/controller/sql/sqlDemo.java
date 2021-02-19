package com.bread.coalquality.mvc.controller.sql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
public class sqlDemo {



    public static void main(String[] args) {

        List<String> conditionList = Arrays.asList("2","3","1","4","10","5");

        Collections.sort(conditionList,(Comparator.naturalOrder()));


       /* Collections.sort(conditionList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
*/
        System.out.println(conditionList);


        List<String> returnKeyList = Arrays.asList("2","3","1","4","10","5");

        Collections.sort(returnKeyList,(Comparator.naturalOrder()));

      /*  Collections.sort(returnKeyList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
        });*/
        System.out.println(returnKeyList);


    }

    public static String selectPersonLike(final String id, final String firstName, final String lastName) {
        return new SQL() {{
            SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FIRST_NAME, P.LAST_NAME");
            FROM("PERSON P");
            if (id != null) {
                WHERE("P.ID like #{id}");
            }
            if (firstName != null) {
                WHERE("P.FIRST_NAME like #{firstName}");
            }
            if (lastName != null) {
                WHERE("P.LAST_NAME like #{lastName}");
            }
            ORDER_BY("P.LAST_NAME");
        }}.toString();
    }
}
