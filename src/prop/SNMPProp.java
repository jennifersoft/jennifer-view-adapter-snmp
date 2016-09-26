package prop;

public class SNMPProp {
	private String pattern;
	private String dateFormat;
	private String trapOidFatal;
	private String trapOidWarning;
	private String trapOidNormal;
	private String trapTargetCommunity;
	private String trapTargetAddress;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTrapOidFatal() {
		return trapOidFatal;
	}

	public void setTrapOidFatal(String trapOidFatal) {
		this.trapOidFatal = trapOidFatal;
	}

	public String getTrapOidWarning() {
		return trapOidWarning;
	}

	public void setTrapOidWarning(String trapOidWarning) {
		this.trapOidWarning = trapOidWarning;
	}

	public String getTrapOidNormal() {
		return trapOidNormal;
	}

	public void setTrapOidNormal(String trapOidNormal) {
		this.trapOidNormal = trapOidNormal;
	}

	public String getTrapTargetCommunity() {
		return trapTargetCommunity;
	}

	public void setTrapTargetCommunity(String trapTargetCommunity) {
		this.trapTargetCommunity = trapTargetCommunity;
	}

	public String getTrapTargetAddress() {
		return trapTargetAddress;
	}

	public void setTrapTargetAddress(String trapTargetAddress) {
		this.trapTargetAddress = trapTargetAddress;
	}

	public String toString() {
		return "pattern: " + pattern + ", " +
				"dateFormat: " + dateFormat + ", " +
				"trapOidFatal: " + trapOidFatal + ", " +
				"trapOidWarning: " + trapOidWarning + ", " +
				"trapOidNormal: " + trapOidNormal + ", " +
				"trapTargetCommunity: " + trapTargetCommunity + ", " +
				"trapTargetAddress: " + trapTargetAddress;
 	}
}
