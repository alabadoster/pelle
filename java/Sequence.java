import java.util.List;

public class Sequence extends Expression {
	public List<Expression> body;

	public Sequence(List<Expression> body){
		this.body = body;
	}

	public String toString(){
		String result = "[seq body:";

		if(body.size() == 0) result += " <none>";
		else for(Expression exp : body) result += " " + exp.toString();

		return result + "]";
	}
}