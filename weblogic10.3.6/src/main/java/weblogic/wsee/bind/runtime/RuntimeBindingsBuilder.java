package weblogic.wsee.bind.runtime;

import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.IOException;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.runtime.internal.RuntimeBindingsBuilderImpl;
import weblogic.wsee.wsdl.WsdlDefinitions;

public interface RuntimeBindingsBuilder {
   void addTylar(Tylar var1);

   void addGlobalTypeToBind(QName var1, Class var2);

   void addGlobalElementToBind(QName var1, Class var2);

   void addLocalElementToBind(QName var1, String var2, Class var3);

   void set109Mappings(JavaWsdlMappingBean var1);

   void setWsdl(WsdlDefinitions var1);

   void addSchema(SchemaDocument var1);

   RuntimeBindings createRuntimeBindings() throws IOException, XmlException;

   RuntimeBindings createGenericRuntimeBindings() throws IOException, XmlException;

   void setTreatEnumsAsSimpleTypes(boolean var1);

   public static class Factory {
      public static RuntimeBindingsBuilder newInstance() {
         return new RuntimeBindingsBuilderImpl();
      }
   }
}
