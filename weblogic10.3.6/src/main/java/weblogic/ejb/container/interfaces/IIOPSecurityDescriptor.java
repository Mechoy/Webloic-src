package weblogic.ejb.container.interfaces;

public interface IIOPSecurityDescriptor {
   String NONE_VAL = "none";
   String SUPPORTED_VAL = "supported";
   String REQUIRED_VAL = "required";

   void setTransport_integrity(String var1);

   String getTransport_integrity();

   void setTransport_confidentiality(String var1);

   String getTransport_confidentiality();

   void setTransport_client_cert_authentication(String var1);

   String getTransport_client_cert_authentication();

   void setClient_authentication(String var1);

   String getClient_authentication();

   void setIdentity_assertion(String var1);

   String getIdentity_assertion();
}
