import java.util.Scanner;
import java.util.Random;

public class Game {
    static Player player1 = new Player();
    static Player player2 = new Player();
    static Random rd = new Random();
    static Scanner in = new Scanner(System.in);
    
    static void Start() {
        player1.printTable();
        player2.printTable();
        int CoinToss = rd.nextInt(2)+1;
        System.out.println(CoinToss);
        if(CoinToss == 1) {
            player1.status = 1;
            player2.setStatus(0);
            System.out.println("Player 1's turn");
        }
        if(CoinToss == 2) {
            player2.status = 1;
            player1.setStatus(0);
            System.out.println("Player 2's turn");
        }
    }
    static void Play() {
    while (player1.CheckWinCon() == false && player2.CheckWinCon() ==false) {
        System.out.println("Insert your move: ");
        int n = in.nextInt();
        System.out.println(player1.getStatus()+" "+player2.getStatus());
        if(player1.getStatus() == 1) {
            player1.move(n);
            if(player1.CheckWinCon() == true) {
                System.out.println("Player 1 win");
            }   else {
                player2.setStatus(1);
                player1.setStatus(0);
                System.out.println("Player 2's turn");
            }
            
        }else if(player2.getStatus() == 1) {
            player2.move(n);
            if(player2.CheckWinCon() == true) {
                System.out.println("Player 2 win");
            }   else {
                player1.setStatus(1);
                player2.setStatus(0);
                System.out.println("Player 1's turn");
            }
            
        }
    }
}
}
