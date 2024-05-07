package weblogic.wsee.addressing;

import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public class AddressingProviderSubmissionImpl extends AbstractAddressingProvider {
   private static AddressingProviderSubmissionImpl provider = new AddressingProviderSubmissionImpl();

   public String getNamespaceURI() {
      return "http://schemas.xmlsoap.org/ws/2004/08/addressing";
   }

   public WSAVersion getWSAVersion() {
      return WSAVersion.MemberSubmission;
   }

   protected boolean isWSA09() {
      return true;
   }

   public String getAnonymousNamespaceURI() {
      return "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
   }

   public static AddressingProviderSubmissionImpl getProviderInstance() {
      return provider;
   }
}
