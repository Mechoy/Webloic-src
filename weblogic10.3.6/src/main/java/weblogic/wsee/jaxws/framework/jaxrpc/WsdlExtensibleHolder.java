package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.model.wsdl.WSDLExtensible;
import com.sun.xml.ws.api.model.wsdl.WSDLExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.wsdl.WsdlDocumentation;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlExtension;

public class WsdlExtensibleHolder implements WSDLExtension, WsdlExtensible {
   private Map<String, List<WsdlExtension>> extensions = new HashMap();

   public static WsdlExtensible get(WSDLExtensible var0) {
      synchronized(var0) {
         WsdlExtensibleHolder var2 = (WsdlExtensibleHolder)var0.getExtension(WsdlExtensibleHolder.class);
         if (var2 == null) {
            var2 = new WsdlExtensibleHolder();
            var0.addExtension(var2);
         }

         return var2;
      }
   }

   public QName getName() {
      return null;
   }

   public WsdlExtension getExtension(String var1) {
      List var2 = (List)this.extensions.get(var1);
      return var2 != null && var2.size() > 0 ? (WsdlExtension)var2.get(0) : null;
   }

   public List<WsdlExtension> getExtensionList(String var1) {
      List var2 = (List)this.extensions.get(var1);
      return var2 != null ? var2 : Collections.EMPTY_LIST;
   }

   public Map<String, List<WsdlExtension>> getExtensions() {
      return this.extensions;
   }

   public void putExtension(WsdlExtension var1) {
      Object var2 = (List)this.extensions.get(var1.getKey());
      if (var2 == null) {
         var2 = new ArrayList();
         this.extensions.put(var1.getKey(), var2);
      }

      ((List)var2).add(var1);
   }

   public WsdlDocumentation getDocumentation() {
      throw new UnsupportedOperationException();
   }
}
