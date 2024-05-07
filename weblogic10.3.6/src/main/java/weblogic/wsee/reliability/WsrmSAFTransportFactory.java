package weblogic.wsee.reliability;

import weblogic.messaging.saf.SAFTransport;

public class WsrmSAFTransportFactory {
   private static Object transport;

   public static synchronized SAFTransport getTransport() {
      if (transport == null) {
         try {
            transport = Class.forName("weblogic.wsee.reliability.WsrmSAFTransport").newInstance();
         } catch (ClassNotFoundException var1) {
            throw new AssertionError(var1);
         } catch (IllegalAccessException var2) {
            throw new AssertionError(var2);
         } catch (InstantiationException var3) {
            throw new AssertionError(var3);
         }
      }

      return (SAFTransport)transport;
   }
}
