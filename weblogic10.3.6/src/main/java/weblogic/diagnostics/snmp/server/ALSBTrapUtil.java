package weblogic.diagnostics.snmp.server;

import java.util.LinkedList;
import weblogic.diagnostics.snmp.agent.SNMPTrapException;
import weblogic.diagnostics.snmp.agent.SNMPTrapSender;
import weblogic.diagnostics.snmp.agent.SNMPTrapUtil;

public class ALSBTrapUtil {
   public static void sendALSBAlert(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11) throws SNMPTrapException {
      SNMPTrapSender var12 = SNMPTrapUtil.getInstance().getSNMPTrapSender();
      if (var12 != null) {
         LinkedList var13 = new LinkedList();
         var13.add(new Object[]{"trapALSBAlertTrapType", var0});
         var13.add(new Object[]{"trapALSBAlertSeverity", var1});
         var13.add(new Object[]{"trapALSBAlertDomainName", var2});
         var13.add(new Object[]{"trapALSBAlertServerName", var3});
         var13.add(new Object[]{"trapALSBAlertAlertId", var4});
         var13.add(new Object[]{"trapALSBAlertRuleId", var5});
         var13.add(new Object[]{"trapALSBAlertRuleName", var6});
         var13.add(new Object[]{"trapALSBAlertRuleCondition", var7});
         var13.add(new Object[]{"trapALSBAlertAlertTime", var8});
         var13.add(new Object[]{"trapALSBAlertAnnotation", var9});
         var13.add(new Object[]{"trapALSBAlertServiceName", var10});
         var13.add(new Object[]{"trapALSBAlertServicePath", var11});
         var12.sendTrap("wlsALSBAlert", var13);
      }

   }
}
