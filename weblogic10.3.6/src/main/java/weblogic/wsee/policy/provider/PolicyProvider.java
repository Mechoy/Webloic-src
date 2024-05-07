package weblogic.wsee.policy.provider;

import weblogic.wsee.policy.framework.PolicyAssertionFactory;

public class PolicyProvider {
   protected PolicyAssertionFactory assertionFactory;
   protected PolicyValidationHandler validationHandler;
   protected ServiceConfigurationHandler serviceConfigHandler;
   protected ClientConfigurationHandler clientConfigHandler;

   public PolicyProvider() {
   }

   public PolicyProvider(PolicyAssertionFactory var1, ClientConfigurationHandler var2, ServiceConfigurationHandler var3, PolicyValidationHandler var4) {
      this.assertionFactory = var1;
      this.clientConfigHandler = var2;
      this.serviceConfigHandler = var3;
      this.validationHandler = var4;
   }

   public PolicyAssertionFactory getAssertionFactory() {
      return this.assertionFactory;
   }

   public void setAssertionFactory(PolicyAssertionFactory var1) {
      this.assertionFactory = var1;
   }

   public ServiceConfigurationHandler getServiceConfigHandler() {
      return this.serviceConfigHandler;
   }

   public void setServiceConfigHandler(ServiceConfigurationHandler var1) {
      this.serviceConfigHandler = var1;
   }

   public ClientConfigurationHandler getClientConfigHandler() {
      return this.clientConfigHandler;
   }

   public void setClientConfigHandler(ClientConfigurationHandler var1) {
      this.clientConfigHandler = var1;
   }

   public PolicyValidationHandler getValidationHandler() {
      return this.validationHandler;
   }

   public void setValidationHandler(PolicyValidationHandler var1) {
      this.validationHandler = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.assertionFactory != null) {
         var1.append("assertionFactory = " + this.assertionFactory.getClass().getName());
      }

      if (this.validationHandler != null) {
         var1.append("\nvalidationHandler = " + this.validationHandler.getClass().getName());
      }

      if (this.serviceConfigHandler != null) {
         var1.append("\nserviceConfigHandler = " + this.serviceConfigHandler.getClass().getName());
      }

      if (this.clientConfigHandler != null) {
         var1.append("\nclientConfigHandler = " + this.clientConfigHandler.getClass().getName());
      }

      return var1.toString();
   }
}
