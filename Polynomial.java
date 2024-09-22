public class Polynomial{
	//field
	double[] coefficients;

	//no argument constructor
	public Polynomial(){
		coefficients = new double[]{0}; 
	}
	
	//argument constructor
	public Polynomial (double[] coef){
		coefficients = coef;
	}

	public Polynomial add(Polynomial poly){
		//getting coefficients of both polynomials 
		double[] coefficient1 = coefficients;
		double[] coefficient2 = poly.coefficients; 
		
		//getting length of resulting polynomial
		int maxLength = Math.max(coefficient1.length, coefficient2.length);
        double[] result = new double[maxLength];
		
		
		for (int i = 0; i < maxLength; i++) {
			
			//takes coefficients from poly1
            if (i < coefficient1.length) {
                result[i] += coefficient1[i];
            }
			//takes from poly2
            if (i < coefficient2.length) {
                result[i] += coefficient2[i];
            }
        }
		return new Polynomial(result);
	}

	public double evaluate(double x){
		//set to zero
		double result = 0.0;
		
		//itterate thru the array for the given value
		for (int i = 0; i < coefficients.length; i++){
			result += coefficients[i] * Math.pow(x, i);
		}
		return result; 
	}

	public boolean hasRoot(double x){
		return evaluate(x) == 0; //means there is root 
	}

}
