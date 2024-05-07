package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.BindingContext;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.staxb.runtime.Marshaller;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamWriter;
import weblogic.wsee.util.Verbose;

final class LiteralSerializerContext extends BaseSerializerContext {
   private final Marshaller marshaller;
   private com.bea.xbeanmarshal.runtime.Marshaller xBeanMarshaller;

   public LiteralSerializerContext(BindingContext var1, com.bea.xbeanmarshal.runtime.BindingContext var2, SchemaTypeLoader var3, MarshalOptions var4) throws XmlException {
      super(var3, var4);
      this.marshaller = var1.createMarshaller();
      if (var2 != null) {
         this.xBeanMarshaller = var2.createMarshaller();
      }

   }

   protected void marshalType(XMLStreamWriter var1, Object var2, QName var3, XmlTypeName var4, Class var5) throws XmlException {
      this.marshaller.marshalType(var1, var2, var3, var4, var5.getName(), this.marshalOptions);
   }

   protected void marshalElement(XMLStreamWriter var1, Object var2, XmlTypeName var3, Class var4) throws XmlException {
      this.marshaller.marshalElement(var1, var2, var3, var4.getName(), this.marshalOptions);
   }

   public void serializeReferencedObjects(SOAPElement var1) {
   }

   public void serializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, Object var6, QName var7) throws XmlException {
      if (this.xBeanMarshaller != null && !force_v91_xmlbean_marshalling) {
         this.xBeanMarshaller.serializeXmlObject(var1, this.getDocument(), var4, var6, var7);
      } else {
         if (!force_v91_xmlbean_marshalling) {
            Verbose.log((Object)(" WARNING !  encountered XmlBean type '" + var5.getName() + "' but 9.2+ XmlBeansBindings were not loaded at service deployment time.  Using " + "9.1 style XmlBeans marshalling.  Some 9.2+ features such as arrays of XmlBeans may fail."));
         }

         this.v91serializeXmlObjects(var1, var2, var3, var4, var5, var6, var7);
      }

   }
}
