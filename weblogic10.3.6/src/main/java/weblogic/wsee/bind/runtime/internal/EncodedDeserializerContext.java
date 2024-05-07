package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.BindingContext;
import com.bea.staxb.runtime.EncodingStyle;
import com.bea.staxb.runtime.SoapUnmarshaller;
import com.bea.staxb.runtime.UnmarshalOptions;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;

class EncodedDeserializerContext extends BaseDeserializerContext {
   private SoapUnmarshaller unmarshaller;

   public EncodedDeserializerContext(BindingContext var1, SchemaTypeLoader var2, EncodingStyle var3, Element var4, boolean var5) throws XmlException {
      super(var2, var5);

      assert var4 != null;

      assert var3 != null;

      assert var1 != null;

      this.unmarshaller = var1.createSoapUnmarshaller(var3, var4);
   }

   protected Object unmarshalType(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException {
      return this.unmarshaller.unmarshalType(var1, var2, var3.getName(), this.OPTIONS);
   }

   protected Object unmarshalElement(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException {
      throw new IllegalStateException("unmarshallElement not supported for Soap Encoding");
   }

   protected Object unmarshalType(XMLStreamReader var1, QName var2, Class var3) throws XmlException {
      Object var4 = this.unmarshaller.unmarshalType(var1, var2, var3.getName(), (UnmarshalOptions)null);
      return var4;
   }

   protected Object unmarshalElement(XMLStreamReader var1, QName var2, Class var3) throws XmlException {
      throw new IllegalStateException("unmarshallElement not supported for Soap Encoding");
   }

   public Object deserializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, QName var6) throws XmlException {
      return this.v91deserializeXmlObjects(var1, var2, var3, var4, var5, var6);
   }
}
