import java.util.List;

public class Invocation extends Expression {
	public Expression func;
	public List<Expression> args;

	public Invocation(Expression func, List<Expression> args){
		this.func = func;
		this.args = args;
	}

	public String toString(){
		String result = "(call: " + func.toString() + ", args:";

		if(args.size() == 0) result += " <none>";
		else for(Expression exp : args) result += " " + exp.toString();

		return result + ")";
	}
}