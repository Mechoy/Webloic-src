package weblogic.wsee.security.wssc.dk;

import java.security.Key;
import java.util.Arrays;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class DKCredential {
   private SecurityTokenReference str;
   private SecurityToken st;
   private String algorithm;
   private int generation = -1;
   private int offset = -1;
   private int length = -1;
   private String label;
   private byte[] nonce;
   private Key secretKey;

   public SecurityTokenReference getTokenReference() {
      return this.str;
   }

   public void setTokenReference(SecurityTokenReference var1) {
      this.str = var1;
   }

   public SecurityToken getSecurityToken() {
      return this.st;
   }

   public void setSecurityToken(SecurityToken var1) {
      this.st = var1;
   }

   public String getAlgorithm() {
      return this.algorithm;
   }

   public void setAlgorithm(String var1) {
      this.algorithm = var1;
   }

   public int getGeneration() {
      return this.generation;
   }

   public void setGeneration(int var1) {
      this.generation = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public int getLength() {
      return this.length;
   }

   public void setLength(int var1) {
      this.length = var1;
   }

   public byte[] getNonce() {
      return this.nonce;
   }

   public void setNonce(byte[] var1) {
      this.nonce = var1;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public void setSecretKey(Key var1) {
      this.secretKey = var1;
   }

   public Key getSecretKey() {
      return this.secretKey;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DKCredential)) {
         return false;
      } else {
         DKCredential var2 = (DKCredential)var1;
         return equals(this.getSecurityToken(), var2.getSecurityToken()) && equals(this.getLabel(), var2.getLabel()) && Arrays.equals(this.getNonce(), var2.getNonce());
      }
   }

   private static final boolean equals(Object var0, Object var1) {
      return var0 == null && var1 == null || var0 != null && var0.equals(var1);
   }
}
