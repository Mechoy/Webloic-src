package weblogic.xml.crypto.wss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;

public class WssPolicyContextHandler implements ContextHandler {
   public static final String SERVICE_URI = "com.bea.contextelement.wsee.serviceUri";
   public static final String CREDENTIAL_PROVIDER_LIST = "com.bea.contextelement.wsee.credentialProviders";
   public static final String TOKEN_HANDLER_LIST = "com.bea.contextelement.wsee.tokenHandlers";
   public static final String PROP_USE_X509_FOR_IDENTITY = "UseX509ForIdentity";
   private List names = new ArrayList();
   private Map contextElements = new HashMap();

   public int size() {
      return this.names.size();
   }

   public String[] getNames() {
      return (String[])((String[])this.names.toArray(new String[this.names.size()]));
   }

   public Object getValue(String var1) {
      Object var2 = null;
      ContextElement var3 = (ContextElement)this.contextElements.get(var1);
      if (var3 != null) {
         var2 = var3.getValue();
      }

      return var2;
   }

   public ContextElement[] getValues(String[] var1) {
      if (var1 != null && var1.length != 0) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            ContextElement var5 = (ContextElement)this.contextElements.get(var4);
            if (var5 != null) {
               var2.add(var5);
            }
         }

         return (ContextElement[])((ContextElement[])var2.toArray(new ContextElement[var2.size()]));
      } else {
         return null;
      }
   }

   public void addContextElement(String var1, Object var2) {
      this.contextElements.put(var1, new ContextElement(var1, var2));
      this.names.add(var1);
   }
}
