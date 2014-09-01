package Enums;

public enum AutoParams {
	remoteMachine("remote.machine"),
	machine("machine"),
	sutUrl("sut.url"),
	envFile("env.file")
	
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
