import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread {
  private int a4;
  private int d4;
  private int fromIndex;
  private int toIndex;
  private int[] Zh;

  public T4() {
    this.fromIndex = 3 * Data.H;
    this.toIndex = 4 * Data.H - 1;
  }

  @Override
  public void run() {
    try {
        System.out.println("T4 thread started");

        // Введення Z
        Data.fillVector(Data.Z);

        // Введення MD
        Data.fillMatrix(Data.MD);

        Data.B1.await(); // Чекаємо на завершення введення данних у T1 та T3

        // Обчислення 1
        Zh = Data.getPartOfVector(Data.Z, fromIndex, toIndex);
        a4 = Data.getMaxElementOfVector(Zh);

        /**
         * Обчислення 2
         * КД1
         * Безпечний запис СР а
         */
        Data.a.updateAndGet(a -> Math.max(a, a4));

        // Сигнал задачам T1, T2, T3 про завершення обчислення СР a
        Data.S4.release(3);

        // Очікуємо завершення обчислення 2 у задачач T1, T2, T3
        Data.S1.acquire();
        Data.S2.acquire();
        Data.S3.acquire();

        /**
         * Копія a4 = a
         * КД2, CS1
         */
        Data.CS1.lock();
        a4 = Data.a.get();
        Data.CS1.unlock();

        /**
         * Копія d4 = d
         * КД3, CS2
         */
        synchronized(Data.CS2) {
          d4 = Data.d;
        }

        // Обчислення 3
        Data.calculateExpression(a4, d4, fromIndex, toIndex);

        System.out.println("Thread T4 finished");

        // Сигнал задачі T1 про заврешення виконання обчислення 3
        Data.S5.release();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    } catch (BrokenBarrierException e) {
        throw new RuntimeException(e);
    }
  }
}
