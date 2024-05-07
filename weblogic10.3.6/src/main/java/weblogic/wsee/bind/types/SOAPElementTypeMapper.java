package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class SOAPElementTypeMapper implements TypeMapper {
   private static final boolean verbose = Verbose.isVerbose(SOAPElementTypeMapper.class);

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, boolean var7, String var8) throws XmlException {
      this.serialize(var2, var1);
   }

   private void serialize(Object var1, SOAPElement var2) throws XmlException {
      if (verbose) {
         Verbose.here();
      }

      if (var1 != null) {
         if (var1 instanceof SOAPElement) {
            SOAPElement var3 = (SOAPElement)var1;

            try {
               var2.addChildElement(var3);
            } catch (SOAPException var5) {
               throw new XmlException("Failed to add child element", var5);
            }
         } else {
            throw new XmlException("object type unknown: " + var1.getClass());
         }
      }
   }

   public Object deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException {
      if (verbose) {
         Verbose.here();
      }

      return var1;
   }

   public void serializeElement(SOAPElement var1, Object var2, Class var3, QName var4, SerializerContext var5, boolean var6, String var7) throws XMLStreamException, XmlException {
      this.serialize(var2, var1);
   }

   public Object deserializeElement(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException, XMLStreamException {
      if (verbose) {
         Verbose.here();
      }

      return var1;
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" should not be encoding SOAPElement as XMLType xs:base64Binary");
   }
}
