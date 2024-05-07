package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class X509IssuerSerial implements DSIGConstants {
   private String issuerName;
   private BigInteger serialNumber;

   public X509IssuerSerial(String var1, BigInteger var2) {
      this.issuerName = var1;
      this.serialNumber = var2;
   }

   private X509IssuerSerial(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public String getIssuerName() {
      return this.issuerName;
   }

   public BigInteger getIssuerSerialNumber() {
      return this.serialNumber;
   }

   public String toString() {
      return "IssuerSerial:  issuerName=" + this.issuerName + "  serialNumber=" + this.serialNumber;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "X509IssuerSerial", var3 + 2);
      StreamUtils.addElement(var1, var2, "X509IssuerName", (String)this.issuerName, var3 + 4, 2);
      StreamUtils.addElement(var1, var2, "X509SerialNumber", this.serialNumber.toString(), var3 + 4);
      StreamUtils.addEnd(var1, var2, "X509IssuerSerial", var3 + 2);
   }

   public static X509IssuerSerial fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      return StreamUtils.peekElement(var0, var1, "X509IssuerSerial") ? new X509IssuerSerial(var0, var1) : null;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      XMLEvent var3 = var1.next();
      this.issuerName = StreamUtils.getValue(var1, var2, "X509IssuerName");
      String var4 = StreamUtils.getValue(var1, var2, "X509SerialNumber");

      try {
         this.serialNumber = new BigInteger(var4);
      } catch (NumberFormatException var6) {
         throw new XMLStreamException(var4 + " is not a valid Integer");
      }

      StreamUtils.closeScope(var1, var2, "X509IssuerSerial");
   }
}
