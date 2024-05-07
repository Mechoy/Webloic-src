package weblogic.wsee.bind.buildtime;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.TypeFamily;

public interface BuildtimeBindings {
   String getClassFromSchemaType(QName var1);

   String getClassFromSchemaElement(QName var1);

   boolean useCheckedExceptionFromWsdlFault();

   String getExceptionClassFromFaultMessageType(FaultMessage var1);

   String getExceptionClassFromFaultMessageElement(FaultMessage var1);

   String getWrappedSimpleClassNameFromFaultMessageType(FaultMessage var1);

   String getWrappedSimpleClassNameFromFaultMessageElement(FaultMessage var1);

   List getElementNamesCtorOrderFromFaultMessageElement(FaultMessage var1);

   List getElementNamesCtorOrderFromFaultMessageType(FaultMessage var1);

   List getElementNamesCtorOrderFromException(JClass var1);

   QName getSchemaType(String var1);

   SchemaDocument[] getSchemas();

   void generate109Descriptor(JavaWsdlMappingBean var1);

   Class getBuilderClass();

   LinkedHashMap getJavaTypesForWrapperElement(QName var1);

   public static class Loader {
      public static BuildtimeBindings reloadBuildtimeBindings(URI var0, TypeFamily var1) throws IOException, XmlException {
         Factory var2 = var1.getBuildtimeBindingsFactory();
         return var2.loadFromURI(var0);
      }
   }

   public interface Factory {
      BuildtimeBindings loadFromURI(URI var1) throws IOException, XmlException;
   }
}
