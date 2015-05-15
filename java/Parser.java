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
		Expression result = null;

		Token peeked = stream.peek();

		if(peeked == null){
			return null;

		} else if(isPunc("(", peeked)){
			result = parseInvocationOrKeyword();

		} else if(isPunc("[", peeked)){
			result = parseSequence();

		} else if(isId(peeked)){
			result = parseIdentifier();

		} else if(isNumber(peeked)){
			result = parseNumber();

		}

		return result;
	}

	private Expression parseInvocationOrKeyword(){
		Expression result = null;

		skipPunc("(");

		Token peeked = stream.peek();
		if(isKeyword(peeked)){
			result = parseKeyword();
		} else {
			result = parseInvocation();
		}

		skipPunc(")");

		return result;
	}

	private Expression parseKeyword(){

		Token keywordToken = stream.next();
		if(keywordToken.value.equals("->")){
			return parseFuncDefinition();
		}

		return null;
	}

	private FuncDefinition parseFuncDefinition(){

		skipPunc("(");
		List<Identifier> params = parseIdentifierList(")");
		skipPunc(")");

		Expression body = parseExpression();

		return new FuncDefinition(null, params, body);
	}

	private Invocation parseInvocation(){
		Expression func = parseExpression();
		if(func == null) expectedExpression();

		List<Expression> args = parseExpressionList(")");

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


	private List<Identifier> parseIdentifierList(String terminatingSymbol){
		List<Identifier> result = new ArrayList<Identifier>();

		Token peeked = stream.peek();
		while(!isPunc(terminatingSymbol, peeked)){
			
			Identifier id = parseIdentifier();
			if(id == null) expectedMissing("'" + terminatingSymbol + "'");

			result.add(id);
			
			peeked = stream.peek();
		}

		return result;
	}

	private Identifier parseIdentifier(){
		return new Identifier(stream.next().value);
	}

	private Number parseNumber(){
		return new Number(stream.next().value);
	}

	private boolean isId(Token in){
		return checkToken(Token.Identifier(""), in);
	}

	private boolean isKeyword(Token in){
		return checkToken(Token.Keyword(""), in);
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