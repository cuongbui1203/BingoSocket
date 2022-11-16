import java.util.Arrays;

public class Player {

    int status;
    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }
    int []check ;
    int []addressCheck ;
    int [][]PlayerTable ;
    String name;
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    Player() {
        this.name = "";
        this.status = 0;
        this.check = new int[25];
        this.addressCheck =  new int[25];
        this.PlayerTable = BingoTable.RandomTable();
    }


    void printTable(){
        System.out.println("Player 's table:");
        for(int i = 0; i < 5; i++) 
        System.out.println(Arrays.toString(this.PlayerTable[i])); 
    }



    void move(int n) {

        n--;
        for(int i = 0; i < 5; i++)
        for(int j = 0; j < 5; j++) {
            if( PlayerTable[i][j] == n && check[n] ==0 ) {
                check[n]++;
                addressCheck[5*i+j]++;
            }
        }
    }
    boolean CheckWinCon() {
        int point = 0;
        for(int i = 0; i < 25; i = i + 5) {
            if(addressCheck[i] == 1 && addressCheck[i+1] == 1 &&
                addressCheck[i+2] == 1 && addressCheck[i+3] == 1 && addressCheck[i+4] == 1 ) {
                    point++;
                    System.out.println(point);
                } }
        for(int i = 0; i < 5; i++) {
            if(addressCheck[i] == 1 && addressCheck[i+5] == 1 &&
                addressCheck[i+10] == 1 && addressCheck[i+15] == 1 && addressCheck[i+20] == 1 ) {
                    point++;
                    System.out.println(point);
                }
        }
        if(addressCheck[0] == 1 && addressCheck[6] == 1 &&
        addressCheck[12] == 1 && addressCheck[18] == 1 && addressCheck[24] == 1 ) {
            point ++;
            System.out.println(point);
        }
        if(addressCheck[4] == 1 && addressCheck[8] == 1 &&
        addressCheck[12] == 1 && addressCheck[16] == 1 && addressCheck[20] == 1 ) {
            point ++;
            System.out.println(point);
        }
        if(point < 5) return false;
            else return true;
    }



}
