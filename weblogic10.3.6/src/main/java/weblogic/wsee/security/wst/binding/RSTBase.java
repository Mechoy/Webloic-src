package weblogic.wsee.security.wst.binding;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class RSTBase extends TrustDOMStructure {
   private static final long serialVersionUID = 6999878704318357569L;
   public static final String ATTR_CONTEXT = "Context";
   protected transient SecurityTokenHandler tokenHandler;
   protected String context;
   protected RequestType requestType;
   protected TokenType tokenType;
   protected AppliesTo appliesTo;
   protected Entropy entropy;
   protected Lifetime lifetime;
   protected KeySize keySize;
   protected KeyType keyType;
   protected ComputedKeyAlgorithm computedKeyAlgorithm;
   protected SecondaryParameters secondaryParameters;

   public void setContext(String var1) {
      this.context = var1;
   }

   public String getContext() {
      return this.context;
   }

   public void setRequestType(RequestType var1) {
      this.requestType = var1;
   }

   public RequestType getRequestType() {
      return this.requestType;
   }

   public void setTokenType(TokenType var1) {
      this.tokenType = var1;
   }

   public TokenType getTokenType() {
      return this.tokenType;
   }

   public void setAppliesTo(AppliesTo var1) {
      this.appliesTo = var1;
   }

   public AppliesTo getAppliesTo() {
      return this.appliesTo;
   }

   public void setEntropy(Entropy var1) {
      this.entropy = var1;
   }

   public Entropy getEntropy() {
      return this.entropy;
   }

   public void setLifetime(Lifetime var1) {
      this.lifetime = var1;
   }

   public Lifetime getLifetime() {
      return this.lifetime;
   }

   public void setKeySize(KeySize var1) {
      this.keySize = var1;
   }

   public KeySize getKeySize() {
      return this.keySize;
   }

   public void setKeyType(KeyType var1) {
      this.keyType = var1;
   }

   public KeyType getKeyType() {
      return this.keyType;
   }

   public ComputedKeyAlgorithm getComputedKeyAlgorithm() {
      return this.computedKeyAlgorithm;
   }

   public void setComputedKeyAlgorithm(ComputedKeyAlgorithm var1) {
      this.computedKeyAlgorithm = var1;
   }

   public SecondaryParameters getSecondaryParameters() {
      return this.secondaryParameters;
   }

   public void setSecondaryParameters(SecondaryParameters var1) {
      this.secondaryParameters = var1;
   }

   public void setTokenHandler(SecurityTokenHandler var1) {
      this.tokenHandler = var1;
   }

   public SecurityTokenHandler getTokenHandler() {
      return this.tokenHandler;
   }

   protected void marshalRSTContents(Element var1, Map var2) throws MarshalException {
      if (this.context != null) {
         setAttribute(var1, "Context", this.context);
      }

      if (this.tokenType != null) {
         this.tokenType.marshal(var1, (Node)null, var2);
      }

      if (this.appliesTo != null) {
         this.appliesTo.marshal(var1, (Node)null, var2);
      }

      if (this.entropy != null) {
         this.entropy.marshal(var1, (Node)null, var2);
      }

      if (this.lifetime != null) {
         this.lifetime.marshal(var1, (Node)null, var2);
      }

      if (this.keySize != null) {
         this.keySize.marshal(var1, (Node)null, var2);
      }

      if (this.keyType != null) {
         this.keyType.marshal(var1, (Node)null, var2);
      }

      if (this.computedKeyAlgorithm != null) {
         this.computedKeyAlgorithm.marshal(var1, (Node)null, var2);
      }

      if (this.secondaryParameters != null) {
         this.secondaryParameters.marshal(var1, (Node)null, var2);
      }

   }

   protected void unmarshalRSTContents(Element var1) throws MarshalException {
      this.context = getAttributeValueAsString(var1, new QName("Context"));
      Element var2 = getElementByTagName(var1, "TokenType", true);
      if (var2 != null) {
         this.tokenType = new TokenType(var2.getNamespaceURI());
         this.tokenType.unmarshal(var2);
      }

      Element var3 = getElementByTagName(var1, "AppliesTo", true);
      if (var3 != null) {
         this.appliesTo = new AppliesTo(var3.getNamespaceURI());
         this.appliesTo.unmarshal(var3);
      }

      Element var4 = getElementByTagName(var1, "Entropy", true);
      if (var4 != null) {
         this.entropy = new Entropy(var4.getNamespaceURI());
         this.entropy.unmarshal(var4);
      }

      Element var5 = getElementByTagName(var1, "Lifetime", true);
      if (var5 != null) {
         this.lifetime = new Lifetime(var5.getNamespaceURI());
         this.lifetime.unmarshal(var5);
      }

      Element var6 = getElementByTagName(var1, "KeySize", true);
      if (var6 != null) {
         this.keySize = new KeySize(var6.getNamespaceURI());
         this.keySize.unmarshal(var6);
      }

      Element var7 = getElementByTagName(var1, "KeyType", true);
      if (var7 != null) {
         this.keyType = new KeyType(var7.getNamespaceURI());
         this.keyType.unmarshal(var7);
      }

      Element var8 = getElementByTagName(var1, "ComputedKeyAlgorithm", true);
      if (var8 != null) {
         this.computedKeyAlgorithm = new ComputedKeyAlgorithm(var8.getNamespaceURI());
         this.computedKeyAlgorithm.unmarshal(var8);
      }

      Element var9 = getElementByTagName(var1, "SecondaryParameters", true);
      if (var9 != null) {
         this.secondaryParameters = new SecondaryParameters(var9.getNamespaceURI());
         this.secondaryParameters.unmarshal(var9);
      }

   }
}
