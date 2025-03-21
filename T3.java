import java.util.concurrent.BrokenBarrierException;

public class T3 extends Thread {
  private int a3;
  private int d3;
  private int fromIndex;
  private int toIndex;
  private int[] Zh;

  public T3() {
    this.fromIndex = 2 * Data.H;
    this.toIndex = 3 * Data.H - 1;
  }

  @Override
  public void run() {
    try {
        System.out.println("T3 thread started");

        // Введення MC
        Data.fillMatrix(Data.MC);

        // Введення MR
        Data.fillMatrix(Data.MR);

        Data.B1.await(); // Чекаємо на завершення введення данних у T1 та T4

        // Обчислення 1
        Zh = Data.getPartOfVector(Data.Z, fromIndex, toIndex);
        a3 = Data.getMaxElementOfVector(Zh);

        /**
         * Обчислення 2
         * КД1
         * Безпечний запис СР а
         */
        Data.a.updateAndGet(a -> Math.max(a, a3));

        // Сигнал задачам T1, T2, T4 про завершення обчислення СР a
        Data.S3.release(3);

        // Очікуємо завершення обчислення 2 у задачач T1, T2, T4
        Data.S1.acquire();
        Data.S2.acquire();
        Data.S4.acquire();

        /**
         * Копія a3 = a
         * КД2, CS1
         */
        Data.CS1.lock();
        a3 = Data.a.get();
        Data.CS1.unlock();

        /**
         * Копія d3 = d
         * КД3, CS2
         */
        synchronized(Data.CS2) {
          d3 = Data.d;
        }

        // Обчислення 3
        Data.calculateExpression(a3, d3, fromIndex, toIndex);

        System.out.println("Thread T3 finished");

        // Сигнал задачі T1 про заврешення виконання обчислення 3
        Data.S5.release();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    } catch (BrokenBarrierException e) {
        throw new RuntimeException(e);
    }
  }
}
