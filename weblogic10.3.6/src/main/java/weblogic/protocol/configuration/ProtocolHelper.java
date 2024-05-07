package weblogic.protocol.configuration;

import weblogic.protocol.ProtocolManager;

public class ProtocolHelper {
   public static String[] getSupportedProtocols() {
      return ProtocolManager.getProtocols();
   }
}
