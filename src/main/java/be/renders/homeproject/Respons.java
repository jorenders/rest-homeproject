package be.renders.homeproject;

public class Respons {
	private String responsString = "succes";
	private ResponseCode responsCode = ResponseCode.OK;

	
	
	public Respons(ResponseCode responseCode, String responsString) {
		this.responsCode = responseCode;
		this.responsString = responsString;
	}
	
	public Respons(ResponseCode responseCode) {
		this.responsCode = responseCode;
		if (!ResponseCode.OK.equals(responseCode)) {
			responsString = "failed";
		}
	}

	public String getResponsString() {
		return responsString;
	}

	public void setResponsString(String responsString) {
		this.responsString = responsString;
	}

	public ResponseCode getResponsCode() {
		return responsCode;
	}

	public void setResponsCode(ResponseCode responsCode) {
		this.responsCode = responsCode;
	}
	
	
}