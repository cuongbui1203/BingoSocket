import java.util.Scanner;
import java.util.Random;

public class Game {
    static Scanner in = new Scanner(System.in);
    static Player player1 = new Player(in);
    static Player player2 = new Player(in);
    static Random rd = new Random();
    
    
    static void Start() {
        player1.printTable();
        player2.printTable();
        int CoinToss = rd.nextInt(2)+1;
       // System.out.println(CoinToss);
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
        
       // System.out.println(player1.getStatus()+" "+player2.getStatus());
        if(player1.getStatus() == 1) {  
            player1.nhap();

            player2.move(player1.getN());

            if(player1.maxpoint >= 5) {
                System.out.println("Player 1 win");
            }   else {
                player2.setStatus(1);
                player1.setStatus(0);
                System.out.println("Player 1 point:"+" "+player1.maxpoint);
                System.out.println("Player 2 point:"+ " "+ player2.maxpoint);
                System.out.println("Player 2's turn");
            }
            
        }else if(player2.getStatus() == 1) {
            player2.nhap();
            player1.move(player2.getN());

            if(player2.maxpoint >= 5) {
                System.out.println("Player 2 win");
                break;
            }   else {
                player1.setStatus(1);
                player2.setStatus(0);
                System.out.println("Player 1 point:"+" "+player1.maxpoint);
                System.out.println("Player 2 point:"+ " "+ player2.maxpoint);
                System.out.println("Player 1's turn");
            }
            
        }
    }
}
}
