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
	
	private int F;  		// número de vértices passíveis de serem atingidos
	private int T;  		// número de viajantes (travellers)
    private float deadline;	// tempo máximo por viagem
    
    private ArrayList<Vertice> vertices;	// vector de vértices
	private double matrix[][];				// matriz de distâncias
	
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
    		this.buildMatrix(); // construir a matriz de distâncias
    		
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
    
    /* Filtrar os vértices que nunca serão tomados, ou seja, que não respeitam a seguinte restrição:
     * 
     * distância(origem -> vértice) + distância(vértice -> destino)  >  TMax
     *
     * Para tal, esses vértices serão removidos do Vector de Vértices
     */
    public void filter() {
    	
    	int i, j;
    	ArrayList<Integer> novo = new ArrayList<Integer>();
    	
    	// colocar os indíces dos vértices que não poderão ser tomados num novo vector
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

    	// atualizar o número de vértices atingíveis do problema
    	this.variables = this.vertices.size();    	
    }


    /* Construir a matriz de distâncias sem repetir dados. Exemplos:
     * 
     * O trajecto 4->7 é o mesmo que o trajecto 7->4
     * O trajecto 5->6 é mesmo que o trajecto 6->5
     *
     * De igual forma, a distância entre um vértice e esse mesmo vértice (é nula) não é calculada
     *
     *
     * 	 A matriz construída tem os valores calculados truncados a 3 casas decimais, sendo posteriormente multiplicados por 1000
     * para poupar espaço em memória, dado que os floats ocupam mais bytes. Para tal, é também necessária a utilização duma
     * função auxiliar (round) que arredonda os valores calculados 
     */
    public void buildMatrix() {
    	
    	int i, j;
    	
    	//alocar espaço para a matriz
    	this.matrix = new double[this.variables][this.variables];
    	
    	for(i=0 ; i<this.variables ; i++){
    				
    		for(j=i+1 ; j<this.variables ; j++) {
    							
    			matrix[i][j] = Math.sqrt( Math.pow((this.vertices.get(j).getX() - this.vertices.get(i).getX()), 2) + 
    									  Math.pow((this.vertices.get(j).getY() - this.vertices.get(i).getY()), 2) );
    		}
    	}
    }

    /* Retorna a distância entre dois vértices cujos índices foram recebidos */
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

		    if (bi.getBooleanAllele(i)) { // se gene na posição 'i' = 1

		    	if (time + this.getDistance(lastVertice, i) + this.getDistance(i, this.variables-1) > deadline) { // se o plano for inadmissível
				    feasible = false;
				    penalty += vertices.get(i).getScore();
				} 
				else {// se o plano for admissível
					fitness += vertices.get(i).getScore(); // atualiza o valor do fitness
				    time += this.getDistance(lastVertice, i); // atualiza o timing do plano
				}
				
				lastVertice = i;
		    }
	
		    if (!bi.getBooleanAllele(i)) // se gene na posição 'i' = 0
		    	first += vertices.get(i).getScore();	
		}
	    
		if (feasible) { // se o plano tiver sido classificado como admissível
		    fitness = 1.0 / (this.totalScore + 1 - fitness);
		} else { // caso contrário
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

    	//imprimir os números das colunas
    	for(i=0 ; i<this.variables ; i++)
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    	sb.append("\n");

    	for(i=0 ; i<this.variables ; i++){
    		
    		//imprimir o número da linha que vai ser imprimida
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    		//imprimir tabs antes de começar a imprimir os valores da linha
    		for(int k=0 ; k<=i ; k++)
    			sb.append("	");

    		//imprimir os valores da linha truncados (e arredondados) a 4 dígitos
    		for(j=i+1 ; j<this.variables ; j++)			
    				sb.append(matrix[i][j] + "	");

    		sb.append("\n");
    	}
    	
    	return sb.toString();
    }
}
