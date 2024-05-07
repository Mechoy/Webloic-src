package weblogic.xml.crypto.dsig.keyinfo;

import java.math.BigInteger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.utils.StaxUtils;

public class X509IssuerSerialImpl implements X509IssuerSerial, WLXMLStructure {
   private String issuerName;
   private BigInteger serialNumber;

   public X509IssuerSerialImpl() {
   }

   public X509IssuerSerialImpl(String var1, BigInteger var2) {
      this.issuerName = var1;
      this.serialNumber = var2;
   }

   public String getIssuerName() {
      return this.issuerName;
   }

   public BigInteger getSerialNumber() {
      return this.serialNumber;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "X509IssuerName", this.issuerName);
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber", this.serialNumber.toString());
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   public void read(XMLStreamReader var1, boolean var2) throws MarshalException {
      try {
         StaxUtils.readStart(var1, "http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
         this.issuerName = StaxUtils.readElement(var1, "http://www.w3.org/2000/09/xmldsig#", "X509IssuerName");
         String var3 = StaxUtils.readElement(var1, "http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber");
         this.serialNumber = new BigInteger(var3);
         if (var2) {
            StaxUtils.readEnd(var1, "http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
         }

      } catch (XMLStreamException var4) {
         throw new MarshalException(var4);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      this.read(var1, true);
   }
}
