import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread {
  private int a2;
  private int d2;
  private int fromIndex;
  private int toIndex;
  private int[] Zh;

  public T2() {
    this.fromIndex = Data.H;
    this.toIndex = 2 * Data.H - 1;
  }

  @Override
  public void run() {
    try {
        System.out.println("T2 thread started");

        Data.B1.await(); // Чекаємо на завершення введення данних у T1, T3 та T4

        // Обчислення 1
        Zh = Data.getPartOfVector(Data.Z, fromIndex, toIndex);
        a2 = Data.getMaxElementOfVector(Zh);

        /**
         * Обчислення 2
         * КД1
         * Безпечний запис СР а
         */
        Data.a.updateAndGet(a -> Math.max(a, a2));

        // Сигнал задачам T1, T3, T4 про завершення обчислення СР a
        Data.S2.release(3);

        // Очікуємо завершення обчислення 2 у задачач T1, T3, T4
        Data.S1.acquire();
        Data.S3.acquire();
        Data.S4.acquire();

        /**
         * Копія a2 = a
         * КД2, CS1
         */
        Data.CS1.lock();
        a2 = Data.a.get();
        Data.CS1.unlock();

        /**
         * Копія d2 = d
         * КД3, CS2
         */
        synchronized(Data.CS2) {
          d2 = Data.d;
        }

        // Обчислення 3
        Data.calculateExpression(a2, d2, fromIndex, toIndex);

        System.out.println("Thread T2 finished");

        // Сигнал задачі T1 про заврешення виконання обчислення 3
        Data.S5.release();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    } catch (BrokenBarrierException e) {
        throw new RuntimeException(e);
    }
  }
}
