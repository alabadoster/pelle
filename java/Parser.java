import java.util.ArrayList;
import java.util.List;

public class Parser {
	public final Tokenizer stream;

	public Parser() {
		stream = new Tokenizer();
	}

	public Expression parse(String input){
		stream.feed(input);

		return parseCall();
	}

	private Call parseCall(){
		skipPunc("(");

		Token peeked = stream.peek();
		Expression head = null;

		if(peeked == null){
			expectedMissing("expression");

		} else if(isPunc("(", peeked)){
			head = parseCall();

		} else if(isIdOrKeyword(peeked)){
			head = parseIdentifier();

		} else {
			expectedExpressionError(peeked);
		}

		List<Expression> body = parseCallArguments();

		skipPunc(")");

		return new Call(head, body);
	}

	private List<Expression> parseCallArguments(){
		ArrayList<Expression> result = new ArrayList<Expression>();

		Token peeked = stream.peek();
		while(!isPunc(")", peeked)){
			if(peeked == null) expectedMissing("')'");

			if(isIdOrKeyword(peeked)){
				result.add(parseIdentifier());

			} else if(isNumber(peeked)){
				result.add(parseNumber());

			} else if(isPitch(peeked)){
				result.add(parsePitch());

			} else if(isPunc("(", peeked)){
				result.add(parseCall());

			} else {
				expectedExpressionError(peeked);
			}
			
			peeked = stream.peek();
		}

		return result;
	}

	private Identifier parseIdentifier(){
		Token t = stream.next();
		boolean isKeyword = (t.type == Token.TokenType.KEYWORD);
		return new Identifier(t.value, isKeyword);
	}

	private Number parseNumber(){
		return new Number(stream.next().value);
	}

	private Pitch parsePitch(){
		return new Pitch(stream.next().value);
	}

	private Sequence parseSequence(){
		return new Sequence(null);
	}

	private boolean isIdOrKeyword(Token in){
		return checkToken(Token.Identifier(""), in) || checkToken(Token.Keyword(""), in);
	}

	private boolean isNumber(Token in){
		return checkToken(Token.Number(""), in);
	}

	private boolean isPitch(Token in){
		return checkToken(Token.Pitch(""), in);
	}

	private boolean isPunc(String punc, Token in){
		return checkToken(Token.Punctuation(punc), in);
	}

	private void skipPunc(String punc){
		Token in = stream.next();
		if(in == null) expectedMissing("'" + punc + "'");
		if(!isPunc(punc, in)) expectedPunc(punc, in);
	}

	private boolean checkToken(Token expectedToken, Token foundToken){
		boolean ignoreTokenValue = (expectedToken.type != Token.TokenType.PUNCTUATION);

		return (foundToken != null
			&& expectedToken.type == foundToken.type 
			&& (expectedToken.value.equals(foundToken.value) || ignoreTokenValue));
	}

	private void expectedPunc(String punc, Token foundToken){
		stream.exitError("Expected '" + punc + "', found '" + foundToken.value + "'");
	}

	private void expectedIdentifier(Token foundToken){
		stream.exitError("Expected identifier, found '" + foundToken.value + "'");
	}

	private void expectedExpressionError(Token foundToken){
		stream.exitError("Expected expression, found '" + foundToken.value + "'");
	}

	private void expectedMissing(String expectedStr){
		stream.exitError("Expected " + expectedStr + ", found nothing");
	}
}