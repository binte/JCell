package jcell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataLoading {

	/* Ler a string contendo o número de vértices, o número de viajantes (Travellers) ou o tempo máximo de cada viagem

	Exemplo:

	n 21
	m 2
	tmax 7.5
	4.600	7.100	0
	(...)

	número de vértices/linhas = 21
	número de viajantes = 2
	tempo máximo = 7.5

	*/
	public static String getParameter(Scanner input) throws FileNotFoundException {
		
		String line;
		StringTokenizer strTok;
		
		line = input.nextLine();
		
		/* Inicializar a utilização do StringTokenizer */
		strTok = new StringTokenizer(line, " ");

		/* Descartar o identificador */
		strTok.nextToken();

		/* Retornar uma string contendo apenas o int/float que se pretende ler */
		return strTok.nextToken();
	}
	
	/* Realizar o parsing das linhas referentes aos vértices no ficheiro recebido como parâmetro */
	public static void readFile(Scanner input, ArrayList<? extends Vertice> vertices) {

		int numLine = 0;
			
	 	/* ler linha a linha do ficheiro, para a string 'line', e em cada iteração do ciclo processar essa linha */
	 	while ( input.hasNext() ) {
		
	 		atualiza((ArrayList<Vertice>)vertices, numLine, input.nextLine());  // atualiza o vector de Vértices
			numLine++;
	 	}
	}

	/* Realizar o parsing das linhas referentes aos vértices no ficheiro recebido como parâmetro */
	public static void readFile_TW(Scanner input, ArrayList<? extends Vertice> vertices) {
			
	 	/* ler linha a linha do ficheiro, para a string 'line', e em cada iteração do ciclo processar essa linha */
	 	while ( input.hasNext() )	
	 		atualiza_TW((ArrayList<Vertice_TW>) vertices, input.nextLine());  // atualiza o vector de Vértices
	}

	
	/* Atualizar o vetor de vértices com a linha lida do ficheiro */
	private static void atualiza(ArrayList<Vertice> vertices, int numLine, String line) throws NumberFormatException {

		Vertice novo = new Vertice();
		StringTokenizer strTok;
		
		/* Inicializar a utilização do StringTokenizer */
		strTok = new StringTokenizer(line, " \t");
		
		/* Processar com o strtok a linha lida do ficheiro, preenchendo o array de vértices com os dados processados */
		novo.setID(numLine);

		novo.setX(Float.parseFloat(strTok.nextToken()));
		novo.setY(Float.parseFloat(strTok.nextToken()));
		
		novo.setScore(Integer.parseInt(strTok.nextToken()));
		
		vertices.add(novo);
	}
	
	/* Atualizar o vector de vértices com a linha lida do ficheiro */
	private static void atualiza_TW(ArrayList<Vertice_TW> vertices, String line) throws NumberFormatException {

		Vertice_TW novo = new Vertice_TW();
		StringTokenizer strTok;
		float tmp;
		
		/* Inicializar a utilização do StringTokenizer */
		strTok = new StringTokenizer(line, " \t");
		
		/* Processar com o strtok a linha lida do ficheiro, preenchendo o array de vértices com os dados processados */
		novo.setID(Integer.parseInt(strTok.nextToken()));

		novo.setX(Float.parseFloat(strTok.nextToken()));
		novo.setY(Float.parseFloat(strTok.nextToken()));
		
		novo.setDuration(Float.parseFloat(strTok.nextToken()));
		novo.setScore(Float.parseFloat(strTok.nextToken()));

		/* Pelo menos três parâmetros serão descartados */
		strTok.nextToken();
		strTok.nextToken();
		strTok.nextToken();

		
		/* Vários casos podem suceder aqui:
		 * 
		 *  - Vértice depósito: só tem mais um parâmetro, que corresponde ao fecho da sua janela
		 *  - Restantes vértices: podem ter mais parâmetros que devem ser descartados, sendo que apenas os dois últimos
		 *  interessam, pois correspondem à abertura e ao fecho das suas janelas temporais
		 *  
		 *  A instrução e o ciclo que se seguem compreendem ambos os casos.
		 *  
		 **/
		tmp = Float.parseFloat(strTok.nextToken());
	
		while ( strTok.hasMoreTokens() ) {
				
			novo.setBegin(tmp);
			tmp = Float.parseFloat(strTok.nextToken());
		}
		
		novo.setEnd(tmp);
		
		vertices.add(novo);
	}
}

