package weblogic.wsee.tools.jws.validation.jaxws;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.jws.security.WssConfiguration;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.validation.BaseSBValidator;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.PolicyValidateUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wstx.wsat.Transactional;

public class SBValidator extends BaseSBValidator {
   WSATValidator wsatValidator = new WSATValidator();

   public SBValidator(JwsBuildContext var1, WebServiceSEIDecl var2, boolean var3) {
      super(var1, var2, var3);
   }

   protected void visitImpl(JClass var1) {
      int var2 = 0;

      for(Iterator var3 = ((WebServiceSEIDecl)this.webService).getPorts(); var3.hasNext(); ++var2) {
         PortDecl var4 = (PortDecl)var3.next();
         if (!"http".equals(var4.getProtocol())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ports.jaxws.notHttp", new Object[]{var1.getQualifiedName()})));
         }
      }

      if (var2 > 1) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ports.jaxws.moreThanOne", new Object[]{var1.getQualifiedName()})));
      }

      PolicyValidateUtil.checkClassScopedOWSMPolicy(var1, this.getLogger());
      PolicyValidateUtil.checkClassScopedWebLogicProprietaryPolicy((WebServiceSEIDecl)this.webService, this.getLogger());
      this.checkPolicies(var1, ((WebServiceSEIDecl)this.webService).getPoilices());
      this.wsatValidator.visitImpl(var1, this.getLogger());
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = super.getNoAnnotationValidator(var1);
      var2.addMatchingRule("annotation.jaxws.notAllowed", new PackageMatchingRule("weblogic", new Class[]{Policy.class, Policies.class, WssConfiguration.class, SecurityPolicy.class, SecurityPolicies.class, Transactional.class}, true));
      return var2;
   }

   protected void visitImpl(JField var1) {
   }

   protected void visitImpl(WebMethodDecl var1) {
      PolicyValidateUtil.checkMethodScopedWebLogicProprietaryPolicy(var1, this.getLogger());
      this.checkPolicies(var1.getJMethod(), var1.getPoilices());
      this.wsatValidator.visitImpl(var1, this.getLogger());
   }

   private void checkPolicies(JAnnotatedElement var1, Iterator<PolicyDecl> var2) {
      JAnnotation var3 = var1.getAnnotation("weblogic.jws.Policy");
      JAnnotation var4 = var1.getAnnotation("weblogic.jws.Policies");
      if (var3 != null && var4 != null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.bothAnnotations", new Object[0])));
      }

      JAnnotation var5 = var1.getAnnotation(SecurityPolicy.class.getName());
      JAnnotation var6 = var1.getAnnotation(SecurityPolicies.class.getName());
      if (var5 != null && var6 != null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.bothSecurityAnnotations", new Object[0])));
      }

      HashSet var7 = new HashSet();

      while(var2.hasNext()) {
         PolicyDecl var8 = (PolicyDecl)var2.next();
         if (var7.contains(var8.getUri())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.duplicateUri", new Object[]{var8.getUri()})));
         } else {
            var7.add(var8.getUri());
         }

         if (!StringUtil.isEmpty(var8.getUriError())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.uriInError", new Object[]{var8.getUri(), var8.getUriError()})));
         } else if (!var8.isBuiltInPolicy()) {
            this.checkConnection(var8, var1);
         }
      }

      var7.clear();
      if (var6 != null) {
         JAnnotationValue var14 = var6.getValue("value");
         JAnnotation[] var9 = var14.asAnnotationArray();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            JAnnotation var12 = var9[var11];
            String var13 = var12.getValue("uri").asString();
            if (var7.contains(var13)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.duplicateUri", new Object[]{var13})));
            } else {
               var7.add(var13);
            }
         }
      }

   }

   private void checkConnection(PolicyDecl var1, JAnnotatedElement var2) {
      InputStream var3 = null;

      try {
         var3 = var1.getPolicyURI().toURL().openStream();
         if (var3 == null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "policy.notFound", new Object[]{var1.getPolicyURI().toString()})));
         }
      } catch (MalformedURLException var15) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "policy.uriInError", new Object[]{var1.getUri(), var15.getMessage()})));
      } catch (IOException var16) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "policy.notFound", new Object[]{var1.getPolicyURI().toString(), var16.getMessage()})));
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var14) {
            }
         }

      }

   }
}
