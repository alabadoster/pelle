import java.util.List;

public class Sequence extends Expression {
	public List<Expression> body;

	public Sequence(List<Expression> body){
		this.body = body;
	}
}