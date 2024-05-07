package weblogic.xml.crypto.wss.api;

import java.util.Calendar;
import java.util.List;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.security.WssRuntime;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.TimestampImpl;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.dom.marshal.MarshalException;

public class WSSecurityFactory {
   private static WSSecurityFactory factory;

   private WSSecurityFactory() {
   }

   public static WSSecurityFactory getInstance() {
      return factory;
   }

   public static Security newSecurity(WSSecurityContext var0) {
      return new SecurityImpl(var0);
   }

   public static UsernameToken newUsernameToken(String var0, String var1) {
      return null;
   }

   public static UsernameToken newUsernameToken(String var0, String var1, String var2, String var3, String var4, String var5, Calendar var6, List var7) {
      return null;
   }

   public static Timestamp newTimestamp(String var0, Calendar var1, Calendar var2) {
      return new TimestampImpl(var0, var1, var2);
   }

   public static Timestamp newTimestamp(String var0, boolean var1, int var2) {
      return new TimestampImpl(var0, var1, var2);
   }

   public static Security unmarshalAndProcessSecurity(SOAPMessageContext var0) throws MarshalException {
      SecurityImpl var1 = new SecurityImpl();
      var1.unmarshal(var0);
      return var1;
   }

   static {
      WssRuntime.init();
      factory = new WSSecurityFactory();
   }
}
