package weblogic.webservice.encoding;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.rpc.encoding.SerializerFactory;
import javax.xml.soap.SOAPElement;
import weblogic.xml.schema.binding.BindingException;
import weblogic.xml.schema.binding.ClassContext;
import weblogic.xml.schema.binding.SchemaContext;
import weblogic.xml.schema.binding.TypeMappingEntry;
import weblogic.xml.schema.binding.internal.TypeMappingBase;
import weblogic.xml.schema.binding.internal.XSDTypeMapping;
import weblogic.xml.schema.model.ExpName;
import weblogic.xml.stream.XMLName;

/** @deprecated */
public class GenericTypeMapping extends TypeMappingBase {
   private weblogic.webservice.core.encoding.stream.SOAPElementCodec genericCodec = new weblogic.webservice.core.encoding.stream.SOAPElementCodec();
   private static final QName anyType = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
   private static final ExpName anyTypeExp = new ExpName("http://www.w3.org/2001/XMLSchema", "anyType");

   public GenericTypeMapping() {
      try {
         this.setParent(XSDTypeMapping.createXSDMapping());
         this.register(SOAPElement.class, anyType, this.genericCodec, this.genericCodec);
      } catch (BindingException var2) {
         throw new JAXRPCException("Failed to create binding", var2);
      }
   }

   public boolean isRegistered(Class var1, QName var2) {
      return SOAPElement.class.isAssignableFrom(var1) ? true : super.isRegistered(var1, var2);
   }

   public SerializerFactory getSerializer(Class var1, QName var2) {
      Object var3 = super.getSerializer(var1, var2);
      if (var3 == null && SOAPElement.class.equals(var1)) {
         var3 = this.genericCodec;
      }

      return (SerializerFactory)var3;
   }

   public TypeMappingEntry get(XMLName var1, SchemaContext var2) {
      TypeMappingEntry var3 = super.get(var1, var2);
      if (var3 == null) {
         var2.setJavaType(SOAPElement.class.getName());
         var3 = super.get(anyTypeExp, var2);
      }

      return var3;
   }

   public Class getClassFromXMLName(XMLName var1) {
      Class var2 = super.getClassFromXMLName(var1);
      if (var2 == null) {
         var2 = SOAPElement.class;
      }

      return var2;
   }

   public TypeMappingEntry get(Class var1, ClassContext var2) {
      TypeMappingEntry var3 = super.get(var1, var2);
      if (var3 == null) {
         var2.setSchemaType(anyTypeExp);
         var3 = super.get(var1, var2);
      }

      return var3;
   }

   public DeserializerFactory getDeserializer(Class var1, QName var2) {
      Object var3 = super.getDeserializer(var1, var2);
      if (var3 == null && SOAPElement.class.isAssignableFrom(var1)) {
         var3 = this.genericCodec;
      }

      return (DeserializerFactory)var3;
   }
}
