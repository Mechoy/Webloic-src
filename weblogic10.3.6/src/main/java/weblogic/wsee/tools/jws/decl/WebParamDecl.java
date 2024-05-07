package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import com.bea.util.jam.JParameter;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.xml.rpc.holders.Holder;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.util.JamUtil;

public class WebParamDecl extends WebTypeDecl {
   private final WebParam.Mode mode;
   private final String parameterName;
   private final JParameter jParameter;

   public WebParamDecl(WebMethodDecl var1, JParameter var2, int var3) {
      super(var1, var2, var2.getType(), WebParam.class, getDefaultName(var1, var2, var3));
      this.jParameter = var2;
      this.mode = this.getMode(var2);
      this.parameterName = var2.getSimpleName();
   }

   private static String getDefaultName(WebMethodDecl var0, JParameter var1, int var2) {
      if (var0.getWebService().getType() == WebServiceType.JAXWS) {
         return var0.getSoapBinding().isDocLiteralBare() ? var0.getName() : "arg" + var2;
      } else {
         return var1.getSimpleName();
      }
   }

   private WebParam.Mode getMode(JParameter var1) {
      WebParam.Mode var2 = Mode.IN;
      JAnnotation var3 = var1.getAnnotation(WebParam.class);
      if (var3 != null) {
         var2 = (WebParam.Mode)JamUtil.getAnnotationEnumValue(var3, "mode", WebParam.Mode.class, Mode.IN);
      }

      if (var3 == null && this.isHolderType()) {
         var2 = Mode.OUT;
      }

      return var2;
   }

   public JParameter getJParameter() {
      return this.jParameter;
   }

   public String getParameterName() {
      return this.parameterName;
   }

   public WebParam.Mode getMode() {
      return this.mode;
   }

   String getDefaultPartName() {
      return this.getWebMethodDecl().getSoapBinding().isDocLiteralBare() ? this.getParameterName() : this.getWebTypeDeclName();
   }

   public boolean isHolderType() {
      JClass var1;
      if (this.getWebMethodDecl().getWebService().getType() == WebServiceType.JAXRPC) {
         var1 = this.jParameter.getType().getClassLoader().loadClass(Holder.class.getName());
      } else {
         var1 = this.jParameter.getType().getClassLoader().loadClass(javax.xml.ws.Holder.class.getName());
      }

      return var1.isAssignableFrom(this.jParameter.getType());
   }

   public JClass getRealType() {
      if (this.isHolderType()) {
         JField[] var1 = this.jParameter.getType().getFields();
         JField[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            JField var5 = var2[var4];
            if ("value".equals(var5.getSimpleName())) {
               return var5.getType();
            }
         }

         throw new IllegalStateException("Public field \"value\" is not found in " + this.jParameter.getType().getQualifiedName() + ". It is not a valid JAXRPC holder class.");
      } else {
         return this.jParameter.getType();
      }
   }
}
