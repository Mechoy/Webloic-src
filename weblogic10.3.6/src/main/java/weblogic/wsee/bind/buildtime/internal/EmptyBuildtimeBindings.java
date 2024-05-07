package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.JaxRpcBuiltinBindingLoader;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;

public class EmptyBuildtimeBindings implements BuildtimeBindings {
   BindingLoader _loader = JaxRpcBuiltinBindingLoader.getInstance();

   public boolean useCheckedExceptionFromWsdlFault() {
      return false;
   }

   public String getClassFromSchemaType(QName var1) {
      return this.getClassFromXmlType(XmlTypeName.forTypeNamed(var1));
   }

   public String getClassFromSchemaElement(QName var1) {
      return this.getClassFromXmlType(XmlTypeName.forGlobalName('e', var1));
   }

   public String getExceptionClassFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public String getExceptionClassFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public String getWrappedSimpleClassNameFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public String getWrappedSimpleClassNameFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromException(JClass var1) {
      return null;
   }

   public QName getSchemaType(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("EmptyBuildtimeBindings.getSchemaType(): null javaType");
      } else if (var1.equals(TylarJ2SBindingsBuilderImpl.SOAPELEMENT_CLASSNAME)) {
         return SoapAwareJava2Schema.XS_ANYTYPE;
      } else {
         JavaTypeName var2 = JavaTypeName.forString(var1);
         BindingTypeName var3 = this._loader.lookupTypeFor(var2);
         QName var4 = var3 == null ? null : var3.getXmlName().getQName();
         return var4;
      }
   }

   public SchemaDocument[] getSchemas() {
      return null;
   }

   public void generate109Descriptor(JavaWsdlMappingBean var1) {
   }

   public Class getBuilderClass() {
      return null;
   }

   public SchemaTypeLoader getSchemaTypeLoader() {
      return null;
   }

   public LinkedHashMap getJavaTypesForWrapperElement(QName var1) {
      return null;
   }

   private String getClassFromXmlType(XmlTypeName var1) {
      BindingTypeName var2 = this._loader.lookupPojoFor(var1);
      if (var2 != null) {
         return var2.getJavaName().toString();
      } else {
         var2 = this._loader.lookupXmlObjectFor(var1);
         if (var2 != null) {
            return var2.getJavaName().toString();
         } else {
            throw new IllegalArgumentException("EmptyBuildtimeBindings.getClassFromXmlType(): unable to find java type for " + var1);
         }
      }
   }

   public static class Factory implements BuildtimeBindings.Factory {
      private static Factory instance;

      private Factory() {
      }

      public BuildtimeBindings loadFromURI(URI var1) throws IOException, XmlException {
         return new EmptyBuildtimeBindings();
      }

      public static Factory getInstance() {
         if (instance == null) {
            instance = new Factory();
         }

         return instance;
      }
   }
}
