package weblogic.wsee.wsdl;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.util.List;
import java.util.Map;

public interface WsdlTypes extends WsdlElement {
   Map getNameSpaceDefs();

   SchemaDocument[] getSchemaArray();

   List getNodeList();

   List getNodeListWithoutImport();

   SchemaDocument[] getSchemaArrayWithoutImport();

   List<? extends WsdlSchema> getSchemaListWithoutImport();

   List<? extends WsdlSchema> getImportedWsdlSchemas();
}
