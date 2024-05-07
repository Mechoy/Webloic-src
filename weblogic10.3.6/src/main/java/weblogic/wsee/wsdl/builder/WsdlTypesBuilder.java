package weblogic.wsee.wsdl.builder;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlTypesBuilder extends WsdlTypes {
   List<WsdlSchemaBuilder> getSchemaListWithoutImport();

   List<WsdlSchemaBuilder> getImportedWsdlSchemas();

   void parse(Element var1, String var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2) throws WsdlException;

   void addSchemas(SchemaDocument[] var1) throws WsdlException;

   void addSchema(WsdlSchemaBuilder var1);
}
