package weblogic.wsee.bind.types;

import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.runtime.internal.XOPMarshaller;
import com.bea.xml.XmlException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.util.MtomUtil;
import weblogic.xml.saaj.SOAPMessageImpl;

public class XOPByteArrayMarshallerImpl extends XOPMarshaller {
   private final SOAPMessageImpl soapMessage;

   public XOPByteArrayMarshallerImpl(SOAPMessage var1) {
      this.soapMessage = (SOAPMessageImpl)var1;
   }

   public void marshalXOP(Object var1, Node var2) throws XmlException {
      if (var1 != null) {
         MtomUtil.replaceContentWithIncludeElement(this.soapMessage, (byte[])((byte[])var1), (SOAPElement)var2);
      }
   }

   public boolean canMarshalXOP(BindingTypeName var1) {
      assert var1 != null;

      return this.soapMessage.getIsMTOMmessage() && this.soapMessage.getProperty("weblogic.wsee.xop.normal") == null && JAVA_BYTE_ARRAY_TYPE.equals(var1.getJavaName()) && (XML_BASE64BINARY_TYPE.equals(var1.getXmlName()) || SOAP_ENC_BASE64_TYPE.equals(var1.getXmlName()));
   }

   public boolean isMtomOverSecurity(BindingTypeName var1) {
      assert var1 != null;

      return this.soapMessage.getIsMTOMmessage() && this.soapMessage.getProperty("weblogic.wsee.xop.normal") != null && JAVA_BYTE_ARRAY_TYPE.equals(var1.getJavaName()) && (XML_BASE64BINARY_TYPE.equals(var1.getXmlName()) || SOAP_ENC_BASE64_TYPE.equals(var1.getXmlName()));
   }

   public void recordBase64Element(Node var1) {
      Object var2 = (Set)this.soapMessage.getProperty("weblogic.wsee.xop.normal.set");
      if (var2 == null) {
         var2 = new HashSet();
         this.soapMessage.setProperty("weblogic.wsee.xop.normal.set", var2);
      }

      if (!((Set)var2).contains(var1)) {
         ((Set)var2).add(var1);
      }

   }
}
