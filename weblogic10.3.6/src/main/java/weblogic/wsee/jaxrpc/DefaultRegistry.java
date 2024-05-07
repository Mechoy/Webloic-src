package weblogic.wsee.jaxrpc;

import java.io.IOException;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.encoding.TypeMapping;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.xml.schema.binding.TypeMappingFactory;
import weblogic.xml.schema.binding.util.StdNamespace;

public final class DefaultRegistry extends TypeMappingRegistryImpl {
   private static final String SOAP12_ENC = "http://www.w3.org/2002/12/soap-encoding";
   private RuntimeBindings bindings;

   DefaultRegistry(RuntimeBindings var1) throws JAXRPCException {
      this.bindings = var1;
      TypeMappingFactory var2 = TypeMappingFactory.newInstance();
      TypeMapping var3 = (TypeMapping)var2.createDefaultMapping();
      this.register(var3, new String[]{StdNamespace.instance().soapEncoding()});
      this.register(var3, new String[]{"http://www.w3.org/2002/12/soap-encoding"});
   }

   void loadConfig(String var1) throws IOException {
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append("[ registerd encodingStyle = ");
      String[] var2 = this.getRegisteredNamespaces();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.append(var2[var3]);
         var1.append(",");
      }

      var1.append("]");
      return var1.toString();
   }
}
