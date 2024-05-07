package weblogic.wsee.security.bst;

import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public abstract class BSTCredentialProvider implements CredentialProvider {
   protected static final boolean verbose = Verbose.isVerbose(BSTCredentialProvider.class);

   public String[] getValueTypes() {
      return WSSConstants.BUILTIN_BST_VALUETYPES;
   }

   protected static boolean isForEncryption(Purpose var0) {
      return var0 != null && var0.equals(Purpose.ENCRYPT);
   }

   protected static boolean isForDecryption(Purpose var0) {
      return var0 != null && var0.equals(Purpose.DECRYPT);
   }

   protected static boolean isForVerification(Purpose var0) {
      return var0 != null && var0.equals(Purpose.VERIFY);
   }

   protected static boolean isForSigning(Purpose var0) {
      return var0 != null && var0.equals(Purpose.SIGN);
   }

   protected static boolean isForIdentity(Purpose var0) {
      return var0 != null && var0.equals(Purpose.IDENTITY);
   }

   protected static boolean isForResponseEncryption(Purpose var0) {
      return var0 != null && var0.equals(Purpose.ENCRYPT_RESPONSE);
   }
}
