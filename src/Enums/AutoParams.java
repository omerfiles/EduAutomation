package Enums;

public enum AutoParams {
	remoteMachine("remote.machine"),
	sutUrl("sut.url")
	
	;
	
	private final String text;
	
	private AutoParams(final String text){
		this.text=text;
	}
	
	@Override 
	public String toString(){
		return text;
	}

}
