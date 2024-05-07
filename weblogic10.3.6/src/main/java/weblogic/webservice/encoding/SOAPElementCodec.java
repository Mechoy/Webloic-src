package weblogic.webservice.encoding;

import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPElement;
import weblogic.xml.schema.binding.DeserializationContext;
import weblogic.xml.schema.binding.DeserializationException;
import weblogic.xml.schema.binding.SerializationContext;
import weblogic.xml.schema.binding.SerializationException;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xmlnode.XMLNode;

/** @deprecated */
public abstract class SOAPElementCodec extends AbstractCodec {
   protected abstract Object deserializeSOAPElement(SOAPElement var1, DeserializationContext var2) throws SOAPFaultException;

   protected abstract SOAPElement serializeToSOAPElement(Object var1, XMLName var2, SOAPElement var3, SerializationContext var4) throws SOAPFaultException;

   public final Object deserialize(XMLName var1, XMLInputStream var2, DeserializationContext var3) throws DeserializationException {
      try {
         var2.skipElement();
      } catch (XMLStreamException var5) {
         throw new DeserializationException("stream error", var5);
      }

      return this.deserializeSOAPElement(var3.getSOAPElement(), var3);
   }

   public final Object deserialize(XMLName var1, Attribute var2, DeserializationContext var3) throws DeserializationException {
      throw new DeserializationException("SOAPElementCodec does not support Attribute deserialization");
   }

   public final void serialize(Object var1, XMLName var2, XMLOutputStream var3, SerializationContext var4) throws SerializationException {
      Object var5 = null;
      SOAPElement var6 = this.serializeToSOAPElement(var1, var2, (SOAPElement)var5, var4);
      if (var6 instanceof XMLNode) {
         XMLNode var7 = (XMLNode)var6;

         try {
            var7.write(var3);
         } catch (XMLStreamException var9) {
            throw new SerializationException("stream error", var9);
         }
      } else {
         throw new SerializationException("child must be an instance of " + XMLNode.class);
      }
   }
}
