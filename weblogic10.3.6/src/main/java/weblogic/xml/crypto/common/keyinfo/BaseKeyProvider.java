package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class BaseKeyProvider implements KeyProvider {
   private final byte[] identifier;
   private final String name;
   private final Collection uri;
   private final SecurityToken token;

   protected BaseKeyProvider(String var1, byte[] var2, String var3) {
      this(var1, var2, (Collection)Collections.singleton(var3));
   }

   protected BaseKeyProvider(String var1, byte[] var2, Collection var3) {
      this(var1, var2, (Collection)var3, (SecurityToken)null);
   }

   protected BaseKeyProvider(String var1, byte[] var2, String var3, SecurityToken var4) {
      this(var1, var2, (Collection)Collections.singleton(var3), var4);
   }

   protected BaseKeyProvider(String var1, byte[] var2, Collection var3, SecurityToken var4) {
      this.name = var1;
      this.identifier = var2;
      this.uri = var3;
      this.token = var4;
   }

   public KeySelectorResult getKeyByIdentifier(byte[] var1, String var2, KeySelector.Purpose var3) {
      return KeyUtils.matches(var1, this.identifier) ? this.getKey(var2, var3) : null;
   }

   public KeySelectorResult getKeyByName(String var1, String var2, KeySelector.Purpose var3) {
      return var1 != null && var1.equals(this.name) ? this.getKey(var2, var3) : null;
   }

   public KeySelectorResult getKeyByURI(String var1, String var2, KeySelector.Purpose var3) {
      return this.uri.contains(var1) ? this.getKey(var2, var3) : null;
   }

   public KeySelectorResult getKeyBySubjectName(String var1, String var2, KeySelector.Purpose var3) {
      return null;
   }

   public KeySelectorResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeySelector.Purpose var4) {
      return null;
   }

   public KeySelectorResult getKeyBySTR(SecurityTokenReference var1, String var2, KeySelector.Purpose var3) {
      return null;
   }

   public byte[] getIdentifier() {
      return this.identifier;
   }

   public String getName() {
      return this.name;
   }

   public String getUri() {
      return this.uri != null && !this.uri.isEmpty() ? (String)this.uri.iterator().next() : null;
   }

   public SecurityToken getSecurityToken() {
      return this.token;
   }

   public String toString() {
      return this.getClass() + "{" + "identifier=" + (this.identifier == null ? null : "length:" + this.identifier.length) + ", name='" + this.name + "'" + ", uri='" + (this.uri != null && !this.uri.isEmpty() ? Arrays.deepToString(this.uri.toArray()) : "") + "'" + "}";
   }
}
