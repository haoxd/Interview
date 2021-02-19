package com.bread.coalquality.mvc.controller.dachangmianshiti;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Description: 58 同城字符串常量池
 * @Author: haoxd
 * @Version: 1.0
 */
public class StringPool {


    /**
     *
     *
     *
     * **/
    public static void main(String[] args) {


        String a = new StringBuilder("123").append("456").toString();
        System.out.println(a);
        System.out.println(a.intern());
        System.out.println(a == a.intern());


        String c = new StringBuilder("ja").append("va").toString();
        System.out.println(c);
        System.out.println(c.intern());
        System.out.println(c == c.intern());


        int[] sums = {1, 2, 3, 4, 6};
        int count = 6;


        int[] aa =twoSum(sums, count);


        for (int i = 0; i < aa.length; i++) {

            System.out.println(aa[i]);
        }



    }
    /**
     *   int[] sums = {1, 5, 3, 4, 6};
     int count = 6;
     * */
    public static int[] twoSum(int[] nums, int target) {
        int[] indexs = new int[2];

        // 建立k-v ，一一对应的哈希表
        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (hash.containsKey(nums[i])) {
                indexs[0] = i;
                indexs[1] = hash.get(nums[i]);
                return indexs;
            }
            hash.put(target - nums[i], i);
        }

        return indexs;
    }

    public static int[] twoSum2(int[] nums, int target) {


        for (int i = 0; i < nums.length; i++) {

            for (int j = i + 1; j < nums.length; j++) {


                System.out.println("############################");
                System.out.println(nums[i]);
                System.out.println(nums[j]);
                System.out.println(target - nums[i]);
                System.out.println("############################");
                if (target - nums[i] == nums[j]) {
                    return new int[]{i, j};
                }

            }

        }

        return null;
    }
}
