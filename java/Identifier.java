public class Identifier extends AST {
	public final String name;
	public boolean isKeyword;

	public Identifier(String name){
		this.name = name;
		this.isKeyword = false;
	}

	public Identifier(String name, boolean keyword){
		this.name = name;
		this.isKeyword = keyword;
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