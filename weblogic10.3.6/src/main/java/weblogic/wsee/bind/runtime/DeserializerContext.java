package weblogic.wsee.bind.runtime;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;

public interface DeserializerContext extends BindingContext {
   Object deserializeType(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException;

   Object deserializeElement(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException;

   Object deserializeWrappedElement(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException;

   Object deserializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, QName var6) throws XmlException;

   void setMessageContext(MessageContext var1);

   MessageContext getMessageContext();
}
