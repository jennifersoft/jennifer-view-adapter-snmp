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
import org.snmp4j.event.ResponseEvent;
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

    private long startTime = System.currentTimeMillis();
    private static TrapSender trapSender = null;
    private static Vector<Map<String, String>> queue = new Vector();

    private TransportMapping transport;

    private TrapSender() {}

    public static TrapSender getInstance() {
        if (trapSender == null) {
            trapSender = new TrapSender();
            trapSender.start();
        }

        return trapSender;
    }

    public void sendMsg() throws InterruptedException {
        SNMPProp prop = SNMPConfig.getLog();

        Vector v = queue;
        queue = new Vector();

        String snmpTrapTargetAddress = prop.getTrapTargetAddress();

        if (snmpTrapTargetAddress == null) {
            if (System.currentTimeMillis() - startTime > 10000) {
                LogUtil.error("SNMP Trap Target Address is not configured.");
                startTime = System.currentTimeMillis();
            }
            return;
        }

        String community = prop.getTrapTargetCommunity();

        // Create Transport Mapping
        try {

            // Create Target
            CommunityTarget cTarget = new CommunityTarget();
            cTarget.setCommunity(new OctetString(community));
            cTarget.setVersion(SnmpConstants.version2c);
            cTarget.setAddress(new UdpAddress(snmpTrapTargetAddress));
            cTarget.setRetries(0);
            cTarget.setTimeout(5000);

            Snmp snmp = new Snmp(transport);

            // Create PDU for V2
            int dataCount = v.size();

            for (int i = 0; i < dataCount; i++) {
                PDU pdu = new PDU();
                Map<String, String> obj = (Map<String, String>) v.get(i);

                String level = obj.get("level");
                String message = obj.get("message");
                String trapOid = null;

                if(level == "FATAL") {
                    trapOid = prop.getTrapOidFatal();
                } else if(level == "WARNING") {
                    trapOid = prop.getTrapOidWarning();
                } else if(level == "NORMAL") {
                    trapOid = prop.getTrapOidNormal();
                }

                if(trapOid != null) {
                    // need to specify the system up time
                    pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                            new OctetString(new Date().toString())));
                    pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
                            trapOid)));
                    pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                            new IpAddress(snmpTrapTargetAddress.split("/")[0])));

                    pdu.add(new VariableBinding(new OID(trapOid), new OctetString(message)));
                    pdu.setType(PDU.NOTIFICATION);

                    // Send the PDU
                    ResponseEvent res = snmp.send(pdu, cTarget);

                    // Logging event messages
                    LogUtil.info(res.toString() + " (" + i + "/" + dataCount + ")");
                }
            }
            snmp.close();

        } catch (IOException e) {
            LogUtil.error(e.getMessage());
        }
    }

    public void run() {
        boolean work = true;

        try {
            transport = new DefaultUdpTransportMapping();
            transport.listen();

            while (work) {
                Thread.sleep(3000);
                sendMsg();
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
    }

    public void addTrapMsg(String level, String message) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("level", level);
        data.put("message", message);

        queue.add(data);
    }

}
