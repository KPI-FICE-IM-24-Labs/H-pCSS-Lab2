import java.sql.Ref;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Data {
  public static int N;
  public static int P;
  public static int H;
  public static int d;
  public static int[] Z;

  public static AtomicInteger a;
  public static CyclicBarrier B1;

  public static Semaphore S1;
  public static Semaphore S2;
  public static Semaphore S3;
  public static Semaphore S4;
  public static Semaphore S5;

  public static ReentrantLock CS1;
  public static Object CS2; // КД3

  public static int[][] MC;
  public static int[][] MD;
  public static int[][] MR;
  public static int[][] MU;

  public static void initialize() {
    Data.N = 16;
    Data.P = 4;
    Data.H = N / P;
    Data.Z = new int[N];

    Data.B1 = new CyclicBarrier(4);
    Data.a = new AtomicInteger(Integer.MIN_VALUE);

    Data.S1 = new Semaphore(0);
    Data.S2 = new Semaphore(0);
    Data.S3 = new Semaphore(0);
    Data.S4 = new Semaphore(0);
    Data.S5 = new Semaphore(0);

    Data.CS1 = new ReentrantLock();
    Data.CS2 = new Object();

    Data.MC = new int[N][N];
    Data.MD = new int[N][N];
    Data.MR = new int[N][N];
    Data.MU = new int[N][N];
  }

  public static int[] getPartOfVector(int[] vector, int fromIndex, int toIndex) {
    int[] result = new int[toIndex - fromIndex + 1];
    for (int i = 0; i < result.length; i++) {
      result[i] = vector[i + fromIndex];
    }
    return result;
  }

  public static int getMaxElementOfVector(int[] vector) {
    int result = vector[0];
    for (int i = 0; i < vector.length; i++) {
      if (vector[i] > result) {
        result = vector[i];
      }
    }
    return result;
  }

  public static int[][] multiplyTwoMatrices(int[][] matrix1, int[][] matrix2) {
    int result[][] = new int[matrix1.length][matrix2[0].length];
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix2[0].length; j++) {
        for (int k = 0; k < matrix1[0].length; k++) {
          result[i][j] += matrix1[i][k] * matrix2[k][j];
        }
      }
    }
    return result;
  }

  public static int[][] multiplyMatrixByScalar(int[][] matrix, int scalar) {
    int[][] result = new int[matrix.length][matrix[0].length];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        result[i][j] = matrix[i][j] * scalar;
      }
    }
    return result;
  }

  public static int[][] addTwoMatrices(int[][] matrix1, int[][] matrix2) {
    int[][] result = new int[matrix1.length][matrix1[0].length];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[0].length; j++) {
        result[i][j] = matrix1[i][j] + matrix2[i][j];
      }
    }
    return result;
  }

  public static int[][] getPartOfMatrixRows(int[][] matrix, int fromRow, int toRow) {
    int[][] result = new int[toRow - fromRow + 1][matrix[0].length];
    for (int i = 0; i < result.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
            result[i][j] = matrix[i + fromRow][j];
        }
    }

    return result;
  }

  public static void insertPartOfMatrixRowsIntoMatrix(int[][] part, int[][] matrix, int fromRow) {
    for (int i = 0; i < part.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i + fromRow][j] = part[i][j];
      }
    }
  }

  public static void calculateExpression(int ai, int di, int from, int to) {
    int[][] mdPart = getPartOfMatrixRows(MD, from, to); // MDн
    int[][] mrPart = getPartOfMatrixRows(MR, from, to); // MRн

    int[][] mdMcProduct = multiplyTwoMatrices(mdPart, MC); // MDн * MC

    int[][] scaledMdMc = multiplyMatrixByScalar(mdMcProduct, di); // (MDн * MC) * di

    int[][] scaledMr = multiplyMatrixByScalar(mrPart, ai); // MRн * ai

    int[][] result = addTwoMatrices(scaledMdMc, scaledMr); // (MDн * MC) * di + MRн * ai

    insertPartOfMatrixRowsIntoMatrix(result, MU, from); // MU = (MDн * MC) * di + MRн * ai
  }

  public static void toString(int[][] matrix) {
    System.out.println("Matrix:");
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          System.out.printf("%d ", matrix[i][j]);
        }
        System.out.println();
    }
  }

  public static void fillMatrix(int[][] matrix) {
    for (int i = 0; i < Data.N; i++) {
      for (int j = 0; j < Data.N; j++) {
        matrix[i][j] = 1;
      }
    }
  }

  public static void fillVector(int[] vector) {
    Random random = new Random();
    int randomIndex = random.nextInt(vector.length); // Рандомний індекс вектора для запису більшого значення ща інші

    for (int i = 0; i < Data.N; i++) {
      vector[i] = 1;
    }

    vector[randomIndex] = random.nextInt(9) + 2; // Рандомне число від 2 до 10 для перевірки вибори максимального елементу з вектора
  }
}
