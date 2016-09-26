package util;

import com.jennifersoft.view.adapter.util.LogUtil;
import com.jennifersoft.view.config.ConfigValue;
import prop.SNMPProp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SNMPConfig {
	private static Properties properties = null;

	public static void loadConfig() {
		properties = new Properties();
		
		FileInputStream file = null;
		String path = ConfigValue.adapter_config_path;
		
		try {
			if(path != null) {
				file = new FileInputStream(path);
				properties.load(file);
			}
		} catch (IOException e) {
			LogUtil.error(e.toString());
		}
	}
	
	public static String getValue(String key) {
		if(properties == null) {
			loadConfig();
		}
		
		return properties.getProperty(key);
	}
	
	public static SNMPProp getLog() {
		String pattern = getValue("snmp.pattern");
		String date_format = getValue("snmp.date_format");
		String trap_oid_fatal = getValue("snmp.trap_oid_fatal");
		String trap_oid_warning = getValue("snmp.trap_oid_warning");
		String trap_oid_normal = getValue("snmp.trap_oid_normal");
		String trap_target_community = getValue("snmp.trap_target_community");
		String trap_target_address = getValue("snmp.trap_target_address");

		SNMPProp log = new SNMPProp();
		log.setPattern(pattern != null ? pattern : "[%time] domain=%domainName(%domainId), instance=%instanceName(%instanceId), level=%eventLevel, name=%eventName, value=%value");
		log.setDateFormat(date_format != null ? date_format : "yyyy-MM-dd HH:mm:ss");
		log.setTrapOidFatal(trap_oid_fatal != null ? trap_oid_fatal : null);
		log.setTrapOidWarning(trap_oid_warning != null ? trap_oid_warning : null);
		log.setTrapOidNormal(trap_oid_normal != null ? trap_oid_normal : null);
		log.setTrapTargetCommunity(trap_target_community != null ? trap_target_community : "public");
		log.setTrapTargetAddress(trap_target_address != null ? trap_target_address : "127.0.0.1/162");

		return log;
	}
}
