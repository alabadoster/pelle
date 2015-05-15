public class Identifier extends Expression {
	public final String name;

	public Identifier(String name){
		this.name = name;
	}

	@Override
	public int hashCode(){
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Identifier)) return false;

		Identifier other = (Identifier) obj;
		return this.name.equals(other.name);
	}

	public String toString(){
		return "@" + name; 
	}
}