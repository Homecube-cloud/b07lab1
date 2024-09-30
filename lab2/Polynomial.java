
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Polynomial{
	//field
	double[] coefficients;
	int[] exponents; 

	//no argument constructor
	public Polynomial(){
		coefficients = new double[]{0};
		exponents = new int[]{0}; 		
	}
	
	//argument constructor
	public Polynomial (double[] coef, int[] expo){
		coefficients = coef;
		exponents = expo; 
	}

	//file constructor
    public Polynomial(File file) {
        try {
            //read from file
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String polynomialString = scanner.nextLine();
                parsePolynomial(polynomialString);
            }
            scanner.close();
        } 
		//if no file then it will go to default 
		catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            coefficients = new double[]{0};  
            exponents = new int[]{0};         
        }
    }

	public void parsePolynomial(String polynomial) {

        //spliting polynomial based on + or -
        String[] terms = polynomial.split("(?=[+-])");
        
        //temp values
        double[] temp_coef = new double[terms.length];
        int[] temp_expo = new int[terms.length];
        int count = 0;

		for (int i = 0; i < terms.length; i++) {
			String term = terms[i]; 
			//default values
			double coefficient = 1; 
			int exponent = 0;

			//when it is not constant
			if (term.contains("x")) {
				//split into coef and expo
				String[] parts = term.split("x");

				//determine if coef + or - 
				if (!parts[0].isEmpty()) {
					if (parts[0].equals("+")) {
						coefficient = 1; 
					} 
					else if (parts[0].equals("-")) {
						coefficient = -1; 
					} 
					//parse the coef
					else {
						coefficient = Double.parseDouble(parts[0]); 
					}
				}

				//default expo to 1 
				exponent = 1;

				//if expo then parse it
				if (parts.length > 1 && !parts[1].isEmpty()) {
					exponent = Integer.parseInt(parts[1]); 
				}
			} 

			//constant term 
			else {
                coefficient = Double.parseDouble(term);
                exponent = 0;
            }

            //store into temp
            temp_coef[count] = coefficient;
            temp_expo[count] = exponent;
            count++;
        }

        //create array with exact values 
        coefficients = new double[count];
        exponents = new int[count];
        for (int i = 0; i < count; i++) {
            coefficients[i] = temp_coef[i];
            exponents[i] = temp_expo[i];
        }
    }

	public void saveToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
			
            for (int i = 0; i < coefficients.length; i++) {
	
                //the sign for each term
                if (i > 0) {
                    writer.print(coefficients[i] >= 0 ? "+" : "");
                }

                //append the coefficeint 
                writer.print(coefficients[i]);

                //append the varible(x) term, expo must be more than 1
                if (exponents[i] > 0) {
                    writer.print("x");
                    if (exponents[i] > 1) {
                        writer.print(exponents[i]);
                    }
                }
            }
        } 
		//error warning 
		catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

	public Polynomial add(Polynomial poly){
		 // Calculate the maximum possible size of the result
    int maxLength = this.coefficients.length + poly.coefficients.length;
    
    double[] result_coef = new double[maxLength];
    int[] result_expo = new int[maxLength]; 
    int result_index = 0; 
    
    // Copy coefficients and exponents from the first polynomial
    for (int i = 0; i < this.coefficients.length; i++) {
        result_coef[result_index] = this.coefficients[i]; 
        result_expo[result_index] = this.exponents[i]; 
        result_index++;
    }
    
    // Combine with coefficients and exponents from the second polynomial
    for (int i = 0; i < poly.coefficients.length; i++) {
        boolean found = false; 
        
        // Check if exponent already exists in the result
        for (int j = 0; j < result_index; j++) {
            if (poly.exponents[i] == result_expo[j]) { 
                result_coef[j] += poly.coefficients[i]; 
                found = true; 
                break; 
            }
        }
        
        // If the exponent doesn't exist, add it to the result
        if (!found) {
            result_coef[result_index] = poly.coefficients[i];
            result_expo[result_index] = poly.exponents[i];
            result_index++;
        }
    }

    // Create arrays of exact size needed
    double[] final_coef = new double[result_index];
    int[] final_expo = new int[result_index];

    // Fill in the final arrays
    for (int i = 0; i < result_index; i++) {
        final_coef[i] = result_coef[i];
        final_expo[i] = result_expo[i];
    }
    
    return new Polynomial(final_coef, final_expo);	
		
	}
	
	public Polynomial multiply(Polynomial poly) {
		
		//temp values for storing 
		double[] temp_coef = new double[this.coefficients.length * poly.coefficients.length];
		int[] temp_expo = new int[this.coefficients.length * poly.coefficients.length];
		int result_index = 0;

		//multiplication 
        for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < poly.coefficients.length; j++) {
            
				//combine like terms 
				double new_coef = this.coefficients[i] * poly.coefficients[j];
				int new_expo = this.exponents[i] + poly.exponents[j];
				
				boolean found = false;
				//if expo match then combine
				for (int k = 0; k < result_index; k++) {
					if (temp_expo[k] == new_expo) {
						temp_coef[k] += new_coef; 
						found = true;
						break;
					}
				}
				//if not then create new term
				if (!found) {
					temp_coef[result_index] = new_coef;
					temp_expo[result_index] = new_expo;
					result_index++;
				}
			}
		}
		
		//create exact array size needed
		double[] final_coef = new double[result_index];
		int[] final_expo = new int[result_index];

    
		for (int i = 0; i < result_index; i++) {
			final_coef[i] = temp_coef[i];
			final_expo[i] = temp_expo[i];
		}

		return new Polynomial(final_coef, final_expo);
		    
    }

	public double evaluate(double x){
		//set to zero
		double result = 0.0;
		
		//itterate thru the array for the given value
		for (int i = 0; i < coefficients.length; i++){
			result += coefficients[i] * Math.pow(x, exponents[i]);
		}
		return result; 
	}

	public boolean hasRoot(double x){
		return evaluate(x) == 0; //means there is root 
	}

}