package weblogic.xml.crypto.encrypt.keyinfo;

import java.security.Key;
import java.util.List;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.api.keyinfo.AgreementMethod;

public class KeyInfoFactoryImpl extends weblogic.xml.crypto.dsig.keyinfo.KeyInfoFactoryImpl {
   public KeyInfo newKeyInfo(List var1) {
      return super.newKeyInfo(var1);
   }

   public KeyInfo newKeyInfo(List var1, String var2) {
      return super.newKeyInfo(var1, var2);
   }

   public AgreementMethod newAgreementMethod(byte[] var1, Key var2, String var3) {
      throw new UnsupportedOperationException();
   }
}
