package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;

public class SecondaryParameters extends TrustDOMStructure {
   public static final String NAME = "SecondaryParameters";
   private TokenType tokenType;
   private KeyType keyType;
   private KeySize keySize;
   private CanonicalizationAlgorithm canonicalizationAlgorithm;
   private EncryptionAlgorithm encryptionAlgorithm;
   private EncryptWith encryptWith;
   private SignWith signWith;

   public SecondaryParameters() {
   }

   public SecondaryParameters(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public TokenType getTokenType() {
      return this.tokenType;
   }

   public void setTokenType(TokenType var1) {
      this.tokenType = var1;
   }

   public KeyType getKeyType() {
      return this.keyType;
   }

   public void setKeyType(KeyType var1) {
      this.keyType = var1;
   }

   public KeySize getKeySize() {
      return this.keySize;
   }

   public void setKeySize(KeySize var1) {
      this.keySize = var1;
   }

   public CanonicalizationAlgorithm getCanonicalizationAlgorithm() {
      return this.canonicalizationAlgorithm;
   }

   public void setCanonicalizationAlgorithm(CanonicalizationAlgorithm var1) {
      this.canonicalizationAlgorithm = var1;
   }

   public EncryptionAlgorithm getEncryptionAlgorithm() {
      return this.encryptionAlgorithm;
   }

   public void setEncryptionAlgorithm(EncryptionAlgorithm var1) {
      this.encryptionAlgorithm = var1;
   }

   public EncryptWith getEncryptWith() {
      return this.encryptWith;
   }

   public void setEncryptWith(EncryptWith var1) {
      this.encryptWith = var1;
   }

   public SignWith getSignWith() {
      return this.signWith;
   }

   public void setSignWith(SignWith var1) {
      this.signWith = var1;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.tokenType != null) {
         this.tokenType.marshal(var1, (Node)null, var2);
      }

      if (this.keySize != null) {
         this.keySize.marshal(var1, (Node)null, var2);
      }

      if (this.keyType != null) {
         this.keyType.marshal(var1, (Node)null, var2);
      }

      if (this.canonicalizationAlgorithm != null) {
         this.canonicalizationAlgorithm.marshal(var1, (Node)null, var2);
      }

      if (this.encryptionAlgorithm != null) {
         this.encryptionAlgorithm.marshal(var1, (Node)null, var2);
      }

      if (this.encryptWith != null) {
         this.encryptWith.marshal(var1, (Node)null, var2);
      }

      if (this.signWith != null) {
         this.signWith.marshal(var1, (Node)null, var2);
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getElementByTagName(var1, "TokenType", true);
      if (var2 != null) {
         this.tokenType = new TokenType(var2.getNamespaceURI());
         this.tokenType.unmarshal(var2);
      }

      Element var3 = getElementByTagName(var1, "KeyType", true);
      if (var3 != null) {
         this.keyType = new KeyType(var3.getNamespaceURI());
         this.keyType.unmarshal(var3);
      }

      Element var4 = getElementByTagName(var1, "KeySize", true);
      if (var4 != null) {
         this.keySize = new KeySize(var4.getNamespaceURI());
         this.keySize.unmarshal(var4);
      }

      Element var5 = getElementByTagName(var1, "CanonicalizationAlgorithm", true);
      if (var5 != null) {
         this.canonicalizationAlgorithm = new CanonicalizationAlgorithm(var5.getNamespaceURI());
         this.canonicalizationAlgorithm.unmarshal(var5);
      }

      Element var6 = getElementByTagName(var1, "EncryptionAlgorithm", true);
      if (var6 != null) {
         this.encryptionAlgorithm = new EncryptionAlgorithm(var6.getNamespaceURI());
         this.encryptionAlgorithm.unmarshal(var6);
      }

      Element var7 = getElementByTagName(var1, "EncryptWith", true);
      if (var7 != null) {
         this.encryptWith = new EncryptWith(var7.getNamespaceURI());
         this.encryptWith.unmarshal(var7);
      }

      Element var8 = getElementByTagName(var1, "SignWith", true);
      if (var8 != null) {
         this.signWith = new SignWith(var8.getNamespaceURI());
         this.signWith.unmarshal(var8);
      }

   }

   public String getName() {
      return "SecondaryParameters";
   }
}
