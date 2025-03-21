/**
* Лабораторна робота №2
* Варіант 24
* MU = (MD * MC) * d + max(Z) * MR
* Якимчук Кіріл Сергійович
* Група ІМ-24
* 21.03.2025
*/

public class Program {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            Data.initialize();

            T1 t1 = new T1();
            T2 t2 = new T2();
            T3 t3 = new T3();
            T4 t4 = new T4();

            t1.start();
            t2.start();
            t3.start();
            t4.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch(Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.printf("Time: %d ms", endTime - startTime);
    }
}
