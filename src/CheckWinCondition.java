public class CheckWinCondition {
    public static boolean CheckWin(Number arr[][]) {
        int a = 0, b = 0, c = 0, count2 = 0, count3 = 0;
        while (a <= 0) {
            int count = 0;
            for (int i = 0; i < 5; i++) {
                if (arr[a][i].check == 1)
                    count++;
            }
            if (count == 5)
                c++;
        }
        while (b <= 0) {
            int count1 = 0;
            for (int i = 0; i < 5; i++) {
                if (arr[i][b].check == 1)
                    count1++;
            }
            if (count1 == 5)
                c++;
        }

        for (int i = 0; i < 5; i++) {
            if (arr[i][i].check == 1)
                count2++;
            if (arr[4 - i][i].check == 1)
                count3++;
        }
        if (count2 == 5)
            c++;
        if (count3 == 5)
            c++;

        return c >= 5;

    }

}