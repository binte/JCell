package jcell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataLoading {

	/* Ler a string contendo o n�mero de v�rtices, o n�mero de viajantes (Travellers) ou o tempo m�ximo de cada viagem

	Exemplo:

	n 21
	m 2
	tmax 7.5
	4.600	7.100	0
	(...)

	n�mero de v�rtices/linhas = 21
	n�mero de viajantes = 2
	tempo m�ximo = 7.5

	*/
	public static String getParameter(Scanner input) throws FileNotFoundException {
		
		String line;
		StringTokenizer strTok;
		
		line = input.nextLine();
		
		/* Inicializar a utiliza��o do StringTokenizer */
		strTok = new StringTokenizer(line, " ");

		/* Descartar o identificador */
		strTok.nextToken();

		/* Retornar uma string contendo apenas o int/float que se pretende ler */
		return strTok.nextToken();
	}
	
	/* Realizar o parsing das linhas referentes aos v�rtices no ficheiro recebido como par�metro */
	public static void readFile(Scanner input, ArrayList<? extends Vertice> vertices) {

		int numLine = 0;
			
	 	/* ler linha a linha do ficheiro, para a string 'line', e em cada itera��o do ciclo processar essa linha */
	 	while ( input.hasNext() ) {
		
	 		atualiza((ArrayList<Vertice>)vertices, numLine, input.nextLine());  // atualiza o vector de V�rtices
			numLine++;
	 	}
	}

	/* Realizar o parsing das linhas referentes aos v�rtices no ficheiro recebido como par�metro */
	public static void readFile_TW(Scanner input, ArrayList<? extends Vertice> vertices) {
			
	 	/* ler linha a linha do ficheiro, para a string 'line', e em cada itera��o do ciclo processar essa linha */
	 	while ( input.hasNext() )	
	 		atualiza_TW((ArrayList<Vertice_TW>) vertices, input.nextLine());  // atualiza o vector de V�rtices
	}

	
	/* Atualizar o vetor de v�rtices com a linha lida do ficheiro */
	private static void atualiza(ArrayList<Vertice> vertices, int numLine, String line) throws NumberFormatException {

		Vertice novo = new Vertice();
		StringTokenizer strTok;
		
		/* Inicializar a utiliza��o do StringTokenizer */
		strTok = new StringTokenizer(line, " \t");
		
		/* Processar com o strtok a linha lida do ficheiro, preenchendo o array de v�rtices com os dados processados */
		novo.setID(numLine);

		novo.setX(Float.parseFloat(strTok.nextToken()));
		novo.setY(Float.parseFloat(strTok.nextToken()));
		
		novo.setScore(Integer.parseInt(strTok.nextToken()));
		
		vertices.add(novo);
	}
	
	/* Atualizar o vector de v�rtices com a linha lida do ficheiro */
	private static void atualiza_TW(ArrayList<Vertice_TW> vertices, String line) throws NumberFormatException {

		Vertice_TW novo = new Vertice_TW();
		StringTokenizer strTok;
		float tmp;
		
		/* Inicializar a utiliza��o do StringTokenizer */
		strTok = new StringTokenizer(line, " \t");
		
		/* Processar com o strtok a linha lida do ficheiro, preenchendo o array de v�rtices com os dados processados */
		novo.setID(Integer.parseInt(strTok.nextToken()));

		novo.setX(Float.parseFloat(strTok.nextToken()));
		novo.setY(Float.parseFloat(strTok.nextToken()));
		
		novo.setDuration(Float.parseFloat(strTok.nextToken()));
		novo.setScore(Float.parseFloat(strTok.nextToken()));

		/* Pelo menos tr�s par�metros ser�o descartados */
		strTok.nextToken();
		strTok.nextToken();
		strTok.nextToken();

		
		/* V�rios casos podem suceder aqui:
		 * 
		 *  - V�rtice dep�sito: s� tem mais um par�metro, que corresponde ao fecho da sua janela
		 *  - Restantes v�rtices: podem ter mais par�metros que devem ser descartados, sendo que apenas os dois �ltimos
		 *  interessam, pois correspondem � abertura e ao fecho das suas janelas temporais
		 *  
		 *  A instru��o e o ciclo que se seguem compreendem ambos os casos.
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

