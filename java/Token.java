public class Token {

	public enum TokenType { PUNCTUATION, NUMBER, PITCH, IDENTIFIER, KEYWORD }

	public TokenType type;
	public String value;

	public Token(TokenType type, String value){
		this.type = type;
		this.value = value;
	}

	public String toString(){
		return "(" + type.name() + ": " + value + ")";
	}

	public static Token Punctuation(String value){
		return new Token(TokenType.PUNCTUATION, value);
	}

	public static Token Number(String value){
		return new Token(TokenType.NUMBER, value);
	}

	public static Token Pitch(String value){
		return new Token(TokenType.PITCH, value);
	}

	public static Token Identifier(String value){
		return new Token(TokenType.IDENTIFIER, value);
	}

	public static Token Keyword(String value){
		return new Token(TokenType.KEYWORD, value);
	}

}