package weblogic.xml.crypto.dsig;

import java.security.NoSuchAlgorithmException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class SignatureMethodImpl {
   public static final String SIGNATUREMETHOD_ELEMENT = "SignatureMethod";
   public static final String ALGORITHM_ATTRIBUTE = "Algorithm";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initFactories() {
      SignatureMethodRSA.init();
      SignatureMethodDSA.init();
      SignatureMethodHMAC.init();
   }

   public static void register(SignatureMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static SignatureMethod newSignatureMethod(String var0) throws NoSuchAlgorithmException {
      return newSignatureMethod(var0, (SignatureMethodParameterSpec)null);
   }

   public static SignatureMethod newSignatureMethod(String var0, SignatureMethodParameterSpec var1) throws NoSuchAlgorithmException {
      SignatureMethodFactory var2 = (SignatureMethodFactory)factories.get(var0);
      if (var2 == null) {
         throw new NoSuchAlgorithmException(var0 + " not supported.");
      } else {
         return var2.newSignatureMethod();
      }
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
         var1.writeAttribute("Algorithm", this.getAlgorithm());
         this.writeParameters(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element SignatureMethod", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         this.readParameters(var1);
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read SignatureMethod " + this.getAlgorithm() + ".", var3);
      }
   }

   public abstract String getAlgorithm();

   public abstract void writeParameters(XMLStreamWriter var1) throws MarshalException;

   public abstract void readParameters(XMLStreamReader var1) throws MarshalException;

   static {
      initFactories();
   }
}
