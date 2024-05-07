package weblogic.wsee.wsdl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;

public interface WsdlDefinitions extends WsdlExtensible {
   String getName();

   String getTargetNamespace();

   Set<String> getKnownImportedWsdlLocations();

   WsdlSchema getTheOnlySchema();

   String getEncoding();

   WsdlTypes getTypes();

   Map<QName, ? extends WsdlService> getServices();

   Map<QName, ? extends WsdlPort> getPorts();

   Map<QName, ? extends WsdlBinding> getBindings();

   Map<QName, ? extends WsdlPortType> getPortTypes();

   Map<QName, ? extends WsdlMessage> getMessages();

   String getWsdlLocation();

   List<? extends WsdlDefinitions> getImportedWsdlDefinitions();

   List<? extends WsdlImport> getImports();
}
