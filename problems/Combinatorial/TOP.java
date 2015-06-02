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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import jcell.*; //Use jcell package

public class TOP extends Problem
{
	protected static final double maxFitness = 1; // Maximum Fitness Value
	
	protected int F;  		// n�mero de v�rtices
	protected int T;  		// n�mero de viajantes (travellers)
    protected float deadline;	// tempo m�ximo por viagem
    
    protected ArrayList<? extends Vertice> vertices;	// vector de v�rtices
	protected double matrix[][];				// matriz de dist�ncias
	
	protected float totalScore;	// soma dos pesos de todas as tarefas que passaram o filtro
	protected int collected;  // soma dos pr�mios recolhidos 
	protected int iteration;  // itera��o em que foi encontrado o melhor fitness
	
	protected double bestFitness;
	protected ArrayList<Integer>[] bestTrip;
	
	protected boolean matrixFile; 	 		// indica se a matriz deve ou n�o ser lida de ficheiro
	protected String matrixFileName = null; // caso a vari�vel matrixFile esteja accionada, esta vari�vel ir� conter o nome do ficheiro da matriz
    

    public TOP(String dataFile)
    {
    	super(dataFile);
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    }
    
    
    public int getT() {
    	
    	return this.T;
    }
    
    public float getTotalScore() {
    	
    	return this.totalScore;
    }
    
    public int getCollected() {
    	
    	return this.collected;
    }
    
    public int getIteration() {
    	
    	return this.iteration;
    }
    
    public ArrayList<Integer>[] getBestTrip() {
    	
    	return this.bestTrip;
    }
    
    public boolean getMatrixFile() {
    	
    	return this.matrixFile;
    }
   
    
    public void setMatrixFile(boolean matrixFile) {
    	
    	this.matrixFile = matrixFile;
    }
    
    public void setMatrixFileName(String matrixFileName) {
    	
    	this.matrixFileName = matrixFileName;
    }
    
