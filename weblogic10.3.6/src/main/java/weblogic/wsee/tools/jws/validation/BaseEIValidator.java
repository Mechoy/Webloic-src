package weblogic.wsee.tools.jws.validation;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import weblogic.jws.CallbackService;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.XBeanUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPortType;

public abstract class BaseEIValidator extends BaseValidator<WebServiceSEIDecl> {
   private static final Class[] orphanAnns = new Class[]{Oneway.class, WebResult.class};
   protected Map<String, HashSet<QName>> soapDispatchNames = new HashMap();
   protected Map<QName, String> resultElementNames = new HashMap();
   private Set<WebMethodDecl> visitedWebMethods = new HashSet();
   private boolean strict = false;
   protected static final String SOAPELEMENT_CLASSNAME = SOAPElement.class.getName();

   protected BaseEIValidator(JwsBuildContext var1, WebServiceSEIDecl var2) {
      super(var1, var2);
      this.strict = var1.getTask() == JwsBuildContext.Task.JWSC;
   }

   public final void visit(JClass var1) {
      if (var1 != null && !var1.isUnresolvedType()) {
         this.checkPackageName(var1);
         this.checkTypeAnnotation(var1);
         this.checkHandlerChain(var1);
         this.checkDuplicateOperations(var1);
         this.visitImpl(var1);
      } else {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.class.unknown", new Object[]{var1.getQualifiedName()})));
      }

   }

   protected abstract void visitImpl(JClass var1);

