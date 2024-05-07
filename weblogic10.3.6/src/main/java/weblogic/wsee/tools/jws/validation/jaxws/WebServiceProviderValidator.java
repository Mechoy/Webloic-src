package weblogic.wsee.tools.jws.validation.jaxws;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceProvider;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.jws.security.WssConfiguration;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceProviderDecl;
import weblogic.wsee.tools.jws.validation.BaseValidator;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.PolicyValidateUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wstx.wsat.Transactional;

public class WebServiceProviderValidator extends BaseValidator<WebServiceProviderDecl> {
   WSATValidator wsatValidator = new WSATValidator();

   public WebServiceProviderValidator(JwsBuildContext var1, WebServiceProviderDecl var2) {
      super(var1, var2);
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = new NoAnnotationValidator(var1);
      var2.addMatchingRule("annotation.jaxws.notAllowed", new PackageMatchingRule("weblogic", new Class[]{Policy.class, Policies.class, WssConfiguration.class, SecurityPolicy.class, SecurityPolicies.class, Transactional.class}, true));
      return var2;
   }

   public void visit(JClass var1) {
      if (var1 != null && !var1.isUnresolvedType()) {
         if (((WebServiceProviderDecl)this.webService).getType() != WebServiceType.JAXWS) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.provider.notJAXWS", new Object[]{var1.getQualifiedName()})));
         }

         JAnnotation var2 = var1.getAnnotation(WebServiceProvider.class);
         if (var2 == null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.provider.noWebServiceProvider", new Object[]{var1.getQualifiedName()})));
         } else {
            this.checkWsdlLocation(var2, var1);
            this.wsatValidator.visitProvider(var1, this.getLogger());
            this.checkImplementsProvider(var1);
            this.checkHandlerChain(var1);
            PolicyValidateUtil.checkClassScopedOWSMPolicy(var1, this.getLogger());
         }
      } else {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.unknown", new Object[]{var1.getQualifiedName()})));
      }

   }

   private void checkImplementsProvider(JClass var1) {
      JClass[] var2 = var1.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JClass var5 = var2[var4];
         if (Provider.class.getName().equals(var5.getQualifiedName())) {
            return;
         }
      }

      this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.provider.ProviderNotImplemented", new Object[]{var1.getQualifiedName(), Provider.class.getName()})));
   }

   private void checkWsdlLocation(JAnnotation var1, JClass var2) {
      String var3 = JamUtil.getAnnotationStringValue(var1, "wsdlLocation");
      if (StringUtil.isEmpty(var3)) {
         JAnnotation var4 = var2.getAnnotation(BindingType.class);
         if (var4 == null || !"http://www.w3.org/2004/08/wsdl/http".equals(JamUtil.getAnnotationStringValue(var4, "value"))) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "type.provider.noWsdlLocation", new Object[]{var2.getQualifiedName()})));
         }
      }

   }

   protected JClass getVisitee() {
      return ((WebServiceProviderDecl)this.webService).getJClass();
   }
}
