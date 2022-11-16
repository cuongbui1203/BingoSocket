
import java.util.ArrayList;
import java.util.List;

public class BingoTable {
    /**
     * hàm tạo dãy ngẫu nhiên có 25 giá trị từ 1 đến 25
     */
   /* public static Vector RandomArray() {
    Random rd = new Random();
    Vector v = new Vector();
    int iNew = 0;
    for (int i = 0; i < 25;  ) {
        iNew = rd.nextInt(25);
        if (!v.contains(iNew)){
            i++;
            v.add(iNew+1);
            //System.out.println("Item " + (i+1) + ":" + iNew);
        }
    }
    return v;
} */
    public static List<Integer> RandomArray() {
        java.util.List<Integer> arrayList = new ArrayList();

        for(int i = 0; i < 25; i++){
            arrayList.add(i + 1);
        }

        java.util.Collections.shuffle(arrayList);

        return arrayList;

    }
    public static void main(String[] args) {
        System.out.println(RandomArray().toString());
        System.out.println(RandomArray().toString());
        System.out.println(RandomArray().toString());
    }
    /**
     * tạo bảng 5x5 có 25 số sắp xếp ngẫu nhiên từ 1 đến 25
     */
    public static int [][]RandomTable() {
        List<Integer> r = RandomArray();
        int [][] table = new int[5][5];
        for(int i = 0; i < 5; i++) {
        for(int j = 0; j < 5; j++) {
            table[i][j] = (int) r.get(5*i+j);
        }

        }
        return table;
    }


}