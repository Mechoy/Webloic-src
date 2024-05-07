package weblogic.wsee.bind.runtime;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;

public interface SerializerContext extends BindingContext {
   void setUseMultiRef(boolean var1);

   Document getDocument();

   void setDocument(Document var1);

   void serializeType(SOAPElement var1, Object var2, Class var3, XmlTypeName var4, QName var5, boolean var6, String var7) throws XmlException, XMLStreamException;

   void serializeReferencedObjects(SOAPElement var1) throws XmlException, XMLStreamException;

   void serializeElement(SOAPElement var1, Object var2, Class var3, XmlTypeName var4, boolean var5, String var6) throws XmlException, XMLStreamException;

   void serializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, Object var6, QName var7) throws XmlException;
}
