package weblogic.xml.crypto.encrypt.keyinfo;

import java.security.Key;
import java.security.Provider;
import weblogic.xml.crypto.encrypt.api.keyinfo.AgreementMethod;

public abstract class KeyInfoFactory extends weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory {
   public KeyInfoFactory(String var1, Provider var2) {
      super(var1, var2);
   }

   public abstract AgreementMethod newAgreementMethod(byte[] var1, Key var2, String var3);
}
