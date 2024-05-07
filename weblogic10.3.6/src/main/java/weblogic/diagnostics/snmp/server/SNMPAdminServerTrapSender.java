package weblogic.diagnostics.snmp.server;

import java.rmi.RemoteException;
import java.util.List;
import weblogic.diagnostics.snmp.agent.SNMPTrapException;
import weblogic.diagnostics.snmp.agent.SNMPTrapSender;
import weblogic.logging.DomainLogBroadcasterClient;

public class SNMPAdminServerTrapSender implements SNMPTrapSender {
   public void sendTrap(String var1, List<Object[]> var2) throws SNMPTrapException {
      DomainLogBroadcasterClient var3 = DomainLogBroadcasterClient.getInstance();

      try {
         var3.sendTrap(var1, var2);
      } catch (RemoteException var5) {
         throw new SNMPTrapException(var5);
      }
   }
}
