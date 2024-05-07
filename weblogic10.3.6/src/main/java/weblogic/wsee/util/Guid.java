package weblogic.wsee.util;

import java.rmi.dgc.VMID;
import weblogic.protocol.LocalServerIdentity;

public class Guid {
   public static String generateGuid() {
      String var0 = (new VMID()).toString();
      var0 = var0.replace('[', '(');
      var0 = var0.replace(']', ')');
      return "uuid:" + var0;
   }

   public static String generateGuidStandardChar() {
      String var0 = generateGuid();
      var0 = var0.replace('(', '-');
      var0 = var0.replace(')', '-');
      var0 = var0.replace(':', '-');
      var0 = var0.replace('=', '-');
      var0 = var0.replace(';', '-');
      return var0;
   }

   public static String generateGuidWithServerName() {
      String var0 = LocalServerIdentity.getIdentity().getServerName();
      return "uuid:" + var0 + ":" + generateGuid().substring(5);
   }
}
