package weblogic.xml.crypto.wss;

import java.util.Iterator;
import java.util.List;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class SecurityTokenHelper {
   public static final String getURI(SecurityToken var0) {
      String var1 = var0.getId();
      String var2 = var1 == null ? null : "#" + var0.getId();
      return var2;
   }

   public static final String getIdFromURI(String var0) {
      return var0 != null ? var0.substring(1) : null;
   }

   public static final SecurityToken findSecurityTokenInContext(ContextHandler var0, String var1) throws WSSecurityException {
      WSSecurityContext var2 = (WSSecurityContext)var0.getValue("com.bea.contextelement.xml.SecurityInfo");
      return var2 == null ? null : findSecurityTokenInContext(var2, var1);
   }

   public static final SecurityToken findSecurityTokenInContext(WSSecurityContext var0, String var1) {
      List var2 = var0.getSecurityTokens();
      SecurityToken var3 = null;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         SecurityToken var5 = (SecurityToken)var4.next();
         if (var5.getValueType().equals(var1)) {
            var3 = var5;
            break;
         }
      }

      return var3;
   }

   public static final SecurityToken findSecurityTokenByIdInContext(WSSecurityContext var0, String var1, String var2) {
      List var3 = var0.getSecurityTokens();
      SecurityToken var4 = null;
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         SecurityToken var6 = (SecurityToken)var5.next();
         if (var6.getValueType().equals(var1) && var2.equals(var6.getId())) {
            var4 = var6;
            break;
         }

         if (var6.getCredential() != null && var6.getCredential() instanceof SCCredential) {
            SCCredential var7 = (SCCredential)var6.getCredential();
            if (var6.getValueType().equals(var1) && var2.equals(var7.getIdentifier())) {
               var4 = var6;
               break;
            }
         }
      }

      return var4;
   }

   public static SecurityToken[] findSecurityTokenByType(WSSecurityContext var0, String var1) {
      return (SecurityToken[])((SecurityToken[])var0.getSecurityTokens(var1).toArray(new SecurityToken[0]));
   }
}
