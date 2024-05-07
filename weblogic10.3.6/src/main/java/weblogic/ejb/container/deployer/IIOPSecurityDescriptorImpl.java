package weblogic.ejb.container.deployer;

import weblogic.ejb.container.interfaces.IIOPSecurityDescriptor;

class IIOPSecurityDescriptorImpl implements IIOPSecurityDescriptor {
   private String transport_integrity = "supported";
   private String transport_confidentiality = "supported";
   private String transport_client_cert_authentication = "supported";
   private String client_authentication = "supported";
   private String identity_assertion = "supported";

   public IIOPSecurityDescriptorImpl() {
   }

   public void setTransport_integrity(String var1) {
      this.transport_integrity = var1;
   }

   public String getTransport_integrity() {
      return this.transport_integrity;
   }

   public void setTransport_confidentiality(String var1) {
      this.transport_confidentiality = var1;
   }

   public String getTransport_confidentiality() {
      return this.transport_confidentiality;
   }

   public void setTransport_client_cert_authentication(String var1) {
      this.transport_client_cert_authentication = var1;
   }

   public String getTransport_client_cert_authentication() {
      return this.transport_client_cert_authentication;
   }

   public void setClient_authentication(String var1) {
      this.client_authentication = var1;
   }

   public String getClient_authentication() {
      return this.client_authentication;
   }

   public void setIdentity_assertion(String var1) {
      this.identity_assertion = var1;
   }

   public String getIdentity_assertion() {
      return this.identity_assertion;
   }
}
