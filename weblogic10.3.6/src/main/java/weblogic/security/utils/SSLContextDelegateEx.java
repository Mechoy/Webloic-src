package weblogic.security.utils;

public interface SSLContextDelegateEx extends SSLContextDelegate {
   void enableUnencryptedNullCipher(boolean var1);

   boolean isUnencryptedNullCipherEnabled();
}
