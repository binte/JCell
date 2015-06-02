/* ------------------------------------------
   File: TOP.java
   Author: Celso Coutinho
   Description
   It defines a specific subclass of Problem.
   The TOP Problem
   ------------------------------------------*/

package problems.Combinatorial;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import jcell.*; //Use jcell package

public class TOP extends Problem
{
	private static final double maxFitness = 1; // Maximum Fitness Value
	
	private int F;  		// n�mero de v�rtices pass�veis de serem atingidos
	private int T;  		// n�mero de viajantes (travellers)
    private float deadline;	// tempo m�ximo por viagem
    
    private ArrayList<Vertice> vertices;	// vector de v�rtices
	private double matrix[][];				// matriz de dist�ncias
	
	private int totalScore;	// soma dos pesos de todas as tarefas que passaram o filtro
    

    public TOP()
    {
    	super();
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    	    	    	
    	try {
        	BufferedReader reader = new BufferedReader(new FileReader("p2.2.b.txt"));
        	Scanner input = new Scanner(reader);

        	this.F = Integer.parseInt(DataLoading.getParameter(input));
    		this.T = Integer.parseInt(DataLoading.getParameter(input));
    		this.deadline = Float.parseFloat(DataLoading.getParameter(input));
    		this.vertices = new ArrayList<Vertice>();
    		
    		DataLoading.readFile(input, this.vertices);
    		
			this.filter();
    		this.buildMatrix(); // construir a matriz de dist�ncias
    		
System.out.println(this.toString());

			// calcular a soma dos pesos de todas as tarefas que passaram o filtro
			for (int i = 1; i < this.variables; i++)
				this.totalScore += vertices.get(i).getScore();
    		
    		input.close();
    	}
    	catch (FileNotFoundException ex) {
    		System.err.println("Unable to open file: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);
    	}
    	catch (NumberFormatException ex) {
    		System.err.println("Unable to read number: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);    		
    	}
    	catch (Exception ex) {
    		System.err.println("Error: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);
    	}
    }
    
    /* Filtrar os v�rtices que nunca ser�o tomados, ou seja, que n�o respeitam a seguinte restri��o:
     * 
     * dist�ncia(origem -> v�rtice) + dist�ncia(v�rtice -> destino)  >  TMax
     *
     * Para tal, esses v�rtices ser�o removidos do Vector de V�rtices
     */
    public void filter() {
    	
    	int i, j;
    	ArrayList<Integer> novo = new ArrayList<Integer>();
    	
    	// colocar os ind�ces dos v�rtices que n�o poder�o ser tomados num novo vector
    	for(i=1, j=0 ; i<=this.F-2 ; i++) {
    		
    		if (  Math.sqrt( Math.pow((this.vertices.get(i).getX() - this.vertices.get(0).getX()), 2) + 
    					     Math.pow((this.vertices.get(i).getY() - this.vertices.get(0).getY()), 2) ) + 
    			  Math.sqrt( Math.pow((this.vertices.get(this.F-1).getX() - this.vertices.get(i).getX()), 2) + 
    						 Math.pow((this.vertices.get(this.F-1).getY() - this.vertices.get(i).getY()), 2) ) > this.deadline  )  {
    			
    			novo.add(i);
    			j++;			
    		}
    	}
    	
    	for(i=0, j=0 ; i<novo.size() ; i++, j++)
    		this.vertices.remove(novo.get(i).intValue() - j);

    	// atualizar o n�mero de v�rtices ating�veis do problema
    	this.variables = this.vertices.size();    	
    }


    /* Construir a matriz de dist�ncias sem repetir dados. Exemplos:
     * 
     * O trajecto 4->7 � o mesmo que o trajecto 7->4
     * O trajecto 5->6 � mesmo que o trajecto 6->5
     *
     * De igual forma, a dist�ncia entre um v�rtice e esse mesmo v�rtice (� nula) n�o � calculada
     *
     *
     * 	 A matriz constru�da tem os valores calculados truncados a 3 casas decimais, sendo posteriormente multiplicados por 1000
     * para poupar espa�o em mem�ria, dado que os floats ocupam mais bytes. Para tal, � tamb�m necess�ria a utiliza��o duma
     * fun��o auxiliar (round) que arredonda os valores calculados 
     */
    public void buildMatrix() {
    	
    	int i, j;
    	
    	//alocar espa�o para a matriz
    	this.matrix = new double[this.variables][this.variables];
    	
    	for(i=0 ; i<this.variables ; i++){
    				
    		for(j=i+1 ; j<this.variables ; j++) {
    							
    			matrix[i][j] = Math.sqrt( Math.pow((this.vertices.get(j).getX() - this.vertices.get(i).getX()), 2) + 
    									  Math.pow((this.vertices.get(j).getY() - this.vertices.get(i).getY()), 2) );
    		}
    	}
    }

    /* Retorna a dist�ncia entre dois v�rtices cujos �ndices foram recebidos */
    public double getDistance(int i, int j) {
    	
    	//System.out.println("dist entre " + this.vertices.get(i).getID() + " e " + this.vertices.get(j).getID() + ": " + ((i>j) ? this.matrix[j][i] : this.matrix[i][j]));
    	
    	return ((i>j) ? this.matrix[j][i] : this.matrix[i][j]);
    }

    public Object eval(Individual ind)
    {
		BinaryIndividual bi = (BinaryIndividual)ind;
		double fitness = 0.0;
		boolean feasible = true;
		double time = 0;
		int lastVertice = 0;
		double penalty = 0.0;
		double first = 0.0; // soma dos pesos das tarefas com gene = 0
		
	
		for (int i = 1; i < this.variables; i++) {

		    if (bi.getBooleanAllele(i)) { // se gene na posi��o 'i' = 1

		    	if (time + this.getDistance(lastVertice, i) + this.getDistance(i, this.variables-1) > deadline) { // se o plano for inadmiss�vel
				    feasible = false;
				    penalty += vertices.get(i).getScore();
				} 
				else {// se o plano for admiss�vel
					fitness += vertices.get(i).getScore(); // atualiza o valor do fitness
				    time += this.getDistance(lastVertice, i); // atualiza o timing do plano
				}
				
				lastVertice = i;
		    }
	
		    if (!bi.getBooleanAllele(i)) // se gene na posi��o 'i' = 0
		    	first += vertices.get(i).getScore();	
		}
	    
		if (feasible) { // se o plano tiver sido classificado como admiss�vel
		    fitness = 1.0 / (this.totalScore + 1 - fitness);
		} else { // caso contr�rio
		    fitness = 1 / (this.totalScore*2 + penalty);
		}

		return new Double(fitness);
    }
    
    /* Retorna uma string representativa dos dados do problema */
    public String toString() {
      
    	StringBuilder sb = new StringBuilder();
    	int i, j;

	    sb.append("(");
	    sb.append(this.F);
	    sb.append(":");
	    sb.append(this.variables);
	    sb.append(", ");
	    sb.append(this.T);
	    sb.append(", ");
	    sb.append(this.deadline);
	    sb.append(")\n\n");

    	sb.append("-----------------------------------\n");
    	sb.append("-------- POSSIBLE VERTICES --------\n");
    	sb.append("-----------------------------------\n");
    	
    	for (i=0; i<this.variables; i++)
    		sb.append(this.vertices.get(i).toString() + "\n");

    	sb.append("\n");
    	sb.append("------------------------------------\n");
    	sb.append("-------------- MATRIX --------------\n");
    	sb.append("------------------------------------\n");

    	sb.append("	");

    	//imprimir os n�meros das colunas
    	for(i=0 ; i<this.variables ; i++)
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    	sb.append("\n");

    	for(i=0 ; i<this.variables ; i++){
    		
    		//imprimir o n�mero da linha que vai ser imprimida
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    		//imprimir tabs antes de come�ar a imprimir os valores da linha
    		for(int k=0 ; k<=i ; k++)
    			sb.append("	");

    		//imprimir os valores da linha truncados (e arredondados) a 4 d�gitos
    		for(j=i+1 ; j<this.variables ; j++)			
    				sb.append(matrix[i][j] + "	");

    		sb.append("\n");
    	}
    	
    	return sb.toString();
    }
}
