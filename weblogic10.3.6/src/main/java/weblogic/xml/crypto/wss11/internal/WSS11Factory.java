package weblogic.xml.crypto.wss11.internal;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.dom.marshal.MarshalException;

public class WSS11Factory {
   private static WSS11Factory factory = new WSS11Factory();

   private WSS11Factory() {
   }

   public static WSS11Factory getInstance() {
      return factory;
   }

   public static Security unmarshalAndProcessSecurity(WSS11Context var0) throws MarshalException {
      SecurityImpl var1 = new SecurityImpl();
      var1.unmarshal(var0);
      return var1;
   }

   public static Security unmarshalAndProcessSecurity(SOAPMessageContext var0) throws MarshalException {
      SecurityImpl var1 = new SecurityImpl();
      var1.unmarshal(var0);
      return var1;
   }

   public static Security newSecurity(WSSecurityContext var0) {
      return new SecurityImpl(var0);
   }

   public static SignatureConfirmation newSignatureConfirmation(String var0) {
      return new SignatureConfirmationImpl(var0);
   }

   public static SignatureConfirmation newSignatureConfirmation() {
      return new SignatureConfirmationImpl();
   }
}
