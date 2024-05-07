package weblogic.xml.crypto.common.keyinfo;

import java.security.Key;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class KeySelectorResultImpl implements KeySelectorResult {
   private Key key;
   private String msg;
   private SecurityToken token;

   public KeySelectorResultImpl(Key var1) {
      this(var1, (String)null);
   }

   public KeySelectorResultImpl(Key var1, String var2) {
      this.key = var1;
      this.msg = var2;
   }

   public Key getKey() {
      return this.key;
   }

   public String getMessage() {
      return this.msg;
   }

   public SecurityToken getSecurityToken() {
      return this.token;
   }

   public void setSecurityToken(SecurityToken var1) {
      this.token = var1;
   }

   public String toString() {
      return "[KeySelectorResultImpl: \n[Key: " + this.key + "]" + "\n[Message: " + this.msg + "]" + "\n[SecurityToken: " + this.token + "]]";
   }
}
