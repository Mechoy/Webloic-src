package weblogic.wsee.bind.buildtime;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.WrappedOperationInfo;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.internal.TylarS2JBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.XmlBeansApacheBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.XmlBeansBindingsBuilderImpl;
import weblogic.wsee.wsdl.WsdlDefinitions;

public interface S2JBindingsBuilder {
   void addBaseTypeLibrary(ClassLoader var1);

   void addSchemaDocument(SchemaDocument var1);

   void addHolderType(QName var1);

   void addHolderElement(QName var1);

   void addParamType(QName var1);

   void addParamElement(QName var1);

   void addWrapperOperation(WrappedOperationInfo var1);

   void addFaultType(FaultMessage var1);

   void addFaultElement(FaultMessage var1);

   void setCodegenDir(File var1);

   void setXmlObjectClassLoader(ClassLoader var1);

   BuildtimeBindings createBuildtimeBindings(File var1, WsdlDefinitions var2) throws IOException, XmlException;

   void includeGlobalTypes(boolean var1);

   void sortSchemaTypes(boolean var1);

   void setXsdConfig(File[] var1);

   public static class Factory {
      public static TylarS2JBindingsBuilder createTylarBindingsBuilder() {
         return new TylarS2JBindingsBuilderImpl();
      }

      public static S2JBindingsBuilder createXmlBeansBindingsBuilder() {
         return new XmlBeansBindingsBuilderImpl();
      }

      public static S2JBindingsBuilder createXmlBeansApacheBindingsBuilder() {
         return new XmlBeansApacheBindingsBuilderImpl();
      }
   }
}
