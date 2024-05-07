package weblogic.xml.crypto.wss;

import java.security.Key;
import java.util.List;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;

public class Encryption implements EncryptionInfo {
   private EncryptedData encryptedData;
   private EncryptedKey encryptedKey;
   private List nodeList;
   private KeySelectorResult ksr;

   public Encryption(EncryptedData var1, EncryptedKey var2, List var3, KeySelectorResult var4) {
      this.nodeList = var3;
      this.encryptedData = var1;
      this.encryptedKey = var2;
      this.ksr = var4;
   }

   public Key getKey() {
      return this.getKeySelectorResult().getKey();
   }

   public List getNodes() {
      return this.nodeList;
   }

   public EncryptedData getEncryptedData() {
      return this.encryptedData;
   }

   public KeySelectorResult getKeySelectorResult() {
      return this.ksr;
   }

   public EncryptedKey getEncryptedKey() {
      return this.encryptedKey;
   }
}
