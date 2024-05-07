package weblogic.wsee.addressing;

import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public final class AddressingProviderFactory {
   private static final AddressingProviderFactory instance = new AddressingProviderFactory();

   private AddressingProviderFactory() {
   }

   public static AddressingProviderFactory getInstance() {
      return instance;
   }

   public AddressingProvider getAddressingProvider(MessageContext var1) {
      if (var1 == null) {
         throw new AssertionError("MessageContext should not be null!");
      } else {
         return (AddressingProvider)(AddressingHelper.getWSAVersion(var1).equals(WSAVersion.MemberSubmission) ? AddressingProviderSubmissionImpl.getProviderInstance() : AddressingProvider10Impl.getProviderInstance());
      }
   }

   public AddressingProvider getAddressingProvider(Map var1) {
      return (AddressingProvider)(AddressingHelper.getWSAVersion(var1).equals(WSAVersion.MemberSubmission) ? AddressingProviderSubmissionImpl.getProviderInstance() : AddressingProvider10Impl.getProviderInstance());
   }
}
