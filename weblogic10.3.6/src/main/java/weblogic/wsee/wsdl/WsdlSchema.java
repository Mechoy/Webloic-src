package weblogic.wsee.wsdl;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.util.List;

public interface WsdlSchema extends WsdlElement {
   SchemaDocument getSchema();

   WsdlDefinitions getWsdlDefinitions();

   String getLocationUrl();

   List<? extends WsdlSchemaImport> getImports();
}
