package weblogic.xml.crypto.dsig;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class DigestMethodImpl implements XMLStructure, WLDigestMethod {
   public static final String DIGESTMETHOD_ELEMENT = "DigestMethod";
   public static final String ALGORITHM_ATTRIBUTE = "Algorithm";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();
   protected final String algorithmURI;

   protected DigestMethodImpl(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Algorithm URI cannot be null");
      } else {
         this.algorithmURI = var1;
      }
   }

   private static final void initDigestFactories() {
      JCEDigestMethod.init();
   }

   public static void register(DigestMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static WLDigestMethod newDigestMethod(String var0) throws NoSuchAlgorithmException {
      return newDigestMethod(var0, (DigestMethodParameterSpec)null);
   }

   static WLDigestMethod newDigestMethod(String var0, DigestMethodParameterSpec var1) throws NoSuchAlgorithmException {
      DigestMethodFactory var2 = (DigestMethodFactory)factories.get(var0);
      if (var2 == null) {
         throw new NoSuchAlgorithmException(var0 + " not supported");
      } else {
         return var2.newDigestMethod((DigestMethodParameterSpec)null);
      }
   }

   public static WLDigestMethod newDigestMethod(XMLStreamReader var0) throws XMLStreamException, NoSuchAlgorithmException, MarshalException {
      var0.require(1, "http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
      String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var0);
      WLDigestMethod var1 = newDigestMethod(var2);
      var1.read(var0);
      return var1;
   }

   public String getURI() {
      return this.algorithmURI;
   }

   public abstract AlgorithmParameterSpec getParameterSpec();

   public String getAlgorithm() {
      return this.algorithmURI;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public final void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
         var1.writeAttribute("Algorithm", this.algorithmURI);
         this.writeParameters(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element DigestMethod", var3);
      }
   }

   protected void writeParameters(XMLStreamWriter var1) throws XMLStreamException {
   }

   public final void read(XMLStreamReader var1) throws MarshalException {
      try {
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var1);
         if (!this.algorithmURI.equals(var2)) {
            throw new MarshalException("DigestMethod read (" + var2 + ") was not the one expected (" + this.algorithmURI + ")");
         } else {
            StaxUtils.readStart(var1, "http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
            this.readParameters(var1);
            StaxUtils.readEnd(var1, "http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
         }
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read DigestMethod " + this.getAlgorithm() + ".", var3);
      }
   }

   protected void readParameters(XMLStreamReader var1) throws XMLStreamException {
      StaxUtils.skipChildren(var1);
   }

   static {
      initDigestFactories();
   }
}
