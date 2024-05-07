package weblogic.xml.jaxr.registry.bridge.uddi;

import weblogic.auddi.util.Logger;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public class UDDIBridgeLogger implements JAXRLogger {
   private static UDDIBridgeLogger s_instance;

   private UDDIBridgeLogger() {
   }

   public static UDDIBridgeLogger getInstance() {
      if (s_instance == null) {
         s_instance = new UDDIBridgeLogger();
      }

      return s_instance;
   }

   public void error(String var1) {
      Logger.error(var1);
   }

   public void info(String var1) {
      Logger.info(var1);
   }

   public void debug(String var1) {
      Logger.debug(var1);
   }

   public void trace(String var1) {
      Logger.trace(var1);
   }

   public void error(Throwable var1) {
      Logger.error(var1);
   }

   public void info(Throwable var1) {
      Logger.info(var1);
   }

   public void debug(Throwable var1) {
      Logger.debug(var1);
   }

   public void trace(Throwable var1) {
      Logger.trace(var1);
   }
}
