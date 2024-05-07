package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.BindingContext;
import com.bea.staxb.runtime.EncodingStyle;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.staxb.runtime.SoapMarshaller;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

class EncodedSerializerContext extends BaseSerializerContext {
   private final SoapMarshaller marshaller;

   EncodedSerializerContext(BindingContext var1, SchemaTypeLoader var2, EncodingStyle var3, MarshalOptions var4) throws XmlException {
      super(var2, var4);
      this.marshaller = var1.createSoapMarshaller(var3);
   }

   protected void marshalType(XMLStreamWriter var1, Object var2, QName var3, XmlTypeName var4, Class var5) throws XMLStreamException, XmlException {
      this.marshaller.marshalType(var1, var2, var3, var4, var5.getName(), this.marshalOptions);
   }

   protected void marshalElement(XMLStreamWriter var1, Object var2, XmlTypeName var3, Class var4) throws XMLStreamException, XmlException {
      throw new IllegalStateException("marshallElement not supported for Soap Encoding");
   }

   public void serializeReferencedObjects(SOAPElement var1) throws XmlException {
      XMLStreamWriter var2 = this.getStaxDomWriter(var1);
      this.marshaller.marshalReferenced(var2, this.marshalOptions);
   }

   public void serializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, Object var6, QName var7) throws XmlException {
      this.v91serializeXmlObjects(var1, var2, var3, var4, var5, var6, var7);
   }
}
