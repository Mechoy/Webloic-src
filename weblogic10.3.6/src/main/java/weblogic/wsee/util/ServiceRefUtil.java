package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.List;

public class ServiceRefUtil {
   public static List<NameValuePair> getStandardStubPropsForPortInfo(boolean var0) {
      ArrayList var1 = new ArrayList();
      if (!var0) {
         return var1;
      } else {
         var1.add(new NameValuePair("weblogic.wsee.wsrm.BaseRetransmissionInterval", "PT30S"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.RetransmissionExponentialBackoff", "true"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.NonBufferedSource", "true"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.AcknowledgementInterval", "PT10S"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.NonBufferedDestination", "true"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.InactivityTimeout", "PT10M"));
         var1.add(new NameValuePair("weblogic.wsee.wsrm.SequenceExpiration", "PT60M"));
         var1.add(new NameValuePair("weblogic.wsee.persistence.DefaultLogicalStoreName", "WseeStore"));
         return var1;
      }
   }

   public static class NameValuePair {
      public String name;
      public String defaultValue;

      public NameValuePair(String var1, String var2) {
         this.name = var1;
         this.defaultValue = var2;
      }
   }
}
