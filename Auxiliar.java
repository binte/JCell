import java.io.IOException;


public class Auxiliar {

	public static void getchar()
	{
		try
		{
			System.out.print("\n\n  Press ENTER..");
			System.in.read();
		}
		catch(IOException ex)
		{
			System.err.println("Erro no getchar: " + ex);
			ex.printStackTrace();
			
			System.exit(-1);
		}
	}
}
