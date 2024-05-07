package weblogic.jms;

import weblogic.server.ServiceActivator;

public final class JMSServiceActivator extends ServiceActivator {
   public static final JMSServiceActivator INSTANCE = new JMSServiceActivator();

   private JMSServiceActivator() {
      super("weblogic.jms.JMSServiceServerLifeCycleImpl");
   }

   public String getName() {
      return "JMS Service";
   }

   public String getVersion() {
      return "JMS 1.1";
   }
}
