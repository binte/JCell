/* ------------------------------------------
   File: TOP_dTW.java
   Author: Celso Coutinho
   Description
   It defines a specific subclass of Problem.
   The TOP-dTW Problem, which extends TOP-TW Problem
   ------------------------------------------*/

package problems.Combinatorial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import jcell.DataLoading;
import jcell.Individual;
import jcell.TopIndividual;
import jcell.Vertice;
import jcell.Car;
import jcell.Vertice_TW;


public class TOP_dTW extends TOP_TW {

	private ArrayList<Car> cars;
	

    public TOP_dTW(String dataFile)
    {
    	super(dataFile); 
    	
    	/* Inicializar o arrayList que cont�m as Viaturas (inst�ncias da classe Car) */
    	this.cars = new ArrayList<Car>();
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
        	String params;
        	
        	params = input.nextLine();
        	
    		/* Inicializar a utiliza��o do StringTokenizer */
    		strTok = new StringTokenizer(params, " ()");
    		
    		
    		/* Ler o n�mero de carros */
    		this.T = Integer.parseInt(strTok.nextToken());
    		
    		/* Ler as janelas temporais de cada um dos carros e inserir os carros na lista de carros */
    		for(int i=0 ; i<this.T ; i++) {
    			
    			float opening, closure;
    			
    			opening = Float.parseFloat(strTok.nextToken());
    			closure = Float.parseFloat(strTok.nextToken());
    			
    			/* Inserir o carro cuja janela temporal foi processada na lista de carros */
    			this.cars.add(new Car(opening, closure));
    		}
    		
    		params = input.nextLine();
        	
    		/* Inicializar a utiliza��o do StringTokenizer */
    		strTok = new StringTokenizer(params, " ()");
    		
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
						
					for(int i=0 ; i<viagens_aux[j].size() + 1 ; i++) {

						double aux = this.calc_new_vertice(viagens_aux[j], topPriorityGene, i, times.get(j), j);

						if( aux <= this.deadline && aux - this.cars.get(j).getOpening() < min_dist ) {
								
							min_dist = aux - this.cars.get(j).getOpening();
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
							
								double aux = this.calc_new_vertice(viagens_aux[j], topPriorityGene, i, times.get(j), j);
//System.out.println("topPriorityGene: " + topPriorityGene + "\ntimes.get(j): " + times.get(j) + "\ncalculated: " + aux);

								if( aux <= this.deadline && aux - this.cars.get(j).getOpening() < min_dist ) {
									
									min_dist = aux - this.cars.get(j).getOpening();
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
	
					System.out.println("--------------- VIAGEM " + i + " || time: " + round(times.get(i), 2) +" ---------------");
					System.out.println("--- start: " + this.cars.get(i).getOpening() + " ---");
					
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
	public double calc_new_vertice(ArrayList<Integer> viagem, int topPriorityGene, int pos, double prev_time, int car) {

		int i=0;
		boolean flag = true;
		double time = 0;
		
		
		if(pos!=0) {  // Se n�o se pretender colocar o v�rtice no in�cio da viagem   
			
			/*************************************************************************************************************
			* Calcula o tempo em que o v�rtice escolhido pode come�ar a executar, sem ter em conta a sua janela temporal *
			* mas considerando a abertura da janela temporal do carro                                                    *
			*************************************************************************************************************/
			time = this.getDistance(0, viagem.get(0)) + this.cars.get(car).getOpening();
			
			/* Se a soma da abertura da janela do carro com a dist�ncia do dep�sito ao primeiro v�rtice da rota for inferior 
			 * ao in�cio da janela temporal do primeiro v�rtice da rota, a sua execu��o apenas pode come�ar no in�cio da sua janela temporal */
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
		else // se se pretender colocar o v�rtice no in�cio da viagem este pode come�ar a executar logo ap�s o viajante fazer o trajeto
			time = this.getDistance(0, topPriorityGene)  + this.cars.get(car).getOpening(); // Dep�sito -> V�rtice escolhido (isto 
																								// sem ter em conta janelas temporais)
																								// e ap�s a abertura da janela do carro
		
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
		
		
		/* Se o tempo em que a rota termina exceder o fecho da janela do carro, a rota � inv�lida */
		if( time > this.cars.get(car).getClosure() )
			flag = false;
			
		return (flag ? time : Double.MAX_VALUE);
	}
}
