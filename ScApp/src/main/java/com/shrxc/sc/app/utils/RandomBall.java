package com.shrxc.sc.app.utils;

import java.util.Random;

/**
 * Created by CH on 2018/8/2.
 */

public class RandomBall {

    /**
     * @param num   随机的号码
     * @param balls 机选球的个数
     * @return 返回机选结果
     */
    public static int[] selectBall(int num, int balls) {

        int[] selectedBall = new int[balls];
        int i = 0, ballNum;
        Random random = new Random();
        do {
            ballNum = random.nextInt(num) + 1;
            if (isSelect(ballNum, selectedBall)) {
                selectedBall[i] = ballNum;
                i++;
            }
        } while (i < balls);

        return selectedBall;
    }


    private static boolean isSelect(int ballNum, int[] selectedBall) {

        if (selectedBall.length == 0) {
            return true;
        } else {
            for (int i = 0; i < selectedBall.length; i++) {
                if (ballNum == selectedBall[i]) {
                    return false;
                }
            }
            return true;
        }
    }

}
