package weblogic.wsee.wsdl;

public interface WsdlSchemaImport extends WsdlElement {
   WsdlSchema getSchema();

   String getNamespace();

   String getSchemaLocation();

   boolean isRelative();
}
