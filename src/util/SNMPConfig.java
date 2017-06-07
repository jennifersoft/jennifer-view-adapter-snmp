package util;

import prop.SNMPProp;
import com.jennifersoft.view.extension.util.PropertyUtil;

public class SNMPConfig {
	static final SNMPProp prop = new SNMPProp();
	static final String PATTERN = "[%time] domain=%domainName(%domainId), instance=%instanceName(%instanceId), level=%eventLevel, name=%eventName, value=%value";
	static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	static final String TRAP_OID_FATAL = "1.3.6.1.4.1.27767.1.1";
	static final String TRAP_OID_WARNING = "1.3.6.1.4.1.27767.1.1";
	static final String TRAP_OID_NORMAL = "1.3.6.1.4.1.27767.1.1";
	static final String TRAP_TARGET_COMMUNITY = "public";
	static final String TRAP_TARGET_ADDRESS = "127.0.0.1/162";

	public static SNMPProp getProperties() {
		prop.setPattern(PropertyUtil.getValue("snmp", "pattern", PATTERN));
		prop.setDateFormat(PropertyUtil.getValue("snmp", "date_format", DATE_FORMAT));
		prop.setTrapOidFatal(PropertyUtil.getValue("snmp", "trap_oid_fatal", TRAP_OID_FATAL));
		prop.setTrapOidWarning(PropertyUtil.getValue("snmp", "trap_oid_warning", TRAP_OID_WARNING));
		prop.setTrapOidNormal(PropertyUtil.getValue("snmp", "trap_oid_normal", TRAP_OID_NORMAL));
		prop.setTrapTargetCommunity(PropertyUtil.getValue("snmp", "trap_target_community", TRAP_TARGET_COMMUNITY));
		prop.setTrapTargetAddress(PropertyUtil.getValue("snmp", "trap_target_address", TRAP_TARGET_ADDRESS));

		return prop;
	}
}