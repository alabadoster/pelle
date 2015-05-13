public class CharacterStream {
	String input;
	int length;
	int pos;

	public void feed(String in){
		input = in;
		length = input.length();
		pos = 0;
	}

	public char next(){
		return input.charAt(pos++);
	}

	public char peek(){
		return input.charAt(pos);
	}

	public boolean eof(){
		return pos >= length;
	}

	public void exitError(String msg){
		throw new ParseException("Error at position " + pos + ": " + msg);
	}

}