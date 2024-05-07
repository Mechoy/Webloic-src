package weblogic.wsee.addressing;

import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public class AddressingProvider10Impl extends AbstractAddressingProvider {
   private static final AddressingProvider10Impl provider = new AddressingProvider10Impl();

   public String getAnonymousNamespaceURI() {
      return "http://www.w3.org/2005/08/addressing/anonymous";
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2005/08/addressing";
   }

   public WSAVersion getWSAVersion() {
      return WSAVersion.WSA10;
   }

   protected boolean isWSA09() {
      return false;
   }

   public static AddressingProvider10Impl getProviderInstance() {
      return provider;
   }
}
