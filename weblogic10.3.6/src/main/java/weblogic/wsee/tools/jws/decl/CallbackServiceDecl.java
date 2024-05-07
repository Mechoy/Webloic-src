package weblogic.wsee.tools.jws.decl;

import com.bea.staxb.buildtime.Java2Schema;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.mutable.MAnnotation;
import javax.jws.WebService;
import weblogic.jws.CallbackService;
import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.validation.EIValidatorFactory;
import weblogic.wsee.tools.jws.validation.Validator;
import weblogic.wsee.util.JamUtil;

public class CallbackServiceDecl extends WebServiceSEIDecl {
   CallbackServiceDecl(JwsBuildContext var1, JwsInfo var2, String var3, WebServiceSEIDecl var4) {
      super(var1, var2, var3, var4);
      this.setupBindingTargetNamespace(var4);
   }

   private void setupBindingTargetNamespace(WebServiceSEIDecl var1) {
      String var2 = null;
      JAnnotation var3 = var1.getEIClass().getAnnotation(WebService.class);
      if (var3 != null) {
         var2 = JamUtil.getAnnotationStringValue(var3, "targetNamespace");
      }

      if (var2 == null) {
         var2 = Java2Schema.getDefaultNsFor(var1.getEIClass());
      }

      MAnnotation var4 = (MAnnotation)this.getEIClass().getAnnotation(CallbackService.class);
      if (var4 != null) {
         var4.setSimpleValue("targetNamespace", this.getTargetNamespace(), this.getEIClass().forName(String.class.getName()));
      }

   }

   public boolean validate() {
      Validator var1 = EIValidatorFactory.newInstance(this.ctx, this);
      return var1.validate();
   }

   protected void initNamespaces(JAnnotation var1, JAnnotation var2) {
      this.targetNamespace = this.parentWebServiceDecl.getTargetNamespace();
      this.portTypeNamespaceURI = this.parentWebServiceDecl.getTargetNamespace();
   }
}
