import java.util.ArrayList;
import java.util.List;

public class Parser {
	public final Tokenizer stream;

	public Parser() {
		stream = new Tokenizer();
	}

	public AST parse(String input){
		stream.feed(input);

		return parseExpression();
	}

	private Expression parseExpression(){
		skipPunc("(");

		Token headToken = stream.peek();
		AST head = null;
		if(headToken == null){
			expectedMissing("expression");
		} else if(isPunc("(", headToken)){
			head = parseExpression();
		} else if(isIdOrKeyword(headToken)){
			head = parseIdentifier();
		} else {
			expectedExpressionError(headToken);
		}

		List<AST> body = parseExpressionBody();

		skipPunc(")");

		return new Expression(head, body);
	}

	private List<AST> parseExpressionBody(){
		ArrayList<AST> result = new ArrayList<AST>();

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
				result.add(parseExpression());

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