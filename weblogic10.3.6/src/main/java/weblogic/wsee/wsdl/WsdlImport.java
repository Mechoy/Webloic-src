package weblogic.wsee.wsdl;

public interface WsdlImport extends WsdlElement {
   WsdlDefinitions getDefinitions();

   String getNamespace();

   String getLocation();
}
