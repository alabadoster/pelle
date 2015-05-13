public class Pitch extends Expression {
	public int pitch;

	public Pitch(String value){
		this.pitch = 65;
	}

	public String toString(){
		return "'" + pitch;
	}
}