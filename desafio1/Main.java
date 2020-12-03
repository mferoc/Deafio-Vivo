import java.util.HashMap;
import java.util.Map;

class Main {
  public static void main(String args[]) {
    final int[] hex_arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    final int[][] bitmapMatrix = { { 16 }, { 0 }, { 1 }, { 17 } };
    
    final Map<Integer, Integer> counter = new HashMap<>();
    for (int i = 0; i < hex_arr.length; i++) {
      counter.put(hex_arr[i], 0);
    }

    for (int row = 0; row < bitmapMatrix.length; row++) {
      for (int col = 0; col < bitmapMatrix[row].length; col++) {
        final int matrixElement = bitmapMatrix[row][col];
        if (counter.containsKey(matrixElement)) {
          counter.put(matrixElement, counter.get(matrixElement) + 1);
        }
      }
    }

    System.out.println(counter);

    counter.forEach((k, v) -> {
      System.out.printf("Chave %d aparece %d vezes\n", k, v);
    });
  }
}