import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
	BufferedReader reader;

	public Console() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));

		Parser parser = new Parser();

		String input;
		while(true){
			input = readLine();

			if(input.equals("exit")) break;

			try {
				AST ast = parser.parse(input);
				System.out.println(ast);

			} catch (ParseException e){
				System.out.println(e);
			} catch (Exception e){
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

	public String readLine() throws IOException {
		System.out.print(">> ");
		return reader.readLine();
	}

	public static void main(String[] args) throws IOException {
		new Console();
	}


}