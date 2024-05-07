package weblogic.wsee.tools.jws.validation.jaxrpc;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.internal.elements.UnresolvedClassImpl;
import com.bea.xbean.common.XMLChar;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.MTOM;
import weblogic.ejbgen.RoleMappings;
import weblogic.ejbgen.SecurityRoleRefs;
import weblogic.ejbgen.ServiceEndpointMethod;
import weblogic.ejbgen.Session;
import weblogic.jws.CallbackMethod;
import weblogic.jws.Context;
import weblogic.jws.Conversation;
import weblogic.jws.Conversational;
import weblogic.jws.MessageBuffer;
import weblogic.jws.ServiceClient;
import weblogic.jws.WLHttpTransport;
import weblogic.jws.WLHttpsTransport;
import weblogic.jws.WLJmsTransport;
import weblogic.jws.WLLocalTransport;
import weblogic.jws.security.CallbackRolesAllowed;
import weblogic.jws.security.RolesAllowed;
import weblogic.jws.security.RolesReferenced;
import weblogic.jws.security.RunAs;
import weblogic.jws.security.UserDataConstraint;
import weblogic.wsee.jaxrpc.StubImpl;
import weblogic.wsee.jws.container.Duration;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.validation.BaseSBValidator;
import weblogic.wsee.tools.jws.validation.annotation.AnnotationMatchingRule;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.PolicyValidateUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;

public class SBValidator extends BaseSBValidator {
   public SBValidator(JwsBuildContext var1, WebServiceSEIDecl var2, boolean var3) {
      super(var1, var2, var3);
   }

   protected void visitImpl(JClass var1) {
      this.checkEI(var1);
      this.checkConversational(var1);
      this.checkTypeSecurity(var1);
      this.checkPolicies(var1, ((WebServiceSEIDecl)this.webService).getPoilices());
      this.checkEJB(var1);
      this.checkSOAPPortAvailability(var1);
      PolicyValidateUtil.checkIllegalPolicyMixUp((WebServiceSEIDecl)this.webService, this.getLogger());
   }

