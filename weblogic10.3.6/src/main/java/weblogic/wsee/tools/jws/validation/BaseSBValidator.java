package weblogic.wsee.tools.jws.validation;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JConstructor;
import com.bea.util.jam.JField;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPMessageHandlers;
import weblogic.jws.Binding;
import weblogic.jws.Conversation;
import weblogic.jws.MessageBuffer;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPort;

public abstract class BaseSBValidator extends BaseValidator<WebServiceSEIDecl> {
   private static final Class[] orphanAnns = new Class[]{MessageBuffer.class, Conversation.class, Policy.class, Policies.class};
   private boolean eiValid = true;

   protected BaseSBValidator(JwsBuildContext var1, WebServiceSEIDecl var2, boolean var3) {
      super(var1, var2);
      this.eiValid = var3;
   }

   public final void visit(JClass var1) {
      if (var1 != null && !var1.isUnresolvedType()) {
         this.checkClass(var1);
         this.checkPackage(var1);
         this.validateJwsAnnotations(var1);
         this.checkPorts(var1);
         this.checkEI(var1);
         this.checkHandlerChain(var1);
         this.visitImpl(var1);
      } else {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.unknown", new Object[]{var1.getQualifiedName()})));
      }

   }

   protected abstract void visitImpl(JClass var1);

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = new NoAnnotationValidator(var1);
      if (!StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getEndPointInterface())) {
         var2.addMatchingRule(new PackageMatchingRule(WebService.class.getPackage().getName(), new Class[]{WebService.class, HandlerChain.class}));
         var2.addMatchingRule(new PackageMatchingRule(SOAPBinding.class.getPackage().getName(), new Class[]{SOAPBinding.class, SOAPMessageHandlers.class}));
         var2.addExcludeClass(((WebServiceSEIDecl)this.webService).getEIClass());
      }

      return var2;
   }

   public final void visit(JField var1) {
      this.visitImpl(var1);
   }

   protected abstract void visitImpl(JField var1);

   public final void visit(JMethod var1) {
      WebMethodDecl var2 = ((WebServiceSEIDecl)this.webService).getWebMethod(var1);
      if (var2 == null) {
         this.chceckForOrphans(var1);
      } else {
         this.visitImpl(var2);
      }

   }

   protected abstract void visitImpl(WebMethodDecl var1);

   private void validateJwsAnnotations(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(WebService.class);
      if (var2 == null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.sb.noWebService", new Object[]{var1.getQualifiedName()})));
      }

      if (((WebServiceSEIDecl)this.webService).getCowReader() != null) {
         if (this.isAnnotationPresent(var1, Conversation.class)) {
            boolean var3 = true;
            String var4 = ((WebServiceSEIDecl)this.webService).getWsdlLocation();
            if (!StringUtil.isEmpty(var4)) {
               try {
                  WsdlBinding var5 = ((WsdlPort)((WebServiceSEIDecl)this.webService).getCowReader().getWsdl(var4).getPorts().get(((WebServiceSEIDecl)this.webService).getPortQName())).getBinding();
                  var3 = ConversationUtils.isConversational(var5);
               } catch (WsdlException var7) {
                  JwsLogEvent var6 = new JwsLogEvent(var1, "annotation.Conversation.check.wsdl", new Object[0]);
                  var6.setException(var7);
                  this.getLogger().log(EventLevel.VERBOSE, (LogEvent)var6);
               }
            }

            if (var3) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "annotation.Conversation.notAllowed", new Object[0])));
            }
         }

         if (this.isAnnotationPresent(var1, Policy.class) || this.isAnnotationPresent(var1, Policies.class)) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "annotation.Policy.notAllowed", new Object[0])));
         }
      }

      JAnnotation var8 = var1.getAnnotation(Binding.class);
      if (var8 != null && Binding.Type.SOAP12.toString().equals(var8.getValue("value").asString())) {
         JAnnotation var9 = var1.getAnnotation(SOAPBinding.class);
         if (var9 != null) {
            String var10 = JamUtil.getAnnotationStringValue(var9, "style");
            String var11 = JamUtil.getAnnotationStringValue(var9, "use");
            if ("rpc".equalsIgnoreCase(var10) && "encoded".equalsIgnoreCase(var11)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "bindingType.soap12", new Object[0])));
            }
         }
      }

   }

   private boolean isAnnotationPresent(JClass var1, Class var2) {
      if (var1.getAnnotation(var2) != null) {
         return true;
      } else {
         JMethod[] var3 = var1.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getAnnotation(var2) != null) {
               return true;
            }
         }

         return false;
      }
   }

   private void checkPackage(JClass var1) {
      if (!StringUtil.isLowerCase(var1.getContainingPackage().getQualifiedName())) {
         this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1, "type.pkg.upperCasedPackage", new Object[]{var1.getQualifiedName()})));
      }

   }

   private void checkEI(JClass var1) {
      if (!StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getEndPointInterface())) {
         if (!this.eiValid) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.sb.EIInvalid", new Object[]{((WebServiceSEIDecl)this.webService).getEndPointInterface()})));
         }

         JAnnotation var2 = var1.getAnnotation(WebService.class);
         String var3 = JamUtil.getAnnotationStringValue(var2, "name");
         if (!StringUtil.isEmpty(var3)) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.sb.EICheck", new Object[0])));
         }

         String var4 = JamUtil.getAnnotationStringValue(var2, "wsdlLocation");
         if (!StringUtil.isEmpty(var4)) {
            var2 = ((WebServiceSEIDecl)this.webService).getEIClass().getAnnotation(WebService.class);
            String var5 = JamUtil.getAnnotationStringValue(var2, "wsdlLocation");
            if (!StringUtil.isEmpty(var5) && !var5.equals(var4)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.sb.wsdlLocationDeclaredTwice", new Object[0])));
            }
         }
      }

   }

   private void chceckForOrphans(JMethod var1) {
      Class[] var2 = orphanAnns;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (var1.getAnnotation(var5) != null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.noWebMethod", new Object[]{var5.getName()})));
         }
      }

   }

   private void checkClass(JClass var1) {
      if (var1.isInterface()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.SBNotFound", new Object[0])));
      }

      if (!var1.isPublic()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.isNotPublic", new Object[0])));
      }

      if (var1.isAbstract()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.isAbstract", new Object[0])));
      }

      if (var1.isFinal()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.isFinal", new Object[0])));
      }

      if (!hasDefaultConstructor(var1)) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.noDefaultConstructor", new Object[0])));
      }

      if (hasFinalizeMethod(var1)) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.hasFinalize", new Object[0])));
      }

   }

   private static boolean hasFinalizeMethod(JClass var0) {
      JMethod[] var1 = var0.getDeclaredMethods();
      int var3 = var1.length;
      byte var4 = 0;
      if (var4 < var3) {
         JMethod var5 = var1[var4];
         return JamUtil.isFinalizeMethod(var5);
      } else {
         return false;
      }
   }

   private static boolean hasDefaultConstructor(JClass var0) {
      JConstructor[] var1 = var0.getConstructors();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         JConstructor var3 = var1[var2];
         JParameter[] var4 = var3.getParameters();
         if (var3.isPublic() && (var4 == null || var4.length == 0)) {
            return true;
         }
      }

      return false;
   }

   private void checkPorts(JClass var1) {
      HashSet var2 = new HashSet();
      String var3 = null;
      HashSet var4 = new HashSet();
      HashMap var5 = new HashMap();
      Iterator var6 = ((WebServiceSEIDecl)this.webService).getPorts();

      while(var6.hasNext()) {
         PortDecl var7 = (PortDecl)var6.next();
         if (var2.contains(var7.getPortName())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ports.portNameNotUnique", new Object[]{var7.getProtocol(), var7.getPortName()})));
         }

         var2.add(var7.getPortName());
         if (!((WebServiceSEIDecl)this.webService).isEjb()) {
            if (var3 == null) {
               var3 = var7.getContextPath();
            } else if (!var3.equals(var7.getContextPath())) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ports.contextPathNotIdentical", new Object[0])));
            }
         }

         String var8 = var7.getURI();
         if (var4.contains(var8)) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ports.uriNotUnique", new Object[]{var8})));
         }

         var4.add(var8);
         if (((WebServiceSEIDecl)this.webService).getCallbackService() != null) {
            this.checkPortPath(var7, var5, var1, "type.ports.callback.contextPathServiceUriNotUnique");
         } else {
            this.checkPortPath(var7, var5, var1, "type.ports.contextPathServiceUriNotUnique");
         }
      }

   }

   private void checkPortPath(PortDecl var1, Map<String, Set<String>> var2, JClass var3, String var4) {
      String var5 = var1.getNormalizedPath();
      if (var2.keySet().contains(var5) && (((Set)var2.get(var5)).contains("http") && var1.getProtocol().equals("https") || ((Set)var2.get(var5)).contains("https") && var1.getProtocol().equals("http"))) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3, var4, new Object[]{var5})));
      }

      if (!var2.keySet().contains(var5)) {
         HashSet var6 = new HashSet();
         var6.add(var1.getProtocol());
         var2.put(var5, var6);
      } else {
         Set var7 = (Set)var2.get(var5);
         var7.add(var1.getProtocol());
         var2.put(var5, var7);
      }

   }

   protected JClass getVisitee() {
      return ((WebServiceSEIDecl)this.webService).getJClass();
   }
}
