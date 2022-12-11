package Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;;

public class Player {
    Scanner in;
    int status;
    int[] check;
    int[] addressCheck;
    int[][] PlayerTable;
    String name;
    String id;
    int n;
    // int point;
    int maxpoint;

    public Player(Scanner in) {
        this.in = in;
        this.name = "";
        this.status = 0;
        this.check = new int[25];
        this.addressCheck = new int[25];
        this.PlayerTable = BingoTable.RandomTable();
        this.n = 0;
        // this.point = 0;
        this.maxpoint = 0;
        this.id = "";
    }

    public Player() {
        this.in = null;
        this.name = "";
        this.status = 0;
        this.check = new int[25];
        this.addressCheck = new int[25];
        this.PlayerTable = BingoTable.RandomTable();
        this.n = 0;
        // this.point = 0;
        this.id = "";
        this.maxpoint = 0;
    }
    public Player(String name) {
        this.in = null;
        this.name = name;
        this.status = 0;
        this.check = new int[25];
        this.addressCheck = new int[25];
        this.PlayerTable = BingoTable.RandomTable();
        this.n = 0;
        // this.point = 0;
        this.id = "";
        this.maxpoint = 0;
    }

    public ArrayList<Integer> getPlayerTable() {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int t = 0; t < 5; t++) {
                res.add(PlayerTable[i][t]);
            }
        }
        return res;
    }

    public ArrayList<Integer> getCheckTable() {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i : check)
            res.add(i);
        return res;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return maxpoint;
    }

    void printTable() {
        System.out.println("Player 's table:");
        for (int i = 0; i < 5; i++)
            System.out.println(Arrays.toString(this.PlayerTable[i]));
    }

    void nhap() {
        while (true) {
            n = in.nextInt();

            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 5; j++) {
                    if (PlayerTable[i][j] == n) {
                        if (check[n - 1] == 0) {
                            check[n - 1]++;
                            addressCheck[5 * i + j]++;
                        } else {
                            System.out.println("invalid move!");
                            break;
                        }
                        return;

                    }
                }
        }
        // for(int i = 0; i < 25; i ++)System.out.print(addressCheck[i]);
    }

    public boolean checkMove(int n) {
        if (n > 0 && n < 26) {
            return check[n - 1] == 0;
        } else
            return false;
    }

    public int getN() {
        return n;
    }

    public void move(int a) {

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                if (PlayerTable[i][j] == a) {
                    if (check[a - 1] == 0) {
                        check[a - 1]++;
                        addressCheck[5 * i + j]++;
                        return;
                    }
                }

            }
        // for(int i = 0; i < 25; i ++)System.out.print(addressCheck[i]);

    }

    public boolean CheckWinCon() {
        int point = 0;
        for (int i = 0; i < 25; i = i + 5) {
            if (addressCheck[i] == 1 && addressCheck[i + 1] == 1 &&
                    addressCheck[i + 2] == 1 && addressCheck[i + 3] == 1 && addressCheck[i + 4] == 1) {
                point++;

            }
        }
        for (int i = 0; i < 5; i++) {
            if (addressCheck[i] == 1 && addressCheck[i + 5] == 1 &&
                    addressCheck[i + 10] == 1 && addressCheck[i + 15] == 1 && addressCheck[i + 20] == 1) {
                point++;

            }
        }
        if (addressCheck[0] == 1 && addressCheck[6] == 1 &&
                addressCheck[12] == 1 && addressCheck[18] == 1 && addressCheck[24] == 1) {
            point++;

        }
        if (addressCheck[4] == 1 && addressCheck[8] == 1 &&
                addressCheck[12] == 1 && addressCheck[16] == 1 && addressCheck[20] == 1) {
            point++;

        }
        maxpoint = point > maxpoint ? point : maxpoint;
        // if(point > maxpoint) maxpoint = point;
        if (point < 5)
            return false;
        else
            return true;
    }

}
