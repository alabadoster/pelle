public class Number extends AST {
	public int value;

	public Number(String value){
		this.value = Integer.parseInt(value);
	}

	public String toString(){
		return "" + value;
	}
}