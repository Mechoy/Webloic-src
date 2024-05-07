package weblogic.wsee.wsdl.builder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlSchemaBuilder extends WsdlSchema {
   WsdlDefinitionsBuilder getWsdlDefinitions();

   List<WsdlSchemaImportBuilder> getImports();

   void parse(Element var1, String var2) throws WsdlException;

   void parse(Element var1, String var2, Set<String> var3) throws WsdlException;

   void write(Element var1, WsdlWriter var2) throws WsdlException;

   void write(Document var1, WsdlWriter var2) throws WsdlException;

   void writeToFile(File var1, String var2) throws IOException, WsdlException;

   void write(OutputStream var1, WsdlAddressInfo var2, String var3) throws IOException, WsdlException;

   /** @deprecated */
   void write(OutputStream var1, WsdlAddressInfo var2) throws IOException, WsdlException;
}
