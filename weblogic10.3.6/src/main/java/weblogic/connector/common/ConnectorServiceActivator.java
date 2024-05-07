package weblogic.connector.common;

import weblogic.server.ServiceActivator;

public class ConnectorServiceActivator extends ServiceActivator {
   public static final ConnectorServiceActivator INSTANCE = new ConnectorServiceActivator();

   private ConnectorServiceActivator() {
      super("weblogic.connector.common.ConnectorService");
   }

   public String getVersion() {
      return "1.5";
   }

   public String getName() {
      return "J2EE Connector";
   }
}
