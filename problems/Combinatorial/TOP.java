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
	
	private int F;  		// número de vértices
	private int T;  		// número de viajantes (travellers)
    private float deadline;	// tempo máximo por viagem
    
    private ArrayList<Vertice> vertices;	// vector de vértices
	private double matrix[][];				// matriz de distâncias
	
	private int totalScore;	// soma dos pesos de todas as tarefas que passaram o filtro
	private int collected;  // some dos prémios recolhidos 
	
	private double bestFitness;
	private ArrayList<Integer>[] bestTrip;
    

    public TOP(String dataFile)
    {
    	super(dataFile);
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    	    	    	
    	try {
        	BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        	Scanner input = new Scanner(reader);

        	this.F = Integer.parseInt(DataLoading.getParameter(input));
    		this.T = Integer.parseInt(DataLoading.getParameter(input));
    		this.deadline = Float.parseFloat(DataLoading.getParameter(input));
    		this.vertices = new ArrayList<Vertice>();
    		
    		DataLoading.readFile(input, this.vertices);
    		
			// calcular a soma dos pesos de todas as tarefas antes da execução do filtro
			for (int i = 0; i <= this.F - 1; i++)
				this.totalScore += vertices.get(i).getScore();
			
			this.collected = 0;
			
			this.filter();
    		this.buildMatrix(); // construir a matriz de distâncias
    		
    		input.close();
    		
    		this.bestFitness = 0.0;
    		this.bestTrip = new ArrayList[this.T];
    		
    		for(int i=0 ; i<this.T ; i++)
    			this.bestTrip[i] = new ArrayList<Integer>();
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
    
    
    public int getTotalScore() {
    	
    	return this.totalScore;
    }
    
    public int getCollected() {
    	
    	return this.collected;
    }
    
    
    public ArrayList<Integer>[] getBestTrip() {
    	
    	return this.bestTrip;
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
		// viagem -> array auxiliar que irá conter as viagens que serão construídas para cada viajante num dado cromossoma
		// viagens -> array auxiliar que irá conter todos os vértices "ocupados" num dado instante num dado cromossoma
    	// viagens_aux -> array auxiliar que irá conter cada viagem num arrayList distinto
		// maxValues -> array auxiliar que irá ser utilizado para guardar os índices dos genes de maior prioridade dum cromossoma
		// auxBlackList -> Lista auxiliar que irá conter os índices dos genes de maior prioridade que não podem ser tomados
		// blackList -> Lista auxiliar que irá conter os índices dos genes descartados ao longo duma viagem
    	// times -> Lista que contém os tempos atuais de todas as viagens, incluindo a chegada ao destino
    	// fl -> flag utilizada para assinalar a escolha dum gene
    	// flag -> flag utilizada para saber se continua a ser possível inserir vértices nas viagens
    	// counter -> contador para o #posições que é necessário ignorar dos sucessivos arrays maxValues, por estarem na blacklist
    	
    	ArrayList<Integer>[] viagens_aux = new ArrayList[this.T];
    	ArrayList<Integer> maxValues = new ArrayList<Integer>(), viagens = new ArrayList<Integer>(), blackList = new ArrayList<Integer>(), auxBlackList = new ArrayList<Integer>();
    	ArrayList<Double> times = new ArrayList<Double>(this.T);
    	TopIndividual topInd = (TopIndividual) ind;
    	Random r = new Random();
    	boolean flag = true, fl = false;
		double fitness = 0.0, min_dist = this.deadline + 1;
		int min, topPriorityGene = -1, random, counter, trip = -1, pos = -1;
		

		// inicializar o array que irá conter as viagens
		for(int j=0 ; j<this.T ; j++)
			viagens_aux[j] = new ArrayList<Integer>();
		
		// inicializar o array que irá conter os tempos das viagens
		for(int j=0 ; j<this.T ; j++)
			times.add(0.0);

		/*
		 * Enquanto for possível inserir vértices, é iterado o ciclo em baixo 
		 */
		while(flag)
		{
			
			/* Ciclo utilizado para escolher o vértice de maior prioridade que caiba numa viagem, executando os seguintes passos até
			um vértice ser escolhido, ou até todos serem percorridos
					-> Escolher os vértices de maior prioridade
					-> Aleatoriamente ir escolhendo um desses vértices, até um encaixar numa viagem, ou até todos terem sido percorridos							
			*/			
			while( !fl && ((int) blackList.size()) + viagens.size() < this.variables - 2 ) {

				// inicializar a variável que irá conter o menor valor dos genes do cromossoma
				min = (this.variables >= 10 ? this.variables + 1 : 10);
				
				// Percorrer os genes do cromossoma
				for (int i = 1; i < this.variables - 1; i++) {
					
					/* se o gene que se encontra a ser processado tiver prioridade igual ou superior à máxima prioridade encontrada,
					se ainda não estiver marcado para ser visitado por um viajante e se o gene não estiver na blacklist, indicando
					que já terá sido descartado desta viagem.... */
					if(topInd.getIntegerAllele(i) <= min && !viagens.contains(i) && !blackList.contains(i)) {
						
						if(topInd.getIntegerAllele(i) != min) {
							maxValues.clear();
							min = topInd.getIntegerAllele(i);
						}
						
						/* guardar o índice do gene na lista que contém 
						 os índices dos genes de valor máximo */
						maxValues.add(i);
					}
				}
		
	
				// se existirem vários genes com o mesmo valor (máximo)
				if(maxValues.size() > 1) {
//System.out.println("MAX VALUES > 1");
					// escolher um gene de forma aleatória
					r = new Random(System.currentTimeMillis());
					random = r.nextInt(maxValues.size());
					topPriorityGene = maxValues.get(random);
				}
				else { // se apenas existir um gene com o valor máximo
//System.out.println("MAX VALUES == 1");
					topPriorityGene = maxValues.get(0);
					random = 0;
				}
				
			
				// variável auxiliar utilizada para guardar a distância mínima calculada ao iterar o próximo ciclo 
				min_dist = this.deadline + 1;
				
				// variável auxiliar utilizada para guardar a viagem na qual ocorreu a distância mínima
				trip = -1;
				
				// variável auxiliar utilizada para guardar a posição da viagem na qual ocorreu a distância mínima
				pos = -1;

				
				/*
				 * Ciclo utilizado para implementar a segunda restrição, que impôe que o vértice escolhido para 
				 * entrar nas viagens vai ser colocado no local que mais favorece a viagem. O ciclo externo itera
				 * as viagens e o interno itera as posições de cada viagem.  
				 */
				for(int j=0 ; j<this.T ; j++) {
				
//System.out.println("viagens_aux[j].size(): " + viagens_aux[j].size());
//this.pressEnterToContinue();
						
						for(int i=0 ; i<viagens_aux[j].size() + 1 ; i++) {

//System.out.println("j: " + j + "\ni: " + i);

							double aux = this.calc_new_vertice(viagens_aux[j], topPriorityGene, i, times.get(j));

//System.out.println("topPriorityGene: " + topPriorityGene + "\ntimes.get(j): " + times.get(j) + "\ncalculated: " + aux + "\nmin_dist: " + min_dist);

						if( aux <= this.deadline && aux < min_dist ) {
							
							min_dist = aux;
							trip = j;
							pos = i;
						}
					}
				}
				
				 // se o gene escolhido couber numa rota
		    	if (pos != -1)
					fl = true;  // indicar a escolha dum gene
				else {
					
					counter = 0;
		
					// percorrer todos os vértices de prioridade máxima ainda não escolhidos até um caber na viagem
					for(int k=0 ; !fl && (k+1)<maxValues.size() ; k++) {
						
						int kk;
						
						// colocar o seu índice no array de genes com prioridade máxima, na blacklist
						auxBlackList.add(random + counter);
						blackList.add(maxValues.get(random + counter));
						
						
						// escolher novo gene com prioridade máxima, de forma aleatória
						random = r.nextInt(maxValues.size() - (k+1));
			
						
						// ordenar os arrays blackList e auxBlackList
						Collections.sort(auxBlackList);
						Collections.sort(blackList);
													
		
						// ciclo para contar o número de posições que é preciso saltar do array maxValues, por estarem na blacklist
						for(counter=0, kk=0 ; kk <= k ; kk++)
							if(auxBlackList.get(kk) <= random+counter)
								counter++;
		
						topPriorityGene = maxValues.get(random + counter);	// guardar o índice do novo gene escolhido aleatoriamente
//System.out.println("TOP PRIORITY GENE: " + topPriorityGene);			
		
						 
						min_dist = this.deadline + 1;
						trip = -1;
						pos = -1;			

						
						/*
						 * Ciclo utilizado para implementar a segunda restrição, que impôe que o vértice escolhido para 
						 * entrar nas viagens vai ser colocado no local que mais favorece a viagem. O ciclo externo itera
						 * as viagens e o interno itera as posições de cada viagem.  
						 */
						for(int j=0 ; j<this.T ; j++) {
						
							for(int i=0 ; i<viagens_aux[j].size() + 1 ; i++) {
							
								double aux = this.calc_new_vertice(viagens_aux[j], topPriorityGene, i, times.get(j));
//System.out.println("topPriorityGene: " + topPriorityGene + "\ntimes.get(j): " + times.get(j) + "\ncalculated: " + aux);

								if( aux <= this.deadline && aux <= min_dist ) {
									
									min_dist = aux;
									trip = j;
									pos = i;
								}
							}
						}
						
						 // se o gene escolhido couber numa rota
				    	if (pos != -1)							
							fl = true;  // assinalar a escolha dum gene	
					}
		
					// limpar a blacklist, para que numa próxima iteração do ciclo acima esta esteja vazia
					auxBlackList.clear();
					
					blackList.add(maxValues.get(random + counter));
				}
			}
			
			
			// limpar a blacklist e o array maxValues, para que numa próxima iteração do ciclo acima estes estejam vazios
			blackList.clear();
			maxValues.clear();
				
	
//System.out.println("fl: " + fl);
			
			// se um gene tiver sido seleccionado para ser colocado na viagem
			if(fl) {
				
//System.out.println("times.size(): " + times.size() + "\ntrip: " + trip + "\nmin_dist: " + min_dist);
						
				times.set(trip, min_dist); //atualizar a variável times com o novo tempo de duração da rota
		
			    int prev = topPriorityGene;
			    
				// atualizar array viagens_aux com o novo gene
				for(int j=pos ; j<viagens_aux[trip].size() ; j++) {
					
					int next = viagens_aux[trip].get(j);
					
					viagens_aux[trip].set(j, prev);
					prev = next;
				}
				
				
				// terminar de atualizar array viagens_aux com o novo gene
				viagens_aux[trip].add(prev);
				
				
				// atualizar array viagens com o novo gene, sendo que é descurada a ordenação
				viagens.add(topPriorityGene);
				
				// atualizar o peso do alelo
				topInd.setAllele(topPriorityGene, viagens.size());
				
				// mudar o valor da flag que assinala a escolha dum gene para colocar na viagem para recomeçar novo processo
				fl = false;
					
/*
System.out.println("top priority gene: " + topPriorityGene);
System.out.println("SCORE: " + vertices.get(topPriorityGene).getScore());

System.out.println("--------------- VIAGENS ---------------");
for (int h=0 ; h < viagens.size() ; h++)
	System.out.println("indice " + h + ": " + viagens.get(h));
System.out.println("---------------------------------------");
this.pressEnterToContinue();
*/
				    
					// atualiza o valor do fitness
					fitness += vertices.get(topPriorityGene).getScore();
				}
				else  // caso contrário (se nenhum dos vértices de alta prioridade couber no fim da viagem)
					flag = false;  // atualizar a flag para sair do ciclo
		}
		
		int totalPrizes = ((Double)fitness).intValue();
		
		fitness = fitness/this.totalScore;
		
		if(fitness > this.bestFitness) {
			
			this.bestFitness = fitness;
			this.bestTrip = viagens_aux;
			this.collected = totalPrizes;
			
			// se não se tratar de uma execução de teste
			if(!this.testing) {
				
				for(int i=0 ; i<this.T ; i++) {
	
					System.out.println("-------------- VIAGEM " + i + " || time: " + times.get(i) +" ---------------");
					
					for (int h=0 ; h < this.bestTrip[i].size() ; h++)
						System.out.println("indice " + h + ": " + this.vertices.get(this.bestTrip[i].get(h)).getID());
					
					
					System.out.println("-------------------------------------------------------------------");
				}
				
				System.out.println("\nTotal prémios recolhidos: " + this.collected);
				
				System.out.println("\n");
			}

			
//this.pressEnterToContinue();
		}
		
		return new Double(fitness);
    }
		
	/* Retorna um bool indicando se o array de booleans apenas possui falsos ou não */
	public boolean full(ArrayList<Boolean> flags) {
		
		boolean full = true;
		
		for(int i=0 ; i<this.T && full ; i++)
			if(flags.get(i) == true)
				full = false;
		
		return full;
	}
	
	/* Retorna a nova distância da viagem recebida (com o novo vértice numa dada posição) */
	public double calc_new_vertice(ArrayList<Integer> viagem, int topPriorityGene, int pos, double prev_time) {

		double new_distance;		
		
		if(viagem.size() == 0)
			new_distance = this.getDistance(0, topPriorityGene) + this.getDistance(topPriorityGene, this.variables - 1);
		else
			if(pos == 0)
				new_distance = prev_time - this.getDistance(0, viagem.get(pos))
								 		 + this.getDistance(0, topPriorityGene) 
								 		 + this.getDistance(topPriorityGene, viagem.get(pos));
			else
				if (pos == viagem.size())
					new_distance = prev_time - this.getDistance(viagem.get(viagem.size() - 1), this.variables - 1) 
									 		 + this.getDistance(viagem.get(viagem.size() - 1), topPriorityGene) 
									 		 + this.getDistance(topPriorityGene, this.variables - 1);
				else
					new_distance = prev_time - this.getDistance(viagem.get(pos-1), viagem.get(pos)) 
									 		 + this.getDistance(viagem.get(pos-1), topPriorityGene)
									 		 + this.getDistance(topPriorityGene, viagem.get(pos));
		
		return new_distance;
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
