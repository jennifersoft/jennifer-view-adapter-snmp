package trap;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.jennifersoft.view.adapter.util.LogUtil;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import prop.SNMPProp;
import util.SNMPConfig;

public class TrapSender extends Thread {
    private Vector<Map<String, String>> queue = new Vector();
    private SNMPProp properties;

    public TrapSender(SNMPProp properties) {
        this.properties = properties;
    }

    private void sendAll() throws IOException {
        String snmpTrapTargetAddress = properties.getTrapTargetAddress();

        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();

        // Create Target
        CommunityTarget cTarget = new CommunityTarget();
        cTarget.setCommunity(new OctetString(properties.getTrapTargetCommunity()));
        cTarget.setVersion(SnmpConstants.version2c);
        cTarget.setAddress(new UdpAddress(snmpTrapTargetAddress));
        cTarget.setRetries(0);
        cTarget.setTimeout(5000);

        // Create PDU for
        for (int i = 0; i < queue.size(); i++) {
            Map<String, String> obj = queue.get(i);
            String level = obj.get("level");
            String message = obj.get("message");
            String trapOid = null;

            if(level == "FATAL") {
                trapOid = properties.getTrapOidFatal();
            } else if(level == "WARNING") {
                trapOid = properties.getTrapOidWarning();
            } else if(level == "NORMAL") {
                trapOid = properties.getTrapOidNormal();
            }

            if(trapOid != null) {
                // Logging trapOid
                LogUtil.info("Sending SNMP Trap OID (" + trapOid + ")");

                PDU pdu = new PDU();
                pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                        new OctetString(new Date().toString())));
                pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
                        trapOid)));
                pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                        new IpAddress(snmpTrapTargetAddress.split("/")[0])));

                pdu.add(new VariableBinding(new OID(trapOid), new OctetString(message)));
                pdu.setType(PDU.NOTIFICATION);

                // Send the PDU
                Snmp snmp = new Snmp(transport);
                snmp.send(pdu, cTarget);
                snmp.close();
            }
        }
    }

    public void addMessage(String level, String message) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("level", level);
        data.put("message", message);
        queue.add(data);
    }

    public void run() {
        try {
            sendAll();
        } catch (Exception e) {
            LogUtil.error(e.toString());
        } finally {
            queue = null;
            properties = null;
        }
    }

    public static void main(String[] args) {
        TrapSender trap = new TrapSender(SNMPConfig.getProperties());
        trap.addMessage("FATAL", "a");
        trap.addMessage("FATAL", "b");
        trap.addMessage("FATAL", "c");
        trap.addMessage("WARNING", "d");
        trap.start();
    }
}