   private void checkEI(JClass var1) {
      if (!StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getEndPointInterface()) && var1 == ((WebServiceSEIDecl)this.webService).getEIClass()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.noEI", new Object[]{((WebServiceSEIDecl)this.webService).getEndPointInterface()})));
      }

   }

   protected void visitImpl(JField var1) {
      this.checkServiceClient(var1);
      this.checkCallbackRolesAllowed(var1);
      this.checkConversational(var1);
   }

   protected void visitImpl(WebMethodDecl var1) {
      JMethod var2 = var1.getJMethod();
      this.checkBuffered(var1);
      this.checkPolicies(var2, var1.getPoilices());
      this.checkMethodSecurity(var2);
      this.checkCallbackRolesAllowed(var2);
      this.checkWildcards(var1);
      this.checkOperationName(var1);
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = super.getNoAnnotationValidator(var1);
      var2.addMatchingRule("annotation.jaxrpc.notAllowed", new AnnotationMatchingRule(new Class[]{SecurityPolicies.class, SecurityPolicy.class, MTOM.class, Addressing.class}));
      return var2;
   }

   private void checkWildcards(WebMethodDecl var1) {
      if (var1.getSoapBinding().isDocLiteralWrapped()) {
         Iterator var2 = var1.getWebParams();

         while(var2.hasNext()) {
            WebParamDecl var3 = (WebParamDecl)var2.next();
            if (var3.isWildcardType() && var3.isArray() && var2.hasNext()) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "parameter.wildcard.unboundedNotLast", new Object[]{var3.getName(), var1.getMethodName(), ((WebServiceSEIDecl)this.webService).getJClass().getQualifiedName()})));
            }
         }
      } else if (this.hasInvalidWildcardBinding(var1)) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "method.wildcard.anyNotAllowed", new Object[]{var1.getMethodName(), ((WebServiceSEIDecl)this.webService).getJClass().getQualifiedName()})));
      }

   }

   private boolean hasInvalidWildcardBinding(WebMethodDecl var1) {
      if (var1.getWebResult().isWildcardType() && !var1.getWebResult().isBoundToAnyType()) {
         return true;
      } else {
         Iterator var2 = var1.getWebParams();

         WebParamDecl var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (WebParamDecl)var2.next();
         } while(!var3.isWildcardType() || var3.isBoundToAnyType());

         return true;
      }
   }

   private void checkCallbackRolesAllowed(JMethod var1) {
      if (var1.getAnnotation(CallbackRolesAllowed.class) != null && var1.getAnnotation(CallbackMethod.class) == null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.sb.noCallbackMethod", new Object[]{var1.getQualifiedName()})));
      }

   }

   private void checkEJB(JClass var1) {
      if (((WebServiceSEIDecl)this.webService).isEjb() && ((WebServiceSEIDecl)this.webService).isComplex()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ejb.notAllowed", new Object[]{var1.getQualifiedName(), getComplexAnnotations()})));
      }

   }

   private static String getComplexAnnotations() {
      StringBuilder var0 = new StringBuilder();

      String var2;
      for(Iterator var1 = WebServiceSEIDecl.getComplexAnnotations().iterator(); var1.hasNext(); var0.append(var2)) {
         var2 = (String)var1.next();
         if (var0.length() > 0) {
            var0.append(", ");
         }
      }

      return var0.toString();
   }

   private void checkTypeSecurity(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(RunAs.class);
      if (var2 != null && var2.getValue("mapToPrincipal") == null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "security.mapToPrincipalsMissing", new Object[]{"@RunAs"})));
      }

      if (((WebServiceSEIDecl)this.webService).isEjb()) {
         JAnnotation var3 = var1.getAnnotation(RunAs.class);
         if (var3 != null) {
            JAnnotation var4 = var1.getAnnotation(Session.class);
            if (var4 != null) {
               JAnnotationValue var5 = var4.getValue("runAs");
               if (!var5.asString().equals("UNSPECIFIED")) {
                  this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "security.noSessionRunAs", new Object[0])));
               }
            }
         }

         if (var1.getAnnotation(RolesReferenced.class) != null && var1.getAnnotation(SecurityRoleRefs.class) != null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "security.noSecurityRoleRefsRolesRefereneced", new Object[0])));
         }

         if (var1.getAnnotation(RolesAllowed.class) != null && var1.getAnnotation(RoleMappings.class) != null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "security.noRolesAllowedRoleMappings", new Object[0])));
         }
      }

   }

   private void checkMethodSecurity(JMethod var1) {
      String var2 = "";
      if (var1.getAnnotation(RunAs.class) != null) {
         var2 = "@RunAs";
      } else if (var1.getAnnotation(RolesReferenced.class) != null) {
         var2 = "@RolesReferenced";
      } else if (var1.getAnnotation(UserDataConstraint.class) != null) {
         var2 = "@UserDataConstraint";
      }

      if (((WebServiceSEIDecl)this.webService).isEjb()) {
         JAnnotation var3 = var1.getAnnotation(RolesAllowed.class);
         if (var3 != null) {
            JAnnotation var4 = var1.getAnnotation(ServiceEndpointMethod.class);
            if (var4 != null && var4.getValue("roles").asString().equals("UNSPECIFIED")) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getContainingClass(), "security.noRolesAllowedServiceEndpointMethod", new Object[0])));
            }
         }
      }

      if (var2.length() > 0) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "security.noAnntMethod", new Object[]{var2, var1.getQualifiedName()})));
      }

   }

   private void checkPolicies(JAnnotatedElement var1, Iterator<PolicyDecl> var2) {
      JAnnotation var3 = var1.getAnnotation("weblogic.jws.Policy");
      JAnnotation var4 = var1.getAnnotation("weblogic.jws.Policies");
      if (var3 != null && var4 != null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.bothAnnotations", new Object[0])));
      }

      HashSet var5 = new HashSet();

      while(var2.hasNext()) {
         PolicyDecl var6 = (PolicyDecl)var2.next();
         if (var5.contains(var6.getUri())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.duplicateUri", new Object[]{var6.getUri()})));
         } else {
            var5.add(var6.getUri());
         }

         if (!StringUtil.isEmpty(var6.getUriError())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.uriInError", new Object[]{var6.getUri(), var6.getUriError()})));
         } else if (!var6.isBuiltInPolicy()) {
            this.checkConnection(var6, var1);
         }

         JAnnotation var7 = var1.getAnnotation(UserDataConstraint.class);
         if (var7 != null) {
            UserDataConstraint.Transport var8 = (UserDataConstraint.Transport)JamUtil.getAnnotationEnumValue(var7, "transport", UserDataConstraint.Transport.class, UserDataConstraint.Transport.NONE);
            if (var8 == UserDataConstraint.Transport.NONE) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.withUserDataConstraint", new Object[]{var6.getUri()})));
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

   private void checkCallbackRolesAllowed(JField var1) {
      if (var1.getAnnotation(CallbackRolesAllowed.class) != null && var1.getAnnotation(ServiceClient.class) == null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.field.noServiceClient", new Object[]{var1.getSimpleName()})));
      }

   }

   private void checkServiceClient(JField var1) {
      JClass var2 = var1.getContainingClass().getClassLoader().loadClass(StubImpl.class.getName());
      if (var1.getAnnotation(ServiceClient.class) != null) {
         JClass var3 = var1.getType();
         if (var1.getType().isInterface()) {
            String var4 = var3.getQualifiedName() + "_Stub";
            JClass var5 = var1.getContainingClass().getClassLoader().loadClass(var4);
            if (!(var5 instanceof UnresolvedClassImpl)) {
               var3 = var5;
            }
         }

         if (!var2.isAssignableFrom(var3)) {
            this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1, "type.field.notStub", new Object[]{var1.getSimpleName()})));
         }
      }

   }

   private void checkConversational(JField var1) {
   }

   private boolean isContextField(JField var1) {
      return var1.getAnnotation(Context.class) != null || var1.getAnnotation("weblogic.controls.jws.Common.Context") != null;
   }

   private boolean isControlField(JField var1) {
      return var1.getAnnotation("org.apache.beehive.controls.api.bean.Control") != null;
   }

   private void checkConversational(JClass var1) {
      if (((WebServiceSEIDecl)this.webService).isConversational()) {
         this.checkPhases(var1);
         this.checkSerializable(var1);
         this.checkConversationalAnn(var1);
      }

   }

   private void checkPhases(JClass var1) {
      int var2 = 0;
      Iterator var3 = ((WebServiceSEIDecl)this.webService).getWebMethods();

      while(var3.hasNext()) {
         WebMethodDecl var4 = (WebMethodDecl)var3.next();
         if (var4.getConverationPhase() == Conversation.Phase.START) {
            ++var2;
         }
      }

      if (var2 < 1) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.conversation.wrongNumOfPhases", new Object[]{Conversation.Phase.START})));
      }

   }

   private void checkSerializable(JClass var1) {
      JClass var2 = var1.getClassLoader().loadClass(Serializable.class.getName());
      if (!var2.isAssignableFrom(var1)) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.conversation.notSerializable", new Object[0])));
      }

   }

   private void checkConversationalAnn(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(Conversational.class);
      if (var2 != null) {
         try {
            new Duration(JamUtil.getAnnotationStringValue(var2, "maxAge"));
         } catch (IllegalArgumentException var5) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.conversational.invalidMaxAge", new Object[]{var5.getMessage()})));
         }

         try {
            new Duration(JamUtil.getAnnotationStringValue(var2, "maxIdleTime"));
         } catch (IllegalArgumentException var4) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.conversational.invalidMaxIdleTime", new Object[]{var4.getMessage()})));
         }
      }

   }

   private void checkBuffered(WebMethodDecl var1) {
      JAnnotation var2 = var1.getJMethod().getAnnotation(MessageBuffer.class);
      if (var2 != null) {
         if (var1.getWebResult().getType() != null) {
            this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "method.buffered.void", new Object[0])));
         }

         try {
            new Duration(JamUtil.getAnnotationStringValue(var2, "retryDelay"));
         } catch (IllegalArgumentException var4) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "method.buffered.invalidRetryDelay", new Object[]{var4.getMessage()})));
         }
      }

   }

   private void checkSOAPPortAvailability(JClass var1) {
      if (((WebServiceSEIDecl)this.webService).getCowReader() != null && !StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getWsdlLocation())) {
         WsdlDefinitions var2 = null;

         try {
            var2 = ((WebServiceSEIDecl)this.webService).getCowReader().getWsdl(((WebServiceSEIDecl)this.webService).getWsdlLocation());
         } catch (WsdlException var6) {
         }

         if (var2 != null) {
            String var3 = ((WebServiceSEIDecl)this.webService).getTargetNamespace();
            JAnnotation var4 = var1.getAnnotation(WLJmsTransport.class);
            String var5 = var4 == null ? null : var4.getValue("portName").asString();
            if (!StringUtil.isEmpty(var5) && var2.getPorts().get(new QName(var3, var5)) == null) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "port.name.not.found", new Object[]{var5, var4.toString()})));
            }

            var4 = var1.getAnnotation(WLHttpsTransport.class);
            var5 = var4 == null ? null : var4.getValue("portName").asString();
            if (!StringUtil.isEmpty(var5) && var2.getPorts().get(new QName(var3, var5)) == null) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "port.name.not.found", new Object[]{var5, var4.toString()})));
            }

            var4 = var1.getAnnotation(WLLocalTransport.class);
            var5 = var4 == null ? null : var4.getValue("portName").asString();
            if (!StringUtil.isEmpty(var5) && var2.getPorts().get(new QName(var3, var5)) == null) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "port.name.not.found", new Object[]{var5, var4.toString()})));
            }

            var4 = var1.getAnnotation(WLHttpTransport.class);
            var5 = var4 == null ? null : var4.getValue("portName").asString();
            if (!StringUtil.isEmpty(var5) && var2.getPorts().get(new QName(var3, var5)) == null) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "port.name.not.found", new Object[]{var5, var4.toString()})));
            }
         }
      }

   }

   private void checkOperationName(WebMethodDecl var1) {
      if (!XMLChar.isValidNCName(var1.getName())) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "operation.name.invalid", new Object[]{var1.getName()})));
      }

   }
}
