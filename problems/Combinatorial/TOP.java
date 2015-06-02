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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import jcell.*; //Use jcell package

public class TOP extends Problem
{
	private static final double maxFitness = 1; // Maximum Fitness Value
	
	private int F;  		// n�mero de v�rtices
	private int T;  		// n�mero de viajantes (travellers)
    private float deadline;	// tempo m�ximo por viagem
    
    private ArrayList<Vertice> vertices;	// vector de v�rtices
	private double matrix[][];				// matriz de dist�ncias
	
	private int totalScore;	// soma dos pesos de todas as tarefas que passaram o filtro
	
	private double bestFitness;
	private ArrayList<Integer>[] bestTrip;
    

    public TOP()
    {
    	super();
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    	    	    	
    	try {
        	BufferedReader reader = new BufferedReader(new FileReader("p1.4.r.txt"));
        	Scanner input = new Scanner(reader);

        	this.F = Integer.parseInt(DataLoading.getParameter(input));
    		this.T = Integer.parseInt(DataLoading.getParameter(input));
    		this.deadline = Float.parseFloat(DataLoading.getParameter(input));
    		this.vertices = new ArrayList<Vertice>();
    		
    		DataLoading.readFile(input, this.vertices);
    		
			// calcular a soma dos pesos de todas as tarefas antes da execu��o do filtro
			for (int i = 0; i <= this.F - 1; i++)
				this.totalScore += vertices.get(i).getScore();
			
			this.filter();
    		this.buildMatrix(); // construir a matriz de dist�ncias
    		
    		input.close();
    		
    		this.bestFitness = 0.0;
    		this.bestTrip = new ArrayList[this.T];
    		
    		for(int i=0 ; i<this.T ; i++)
    			this.bestTrip[i] = new ArrayList<Integer>();
    		
System.out.println(this.toString());
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
    
    
    public ArrayList<Integer>[] getBestTrip() {
    	
    	return this.bestTrip;
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
		// viagem -> array auxiliar que ir� conter as viagens que ser�o constru�das para cada viajante num dado cromossoma
		// viagens -> array auxiliar que ir� conter todos os v�rtices "ocupados" num dado instante num dado cromossoma
		// maxValues -> array auxiliar que ir� ser utilizado para guardar os �ndices dos genes de maior prioridade dum cromossoma
		// auxBlackList -> Lista auxiliar que ir� conter os �ndices dos genes de maior prioridade que n�o podem ser tomados
		// blackList -> Lista auxiliar que ir� conter os �ndices dos genes descartados ao longo duma viagem
    	// fl -> flag utilizada para assinalar a escolha dum gene
    	// flag -> flag utilizada para saber se o tempo m�ximo da viagem foi atingido
		// counter -> contador para o #posi��es que � necess�rio ignorar dos sucessivos arrays maxValues, por estarem na blacklist
    	// size -> contador para o n�mero de genes escolhidos nas viagens de todos os viajantes
    	
    	ArrayList<Integer>[] viagens_aux = new ArrayList[this.T];  	
    	ArrayList<Integer> maxValues = new ArrayList<Integer>(), viagens = new ArrayList<Integer>(), blackList = new ArrayList<Integer>(), auxBlackList = new ArrayList<Integer>();
    	TopIndividual intInd = (TopIndividual) ind;
    	Random r = new Random();
    	boolean fl = false, flag;
		double fitness = 0.0, time = 0.0;
		int j, lastVertice = 0, min, topPriorityGene = -1, random, counter;
		

		// inicializar o array que ir� conter as viagens
		for(j=0 ; j<this.T ; j++)
			viagens_aux[j] = new ArrayList<Integer>();
		
		
		// ciclo para percorrer os viajantes
		for(j=0 ; j<this.T ; j++) {
		
			flag=true;
			
			// ciclo para preencher ordenadamente o array viagem, at� que a sua dura��o seja m�xima
			while( flag ) {
						
				
//System.out.println("ENTROU!");
//System.out.println("fl: " + fl + "\nblackList.size: " + blackList.size() + "\nviagens.size: " + viagens.size());

//System.out.println("--------------- VIAGENS ---------------");
//for (int h=0 ; h < viagens.size() ; h++)
	//System.out.println("indice " + h + ": " + viagens.get(h));
//System.out.println("---------------------------------------");
				
//pressEnterToContinue();


				/* Ciclo utilizado para escolher o v�rtice de maior prioridade que caiba na viagem, executando os seguintes passos at�
				um v�rtice ser escolhido, ou at� todos serem percorridos
						-> Escolher os v�rtices de maior prioridade
						-> Aleatoriamente ir escolhendo um desses v�rtices, at� um encaixar na viagem, ou at� todos terem sido percorridos							
				*/			
				while( !fl && ((int) blackList.size()) + viagens.size() < this.variables - 2 ) {
					
					// inicializar a vari�vel que ir� conter o menor valor dos genes do cromossoma
					min = (this.variables >= 10 ? this.variables + 1 : 10);
					
					// Percorrer os genes do cromossoma
					for (int i = 1; i < this.variables - 1; i++) {
						
						/* se o gene que se encontra a ser processado tiver prioridade igual ou superior � m�xima prioridade encontrada,
						se ainda n�o estiver marcado para ser visitado por um viajante e se o gene n�o estiver na blacklist, indicando
						que j� ter� sido descartado desta viagem.... */
						if(intInd.getIntegerAllele(i) <= min && !viagens.contains(i) && !blackList.contains(i)) {
							
							if(intInd.getIntegerAllele(i) != min) {
								maxValues.clear();
								min = intInd.getIntegerAllele(i);
							}
							
							/* guardar o �ndice do gene na lista que cont�m 
							 os �ndices dos genes de valor m�ximo */
							maxValues.add(i);
						}
					}
					

					// se existirem v�rios genes com o mesmo valor (m�ximo)
					if(maxValues.size() > 1) {
//System.out.println("MAX VALUES > 1");
						// escolher um gene de forma aleat�ria
						r = new Random(System.currentTimeMillis());
						random = r.nextInt(maxValues.size());
						topPriorityGene = maxValues.get(random);
					}
					else { // se apenas existir um gene com o valor m�ximo
//System.out.println("MAX VALUES == 1");
						topPriorityGene = maxValues.get(0);
						random = 0;
					}
					
								
					 // se o gene escolhido couber na rota
			    	if (time + this.getDistance(lastVertice, topPriorityGene) + this.getDistance(topPriorityGene, this.variables - 1) <= this.deadline)
						fl = true;  // indicar a escolha dum gene
					else {
				 
	 					counter = 0;

						// percorrer todos os v�rtices de prioridade m�xima ainda n�o escolhidos at� um caber na viagem
						for(int k=0 ; !fl && (k+1)<maxValues.size() ; k++) {
							
							int kk;
							
							// colocar o seu �ndice no array de genes com prioridade m�xima, na blacklist
							auxBlackList.add(random + counter);
							blackList.add(maxValues.get(random + counter));
							
							
							// escolher novo gene com prioridade m�xima, de forma aleat�ria
							random = r.nextInt(maxValues.size() - (k+1));
				
							
							// ordenar os arrays blackList e auxBlackList
							Collections.sort(auxBlackList);
							Collections.sort(blackList);
														
  	
							// ciclo para contar o n�mero de posi��es que � preciso saltar do array maxValues, por estarem na blacklist
							for(counter=0, kk=0 ; kk <= k ; kk++)
								if(auxBlackList.get(kk) <= random+counter)
									counter++;
  	
							topPriorityGene = maxValues.get(random + counter);	// guardar o �ndice do novo gene escolhido aleatoriamente
//System.out.println("TOP PRIORITY GENE: " + topPriorityGene);			

							
							// se o tempo m�ximo da viagem com o novo gene escolhido n�o exceder deadline
							if( time + this.getDistance( (viagens_aux[j].size()==0) ? 0 : lastVertice, topPriorityGene) + 
									   this.getDistance(topPriorityGene, this.variables - 1) <= this.deadline ) {
																	
								fl = true;  // assinalar a escolha dum gene
							}
						}

						// limpar a blacklist, para que numa pr�xima itera��o do ciclo acima esta esteja vazia
						auxBlackList.clear();
						
						blackList.add(maxValues.get(random + counter));
						
					}	    	
				}
				
				// limpar a blacklist e o array maxValues, para que numa pr�xima itera��o do ciclo acima estes estejam vazios
				blackList.clear();
				maxValues.clear();
				
				
				/* A 2� restri��o � implementada dentro do if abaixo */		
				
				// se um gene tiver sido seleccionado para ser colocado na viagem
				if(fl) {
					
					// atualizar a vari�vel t com o tempo j� percorrido
				    time += this.getDistance(lastVertice, topPriorityGene); // atualiza o tempo de dura��o da rota
																		
					// atualizar arrays viagens e viagem com esse gene
					viagens_aux[j].add(topPriorityGene);
					viagens.add(topPriorityGene);
					
					// atualizar o peso do alelo
					intInd.setAllele(topPriorityGene, viagens.size());
		  	
					// na pr�xima itera��o deste ciclo, o �ltimo v�rtice tomado ser� o que agora foi inserido nas rotas
		    		lastVertice = topPriorityGene;
		  	
					// mas se o tempo m�ximo da viagem tiver sido atingido
					if( this.deadline == time + this.getDistance(topPriorityGene, this.variables - 1) )
						flag = false;  // atualizar a flag para sair do ciclo
					
					// mudar o valor da flag que assinala a escolha dum gene para colocar na viagem para recome�ar novo processo
					fl = false;
					
/*
System.out.println("top priority gene: " + topPriorityGene);
System.out.println("SCORE: " + vertices.get(topPriorityGene).getScore());

System.out.println("--------------- VIAGENS ---------------");
for (int h=0 ; h < viagens.size() ; h++)
	System.out.println("indice " + h + ": " + viagens.get(h));
System.out.println("---------------------------------------");
*/
				    
					// atualiza o valor do fitness
					fitness += vertices.get(topPriorityGene).getScore();
				}
				else  // caso contr�rio (se nenhum dos v�rtices de alta prioridade couber no fim da viagem)
					flag = false;  // atualizar a flag para sair do ciclo
			}
			
			
			// reinicializar as vari�veis necess�rias para controlar a evolu��o duma viagem dum viajante
			lastVertice = 0;
			time = 0.0;
		}
		
		fitness = fitness/this.totalScore;
		
		if(fitness > this.bestFitness) {
			
			this.bestFitness = fitness;
			this.bestTrip = viagens_aux;
			
			
			for(int i=0 ; i<this.T ; i++) {

				System.out.println("-------------- VIAGEM " + i + " ---------------");
				
				for (int h=0 ; h < this.bestTrip [i].size() ; h++)
					System.out.println("indice " + h + ": " + this.bestTrip [i].get(h));
				
				System.out.println("---------------------------------------");
			}
			
			System.out.println("\n");
/*
System.out.println("--------------- VIAGENS ---------------");
for (int h=0 ; h < viagens.size() ; h++)
	System.out.println("indice " + h + ": " + viagens.get(h));
System.out.println("---------------------------------------");*/
		}
/*		else {
			
			System.out.println("PASSOU");
			
			for(int i=0 ; i<this.T ; i++) {

				System.out.println("-------------- VIAGEM " + i + " ---------------");
				
				for (int h=0 ; h < this.bestTrip [i].size() ; h++)
					System.out.println("indice " + h + ": " + this.bestTrip [i].get(h));
				
				System.out.println("---------------------------------------");
			}
		}*/
			
//		this.pressEnterToContinue();
		
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
    	
    	sb.append("Total Premios: " + this.totalScore + "\n");
    	
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
    

    
    private static void pressEnterToContinue() {
    	
        System.out.println("Press ENTER to continue...");
        
        try {
        
        	System.in.read();
            
        	while (System.in.available() > 0)
                System.in.read(); //flush the buffer
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
