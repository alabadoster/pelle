public class Pitch extends AST {
	public int pitch;

	public Pitch(String value){
		this.pitch = 65;
	}

	public String toString(){
		return "'" + pitch;
	}
}