   protected boolean isSeparateEI() {
      if (!StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getEndPointInterface())) {
         return true;
      } else {
         return ((WebServiceSEIDecl)this.webService).getJClass() == ((WebServiceSEIDecl)this.webService).getEIClass() && ((WebServiceSEIDecl)this.webService).getJClass().isInterface();
      }
   }

   protected void checkTypeAnnotation(JClass var1) {
      JAnnotation var2;
      if (this.isSeparateEI()) {
         if (!var1.isInterface()) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.EINotInterface", new Object[]{var1.getQualifiedName()})));
         }

         var2 = var1.getAnnotation(WebService.class);
         if (var2 == null) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.noWebService", new Object[]{var1.getQualifiedName()})));
         } else {
            String var3 = JamUtil.getAnnotationStringValue(var2, "endpointInterface");
            if (!StringUtil.isEmpty(var3)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.EIDeclaresEI", new Object[]{var1.getQualifiedName()})));
            }

            String var4 = JamUtil.getAnnotationStringValue(var2, "serviceName");
            if (!StringUtil.isEmpty(var4)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.EIDeclaresServiceName", new Object[]{var1.getQualifiedName()})));
            }
         }
      }

      var2 = var1.getAnnotation(CallbackService.class);
      if (var2 != null) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.ei.declaresCallbackService", new Object[]{var1.getQualifiedName()})));
      }

   }

   private void checkDuplicateOperations(JClass var1) {
      HashSet var2 = new HashSet();

      WebMethodDecl var4;
      for(Iterator var3 = ((WebServiceSEIDecl)this.webService).getWebMethods(); var3.hasNext(); var2.add(var4.getName())) {
         var4 = (WebMethodDecl)var3.next();
         if (var2.contains(var4.getName())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.operationName.duplicate", new Object[]{var4.getName()})));
         }
      }

   }

   private void checkPackageName(JClass var1) {
      if (!StringUtil.isLowerCase(var1.getContainingPackage().getQualifiedName())) {
         this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1, "type.pkg.upperCasedPackage", new Object[]{var1.getQualifiedName()})));
      }

      if (StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getPackageName())) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.package.missing", new Object[0])));
      }

   }

   private void checkResult(JMethod var1, WebMethodDecl var2) {
      this.checkResultName(var1, var2);
   }

   public final void visit(JMethod var1) {
      WebMethodDecl var2 = ((WebServiceSEIDecl)this.webService).getWebMethod(var1);
      if (!this.visitedWebMethods.contains(var2)) {
         if (var2 == null) {
            this.chceckForOrphans(var1);
         } else {
            this.checkPublic(var1);
            this.checkOneway(var1, var2);
            this.checkSoapDispatchName(var1, var2);
            this.checkSoapBinding(var1);
            this.checkParams(var1, var2);
            if (this.strict) {
               this.checkBareParam(var1, var2);
               this.checkResult(var1, var2);
            }

            this.visitImpl(var2);
            Iterator var3 = var2.getWebParams();

            while(var3.hasNext()) {
               WebParamDecl var4 = (WebParamDecl)var3.next();
               this.checkParam(var4, var1, var2);
               this.visitImpl(var4);
            }

            this.visitedWebMethods.add(var2);
         }

      }
   }

   protected abstract void visitImpl(WebMethodDecl var1);

   protected abstract void visitImpl(WebParamDecl var1);

   private void checkSoapBinding(JMethod var1) {
      JAnnotation var2 = var1.getAnnotation(SOAPBinding.class);
      if (var2 != null) {
         JAnnotation var3 = var1.getAnnotation(weblogic.jws.soap.SOAPBinding.class);
         if (var3 != null) {
            if (JamUtil.equals(var2, var3)) {
               this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1, "method.soapBinding.duplicate", new Object[]{var1.getQualifiedName(), SOAPBinding.class.getName(), weblogic.jws.soap.SOAPBinding.class.getName()})));
            } else {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.soapBinding.different", new Object[]{var1.getQualifiedName(), SOAPBinding.class.getName(), weblogic.jws.soap.SOAPBinding.class.getName()})));
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

   private void checkParams(JMethod var1, WebMethodDecl var2) {
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         this.checkUniqueParamElementQNames(var1, var2);
      }

   }

   private void checkUniqueParamElementQNames(JMethod var1, WebMethodDecl var2) {
      HashSet var3 = new HashSet();

      WebParamDecl var5;
      for(Iterator var4 = var2.getWebParams(); var4.hasNext(); var3.add(var5.getElementQName())) {
         var5 = (WebParamDecl)var4.next();
         if (var3.contains(var5.getElementQName())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "parameter.binding.nonUniqueParameterElementName", new Object[]{var5.getName(), var5.getElementQName(), var1.getSimpleName()})));
         }
      }

   }

   protected void checkParam(WebParamDecl var1, JMethod var2, WebMethodDecl var3) {
      if (var1.getTypeFamily() != WebTypeDecl.TypeFamily.POJO) {
         if (((WebServiceSEIDecl)this.webService).getType() == WebServiceType.JAXWS) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "method.jaxws.xmlBeanNotSupported", new Object[0])));
         } else if (!var3.getSoapBinding().isDocumentStyle() && var3.getSoapBinding().isLiteral()) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "method.rpc.xmlBeanNotSupported", new Object[0])));
         } else {
            JAnnotation var4 = var1.getJParameter().getAnnotation(WebParam.class);
            if (var1.getTypeFamily() == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT && var4 != null) {
               this.checkXmlBeanDocument(var2, var1.getJParameter().getType(), var4);
            }
         }
      }

      if (var1.getMode() != Mode.IN) {
         this.checkOutParam(var1, var3);
      }

   }

   private void checkSoapDispatchName(JMethod var1, WebMethodDecl var2) {
      QName var3 = var2.getSoapDispatchName();
      if (var3 != null) {
         String var4 = var2.getAction();
         HashSet var5 = (HashSet)this.soapDispatchNames.get(var4);
         if (var5 == null) {
            var5 = new HashSet();
            this.soapDispatchNames.put(var4, var5);
         }

         if (var5.contains(var3)) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.soapDispatchName.duplicate", new Object[]{var3})));
         }

         var5.add(var3);
      }

   }

   private void checkResultName(JMethod var1, WebMethodDecl var2) {
      if (var2.getSoapBinding().isDocumentStyle() && var2.getWebResult().hasReturn()) {
         QName var3 = var2.getResultElementQName();
         String var4 = var2.getWebResult().getType();
         if (this.resultElementNames.containsKey(var3)) {
            String var5 = (String)this.resultElementNames.get(var3);
            if (!var5.equals(var4)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "result.elementName.duplicate", new Object[]{var3})));
            }
         }

         this.resultElementNames.put(var3, var4);
      }

   }

   private void checkOutParam(WebParamDecl var1, WebMethodDecl var2) {
      if (var2.isOneway()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2.getJMethod(), "parameter.oneway.invalidMode", new Object[0])));
      } else if (!var1.isHolderType()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2.getJMethod(), "parameter.binding.holderRequired", new Object[]{var1.getParameterName()})));
      }

   }

   private void checkXmlBeanDocument(JAnnotatedElement var1, JClass var2, JAnnotation var3) {
      QName var4 = XBeanUtil.getQNameFromXmlBean(this.getClassLoader(), var2.getQualifiedName());
      if (var4 != null) {
         String var5 = JamUtil.getAnnotationStringValue(var3, "name");
         String var6 = JamUtil.getAnnotationStringValue(var3, "targetNamespace");
         if (!StringUtil.isEmpty(var5) && !var5.equals(var4.getLocalPart())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "parameter.xmlbean.invalidName", new Object[]{var3.getQualifiedName(), var5, var4.getLocalPart()})));
         }

         if (!StringUtil.isEmpty(var6) && !var6.equals(var4.getNamespaceURI())) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "parameter.xmlbean.invalidNamespace", new Object[]{var3.getQualifiedName(), var6, var4.getNamespaceURI()})));
         }
      }

   }

   private void checkPublic(JMethod var1) {
      if (!var1.isPublic()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.notPublic", new Object[]{var1.getSimpleName()})));
      }

   }

   private void checkBareParam(JMethod var1, WebMethodDecl var2) {
      if (var2.getSoapBinding().isDocumentStyle() && !var2.getSoapBinding().isWrapped()) {
         List var3 = var2.getParameters(Mode.IN);
         int var4 = var2.getParameters(Mode.OUT).size();
         if (var2.getWebResult().hasReturn()) {
            ++var4;
         }

         if (var3.size() > 1) {
            int var5 = 0;

            for(int var6 = 0; var6 < var3.size(); ++var6) {
               if (!this.isAttachmentParam((WebParamDecl)var3.get(var6))) {
                  ++var5;
               }
            }

            if (var5 > 1) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.bare.invalidNumberOfParameters", new Object[0])));
            }
         }

         if (var4 > 1) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.bare.invalidNumberOfResults", new Object[0])));
         }
      }

   }

   private boolean isAttachmentParam(WebParamDecl var1) {
      String var2 = var1.getRealType().getQualifiedName();
      return "javax.activation.DataHandler".equals(var2) || "javax.activation.DataHandler[]".equals(var2) || "javax.xml.transform.Source".equals(var2) || "javax.xml.transform.Source[]".equals(var2) || "javax.mail.internet.MimeMultipart".equals(var2) || "javax.mail.internet.MimeMultipart[]".equals(var2) || "java.awt.Image".equals(var2) || "java.awt.Image[]".equals(var2);
   }

   private void checkOneway(JMethod var1, WebMethodDecl var2) {
      boolean var3 = var1.getReturnType().isVoidType();
      if (var2.isOneway()) {
         if (!var3) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.oneway.mustReturnVoid", new Object[0])));
         } else if (var2.getParameters(Mode.OUT).size() != 0) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.oneway.noOutputAllowed", new Object[0])));
         }

         if (this.strict) {
            this.checkCheckedExceptions(var1);
         }
      } else if (var2.getSoapBinding().isDocLiteralBare() && var2.getParameters(Mode.OUT).size() == 0 && var3 && (((WebServiceSEIDecl)this.webService).getCowReader() == null || !this.hasEmptyOutput(var2))) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.bare.mustBeOneway", new Object[0])));
      }

   }

   private boolean hasEmptyOutput(WebMethodDecl var1) {
      WsdlDefinitions var2 = this.getDefinitions();
      if (var2 != null) {
         WsdlPortType var3 = (WsdlPortType)var2.getPortTypes().get(((WebServiceSEIDecl)this.webService).getPortTypeQName());

         assert var3 != null : "Port type " + ((WebServiceSEIDecl)this.webService).getPortType() + " not found";

         WsdlOperation var4 = (WsdlOperation)var3.getOperations().get(new QName(((WebServiceSEIDecl)this.webService).getPortTypeNamespaceURI(), var1.getName()));

         assert var4 != null : "Operation " + var1.getName() + " not found";

         WsdlMessage var5 = var4.getOutput();
         if (var5 != null && !var5.getParts().values().iterator().hasNext()) {
            return true;
         }
      }

      return false;
   }

   private WsdlDefinitions getDefinitions() {
      WsdlDefinitions var1 = null;

      try {
         var1 = ((WebServiceSEIDecl)this.webService).getCowReader().getWsdl(((WebServiceSEIDecl)this.webService).getWsdlLocation());
      } catch (WsdlException var3) {
      }

      return var1;
   }

   private void checkCheckedExceptions(JMethod var1) {
      JClass[] var2 = var1.getExceptionTypes();
      if (var2.length > 0) {
         JClass var3 = var1.getContainingClass().getClassLoader().loadClass(Error.class.getName());
         JClass var4 = var1.getContainingClass().getClassLoader().loadClass(RuntimeException.class.getName());
         JClass var5 = var1.getContainingClass().getClassLoader().loadClass(RemoteException.class.getName());

         for(int var6 = 0; var6 < var2.length; ++var6) {
            JClass var7 = var2[var6];
            if (!var3.isAssignableFrom(var7) && !var4.isAssignableFrom(var7) && !var5.isAssignableFrom(var7)) {
               this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.exception.onewayDeclaresChecked", new Object[0])));
               return;
            }
         }
      }

   }

   protected JClass getVisitee() {
      return ((WebServiceSEIDecl)this.webService).getEIClass();
   }
}
