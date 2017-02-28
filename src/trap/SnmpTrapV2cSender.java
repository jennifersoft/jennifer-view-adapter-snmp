package trap;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import prop.SNMPProp;
import snmp.SNMPObject;
import snmp.SNMPObjectIdentifier;
import snmp.SNMPTimeTicks;
import snmp.SNMPTrapSenderInterface;
import snmp.SNMPVarBindList;
import snmp.SNMPVariablePair;
import snmp.SNMPv2TrapPDU;

import com.jennifersoft.view.adapter.util.LogUtil;


public class SnmpTrapV2cSender extends Thread {
	private Queue<SNMPv2TrapPDU> queue;
	private SNMPProp prop;

	public SnmpTrapV2cSender(SNMPProp prop) {
		this.queue = new LinkedList<SNMPv2TrapPDU>();
		this.prop = prop;
	}
	
	public void addTrapMsg(String paramString, String level) {
        String valueTypeString = "snmp.SNMPOctetString";
        
        try {
	        Class valueClass = Class.forName(valueTypeString);
	        SNMPObject itemValue = (SNMPObject)valueClass.newInstance();
	        itemValue.setValue(paramString);
	        
	        addTrapMsg(itemValue, level);
        } catch (Exception e) {
			LogUtil.error("SnmpTrapV2cSender.addTrapMsg(String): " + e.toString());
        }
	}
	
	private void addTrapMsg(SNMPObject itemValue, String level) {
		try {
			String trapOid = prop.getTrapOidNormal();

			if(level == "FATAL") {
				trapOid = prop.getTrapOidFatal();
			} else if(level == "WARNING") {
				trapOid = prop.getTrapOidWarning();
			}

			// use the enterprise OID as the Item ID
			SNMPObjectIdentifier itemID = new SNMPObjectIdentifier(trapOid);

			// use the enterprise OID as the SNMP trap OID
            SNMPObjectIdentifier snmpTrapOID = new SNMPObjectIdentifier(trapOid);

            // let uptime just be system time...
            SNMPTimeTicks sysUptime = new SNMPTimeTicks(System.currentTimeMillis() / 10);
            
            // see if have any additional variable pairs to send, and add them to
            // the VarBindList if so
            SNMPVarBindList varBindList = new SNMPVarBindList();
			if (itemValue != null) 
				varBindList.addSNMPObject(new SNMPVariablePair(itemID, itemValue));
			
			// create trap PDU
            SNMPv2TrapPDU pdu = new SNMPv2TrapPDU(sysUptime, snmpTrapOID, varBindList);

			this.queue.add(pdu);
		} catch (Throwable localThrowable) {
			LogUtil.error("SnmpTrapV2cSender.addTrapMsg(SNMPObject): " + localThrowable.toString());
		}
	}

	public void run() {
		while(!queue.isEmpty()) {
			try {
				SNMPv2TrapPDU pdu = this.queue.poll();
				String community = prop.getTrapTargetCommunity();
				String targetAddress = prop.getTrapTargetAddress();
				InetAddress hostAddress = InetAddress.getByName(targetAddress);

				// send it
				SNMPTrapSenderInterface trapSenderInterface = new SNMPTrapSenderInterface();
				trapSenderInterface.sendTrap(hostAddress, community, pdu);
			} catch (IOException e) {
				LogUtil.error("S193: " + e.toString());
			}
		}
	}
}
