package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public abstract class SecurityTokenImpl implements SecurityToken, Serializable {
   private static final long serialVersionUID = 5976116533830064964L;
   private Object credentials;

   protected SecurityTokenImpl() {
   }

   protected SecurityTokenImpl(Object var1) {
      this.credentials = var1;
   }

   public Object getCredential() {
      return this.credentials;
   }

   public abstract String getValueType();

   public abstract String getId();

   public abstract void setId(String var1);

   public abstract PrivateKey getPrivateKey();

   public abstract PublicKey getPublicKey();

   public abstract Key getSecretKey();
}
