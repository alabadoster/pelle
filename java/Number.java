public class Number extends Expression {
	public int value;

	public Number(String value){
		this.value = Integer.parseInt(value);
	}

	public String toString(){
		return "" + value;
	}
}