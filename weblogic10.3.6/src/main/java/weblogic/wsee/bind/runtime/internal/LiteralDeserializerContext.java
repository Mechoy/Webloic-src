package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.BindingContext;
import com.bea.staxb.runtime.Unmarshaller;
import com.bea.staxb.runtime.internal.PushBackStreamReader;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamReader;
import weblogic.wsee.util.Verbose;

class LiteralDeserializerContext extends BaseDeserializerContext {
   private final Unmarshaller unmarshaller;
   private com.bea.xbeanmarshal.runtime.Unmarshaller xBeanUnmarshaller;

   public LiteralDeserializerContext(BindingContext var1, com.bea.xbeanmarshal.runtime.BindingContext var2, SchemaTypeLoader var3, boolean var4) throws XmlException {
      super(var3, var4);
      this.unmarshaller = var1.createUnmarshaller();
      if (var2 != null) {
         this.xBeanUnmarshaller = var2.createUnmarshaller();
      }

   }

   protected Object unmarshalType(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException {
      return this.unmarshaller.unmarshalType(var1, var2, var3.getName(), this.OPTIONS);
   }

   protected Object unmarshalElement(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException {
      if (this.OPTIONS.isValidation()) {
         PushBackStreamReader var4 = new PushBackStreamReader((XMLStreamReader)var1);
         var1 = var4;
      }

      return this.unmarshaller.unmarshalElement((XMLStreamReader)var1, var2, var3.getName(), this.OPTIONS);
   }

   public Object deserializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, QName var6) throws XmlException {
      if (this.xBeanUnmarshaller != null && !force_v91_xmlbean_marshalling) {
         return this.xBeanUnmarshaller.deserializeXmlObjects(var1, var4, var5, var6);
      } else {
         if (!force_v91_xmlbean_marshalling) {
            Verbose.log((Object)(" WARNING !  encountered XmlBean type '" + var5.getName() + "' but 9.2+ XmlBeansBindings were not loaded at service deployment time.  Using " + "9.1 style XmlBeans marshalling.  Some 9.2+ features such as arrays of XmlBeans may fail."));
         }

         return this.v91deserializeXmlObjects(var1, var2, var3, var4, var5, var6);
      }
   }

   public void setMessageContext(MessageContext var1) {
      super.setMessageContext(var1);
      this.forceDotNetCompatibleUnmarshal(var1);
   }

   private void forceDotNetCompatibleUnmarshal(MessageContext var1) {
      if (var1 == null) {
         this.OPTIONS.setForceDotNetCompatibleUnMarshal(false);
      }

      Boolean var2 = (Boolean)var1.getProperty("weblogic.wsee.dotnet.compatible.binding");
      if (var2 != null) {
         this.OPTIONS.setForceDotNetCompatibleUnMarshal(var2);
      } else {
         this.OPTIONS.setForceDotNetCompatibleUnMarshal(Boolean.parseBoolean(System.getProperty("weblogic.wsee.dotnet.compatible.binding")));
      }

   }
}
