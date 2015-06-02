package jcell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public final class DataLoading {

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
	public static void readFile(Scanner input, ArrayList<Vertice> vertices) {

		int numLine = 0;
		String line;
			
	 	/* ler linha a linha do ficheiro, para a string 'line', e em cada itera��o do ciclo processar essa linha */
	 	while ( input.hasNext() ){
		
	 		atualiza(vertices, numLine, input.nextLine());  // atualiza o vector de V�rtices
			numLine++;
	 	}	 		  	    
	}
	
	/* Atualizar o vector de v�rtices com a linha lida do ficheiro */
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
}

