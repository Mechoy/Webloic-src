package weblogic.xml.crypto.dsig.keyinfo;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyName;
import weblogic.xml.crypto.utils.StaxUtils;

public class KeyNameImpl extends KeyInfoObjectBase implements KeyName, XMLStructure, WLXMLStructure, KeyInfoObjectFactory {
   private String name;

   KeyNameImpl(String var1) {
      this.name = var1;
   }

   private KeyNameImpl() {
   }

   public static void init() {
      register(new KeyNameImpl());
   }

   public String getName() {
      return this.name;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "KeyName");
         var1.writeCharacters(this.name);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write KeyName element.", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.next();
         this.name = var1.getText();
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "KeyName", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read KeyName element.", var3);
      }
   }

   public QName getQName() {
      return DsigConstants.KEYNAME_QNAME;
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException {
      KeyNameImpl var2 = new KeyNameImpl();
      var2.read(var1);
      return var2;
   }

   public Object newKeyInfoObject() {
      return new KeyNameImpl();
   }
}
