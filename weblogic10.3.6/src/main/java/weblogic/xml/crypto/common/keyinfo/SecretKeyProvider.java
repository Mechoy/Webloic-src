package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class SecretKeyProvider extends BaseKeyProvider {
   private final Key key;
   private final String[] algs;
   private final KeySelector.Purpose[] purposes;
   static final boolean DEBUG = false;

   public SecretKeyProvider(Key var1, String var2, byte[] var3, String var4) {
      this(var1, var2, var3, (Collection)Collections.singleton(var4));
   }

   public SecretKeyProvider(Key var1, String var2, byte[] var3, Collection var4) {
      this(var1, var2, var3, (Collection)var4, (SecurityToken)null);
   }

   public SecretKeyProvider(Key var1, String var2, byte[] var3, String var4, SecurityToken var5) {
      this(var1, var2, var3, (Collection)Collections.singleton(var4), var5);
   }

   public SecretKeyProvider(Key var1, String var2, byte[] var3, Collection var4, SecurityToken var5) {
      super(var2, var3, var4, var5);
      if (var1 == null) {
         throw new IllegalArgumentException("Key cannot be null");
      } else {
         this.key = var1;
         this.algs = KeyUtils.getAlgorithms(var1);
         this.purposes = KeyUtils.getPurposes(var1);
         this.dumpDebugInfo();
      }
   }

   public KeySelectorResult getKey(String var1, KeySelector.Purpose var2) {
      if (this.serves(var2) && this.supports(var1)) {
         KeySelectorResultImpl var3 = new KeySelectorResultImpl(this.key);
         var3.setSecurityToken(this.getSecurityToken());
         this.dumpDebugInfo();
         return var3;
      } else {
         return null;
      }
   }

   public KeySelectorResult getKeyBySubjectName(String var1, String var2, KeySelector.Purpose var3) {
      return null;
   }

   public KeySelectorResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeySelector.Purpose var4) {
      return null;
   }

   protected final boolean supports(String var1) {
      return KeyUtils.supports(this.algs, var1);
   }

   protected final boolean serves(KeySelector.Purpose var1) {
      return KeyUtils.serves(this.purposes, var1);
   }

   private void dumpDebugInfo() {
      if (this.key == null) {
         LogUtils.logKeyInfo("SecretKeyProvider key is null");
      } else {
         LogUtils.logKeyInfo("SecretKeyProvider Uri=" + this.getUri() + " Algo =" + this.key.getAlgorithm());
      }
   }
}
