package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PrimeFinderThread extends Thread {

    int a, b;
    private long estimatedTime;
    private List<Integer> primes;

    public PrimeFinderThread(int a, int b) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
    }

    private synchronized void addPrimes(int i) throws InterruptedException {
        estimatedTime = System.currentTimeMillis() - Control.startTime;
        while (estimatedTime >= Control.TMILISECONDS) {
            System.out.println("Esperando");
            wait();
        }
        if (isPrime(i) && i <= b) {
            primes.add(i);
        }

        notify();
    }

    public synchronized void getListPrimes() throws InterruptedException {
        Scanner s = new Scanner(System.in);
        if (s.nextLine() != null) {
            System.out.println("Intervalo (" + a + "," + b + ")");
            notify();
        }
        estimatedTime = System.currentTimeMillis() - Control.startTime;
        while (estimatedTime < Control.TMILISECONDS) {
            wait();
        }
        System.out.println("Intervalo (" + a + "," + b + ") primos son: " + primes.size());
        Thread.sleep(1000);
        Control.startTime = System.currentTimeMillis();

    }

    @Override
    public void run() {
        int i = a;
        while (true) {
            try {
                addPrimes(i);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            i += 1;
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n % 2 != 0;
            for (int i = 3; ans && i * i <= n; i += 2) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

}
