package weblogic.xml.crypto.dsig.keyinfo;

import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.X509Data;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.security.utils.Utils;

public class X509DataImpl extends KeyInfoObjectBase implements X509Data, WLXMLStructure, KeyInfoObjectFactory {
   List content;

   public X509DataImpl(List var1) {
      this.content = var1;
   }

   private X509DataImpl() {
      this.content = new ArrayList();
   }

   public static void init() {
      register(new X509DataImpl());
   }

   public List getContent() {
      return this.content;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "X509Data");
         Iterator var2 = this.content.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof WLXMLStructure) {
               ((WLXMLStructure)var3).write(var1);
            } else if (var3 instanceof X509Certificate) {
               this.writeCertificate(var1, (X509Certificate)var3);
            } else if (var3 instanceof String) {
               this.writeSubjectName(var1, (String)var3);
            } else if (var3 instanceof byte[]) {
               this.writeKeyIdentifier(var1, (byte[])((byte[])var3));
            } else {
               if (!(var3 instanceof X509CRL)) {
                  throw new MarshalException("Unsupported X509Data content object " + var3);
               }

               this.writeCRL(var1, (X509CRL)var3);
            }
         }

         var1.writeEndElement();
      } catch (XMLStreamException var4) {
         throw new MarshalException("Failed to write element X509Data", var4);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();

         while(!StaxUtils.is(var1, DsigConstants.X509DATA_QNAME)) {
            Object var2 = readContentElement(var1);
            this.content.add(var2);
            if (!var1.isStartElement() && !var1.isEndElement()) {
               var1.nextTag();
            }
         }

      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   private void writeCertificate(XMLStreamWriter var1, X509Certificate var2) throws MarshalException {
      try {
         this.write(var1, "X509Certificate", Utils.base64(var2.getEncoded()));
      } catch (CertificateEncodingException var4) {
         throw new MarshalException("Failed to write X509Certificate", var4);
      }
   }

   private void writeSubjectName(XMLStreamWriter var1, String var2) throws MarshalException {
      this.write(var1, "X509SubjectName", var2);
   }

   private void writeKeyIdentifier(XMLStreamWriter var1, byte[] var2) throws MarshalException {
      this.write(var1, "X509KeyIdentifier", Utils.base64(var2));
   }

   private void writeCRL(XMLStreamWriter var1, X509CRL var2) throws MarshalException {
      try {
         this.write(var1, "X509CRL", Utils.base64(var2.getEncoded()));
      } catch (CRLException var4) {
         throw new MarshalException("Failed to write X509CRL", var4);
      }
   }

   private void write(XMLStreamWriter var1, String var2, String var3) throws MarshalException {
      try {
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", var2, var3);
      } catch (XMLStreamException var5) {
         throw new MarshalException("Failed to write " + var2, var5);
      }
   }

   private static Object readContentElement(XMLStreamReader var0) throws MarshalException {
      String var1 = var0.getNamespaceURI();
      if (!"http://www.w3.org/2000/09/xmldsig#".equals(var1)) {
         throw new MarshalException("Unsupported namespace for X509Data content element.");
      } else {
         String var2 = var0.getLocalName();
         if ("X509IssuerSerial".equals(var2)) {
            return readX509IssuerSerial(var0);
         } else if ("X509Certificate".equals(var2)) {
            return readX509Certificate(var0);
         } else if ("X509SubjectName".equals(var2)) {
            return readX509SubjectName(var0);
         } else if ("X509KeyIdentifier".equals(var2)) {
            return readX509KeyIdentifier(var0);
         } else if ("X509CRL".equals(var2)) {
            return readX509CRL(var0);
         } else {
            throw new MarshalException("Unsupported content element for X509Data: " + var2);
         }
      }
   }

   private static X509IssuerSerial readX509IssuerSerial(XMLStreamReader var0) throws MarshalException {
      X509IssuerSerialImpl var1 = new X509IssuerSerialImpl();
      var1.read(var0);
      return var1;
   }

   private static X509Certificate readX509Certificate(XMLStreamReader var0) throws MarshalException {
      String var1 = read(var0, "X509Certificate");
      return CertUtils.getCertificate(Utils.base64(var1));
   }

   private static String readX509SubjectName(XMLStreamReader var0) throws MarshalException {
      return read(var0, "X509SubjectName");
   }

   private static byte[] readX509KeyIdentifier(XMLStreamReader var0) throws MarshalException {
      String var1 = read(var0, "X509KeyIdentifier");
      return Utils.base64(var1);
   }

   private static X509CRL readX509CRL(XMLStreamReader var0) throws MarshalException {
      String var1 = read(var0, "X509CRL");
      byte[] var2 = Utils.base64(var1);
      return CertUtils.getCRL(var2);
   }

   private static String read(XMLStreamReader var0, String var1) throws MarshalException {
      try {
         return StaxUtils.readElement(var0, "http://www.w3.org/2000/09/xmldsig#", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read " + var1, var3);
      }
   }

   public QName getQName() {
      return DsigConstants.X509DATA_QNAME;
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException {
      X509DataImpl var2 = new X509DataImpl();
      var2.read(var1);
      return var2;
   }
}
