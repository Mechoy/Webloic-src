package weblogic.wsee.wsdl.builder;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlSchemaImport;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlSchemaImportBuilder extends WsdlSchemaImport {
   WsdlSchemaBuilder getSchema();

   void parse(Element var1, Set<String> var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);

   void writeToFile(File var1, String var2) throws IOException, WsdlException;
}
