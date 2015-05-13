import java.util.List;

public class Expression extends AST {
	public AST head;
	public List<AST> body;

	public Expression(AST head, List<AST> body){
		this.head = head;
		this.body = body;
	}

	public String toString(){
		String result = "(exp, head: " + head.toString() + ", body:";

		if(body.size() == 0) result += " <none>";
		else for(AST a : body) result += " " + a.toString();

		return result + ")";
	}
}