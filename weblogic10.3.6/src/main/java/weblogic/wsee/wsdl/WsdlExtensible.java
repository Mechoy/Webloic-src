package weblogic.wsee.wsdl;

import java.util.List;
import java.util.Map;

public interface WsdlExtensible extends WsdlElement {
   WsdlExtension getExtension(String var1);

   List<WsdlExtension> getExtensionList(String var1);

   void putExtension(WsdlExtension var1);

   Map<String, List<WsdlExtension>> getExtensions();
}
