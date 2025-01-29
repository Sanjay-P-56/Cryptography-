import java.util.Scanner;

public class ExtendedEuclidean {
    public static void extendedEuclidean(int a, int b) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s%n", "q", "r1", "r2", "r", "t1", "t2", "t");
        System.out.println("-------------------------------------------------------------");

        int r1 = a, r2 = b;
        int t1 = 1, t2 = 0;

        while (r2 > 0) {
            int q = r1 / r2;
            int r = r1 % r2;
            int t = t1 - q * t2;

            System.out.printf("%-10d %-10d %-10d %-10d %-10d %-10d %-10d%n", q, r1, r2, r, t1, t2, t);

            r1 = r2;
            r2 = r;
            t1 = t2;
            t2 = t;
        }

        System.out.println("-------------------------------------------------------------");
        System.out.println("GCD: " + r1);
        System.out.println("Multiplicative Inverse: " + (t1 < 0 ? t1 + b : t1));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first integer (a): ");
        int a = scanner.nextInt();

        System.out.print("Enter second integer (b): ");
        int b = scanner.nextInt();

        extendedEuclidean(a, b);
        scanner.close();
    }
}
