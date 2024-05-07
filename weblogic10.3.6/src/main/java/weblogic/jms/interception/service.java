package weblogic.jms.interception;

import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.AssociationListener;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptor;

public final class service {
   private static service singleton;

   private service() throws InterceptionServiceException {
      this.registerWithInterceptionService();
   }

   public static void initialize() throws InterceptionServiceException {
      if (singleton == null) {
         singleton = new service();
      }
   }

   private void registerWithInterceptionService() throws InterceptionServiceException {
      InterceptionPointNameDescriptor[] var1 = new InterceptionPointNameDescriptor[]{new JMSInterceptionPointNameDescriptor("server name"), new JMSInterceptionPointNameDescriptor("destination name"), new JMSInterceptionPointNameDescriptor("location")};
      MessageInterceptionService.getSingleton().registerInterceptionPointNameDescription("JMS", var1, (AssociationListener)null);
   }

   private class JMSInterceptionPointNameDescriptor extends InterceptionPointNameDescriptor {
      private String title;

      public JMSInterceptionPointNameDescriptor(String var2) {
         this.title = var2;
      }

      public String getTitle() {
         return this.title;
      }

      public int getTotalNumberOfUniqueValue() {
         return 100;
      }

      public boolean isValid(String var1) {
         return true;
      }
   }
}
