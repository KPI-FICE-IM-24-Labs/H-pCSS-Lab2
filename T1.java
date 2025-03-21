import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
  private int a1;
  private int d1;
  private int fromIndex;
  private int toIndex;
  private int[] Zh;

  public T1() {
    this.fromIndex = 0;
    this.toIndex = Data.H - 1;
  }

  @Override
  public void run() {
    try {
        System.out.println("T1 thread started");

        //  Введення d
        Data.d = 1;

        Data.B1.await(); // Чекаємо на завершення введення данних у T3 та T4

        // Обчислення 1
        Zh = Data.getPartOfVector(Data.Z, fromIndex, toIndex);
        a1 = Data.getMaxElementOfVector(Zh);

        /**
         * Обчислення 2
         * КД1
         * Безпечний запис СР а
         */
        Data.a.updateAndGet(a -> Math.max(a, a1));

        // Сигнал задачам T2, T3, T4 про завершення обчислення СР a
        Data.S1.release(3);

        // Очікуємо завершення обчислення 2 у задачач T2, T3, T4
        Data.S2.acquire();
        Data.S3.acquire();
        Data.S4.acquire();

        /**
         * Копія a1 = a
         * КД2, CS1
         */
        Data.CS1.lock();
        a1 = Data.a.get();
        Data.CS1.unlock();

        /**
         * Копія d1 = d
         * КД3, CS2
         */
        synchronized(Data.CS2) {
          d1 = Data.d;
        }

        // Обчислення 3
        Data.calculateExpression(a1, d1, fromIndex, toIndex);

        // Очікуємо завершення обчислення 3 у задачах T2, T3, T4
        Data.S5.acquire(3);

        // Вивід результату
        Data.toString(Data.MU);

        System.out.println("Thread T1 finished");
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    } catch (BrokenBarrierException e) {
        throw new RuntimeException(e);
    }
  }
}
