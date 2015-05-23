/*
 * @(#)Interpreta.java 1.0
 * 
 * Programa que trata los resultados del takeover del cGA y obtiene los valores
 * medios de todas las ejecuciones Bernabé Dorronsoro Díaz 08/07/2006
 * 
 *  
 */

package repast;

import java.io.*;
import java.util.*;

class Interpreta {
	private static Vector res; // where the results are

	public static void main(String args[]) {
		try {

			String number;
			double num = 0.0;
			int tests = 1;
			int generation = 0;

			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			StringTokenizer st = new StringTokenizer(number = br.readLine());

			res = new Vector();
			num = new Double(number).doubleValue();
			
			while (num != 1.0) // while there are data in the file
			{
				res.add(new Double(num));
				number = br.readLine();
				generation++;
				num = new Double(number).doubleValue();
			}

			generation = 0;
			tests++;
			res.add(new Double(num));
			number = br.readLine();
			
			while (number != null) // while there are data in the file
			{
				num = new Double(number).doubleValue();
				
				if ((num == 1.0) && (generation<res.size()-1))
				{
					while (generation < res.size())  // add 1.0 to all the values up to the end
					{
						res.add(generation, new Double(1.0+((Double)res.get(generation)).doubleValue()));
						generation++;
					}
					tests++;
					generation = 0;
				}

				else if (num == 1.0)
				{
					res.add(new Double(tests));
					tests++;
					generation = 0;
				}
				
				else if (generation<res.size()-1)
					res.add(generation, new Double(num+((Double)res.get(generation)).doubleValue()));
				else if (generation>=res.size()-1)
					res.add(new Double((double)tests-1+num));
				
				number = br.readLine();
				generation++;
			}
			
			tests--;
			
			for (int i=0; i<res.size(); i++) // print the result
			{
				System.out.println(((Double)res.get(i)).doubleValue() / (double)tests);
			}
			System.out.println("Numero de pruebas: " + tests);

			// bucle que vaya leyendo las sucesivas ejecuciones y vaya
			// actualizando la lista creada en el anterior
		} catch (EOFException eof) {
			System.out.println("fin del fichero");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
