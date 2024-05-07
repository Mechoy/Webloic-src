package weblogic.wsee.tools.jws.validation.jaxrpc;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.HashSet;
import java.util.Iterator;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import weblogic.jws.Binding;
import weblogic.jws.BufferQueue;
import weblogic.jws.CallbackService;
import weblogic.jws.Conversation;
import weblogic.jws.Conversational;
import weblogic.jws.MessageBuffer;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.CallbackServiceDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.validation.annotation.AnnotationMatchingRule;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.soap11.SoapBinding;

public class CallbackValidator extends EIValidator {
   private final WebServiceDecl parentWebServiceDecl;

   public CallbackValidator(JwsBuildContext var1, CallbackServiceDecl var2) {
      super(var1, var2);
      this.parentWebServiceDecl = var2.getParentWebService();
   }

   protected void checkTypeAnnotation(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(CallbackService.class);
      if (var2 == null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.callback.noCallbackService", new Object[]{var1.getQualifiedName()})));
      }

      if (!this.parentWebServiceDecl.getTargetNamespace().equals(((WebServiceSEIDecl)this.webService).getTargetNamespace())) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.callback.invalidTargetNamespace", new Object[]{var1.getQualifiedName(), this.parentWebServiceDecl.getTargetNamespace(), this.parentWebServiceDecl.getJClass().getQualifiedName()})));
      }

   }

   protected void visitImpl(WebMethodDecl var1) {
      this.validateConversation(var1.getJMethod());
      this.validateOperation(var1.getJMethod());
   }

   protected void visitImpl(JClass var1) {
      super.visitImpl(var1);
      this.addParentSoapDispatchNames();
      this.checkDuplicateOperations(var1);
   }

   protected void checkParam(WebParamDecl var1, JMethod var2, WebMethodDecl var3) {
      super.checkParam(var1, var2, var3);
      if (var1.isHolderType()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "parameter.callback.invalidHolder", new Object[]{var1.getName(), var2.getQualifiedName(), var2.getContainingClass().getQualifiedName()})));
      }

   }

   private void checkDuplicateOperations(JClass var1) {
      if (this.parentWebServiceDecl instanceof WebServiceSEIDecl) {
         WebServiceSEIDecl var2 = (WebServiceSEIDecl)this.parentWebServiceDecl;
         HashSet var3 = new HashSet();
         Iterator var4 = var2.getWebMethods();

         while(var4.hasNext()) {
            var3.add(((WebMethodDecl)var4.next()).getName());
         }

         WebMethodDecl var5;
         for(var4 = ((WebServiceSEIDecl)this.webService).getWebMethods(); var4.hasNext(); var3.add(var5.getName())) {
            var5 = (WebMethodDecl)var4.next();
            if (var3.contains(var5.getName())) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.callback.parentDuplicate", new Object[]{((WebServiceSEIDecl)this.webService).getSimpleName(), this.parentWebServiceDecl.getSimpleName(), var5.getName()})));
            }
         }

      }
   }

   private void validateOperation(JMethod var1) {
      JAnnotation var2 = var1.getAnnotation(WebMethod.class);
      if (var2 != null) {
         String var3 = JamUtil.getAnnotationStringValue(var2, "operationName");
         if (!StringUtil.isEmpty(var3)) {
            String var4 = var1.getSimpleName();
            if (!var3.equals(var4)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.callback.operationNameChanged", new Object[]{var1.getContainingClass().getQualifiedName(), var4, var3})));
            }
         }
      }

   }

   private void validateConversation(JMethod var1) {
      JAnnotation var2 = var1.getAnnotation(Conversation.class);
      if (var2 != null) {
         if (this.parentWebServiceDecl.isConversational() && var1.getContainingClass().getParent() != null) {
            String var3 = JamUtil.getAnnotationStringValue(var2, "value");
            if (var3 != null && Conversation.Phase.START.equals(Conversation.Phase.valueOf(var3))) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.callback.conversationStartNotAllowed", new Object[]{var1.getContainingClass().getQualifiedName(), var1.getQualifiedName()})));
            }
         } else {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.callback.annotationNotAllowed.dependent", new Object[]{var2.getQualifiedName(), var1.getQualifiedName(), Conversational.class.getName()})));
         }
      }

   }

   private void addParentSoapDispatchNames() {
      if (this.parentWebServiceDecl instanceof WebServiceSEIDecl) {
         WebServiceSEIDecl var1 = (WebServiceSEIDecl)this.parentWebServiceDecl;
         Iterator var2 = var1.getWebMethods();

         while(var2.hasNext()) {
            WebMethodDecl var3 = (WebMethodDecl)var2.next();
            QName var4 = var3.getSoapDispatchName();
            if (var4 != null) {
               String var5 = var3.getAction();
               HashSet var6 = (HashSet)this.soapDispatchNames.get(var5);
               if (var6 == null) {
                  var6 = new HashSet();
                  this.soapDispatchNames.put(var5, var6);
               }

               var6.add(var4);
            }

            if (var3.getSoapBinding().isDocumentStyle() && var3.getWebResult().hasReturn()) {
               this.resultElementNames.put(var3.getResultElementQName(), var3.getWebResult().getType());
            }
         }

      }
   }

   String getNoAnnotationKey() {
      return "type.callback.annotationNotAllowed";
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = new NoAnnotationValidator(var1, "type.callback.annotationNotAllowed");
      var2.addMatchingRule(new AnnotationMatchingRule(WebService.class));
      var2.addMatchingRule(new AnnotationMatchingRule(HandlerChain.class));
      var2.addMatchingRule(new PackageMatchingRule(SOAPBinding.class.getPackage().getName(), new Class[]{SOAPBinding.class}));
      var2.addMatchingRule(new PackageMatchingRule("weblogic.ejbgen"));
      var2.addMatchingRule(new PackageMatchingRule(CallbackService.class.getPackage().getName(), new Class[]{CallbackService.class, SoapBinding.class, Conversation.class, Binding.class, UseWLW81BindingTypes.class, MessageBuffer.class, BufferQueue.class}, true));
      return var2;
   }
}
