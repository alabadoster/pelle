import java.util.List;

public class FuncDefinition extends Expression {
	public Identifier name;
	public List<Identifier> params;
	public Expression body;

	public FuncDefinition(Identifier name, List<Identifier> params, Expression body){
		this.name = name;
		this.params = params;
		this.body = body;
	}

	public String toString(){
		String result = "(def func: ";

		if(name == null) result += "<anon>";
		else result += name.toString();

		result += ", params:";

		if(params.size() == 0) result += " <none>";
		else for(Identifier id : params) result += " " + id.toString();

		result += ", body: " + body.toString();

		return result + ")";
	}
}