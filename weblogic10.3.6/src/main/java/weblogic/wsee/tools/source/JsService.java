package weblogic.wsee.tools.source;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;

public class JsService {
   private WsdlService wsdlService;
   private Map portList = new HashMap();

   public JsService(WsdlService var1) {
      this.wsdlService = var1;
   }

   public WsdlService getWsdlService() {
      return this.wsdlService;
   }

   public Iterator getEndpoints() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.portList.values().iterator();

      while(var2.hasNext()) {
         JsPort var3 = (JsPort)var2.next();
         var1.add(var3.getEndpoint());
      }

      return var1.iterator();
   }

   public JsPort getPort(String var1) {
      return (JsPort)this.portList.get(var1);
   }

   public Iterator getPorts() {
      return this.portList.values().iterator();
   }

   JsPort addPort(String var1, WsdlPort var2, JsClass var3) {
      assert var1 != null;

      assert var2 != null;

      assert var3 != null;

      JsPort var4 = new JsPort(var2, var3);
      this.portList.put(var1, var4);
      return var4;
   }

   public boolean has81Conversation() {
      WsdlTypes var1 = this.getWsdlService().getDefinitions().getTypes();
      if (var1 == null) {
         return false;
      } else {
         SchemaDocument[] var2 = var1.getSchemaArray();
         SchemaDocument[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SchemaDocument var6 = var3[var5];
            if ("http://www.openuri.org/2002/04/soap/conversation/".equals(var6.getSchema().getTargetNamespace())) {
               return true;
            }
         }

         return false;
      }
   }
}
