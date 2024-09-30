
import java.io.File;

public class Driver {
	public static void main(String [] args) {
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(10));
		
		double [] c1 = {6,5};
		int [] ex1 = {3,0}; //added expo for poly 1
		Polynomial p1 = new Polynomial(c1, ex1); //added ex1
		
		double [] c2 = {-2, 9};
		int [] ex2 = {4, 0}; //expo for poly 2
		Polynomial p2 = new Polynomial(c2, ex2);//added ex2
		
		Polynomial sum = p1.add(p2);
        System.out.println("s(0) = " + sum.evaluate(0));
		System.out.println("s(1) = " + sum.evaluate(1));

        // Check if 1 is a root of the sum
        if (sum.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");

        // Multiply Polynomials
        Polynomial product = p1.multiply(p2);
        System.out.println("Product at x = 0: " + product.evaluate(0));
		System.out.println("Product at x = 1: " + product.evaluate(1));
	}
}