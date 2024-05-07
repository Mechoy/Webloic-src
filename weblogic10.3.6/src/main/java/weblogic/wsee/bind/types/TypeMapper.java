package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;

public interface TypeMapper {
   void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, boolean var7, String var8) throws XmlException;

   Object deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException;

   void serializeElement(SOAPElement var1, Object var2, Class var3, QName var4, SerializerContext var5, boolean var6, String var7) throws XMLStreamException, XmlException;

   Object deserializeElement(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException, XMLStreamException;
}
