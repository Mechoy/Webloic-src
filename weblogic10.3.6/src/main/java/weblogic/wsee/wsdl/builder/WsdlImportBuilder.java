package weblogic.wsee.wsdl.builder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlImport;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlImportBuilder extends WsdlImport {
   WsdlDefinitionsBuilder getDefinitions();

   boolean isRelative();

   boolean hasCirularImport();

   boolean cirularImportResovled();

   void parse(Element var1, Map<String, WsdlDefinitions> var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);

   void writeToFile(File var1, String var2) throws IOException, WsdlException;

   void writeToFile(File var1) throws IOException, WsdlException;
}
