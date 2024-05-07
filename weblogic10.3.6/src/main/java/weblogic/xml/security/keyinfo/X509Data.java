package weblogic.xml.security.keyinfo;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class X509Data implements DSIGConstants {
   private List issuerSerials = new ArrayList();
   private List subjectNames = new ArrayList();
   private List certs = new ArrayList();
   private List certificateBase64s = new ArrayList();
   private boolean validated = false;

   public X509Data(X509Certificate var1) {
      this.addCertificate(var1);
      this.addSubjectName(var1.getSubjectX500Principal().getName("RFC2253"));
      this.addIssuerSerial(new X509IssuerSerial(var1.getIssuerX500Principal().getName("RFC2253"), var1.getSerialNumber()));
      this.validated = true;
   }

   public X509Data(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void addIssuerSerial(X509IssuerSerial var1) {
      this.issuerSerials.add(var1);
   }

   public void removeIssuerSerial(X509IssuerSerial var1) {
      this.issuerSerials.remove(var1);
   }

   public Iterator getIssuerSerials() {
      return this.issuerSerials.iterator();
   }

   public void addSubjectName(String var1) {
      this.subjectNames.add(var1);
   }

   public void removeSubjectName(String var1) {
      this.subjectNames.remove(var1);
   }

   public Iterator getSubjectNames() {
      return this.subjectNames.iterator();
   }

   public void addCertificate(X509Certificate var1) {
      this.certs.add(var1);
   }

   public void removeCertificate(X509Certificate var1) {
      this.certs.remove(var1);
   }

   public Iterator getCertificates() throws KeyInfoValidationException {
      this.validate();
      return this.certs.iterator();
   }

   private String getCertificateBase64(X509Certificate var1) {
      byte[] var2;
      try {
         var2 = var1.getEncoded();
      } catch (CertificateEncodingException var4) {
         throw new AssertionError(var4);
      }

      return weblogic.xml.security.utils.Utils.base64(var2);
   }

   public void validate() throws KeyInfoValidationException {
      if (!this.validated) {
         Iterator var1 = this.certificateBase64s.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            this.addCertificate(this.convertCert(var2));
         }

         this.validated = true;
      }
   }

   private X509Certificate convertCert(String var1) throws KeyInfoValidationException {
      try {
         return weblogic.xml.security.utils.Utils.certFromBase64(var1);
      } catch (CertificateException var3) {
         throw new KeyInfoValidationException("cannot decode certificate", var3);
      }
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "X509Data", var3);
      Iterator var4 = this.subjectNames.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         StreamUtils.addElement(var1, var2, "X509SubjectName", (String)var5, var3 + 2, 2);
      }

      var4 = this.issuerSerials.iterator();

      while(var4.hasNext()) {
         X509IssuerSerial var6 = (X509IssuerSerial)var4.next();
         var6.toXML(var1, var2, var3 + 2);
      }

      var4 = this.certs.iterator();

      while(var4.hasNext()) {
         X509Certificate var7 = (X509Certificate)var4.next();
         StreamUtils.addElement(var1, var2, "X509Certificate", (String)this.getCertificateBase64(var7), var3 + 2, 2);
      }

      StreamUtils.addEnd(var1, var2, "X509Data", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      XMLEvent var3 = var1.next();

      while(!var1.peek().isEndElement()) {
         String var4 = StreamUtils.getValue(var1, var2, "X509SubjectName");
         if (var4 != null) {
            this.addSubjectName(var4);
         } else {
            X509IssuerSerial var5 = X509IssuerSerial.fromXML(var1, var2);
            if (var5 != null) {
               this.addIssuerSerial(var5);
            } else {
               String var6 = StreamUtils.getValue(var1, var2, "X509Certificate");
               if (var6 != null) {
                  this.certificateBase64s.add(var6);
               } else {
                  if (var1.peek().isEndElement()) {
                     break;
                  }

                  if (var1.peek().isStartElement()) {
                     var1.skipElement();
                  } else {
                     var1.skip();
                  }
               }
            }
         }
      }

      StreamUtils.closeScope(var1, var2, "X509Data");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<X509Data xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <X509SubjectName>\n    CN=Joe User,O=BEA Systems,L=Liberty Corner,ST=New Jersey,C=US\n  </X509SubjectName>\n  <X509IssuerSerial>\n  <X509IssuerName>\n      O=BEA Systems,L=San Francisco,ST=California,C=US\n  </X509IssuerName>\n    <X509SerialNumber>1017788370348</X509SerialNumber>\n  </X509IssuerSerial>\n  <X509Certificate>\nMIIDUDCCAxCgAwIBAgIGAOz5IVHTMAkGByqGSM44BAMwdjELMAkGA1UEBhMCSUUx\nDzANBgNVBAgTBkR1YmxpbjEkMCIGA1UEChMbQmFsdGltb3JlIFRlY2hub2xvZ2ll\ncyBMdGQuMREwDwYDVQQLEwhYL1NlY3VyZTEdMBsGA1UEAxMUQW5vdGhlciBUcmFu\nc2llbnQgQ0EwHhcNMDIwNDAyMjM1OTUyWhcNMTIwNDAyMjI1OTQ2WjBoMQswCQYD\nVQQGEwJJRTEPMA0GA1UECBMGRHVibGluMSQwIgYDVQQKExtCYWx0aW1vcmUgVGVj\naG5vbG9naWVzIEx0ZC4xETAPBgNVBAsTCFgvU2VjdXJlMQ8wDQYDVQQDEwZNb3Jp\nZ3UwggG2MIIBKwYHKoZIzjgEATCCAR4CgYEAhIqwSieM0aAez+6H71gqCfBnwG3d\nPu7JAElb13GkwXRw9RfPh0VtIVjhDJbyKAIuzCk5r54ccRixa8TQ2vWVwodQ9eru\n7jUknAc2rVEAV5mJTrBr7UUve/X9PWsCDN6lXvFLiJp/Pi/F0cyV/CAp+jIp+rol\nTXOhUz9qEjnFYMMCFQCYS/p4gmsHgo3R89EAE/Hc0dhyWQKBgCWG5hn8DM+1rv5s\nTkJBqyZJXNzy4z974s3sAGURfBBGTpB9kFxfxNt480TxkWeDhR+39DMA5TEAsRPu\nQoB6Tgl7K2nNzRdgJjK4YkBObgX1ljWkAnnJCZSbC8Nh2VpkniV0bM79HnsS+eCf\n8bi2qOOiLSzHeOrtzO8PB0YeeTLQA4GEAAKBgH1NBJ9Az5TwY4tDE0dPYVHHABt+\nyLspnT3k9G6YWUMFhZ/+3RuqEPjnKrPfUoXTTJGIACgPU3/PkqwrPVD0JMdpOcnZ\nLHiJ/P7QRQeMwDRoBrs7genB1bDd4pSJrEUcjrkA5uRrIj2Z5fL+UuLiLGPO2rM7\nBNQRIq3QFPdX++NuozowODAOBgNVHQ8BAf8EBAMCB4AwEQYDVR0OBAoECIK7Ljjh\n+EsfMBMGA1UdIwQMMAqACIocVjBaMhJ9MAkGByqGSM44BAMDLwAwLAIUEJJCOHw8\nppxoRyz3s+Vmb4NKIfMCFDgJoZn9zh/3WoYNBURODwLvyBOy\n  </X509Certificate>\n</X509Data>\n");
      X509Data var2 = (X509Data)DSIGReader.read(var1, 11);
      var2.validate();
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
