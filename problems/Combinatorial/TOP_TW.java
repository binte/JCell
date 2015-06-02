/* ------------------------------------------
   File: TOP_TW.java
   Author: Celso Coutinho
   Description
   It defines a specific subclass of Problem.
   The TOP-TW Problem, which extends TOP Problem
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
import java.util.StringTokenizer;

import jcell.*; //Use jcell package

public class TOP_TW extends TOP
{   
	
    public TOP_TW(String dataFile)
    {
    	super(dataFile);    	    	    	
    }
    
    
    public void setT(int T) {
    	
    	this.T = T;
    }
    
    public void setDeadline(float deadline) {
    	
    	this.deadline = deadline;
    }

    /**
     * Inicializar as variáveis através da leitura do ficheiro de dados
     */
    public void init() {
    
    	try 
    	{
        	BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        	Scanner input = new Scanner(reader);
        	StringTokenizer strTok;
        	
        	String params = input.nextLine();
        	
    		/* Inicializar a utilização do StringTokenizer */
    		strTok = new StringTokenizer(params, " ");
    		
    		/* Descartar parâmetros */
    		strTok.nextToken();
    		strTok.nextToken();
    		
    		/* Ler número de vértices, somando o depósito, que não é contabilizado nesta variável da instância */
    		this.F = Integer.parseInt(strTok.nextToken()) + 1;
        	
        	/* Descartar a próxima linha */
        	input.nextLine();
        	
    		this.vertices = new ArrayList<Vertice_TW>();
    		
    		DataLoading.readFile_TW(input, this.vertices);
    		
    		/* O tempo limite de cada viagem corresponde ao fecho da janela do depósito */
    		this.deadline = ( (Vertice_TW) this.vertices.get(0)).getEnd();
    		
			// calcular a soma dos pesos de todas as tarefas antes da execução do filtro
			for (int i = 0; i <= this.F - 1; i++)
				this.totalScore += vertices.get(i).getScore();
			
			this.collected = 0;
			
			if(this.matrixFile)
				this.readMatrix();  // ler a matriz de distâncias de ficheiro
			else {
				
				this.filter();
				this.buildMatrix(); // construir a matriz de distâncias
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
    
    /* Filtrar os vértices V que nunca serão tomados, ou seja, que não respeitam as seguintes restrições:
     * 
     * 2*distância(V -> depósito) + duração(V) > TMax
	 * 
	 * distância(Depósito, V) > fimJanela(V)
	 * 
	 * inicioJanela(V) + duração(V) + distância(Depósito, V) > TMax 
     *
     * Para tal, esses vértices serão removidos do Vector de Vértices
     */
    public void filter() {
    	
    	int i;
    	ArrayList<Integer> novo = new ArrayList<Integer>();
    	double dist;
    	
    	
    	// colocar os indíces dos vértices que não poderão ser tomados num novo vector
    	for(i=1 ; i<=this.F-2 ; i++) {
    		
    		Vertice_TW vertice = (Vertice_TW) this.vertices.get(i);
    		
    		dist = Math.sqrt( Math.pow((vertice.getX() - this.vertices.get(0).getX()), 2) + 
		  			 	 	  Math.pow((vertice.getY() - this.vertices.get(0).getY()), 2) );
    		
    		if ( 2 * dist + vertice.getDuration() > this.deadline  
    				||
    			 dist > vertice.getEnd()
    				||
    			 vertice.getBegin() + vertice.getDuration() + dist > this.deadline)  {
    			
    			novo.add(i);		
    		}
    	}
    	
    	for(i=0 ; i<novo.size() ; i++)
    		this.vertices.remove(novo.get(i).intValue() - i);

    	// atualizar o número de vértices atingíveis do problema
    	this.variables = this.vertices.size();    	
    }
    
    /**
     * Realiza a filtragem dos vértices inatingíveis duma instância, dada a sua matriz de distâncias 
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
			
			/* Inicializar a utilização do StringTokenizer */
			strTok = new StringTokenizer(tmp, "	");

			/* Guardar os valores da primeira linha temporariamente */
			for(i=0 ; i < this.F ; i++)
				line.add(Double.parseDouble(strTok.nextToken()));
			
			
			/* Guardar os valores da primeira coluna */
			column.add(line.get(i-1));
			
			for(i=1 ; i < this.F ; i++) {
			
				tmp = input.nextLine();
				
				/* Inicializar a utilização do StringTokenizer */
				strTok = new StringTokenizer(tmp, "	");
				
				column.add(Double.parseDouble(strTok.nextToken()));
				
				for(j=0 ; j < this.F - 1 ; j++)
					strTok.nextToken();
			}
		} catch (FileNotFoundException ex) {
			
    		System.err.println("Unable to open file: " + ex);
    		ex.printStackTrace();
    		
    		System.exit(-1);
		}
			
		/**
		 *  Filtrar vértices inatingíveis 
		 **/
	    for(i=1 ; i<=this.F-2 ; i++) {
	    		
	    	Vertice_TW vertice = (Vertice_TW) this.vertices.get(i);
	    	
	    	if ( line.get(i) + column.get(i) > this.deadline
	    			||
	    		 line.get(i) > vertice.getEnd()
	    		    ||
	    		 vertice.getBegin() + vertice.getDuration() + column.get(i) > this.deadline)
	    
	    		filtered.add(i);
	    }
	    	
	   	for(i=0 ; i<filtered.size() ; i++)
	    	this.vertices.remove(filtered.get(i).intValue() - i);
	
	    // atualizar o número de vértices atingíveis do problema
	    this.variables = this.vertices.size();

    	return filtered;
    }

    public Object eval(Individual ind) 
    {	
		// viagem -> array auxiliar que irá conter as viagens que serão construídas para cada viajante num dado cromossoma
		// viagens -> array auxiliar que irá conter todos os vértices "ocupados" num dado instante num dado cromossoma
    	// viagens_aux -> array auxiliar que irá conter cada viagem num arrayList distinto
		// maxValues -> array auxiliar que irá ser utilizado para guardar os índices dos genes de maior prioridade dum cromossoma
		// auxBlackList -> Lista auxiliar que irá conter os índices dos genes de maior prioridade que não podem ser tomados
		// blackList -> Lista auxiliar que irá conter os índices dos genes descartados ao longo duma viagem
    	// times -> Lista que contém os tempos atuais de todas as viagens, incluindo a chegada ao destino e as durações das tarefas
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
			while( !fl && ((int) blackList.size()) + viagens.size() < this.variables - 1 ) {

				// inicializar a variável que irá conter o menor valor dos genes do cromossoma
				min = (this.variables >= 10 ? this.variables + 1 : 10);
				
				// Percorrer os genes do cromossoma de forma a obter uma lista com os índices dos genes de valor máximo
				for (int i = 1; i < this.variables ; i++) {
					
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
	
					System.out.println("-------------- VIAGEM " + i + " || time: " + round(times.get(i), 2) +" ---------------");
					
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
	
	/* Retorna a nova duração da viagem recebida (com o novo vértice numa dada posição), ou um valor muito elevado 
	 * que será ignorado, caso não seja possível a inserção do novo vértice devido às janelas temporais */
	public double calc_new_vertice(ArrayList<Integer> viagem, int topPriorityGene, int pos, double prev_time) {

		int i=0;
		boolean flag = true;
		double time = 0;
		
		
		if(pos!=0) {  // Se não se pretender colocar o vértice no início da viagem   
			
			/***************************************************************************************************************
			** Calcula o tempo em que o vértice escolhido pode começar a executar, sem ter em conta a sua janela temporal **
			***************************************************************************************************************/
			time = this.getDistance(0, viagem.get(0));
			
			/* Se a distância do depósito ao primeiro vértice da rota for inferior ao início da janela temporal do 
			 * primeiro vértice da rota, a sua execução apenas pode começar no início da sua janela temporal */
			if( time < ( (Vertice_TW) this.vertices.get(viagem.get(0))).getBegin() )
				time = ( (Vertice_TW) this.vertices.get(viagem.get(0))).getBegin();
		
			for( ; i<pos-1 ; i++) {
				
				/* adicionar a duração do vértice que se encontra a ser iterado à duração da rota, bem como a sua distância ao próximo vértice */
				time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
				time += this.getDistance(viagem.get(i), viagem.get(i+1));
				
				/* Se a distância atual da rota for inferior ao início da janela temporal do próximo vértice, 
				 * a sua execução apenas pode começar no início da sua janela temporal */
				if( time < ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin() )
					time = ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin();
			}
			
			time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
			time += this.getDistance(viagem.get(i++), topPriorityGene);
		}
		else // se se pretender colocar o vértice no início da viagem
			time = this.getDistance(0, topPriorityGene);  // este pode começar a executar logo após o viajante fazer o trajeto
															// Depósito - Vértice escolhido (isto sem ter em conta janelas temporais)
		
		/* Atualiza o tempo calculado em cima, tendo agora em consideração a janela temporal do vértice escolhido */
		if( time <= ( (Vertice_TW) this.vertices.get(topPriorityGene)).getEnd() ) {
			
			if( time < ( (Vertice_TW) this.vertices.get(topPriorityGene)).getBegin() )
				time = ( (Vertice_TW) this.vertices.get(topPriorityGene)).getBegin();
		}
		else
			flag = false;
		
		/* Se for possível executar o vértice na posição escolhida, ver se é possível a execução dos vértices 
		 que se seguem a essa posição, já que serão "empurrados" pela inserção do novo vértice na viagem */
		if(flag && pos < viagem.size()) {
			
			time += ( (Vertice_TW) this.vertices.get(topPriorityGene)).getDuration();
			time += this.getDistance(topPriorityGene, viagem.get(i));
	
			/* Atualiza o tempo calculado, tendo agora em consideração a janela temporal do vértice */
			if( time <= ( (Vertice_TW) this.vertices.get(viagem.get(i))).getEnd() ) {
				
				if( time < ( (Vertice_TW) this.vertices.get(viagem.get(i))).getBegin() )
					time = ( (Vertice_TW) this.vertices.get(viagem.get(i))).getBegin();
			}
			else 
				flag = false;
			
			/* Verificar se com a inserção de um vértice, os que se lhe seguem na viagem 
			 * podem ser executados, tendo em conta a sua janela temporal */
			for( ; i<viagem.size()-1 && flag ; i++) {
				
				time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
				time += this.getDistance(viagem.get(i), viagem.get(i+1));
				
				if( time <= ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getEnd() ) {
					
					if( time < ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin() )
						time = ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin();
				}
				else 
					flag = false;
			}
			
			/* Se a rota for válida, adicionar o tempo de duração do último vértice da rota e a distância para o depósito */
			if(flag) {
				
				time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
				time += this.getDistance(viagem.get(i), 0);
			}
		}
		
		/* Se a rota for válida e se o novo vértice tiver sido colocado no fim da rota, 
		 * adicionar a sua duração e a distância para o depósito ao tempo de duração da rota */
		if(flag && pos == viagem.size()) {
			
			time += ( (Vertice_TW) this.vertices.get(topPriorityGene)).getDuration();
			time += this.getDistance(topPriorityGene, 0);
		}
			
		return (flag ? time : Double.MAX_VALUE);
	}
	
	/* Dado um gene dum cromossoma e um tempo, retorna um valor booleano que 
	 * indica se a respetiva tarefa pode ser executada antes do tempo recebido */
	public boolean fit_TW(int gene, double time) {
		
		boolean flag = true;
		
		if(time > ( (Vertice_TW) this.vertices.get(gene)).getEnd())
			flag = false;
		
		return flag;
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
    		
    		//imprimir os valores da linha truncados (e arredondados) a 4 dígitos
    		for(j=0 ; j<this.variables ; j++)			
    			sb.append(matrix[i][j] + "	");

    		sb.append("\n");
    	}
    	
    	return sb.toString();
    }
}
