package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class TransformImpl implements WLXMLStructure, WLTransform {
   public static final String ALGORITHM_ATTRIBUTE = "Algorithm";
   public static final String TRANSFORM_ELEMENT = "Transform";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initTransformFactories() {
      XPathTransform.init();
      XPathFilter2Transform.init();
      DOMC14NTransform.init();
      ExclDOMC14NTransform.init();
      EnvelopedSignatureTransform.init();
      Base64Transform.init();
   }

   public static void register(TransformFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   static Transform newTransform(String var0, TransformParameterSpec var1) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      TransformFactory var2 = (TransformFactory)factories.get(var0);
      if (var2 == null) {
         throw new NoSuchAlgorithmException(var0 + " not supported");
      } else {
         return var2.newTransform(var1);
      }
   }

   protected static Transform newTransform(String var0) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return newTransform(var0, (TransformParameterSpec)null);
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public abstract String getAlgorithm();

   public AlgorithmParameterSpec getParameterSpec() {
      return null;
   }

   public abstract Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException;

   public final void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "Transform");
         var1.writeAttribute("Algorithm", this.getAlgorithm());
         this.writeParameters(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element Transform", var3);
      }
   }

   protected void writeParameters(XMLStreamWriter var1) throws MarshalException {
   }

   public final void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "Transform");
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var1);
         this.setAlgorithm(var2);
         this.readParameters(var1);
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "Transform", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read Transform " + this.getAlgorithm() + ".", var3);
      }
   }

   protected void readParameters(XMLStreamReader var1) throws MarshalException {
   }

   protected void setAlgorithm(String var1) {
   }

   public static WLTransform readTransform(XMLStreamReader var0) throws MarshalException {
      String var1 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var0);
      Transform var2 = null;

      try {
         var2 = newTransform(var1);
      } catch (NoSuchAlgorithmException var4) {
         throw new MarshalException("Failed to instantiate object for " + var1 + " transform.", var4);
      } catch (InvalidAlgorithmParameterException var5) {
         throw new MarshalException("Failed to instantiate object for " + var1 + " transform.", var5);
      }

      ((WLXMLStructure)var2).read(var0);
      return (WLTransform)var2;
   }

   static {
      initTransformFactories();
   }
}
