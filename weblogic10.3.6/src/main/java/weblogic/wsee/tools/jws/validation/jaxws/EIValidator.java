package weblogic.wsee.tools.jws.validation.jaxws;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JClass;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.SOAPBindingDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.jws.validation.BaseEIValidator;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.PolicyValidateUtil;

public class EIValidator extends BaseEIValidator {
   public EIValidator(JwsBuildContext var1, WebServiceSEIDecl var2) {
      super(var1, var2);
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = new NoAnnotationValidator(var1);
      if (this.isSeparateEI()) {
         var2.addMatchingRule("annotation.jaxws.ei.notAllowed", new PackageMatchingRule("weblogic", new Class[]{SecurityPolicy.class, SecurityPolicies.class}, true));
      }

      return var2;
   }

   protected void visitImpl(JClass var1) {
      this.checkSoapBinding(((WebServiceSEIDecl)this.webService).getSoapBinding(), var1);
      PolicyValidateUtil.checkClassScopedOWSMPolicy(var1, this.getLogger());
      PolicyValidateUtil.checkClassScopedWebLogicProprietaryPolicy((WebServiceSEIDecl)this.webService, this.getLogger());
   }

   protected void visitImpl(WebMethodDecl var1) {
      this.checkSoapBinding(var1.getSoapBinding(), var1.getJMethod());
      this.checkResult(var1);
      PolicyValidateUtil.checkMethodScopedWebLogicProprietaryPolicy(var1, this.getLogger());
   }

   protected void visitImpl(WebParamDecl var1) {
      if (var1.getTypeFamily() != WebTypeDecl.TypeFamily.POJO) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getWebMethodDecl().getJMethod(), "method.jaxws.xmlBeanNotSupported", new Object[0])));
      }

   }

   private void checkSoapBinding(SOAPBindingDecl var1, JAnnotatedElement var2) {
      if (!var1.isDocumentStyle() && !var1.isLiteral() && ((WebServiceSEIDecl)this.webService).getType() == WebServiceType.JAXWS) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "type.ei.jaxws.rpcEncodedNotSupported", new Object[0])));
      }

   }

   private void checkResult(WebMethodDecl var1) {
      if (var1.getWebResult().getTypeFamily() != WebTypeDecl.TypeFamily.POJO) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "method.jaxws.xmlBeanNotSupported", new Object[0])));
      }

   }
}
