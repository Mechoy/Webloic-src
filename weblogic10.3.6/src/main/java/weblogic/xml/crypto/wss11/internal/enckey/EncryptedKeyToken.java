package weblogic.xml.crypto.wss11.internal.enckey;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.dom.marshal.MarshalException;

public class EncryptedKeyToken implements SecurityToken {
   private transient Key key = null;
   private String id = null;
   private EncryptedKeyProvider keyProvider = null;
   private EncryptedKey encryptedKey = null;
   private DOMEncryptContext encCtx = null;
   private String valueType = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey";

   public EncryptedKeyToken() {
   }

   public EncryptedKeyToken(EncryptedKeyProvider var1, String var2) {
      this.keyProvider = var1;
      this.id = var2;
   }

   public EncryptedKeyToken(Key var1, String var2) {
      this.key = var1;
      this.id = var2;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public String getValueType() {
      return this.valueType;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
   }

   public PrivateKey getPrivateKey() {
      return null;
   }

   public PublicKey getPublicKey() {
      return null;
   }

   public Key getSecretKey() {
      return this.key;
   }

   public void setSecretKey(Key var1) {
      this.key = var1;
   }

   public Object getCredential() {
      return this.keyProvider;
   }

   public KeyProvider getKeyProvider() {
      return this.keyProvider;
   }

   public void setEncryptedKey(EncryptedKey var1) throws XMLEncryptionException {
      this.encryptedKey = var1;
      this.id = var1.getId();
      this.keyProvider = new EncryptedKeyProvider(var1, this.key, this);
   }

   public EncryptedKey getEncryptedKey() {
      return this.encryptedKey;
   }

   public void setKeyProvider(EncryptedKeyProvider var1) {
      this.keyProvider = var1;
   }

   public void setDOMEncryptContext(DOMEncryptContext var1) {
      this.encCtx = var1;
   }

   public DOMEncryptContext getDOMEncryptContext() {
      return this.encCtx;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
   }

   public void unmarshal(Node var1) throws MarshalException {
   }

   public byte[] getKeyIdentifier() throws XMLEncryptionException {
      return EncryptedKeyProvider.getKeyIdentifier(this.encryptedKey);
   }

   public List<byte[]> getKeyIdentifiers() throws XMLEncryptionException {
      return EncryptedKeyProvider.getKeyIdentifiers(this.encryptedKey);
   }
}
