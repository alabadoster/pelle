public class Tokenizer {

	final String punctuation = "()[]{},:";

	final String keywords = " -> let ";

	final CharacterStream stream;

	public Token peekedToken;

	public Tokenizer(){
		stream = new CharacterStream();
	}

	public void feed(String input){
		peekedToken = null;
		stream.feed(input);
	}

	public Token next(){
		if(peekedToken != null){
			Token temp = peekedToken;
			peekedToken = null;
			return temp;
		}

		skipWhitespace();

		if(stream.eof()) return null;

		char c = stream.next();

		if(isDigit(c)){
			return readNumber(c);
		}

		if(isPunctuation(c)){
			return Token.Punctuation(String.valueOf(c));
		}

		return readIdentifierOrKeyword(c);
	}

	public Token peek(){
		if(peekedToken == null) peekedToken = next();
		return peekedToken;
	}

	public void exitError(String msg){
		stream.exitError(msg);
	}

	private Token readNumber(char firstDigit){
		return Token.Number(readNumberString(firstDigit));
	}

	private String readNumberString(char firstDigit){
		String value = String.valueOf(firstDigit);
		while(!stream.eof() && isDigit(stream.peek())){
			value += stream.next();
		}

		return value;
	}

	private Token readIdentifierOrKeyword(char firstLetter){
		String value = String.valueOf(firstLetter);
		while(!stream.eof() && isIdentifierLetter(stream.peek())){
			value += stream.next();
		}

		return (keywords.indexOf(" " + value + " ") == -1 ) ? Token.Identifier(value) : Token.Keyword(value);
	}

	private boolean isPunctuation(char c){
		return punctuation.indexOf(c) != -1;
	}

	private boolean isDigit(char c){
		return (c >= '0' && c <= '9');
	}

	private boolean isWhitespace(char c){
		return (c == ' ');
	}

	private boolean isIdentifierFirstLetter(char c){
		return (!(isPunctuation(c) && !isDigit(c)));
	}

	private boolean isIdentifierLetter(char c){
		return (!isPunctuation(c) && !isWhitespace(c));
	}

	private void skipWhitespace(){
		while(!stream.eof() && isWhitespace(stream.peek())) stream.next();
	}

}