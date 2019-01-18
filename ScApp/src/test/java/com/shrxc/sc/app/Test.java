package com.shrxc.sc.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CH on 2018/8/2.
 */

public class Test {

    @org.junit.Test
    public void tese() {

        System.out.println("-----------" + getBetCountWithGamesCount(2, new int[]{10, 11}, 2));
    }


    private void save() {


    }


    private int getBetCountWithGamesCount(int count, int[] selects, int qs) {

        int result = 0;
        if (qs < 2 || qs > count || count < 2) {
            return 0;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < qs; i++) {
            list.add(1);
        }
        for (int i = qs; i < count; i++) {
            list.add(0);
        }
        result += getItemBetCountWithArray(list, selects);
        while (true) {
            if (shouldBreakFromCirculationWithArray(list, qs)) {
                break;
            } else {
                int i;
                for (i = 0; i < list.size() - 1; i++) {
                    if (list.get(i) == 1 && list.get(i + 1) == 0) {
                        int a = list.get(i);
                        int b = list.get(i + 1);
                        list.set(i, b);
                        list.set(i + 1, a);
                        break;
                    }
                }
                for (int j = 0; i < i - 1; j++) {
                    for (int k = 0; k < i - 1 - j; k++) {
                        if (list.get(k) < list.get(k + 1)) {
                            int a = list.get(k);
                            int b = list.get(k + 1);
                            list.set(k, b);
                            list.set(k + 1, a);
                        }
                    }
                }
                result += getItemBetCountWithArray(list, selects);
            }
        }

        return result;
    }

    private int getItemBetCountWithArray(List<Integer> list, int[] selects) {

        int count = 1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 1) {
                count *= selects[i];
            }
        }
        return count;
    }

    private boolean shouldBreakFromCirculationWithArray(List<Integer> list, int qs) {

        boolean shouldbreak = true;
        for (int i = 0; i < list.size() - qs; i++) {
            if (list.get(i) == 1) {
                shouldbreak = false;
                break;
            }
        }
        return shouldbreak;
    }
}
