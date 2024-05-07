package weblogic.wsee.wsdl.builder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.wsdl.RelativeResourceResolver;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;

public interface WsdlDefinitionsBuilder extends WsdlDefinitions {
   void setName(String var1);

   void setTargetNamespace(String var1);

   WsdlSchemaBuilder getTheOnlySchema();

   WsdlTypesBuilder addTypes();

   WsdlTypesBuilder getTypes();

   Map<QName, WsdlServiceBuilder> getServices();

   WsdlServiceBuilder addService(QName var1);

   Map<QName, WsdlPortBuilder> getPorts();

   WsdlBindingBuilder addBinding(QName var1, WsdlPortTypeBuilder var2);

   Map<QName, WsdlBindingBuilder> getBindings();

   WsdlPortTypeBuilder addPortType(QName var1);

   Map<QName, WsdlPortTypeBuilder> getPortTypes();

   WsdlMessageBuilder addMessage(QName var1);

   Map<QName, WsdlMessageBuilder> getMessages();

   void setRelativeResourceResolver(RelativeResourceResolver var1);

   RelativeResourceResolver getRelativeResourceResolver();

   void writeToFile(File var1, String var2) throws IOException, WsdlException;

   /** @deprecated */
   void write(OutputStream var1, WsdlAddressInfo var2) throws IOException, WsdlException;

   void write(OutputStream var1, WsdlAddressInfo var2, String var3) throws IOException, WsdlException;

   List<WsdlDefinitionsBuilder> getImportedWsdlDefinitions();

   WsdlDefinitionsBuilder findImport(String var1);

   WsdlDefinitionsBuilder findAbsoluteImport(String var1);

   List<WsdlImportBuilder> getImports();
}
