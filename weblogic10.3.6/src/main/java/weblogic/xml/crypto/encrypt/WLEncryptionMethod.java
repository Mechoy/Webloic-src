package weblogic.xml.crypto.encrypt;

import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class WLEncryptionMethod implements WLXMLStructure, EncryptionMethod {
   public static final String TAG_ENCRYPTION_METHOD = "EncryptionMethod";
   public static final String TAG_KEY_SIZE = "KeySize";
   public static String ATTR_ALGORITHM = "Algorithm";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();
   private final String algorithm;
   private final Integer keySize;
   protected final AlgorithmParameterSpec params;

   protected WLEncryptionMethod(String var1, Integer var2, AlgorithmParameterSpec var3) {
      this.algorithm = var1;
      this.keySize = var2;
      this.params = var3;
   }

   protected static final void initFactories() {
      EncryptionAlgorithmDES3.init();
      EncryptionAlgorithmAES.init();
      KeyWrapDES3.init();
      KeyWrapAES.init();
      KeyWrapRSA.init();
      KeyWrapRSAOAEP.init();
   }

   public static void register(WLEncryptionMethodFactory var0) {
      factories.put(var0.getAlgorithm(), var0);
   }

   static WLEncryptionMethod get(String var0, Integer var1, AlgorithmParameterSpec var2) throws XMLEncryptionException {
      WLEncryptionMethodFactory var3 = getFactory(var0);
      return var3.getEncryptionMethod(var2, var1);
   }

   private static WLEncryptionMethodFactory getFactory(String var0) throws XMLEncryptionException {
      WLEncryptionMethodFactory var1 = (WLEncryptionMethodFactory)factories.get(var0);
      if (var1 == null) {
         throw new XMLEncryptionException(var0 + " not supported");
      } else {
         return var1;
      }
   }

   static KeyWrap getKeyWrap(String var0, AlgorithmParameterSpec var1, Integer var2) throws XMLEncryptionException {
      WLEncryptionMethodFactory var3 = getFactory(var0);
      return var3.getKeyWrap(var1, var2);
   }

   static EncryptionAlgorithm getEncryptionAlgorithm(String var0, AlgorithmParameterSpec var1, Integer var2) throws XMLEncryptionException {
      WLEncryptionMethodFactory var3 = getFactory(var0);
      return var3.getEncryptionAlgorithm(var1, var2);
   }

   public final String getAlgorithm() {
      return this.algorithm;
   }

   public final AlgorithmParameterSpec getParameterSpec() {
      return this.params;
   }

   public final Integer getKeySize() {
      return this.keySize;
   }

   /** @deprecated */
   public final void read(XMLStreamReader var1) {
      throw new UnsupportedOperationException("Use WLEncryptionMethod.newInstance() instead.");
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod");
         var1.writeAttribute(ATTR_ALGORITHM, this.getAlgorithm());
         if (this.keySize != null) {
            StaxUtils.writeElement(var1, "http://www.w3.org/2001/04/xmlenc#", "KeySize", this.keySize.toString());
         }

         this.writeParameters(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Problem writing EncryptionMethod", var3);
      }
   }

   public static WLEncryptionMethod newInstance(XMLStreamReader var0) throws MarshalException {
      try {
         var0.require(1, "http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod");
      } catch (XMLStreamException var8) {
         throw new MarshalException("Error marshalling EncryptionMethod", var8);
      }

      String var1 = StaxUtils.getAttributeValue("http://www.w3.org/2001/04/xmlenc#", ATTR_ALGORITHM, var0);
      if (var1 == null) {
         throw new MarshalException("EncryptionMethod did not contain required attribute, " + ATTR_ALGORITHM);
      } else {
         try {
            var0.next();
         } catch (XMLStreamException var7) {
            throw new MarshalException(var7);
         }

         try {
            WLEncryptionMethodFactory var2 = getFactory(var1);
            Integer var3 = readKeySize(var0);
            AlgorithmParameterSpec var4 = var2.readParameters(var0);
            StaxUtils.readEnd(var0, "http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod");
            return var2.getEncryptionMethod(var4, var3);
         } catch (XMLEncryptionException var5) {
            throw new MarshalException(var5);
         } catch (XMLStreamException var6) {
            throw new MarshalException(var6);
         }
      }
   }

   private static Integer readKeySize(XMLStreamReader var0) throws MarshalException {
      Integer var1;
      if (var0.isStartElement() && "KeySize".equals(var0.getLocalName())) {
         String var2;
         try {
            var2 = StaxUtils.readElement(var0, "http://www.w3.org/2001/04/xmlenc#", "KeySize");
         } catch (XMLStreamException var4) {
            throw new MarshalException(var4);
         }

         if (var2 != null) {
            var1 = new Integer(var2);
         } else {
            var1 = null;
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public String toString() {
      String var1 = "EncryptionMethod: algorithm = " + this.getAlgorithm() + ";";
      if (this.keySize != null) {
         var1 = var1 + "KeySize = " + this.keySize + ";";
      }

      if (this.params != null) {
         var1 = var1 + "Parameters = " + this.params + ";";
      }

      return var1;
   }

   AlgorithmParameterSpec readParameters(XMLStreamReader var1) throws MarshalException {
      return null;
   }

   protected void writeParameters(XMLStreamWriter var1) throws XMLStreamException {
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   static {
      initFactories();
   }
}
