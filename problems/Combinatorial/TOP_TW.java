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
     * Inicializar as vari�veis atrav�s da leitura do ficheiro de dados
     */
    public void init() {
    
    	try 
    	{
        	BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        	Scanner input = new Scanner(reader);
        	StringTokenizer strTok;
        	
        	String params = input.nextLine();
        	
    		/* Inicializar a utiliza��o do StringTokenizer */
    		strTok = new StringTokenizer(params, " ");
    		
    		/* Descartar par�metros */
    		strTok.nextToken();
    		strTok.nextToken();
    		
    		/* Ler n�mero de v�rtices, somando o dep�sito, que n�o � contabilizado nesta vari�vel da inst�ncia */
    		this.F = Integer.parseInt(strTok.nextToken()) + 1;
        	
        	/* Descartar a pr�xima linha */
        	input.nextLine();
        	
    		this.vertices = new ArrayList<Vertice_TW>();
    		
    		DataLoading.readFile_TW(input, this.vertices);
    		
    		/* O tempo limite de cada viagem corresponde ao fecho da janela do dep�sito */
    		this.deadline = ( (Vertice_TW) this.vertices.get(0)).getEnd();
    		
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
    
    /* Filtrar os v�rtices V que nunca ser�o tomados, ou seja, que n�o respeitam as seguintes restri��es:
     * 
     * 2*dist�ncia(V -> dep�sito) + dura��o(V) > TMax
	 * 
	 * dist�ncia(Dep�sito, V) > fimJanela(V)
	 * 
	 * inicioJanela(V) + dura��o(V) + dist�ncia(Dep�sito, V) > TMax 
     *
     * Para tal, esses v�rtices ser�o removidos do Vector de V�rtices
     */
    public void filter() {
    	
    	int i;
    	ArrayList<Integer> novo = new ArrayList<Integer>();
    	double dist;
    	
    	
    	// colocar os ind�ces dos v�rtices que n�o poder�o ser tomados num novo vector
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
			
			
			/* Guardar os valores da primeira coluna */
			column.add(line.get(i-1));
			
			for(i=1 ; i < this.F ; i++) {
			
				tmp = input.nextLine();
				
				/* Inicializar a utiliza��o do StringTokenizer */
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
		 *  Filtrar v�rtices inating�veis 
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
	
	    // atualizar o n�mero de v�rtices ating�veis do problema
	    this.variables = this.vertices.size();

    	return filtered;
    }

    public Object eval(Individual ind) 
    {	
		// viagem -> array auxiliar que ir� conter as viagens que ser�o constru�das para cada viajante num dado cromossoma
		// viagens -> array auxiliar que ir� conter todos os v�rtices "ocupados" num dado instante num dado cromossoma
    	// viagens_aux -> array auxiliar que ir� conter cada viagem num arrayList distinto
		// maxValues -> array auxiliar que ir� ser utilizado para guardar os �ndices dos genes de maior prioridade dum cromossoma
		// auxBlackList -> Lista auxiliar que ir� conter os �ndices dos genes de maior prioridade que n�o podem ser tomados
		// blackList -> Lista auxiliar que ir� conter os �ndices dos genes descartados ao longo duma viagem
    	// times -> Lista que cont�m os tempos atuais de todas as viagens, incluindo a chegada ao destino e as dura��es das tarefas
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
			while( !fl && ((int) blackList.size()) + viagens.size() < this.variables - 1 ) {

				// inicializar a vari�vel que ir� conter o menor valor dos genes do cromossoma
				min = (this.variables >= 10 ? this.variables + 1 : 10);
				
				// Percorrer os genes do cromossoma de forma a obter uma lista com os �ndices dos genes de valor m�ximo
				for (int i = 1; i < this.variables ; i++) {
					
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
	
	/* Retorna a nova dura��o da viagem recebida (com o novo v�rtice numa dada posi��o), ou um valor muito elevado 
	 * que ser� ignorado, caso n�o seja poss�vel a inser��o do novo v�rtice devido �s janelas temporais */
	public double calc_new_vertice(ArrayList<Integer> viagem, int topPriorityGene, int pos, double prev_time) {

		int i=0;
		boolean flag = true;
		double time = 0;
		
		
		if(pos!=0) {  // Se n�o se pretender colocar o v�rtice no in�cio da viagem   
			
			/***************************************************************************************************************
			** Calcula o tempo em que o v�rtice escolhido pode come�ar a executar, sem ter em conta a sua janela temporal **
			***************************************************************************************************************/
			time = this.getDistance(0, viagem.get(0));
			
			/* Se a dist�ncia do dep�sito ao primeiro v�rtice da rota for inferior ao in�cio da janela temporal do 
			 * primeiro v�rtice da rota, a sua execu��o apenas pode come�ar no in�cio da sua janela temporal */
			if( time < ( (Vertice_TW) this.vertices.get(viagem.get(0))).getBegin() )
				time = ( (Vertice_TW) this.vertices.get(viagem.get(0))).getBegin();
		
			for( ; i<pos-1 ; i++) {
				
				/* adicionar a dura��o do v�rtice que se encontra a ser iterado � dura��o da rota, bem como a sua dist�ncia ao pr�ximo v�rtice */
				time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
				time += this.getDistance(viagem.get(i), viagem.get(i+1));
				
				/* Se a dist�ncia atual da rota for inferior ao in�cio da janela temporal do pr�ximo v�rtice, 
				 * a sua execu��o apenas pode come�ar no in�cio da sua janela temporal */
				if( time < ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin() )
					time = ( (Vertice_TW) this.vertices.get(viagem.get(i+1))).getBegin();
			}
			
			time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
			time += this.getDistance(viagem.get(i++), topPriorityGene);
		}
		else // se se pretender colocar o v�rtice no in�cio da viagem
			time = this.getDistance(0, topPriorityGene);  // este pode come�ar a executar logo ap�s o viajante fazer o trajeto
															// Dep�sito - V�rtice escolhido (isto sem ter em conta janelas temporais)
		
		/* Atualiza o tempo calculado em cima, tendo agora em considera��o a janela temporal do v�rtice escolhido */
		if( time <= ( (Vertice_TW) this.vertices.get(topPriorityGene)).getEnd() ) {
			
			if( time < ( (Vertice_TW) this.vertices.get(topPriorityGene)).getBegin() )
				time = ( (Vertice_TW) this.vertices.get(topPriorityGene)).getBegin();
		}
		else
			flag = false;
		
		/* Se for poss�vel executar o v�rtice na posi��o escolhida, ver se � poss�vel a execu��o dos v�rtices 
		 que se seguem a essa posi��o, j� que ser�o "empurrados" pela inser��o do novo v�rtice na viagem */
		if(flag && pos < viagem.size()) {
			
			time += ( (Vertice_TW) this.vertices.get(topPriorityGene)).getDuration();
			time += this.getDistance(topPriorityGene, viagem.get(i));
	
			/* Atualiza o tempo calculado, tendo agora em considera��o a janela temporal do v�rtice */
			if( time <= ( (Vertice_TW) this.vertices.get(viagem.get(i))).getEnd() ) {
				
				if( time < ( (Vertice_TW) this.vertices.get(viagem.get(i))).getBegin() )
					time = ( (Vertice_TW) this.vertices.get(viagem.get(i))).getBegin();
			}
			else 
				flag = false;
			
			/* Verificar se com a inser��o de um v�rtice, os que se lhe seguem na viagem 
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
			
			/* Se a rota for v�lida, adicionar o tempo de dura��o do �ltimo v�rtice da rota e a dist�ncia para o dep�sito */
			if(flag) {
				
				time += ( (Vertice_TW) this.vertices.get(viagem.get(i))).getDuration();
				time += this.getDistance(viagem.get(i), 0);
			}
		}
		
		/* Se a rota for v�lida e se o novo v�rtice tiver sido colocado no fim da rota, 
		 * adicionar a sua dura��o e a dist�ncia para o dep�sito ao tempo de dura��o da rota */
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
}