    public void setIteration(int iteration) {
    	
    	this.iteration = iteration;
    }
    
    
    /** 
     * Inicializar as vari�veis atrav�s da leitura do ficheiro de dados
     */
    public void init() {
    	
       	try {
        	BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        	Scanner input = new Scanner(reader);
        	
        	String s = DataLoading.getParameter(input);
        	s.trim();
        	char firstChar = s.charAt(0);
        	
        	this.F = Integer.parseInt(s);
    		this.T = Integer.parseInt(DataLoading.getParameter(input));
    		this.deadline = Float.parseFloat(DataLoading.getParameter(input));
    		this.vertices = new ArrayList<Vertice>();
    		
    		DataLoading.readFile(input, this.vertices);
    		
			// calcular a soma dos pesos de todas as tarefas antes da execu��o do filtro
			for (int i = 0; i <= this.F - 1; i++)
				this.totalScore += vertices.get(i).getScore();
			
			this.collected = 0;

			
			if(this.matrixFile)
				this.readMatrix();  // ler a matriz de dist�ncias de ficheiro
			else {
				
				this.filter();
				this.buildMatrix(); // construir a matriz de dist�ncias
			}
    		
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
 
    /** Filtrar os v�rtices que nunca ser�o tomados, ou seja, que n�o respeitam a seguinte restri��o:
     * 
     * dist�ncia(origem -> v�rtice) + dist�ncia(v�rtice -> destino)  >  TMax
     *
     * Para tal, esses v�rtices ser�o removidos do Vector de V�rtices
     */
    public void filter() {
    	
    	int i;
    	ArrayList<Integer> novo = new ArrayList<Integer>();
    	
    	// colocar os ind�ces dos v�rtices que n�o poder�o ser tomados num novo vector
    	for(i=1 ; i<=this.F-2 ; i++) {
    		
    		if (  Math.sqrt( Math.pow((this.vertices.get(i).getX() - this.vertices.get(0).getX()), 2) + 
    					     Math.pow((this.vertices.get(i).getY() - this.vertices.get(0).getY()), 2) ) + 
    			  Math.sqrt( Math.pow((this.vertices.get(this.F-1).getX() - this.vertices.get(i).getX()), 2) + 
    						 Math.pow((this.vertices.get(this.F-1).getY() - this.vertices.get(i).getY()), 2) ) > this.deadline  )  {
    			
    			novo.add(i);		
    		}
    	}
    	
    	for(i=0 ; i<novo.size() ; i++)
    		this.vertices.remove(novo.get(i).intValue() - i);

    	// atualizar o n�mero de v�rtices ating�veis do problema
    	this.variables = this.vertices.size(); 	
    }
    
    /**
     * Realiza a filtragem dos v�rtices inating�veis duma inst�ncia, dada a sua matriz de dist�ncias 
     * 
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> filterWithMatrix() {
    	
    	int i, j = 0;
    	BufferedReader reader;
    	ArrayList<Integer> filtered = new ArrayList<Integer>();
    	ArrayList<Double> line = new ArrayList<Double>(), column = new ArrayList<Double>();
		
		try {
			reader = new BufferedReader(new FileReader(this.matrixFileName));
	    	Scanner input = new Scanner(reader);
	    	StringTokenizer strTok;
	    	String tmp;
			
			tmp = input.nextLine();
			
			/* Inicializar a utiliza��o do StringTokenizer */
			strTok = new StringTokenizer(tmp, "	");

			/* Guardar os valores da primeira linha temporariamente */
			for(i=0 ; i < this.F ; i++)
				line.add(Double.parseDouble(strTok.nextToken()));
			
			
			/* Guardar os valores da �ltima coluna */
			column.add(line.get(i-1));
			
			for(i=1 ; i < this.F ; i++) {
			
				tmp = input.nextLine();
				
				/* Inicializar a utiliza��o do StringTokenizer */
				strTok = new StringTokenizer(tmp, "	");
				
				for(j=0 ; j < this.F - 1 ; j++)
					strTok.nextToken();
				
				column.add(Double.parseDouble(strTok.nextToken()));
			}
		} catch (FileNotFoundException ex) {
			
    		System.err.println("Unable to open file: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);
		}
			
		/**
		 *  Filtrar v�rtices inating�veis 
		 **/
	    for(i=0 ; i<=this.F-2 ; i++) {
	    		
	    	if ( line.get(i) + column.get(i) > this.deadline )
	    		filtered.add(i);
	    }
	    	
	   	for(i=0 ; i<filtered.size() ; i++)
	    	this.vertices.remove(filtered.get(i).intValue() - i);
	
	    // atualizar o n�mero de v�rtices ating�veis do problema
	    this.variables = this.vertices.size();

    	return filtered;
    }
    
    /**
     * Ler a matriz de dist�ncias de ficheiro
     */
    public void readMatrix() {
    	
    	int i, j = 0;
    	BufferedReader reader;
    	ArrayList<Integer> filtered = filterWithMatrix();
    	
    	
		try {
			reader = new BufferedReader(new FileReader(matrixFileName));	
	    	Scanner input = new Scanner(reader);
	    	StringTokenizer strTok;
	    	String tmp = new String();
			
			
			/* Inicializar a utiliza��o do StringTokenizer */
			strTok = new StringTokenizer(tmp, "	");
			
	
	    	/**
	    	 * Preencher a matriz de dist�ncias
	    	 */
	    	
	    	//alocar espa�o para a matriz
	    	this.matrix = new double[this.variables][this.variables];	

			
			int ii=0, jj;
			
			for(i=0 ; i < this.F ; i++) {  // iterar as linhas
	    
				tmp = input.nextLine();
				
				/* Inicializar a utiliza��o do StringTokenizer */
				strTok = new StringTokenizer(tmp, "	");
				
				/* se a linha que se encontra a ser iterada n�o estiver contida nos v�rtices filtrados */
				if( !filtered.contains(i) ) {
					
					jj=0;
					
					for(j=0 ; j < this.F ; j++) {  // iterar as colunas
						
						/* Se a coluna que se encontra a ser iterada estiver contida nos v�rtices filtrados */
						if( filtered.contains(j) ) {
							jj++; // incrementar o n�mero de colunas filtradas na linha que se encontra a ser iterada
							Double.parseDouble(strTok.nextToken()); // ignorar o token seguinte, por corresponder a um v�rtice filtrado
						}
						else // Se o v�rtice n�o tiver sido filtrado
							this.matrix[i-ii][j-jj] = round(Double.parseDouble(strTok.nextToken()), 2);  // Guardar a dist�ncia arredondada e truncada
		    		}
				}
				else // caso contr�rio, incrementar o n�mero de linhas filtradas
					ii++;
			}
			
		} catch (FileNotFoundException ex) {
			
    		System.err.println("Unable to open file: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);
		}
    }


    /** Construir a matriz de dist�ncias com repeti��o de dados. Exemplos:
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
    				
    		for(j=0 ; j<this.variables ; j++) {
    			
    			/* Calcular a dist�ncia entre os v�rtices */
    			double value = Math.sqrt( Math.pow((this.vertices.get(j).getX() - this.vertices.get(i).getX()), 2) + 
					  	   				  Math.pow((this.vertices.get(j).getY() - this.vertices.get(i).getY()), 2) );
    			
    			/* Guardar a dist�ncia arredondada e truncada */
    			this.matrix[i][j] = round(value, 2);
    		}
    	}
    }

    /* Retorna a dist�ncia entre dois v�rtices cujos �ndices foram recebidos */
    public double getDistance(int i, int j) {
    	
//System.out.println("dist entre " + this.vertices.get(i).getID() + " e " + this.vertices.get(j).getID() + ": " + ((i>j) ? this.matrix[j][i] : this.matrix[i][j]));
    	
    	return this.matrix[j][i];
    }

    public Object eval(Individual ind)
    {
		// viagem -> array auxiliar que ir� conter as viagens que ser�o constru�das para cada viajante num dado cromossoma
		// viagens -> array auxiliar que ir� conter todos os v�rtices "ocupados" num dado instante num dado cromossoma
    	// viagens_aux -> array auxiliar que ir� conter cada viagem num arrayList distinto
		// maxValues -> array auxiliar que ir� ser utilizado para guardar os �ndices dos genes de maior prioridade dum cromossoma
		// auxBlackList -> Lista auxiliar que ir� conter os �ndices dos genes de maior prioridade que n�o podem ser tomados
		// blackList -> Lista auxiliar que ir� conter os �ndices dos genes descartados ao longo duma viagem
    	// times -> Lista que cont�m os tempos atuais de todas as viagens, incluindo a chegada ao destino
    	// fl -> flag utilizada para assinalar a escolha dum gene
    	// flag -> flag utilizada para saber se continua a ser poss�vel inserir v�rtices nas viagens
    	// counter -> contador para o #posi��es que � necess�rio ignorar dos sucessivos arrays maxValues, por estarem na blacklist
    	
    	ArrayList<Integer>[] viagens_aux = new ArrayList[this.T];
    	ArrayList<Integer> maxValues = new ArrayList<Integer>(), viagens = new ArrayList<Integer>(), blackList = new ArrayList<Integer>(), auxBlackList = new ArrayList<Integer>();
    	ArrayList<Double> times = new ArrayList<Double>(this.T);
    	TopIndividual topInd = (TopIndividual) ind;
    	Random r = new Random();
    	boolean flag = true, fl = false;
		double fitness = 0.0, min_dist = this.deadline + 1;
		int min, topPriorityGene = -1, random, counter, trip = -1, pos = -1;
		

		// inicializar o array que ir� conter as viagens
		for(int j=0 ; j<this.T ; j++)
			viagens_aux[j] = new ArrayList<Integer>();
		
		// inicializar o array que ir� conter os tempos das viagens
		for(int j=0 ; j<this.T ; j++)
			times.add(0.0);

		/*
		 * Enquanto for poss�vel inserir v�rtices, � iterado o ciclo em baixo 
		 */
		while(flag)
		{
			
			/* Ciclo utilizado para escolher o v�rtice de maior prioridade que caiba numa viagem, executando os seguintes passos at�
			um v�rtice ser escolhido, ou at� todos serem percorridos
					-> Escolher os v�rtices de maior prioridade
					-> Aleatoriamente ir escolhendo um desses v�rtices, at� um encaixar numa viagem, ou at� todos terem sido percorridos							
			*/			
			while( !fl && ((int) blackList.size()) + viagens.size() < this.variables - 2 ) {

				// inicializar a vari�vel que ir� conter o menor valor dos genes do cromossoma
				min = (this.variables >= 10 ? this.variables + 1 : 10);
				
				// Percorrer os genes do cromossoma de forma a obter uma lista com os �ndices dos genes de valor m�ximo
				for (int i = 1; i < this.variables - 1; i++) {
					
					/* se o gene que se encontra a ser processado tiver prioridade igual ou superior � m�xima prioridade encontrada,
					se ainda n�o estiver marcado para ser visitado por um viajante e se o gene n�o estiver na blacklist, indicando
					que j� ter� sido descartado desta viagem.... */
					if(topInd.getIntegerAllele(i) <= min && !viagens.contains(i) && !blackList.contains(i)) {
						
						if(topInd.getIntegerAllele(i) != min) {
							maxValues.clear();
							min = topInd.getIntegerAllele(i);
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
				
			
				// vari�vel auxiliar utilizada para guardar a dist�ncia m�nima calculada ao iterar o pr�ximo ciclo 
				min_dist = this.deadline + 1;
				
				// vari�vel auxiliar utilizada para guardar a viagem na qual ocorreu a dist�ncia m�nima
				trip = -1;
				
				// vari�vel auxiliar utilizada para guardar a posi��o da viagem na qual ocorreu a dist�ncia m�nima
				pos = -1;

				
				/*
				 * Ciclo utilizado para implementar a segunda restri��o, que imp�e que o v�rtice escolhido para 
				 * entrar nas viagens vai ser colocado no local que mais favorece a viagem. O ciclo externo itera
				 * as viagens e o interno itera as posi��es de cada viagem.  
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
		
						 
						min_dist = this.deadline + 1;
						trip = -1;
						pos = -1;			

						
						/*
						 * Ciclo utilizado para implementar a segunda restri��o, que imp�e que o v�rtice escolhido para 
						 * entrar nas viagens vai ser colocado no local que mais favorece a viagem. O ciclo externo itera
						 * as viagens e o interno itera as posi��es de cada viagem.  
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
		
					// limpar a blacklist, para que numa pr�xima itera��o do ciclo acima esta esteja vazia
					auxBlackList.clear();
					
					blackList.add(maxValues.get(random + counter));
				}
			}
			
			
			// limpar a blacklist e o array maxValues, para que numa pr�xima itera��o do ciclo acima estes estejam vazios
			blackList.clear();
			maxValues.clear();
				
	
//System.out.println("fl: " + fl);
			
			// se um gene tiver sido seleccionado para ser colocado na viagem
			if(fl) {
				
//System.out.println("times.size(): " + times.size() + "\ntrip: " + trip + "\nmin_dist: " + min_dist);
						
				times.set(trip, min_dist); //atualizar a vari�vel times com o novo tempo de dura��o da rota
		
			    int prev = topPriorityGene;
			    
				// atualizar array viagens_aux com o novo gene
				for(int j=pos ; j<viagens_aux[trip].size() ; j++) {
					
					int next = viagens_aux[trip].get(j);
					
					viagens_aux[trip].set(j, prev);
					prev = next;
				}
				
				
				// terminar de atualizar array viagens_aux com o novo gene
				viagens_aux[trip].add(prev);
				
				
				// atualizar array viagens com o novo gene, sendo que � descurada a ordena��o
				viagens.add(topPriorityGene);
				
				// atualizar o peso do alelo
				topInd.setAllele(topPriorityGene, viagens.size());
				
				// mudar o valor da flag que assinala a escolha dum gene para colocar na viagem para recome�ar novo processo
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
				else  // caso contr�rio (se nenhum dos v�rtices de alta prioridade couber no fim da viagem)
					flag = false;  // atualizar a flag para sair do ciclo
		}
		
		int totalPrizes = ((Double)fitness).intValue();
		
		fitness = fitness/this.totalScore;
		
		if(fitness > this.bestFitness) {
			
			this.bestFitness = fitness;
			this.bestTrip = viagens_aux;
			this.collected = totalPrizes;
			
			// se n�o se tratar de uma execu��o de teste
			if(!this.testing) {
				
				for(int i=0 ; i<this.T ; i++) {
	
					System.out.println("-------------- VIAGEM " + i + " || time: " + round(times.get(i), 2) +" ---------------");
					
					for (int h=0 ; h < this.bestTrip[i].size() ; h++)
						System.out.println("indice " + h + ": " + this.vertices.get(this.bestTrip[i].get(h)).getID());
					
					
					System.out.println("-------------------------------------------------------------------");
				}
				
				System.out.println("\nTotal pr�mios recolhidos: " + this.collected);
				
				System.out.println("\n");
			}

			
//this.pressEnterToContinue();
		}
		
		return new Double(fitness);
    }
		
	/* Retorna um bool indicando se o array de booleans apenas possui falsos ou n�o */
	public boolean full(ArrayList<Boolean> flags) {
		
		boolean full = true;
		
		for(int i=0 ; i<this.T && full ; i++)
			if(flags.get(i) == true)
				full = false;
		
		return full;
	}
	
	/* Arredonda um double a um dado n�mero de casas decimais */
	public double round(double value, int n) {
		
		BigDecimal bd = new BigDecimal(value);

		/* Truncar o valor */
		bd = bd.setScale(n, BigDecimal.ROUND_HALF_UP);

		/* Retornar o valor */
		return bd.doubleValue();
	}
	
	/* Retorna a nova dist�ncia da viagem recebida (com o novo v�rtice numa dada posi��o) */
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

    	//imprimir os n�meros das colunas
    	for(i=0 ; i<this.variables ; i++)
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    	sb.append("\n");

    	for(i=0 ; i<this.variables ; i++){
    		
    		//imprimir o n�mero da linha que vai ser imprimida
    		sb.append(this.vertices.get(i).getID() + "	");
    		
    		//imprimir os valores da linha truncados (e arredondados) a 4 d�gitos
    		for(j=0 ; j<this.variables ; j++)			
    			sb.append(matrix[i][j] + "	");

    		sb.append("\n");
    	}
    	
    	return sb.toString();
    }
    
    
    protected void pressEnterToContinue() {
    	
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
