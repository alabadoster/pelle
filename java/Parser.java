import java.util.ArrayList;
import java.util.List;

public class Parser {
	public final Tokenizer stream;

	public Parser() {
		stream = new Tokenizer();
	}

	public Expression parse(String input){
		stream.feed(input);

		return parseExpression();
	}

	private Expression parseExpression(){

		Token peeked = stream.peek();

		Expression result = null;
		if(peeked == null){
			return null;

		} else if(isPunc("(", peeked)){
			result = parseInvocation();

		} else if(isPunc("[", peeked)){
			result = parseSequence();

		} else if(isIdOrKeyword(peeked)){
			result = parseIdentifier();

		} else if(isNumber(peeked)){
			result = parseNumber();

		}

		return result;
	}

	private Invocation parseInvocation(){
		skipPunc("(");

		Expression func = parseExpression();
		if(func == null) expectedExpression();

		List<Expression> args = parseExpressionList(")");

		skipPunc(")");

		return new Invocation(func, args);
	}


	private Sequence parseSequence(){
		skipPunc("[");

		List<Expression> body = parseExpressionList("]");

		skipPunc("]");

		return new Sequence(body);
	}

	private List<Expression> parseExpressionList(String terminatingSymbol){
		List<Expression> result = new ArrayList<Expression>();

		Token peeked = stream.peek();
		while(!isPunc(terminatingSymbol, peeked)){
			
			Expression exp = parseExpression();
			if(exp == null) expectedMissing("'" + terminatingSymbol + "'");

			result.add(exp);
			
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

	private boolean isIdOrKeyword(Token in){
		return checkToken(Token.Identifier(""), in) || checkToken(Token.Keyword(""), in);
	}

	private boolean isNumber(Token in){
		return checkToken(Token.Number(""), in);
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

	private void expectedMissing(String expectedStr){
		stream.exitError("Expected " + expectedStr + ", found nothing");
	}

	private void expectedExpression(){
		expectedMissing("expression");
	}
}