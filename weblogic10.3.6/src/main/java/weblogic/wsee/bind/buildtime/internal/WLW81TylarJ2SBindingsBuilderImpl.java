package weblogic.wsee.bind.buildtime.internal;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.util.JamUtil;

public class WLW81TylarJ2SBindingsBuilderImpl extends TylarJ2SBindingsBuilderImpl implements J2SBindingsBuilder {
   public WLW81TylarJ2SBindingsBuilderImpl(JClass var1) {
      this.mBinder = new WLW81SoapAwareJava2Schema(this, this.isRpcEncoded(var1), this.getNamespaceFromServiceClass(var1));
      this.mBinder.setJaxRpcRules(true);
   }

   public String getNamespaceFromServiceClass(JClass var1) {
      String var2 = null;
      JAnnotation var3 = var1.getAnnotation(WebService.class);
      if (var3 != null) {
         var2 = JamUtil.getAnnotationStringValue(var3, "targetNamespace");
      }

      if (var2 == null || var2.equals("")) {
         var2 = "http://www.openuri.org/";
      }

      JAnnotation var4 = var1.getAnnotation(SOAPBinding.class);
      if (var4 != null) {
         String var5 = JamUtil.getAnnotationStringValue(var4, "style");
         String var6 = JamUtil.getAnnotationStringValue(var4, "use");
         if (var2 != null && !var2.equals("") && var5 != null && var5.equals("RPC") && var6 != null && var6.equals("ENCODED")) {
            if (var2.charAt(var2.length() - 1) != '/') {
               var2 = var2 + '/';
            }

            var2 = var2 + "encodedTypes";
         }
      }

      return var2;
   }

   private boolean isRpcEncoded(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(SOAPBinding.class);
      if (var2 != null) {
         String var3 = JamUtil.getAnnotationStringValue(var2, "style");
         String var4 = JamUtil.getAnnotationStringValue(var2, "use");
         if (var3 != null && var4 != null && var3.equals("RPC") && var4.equals("ENCODED")) {
            return true;
         }
      }

      return false;
   }
}
