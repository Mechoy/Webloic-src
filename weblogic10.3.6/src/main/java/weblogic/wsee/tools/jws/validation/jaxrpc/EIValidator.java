package weblogic.wsee.tools.jws.validation.jaxrpc;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.Iterator;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebParam.Mode;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.MTOM;
import weblogic.jws.Binding;
import weblogic.jws.Types;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.TypesUtil;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.SOAPBindingDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.jws.validation.BaseEIValidator;
import weblogic.wsee.tools.jws.validation.annotation.AnnotationMatchingRule;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.jws.validation.annotation.PackageMatchingRule;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.PolicyValidateUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.XBeanUtil;

public class EIValidator extends BaseEIValidator {
   private int noOfNonHeaderParams = 0;

   public EIValidator(JwsBuildContext var1, WebServiceSEIDecl var2) {
      super(var1, var2);
   }

   protected NoAnnotationValidator getNoAnnotationValidator(Logger var1) {
      NoAnnotationValidator var2 = new NoAnnotationValidator(var1);
      if (this.isSeparateEI()) {
         var2.addMatchingRule(new PackageMatchingRule("weblogic.jws", new Class[]{Binding.class, Types.class}));
         var2.addMatchingRule(new PackageMatchingRule("weblogic.ejbgen"));
         var2.addMatchingRule("annotation.jaxrpc.notAllowed", new AnnotationMatchingRule(new Class[]{SecurityPolicies.class, SecurityPolicy.class, MTOM.class, Addressing.class}));
      }

      return var2;
   }

   protected void visitImpl(JClass var1) {
      if (((WebServiceSEIDecl)this.webService).getCowReader() != null && StringUtil.isEmpty(((WebServiceSEIDecl)this.webService).getWsdlLocation())) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.cow.noWsdlLocation", new Object[0])));
      }

      PolicyValidateUtil.checkIllegalPolicyMixUp((WebServiceSEIDecl)this.webService, this.getLogger());
   }

   protected void visitImpl(WebMethodDecl var1) {
      this.checkResult(var1);
      this.checkTypes(var1);
      this.checkTypesAnnotation(var1);
      this.noOfNonHeaderParams = 0;
   }

   protected void visitImpl(WebParamDecl var1) {
      if (var1.getMode() != Mode.IN) {
         this.checkOutParam(var1);
      }

      this.checkXmlBeans(var1);
   }

   private void checkOutParam(WebParamDecl var1) {
      WebMethodDecl var2 = var1.getWebMethodDecl();
      if (var2.getSoapBinding().isDocumentStyle() && !var1.isHeader()) {
         ++this.noOfNonHeaderParams;
         if (var2.isOneway() || var2.getWebResult().hasReturn() || this.noOfNonHeaderParams > 1) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2.getJMethod(), "parameter.binding.invalidModeForDoc", new Object[0])));
         }
      }

   }

   private void checkXmlBeans(WebParamDecl var1) {
      if (var1.getTypeFamily() != WebTypeDecl.TypeFamily.POJO) {
         SOAPBindingDecl var2 = var1.getWebMethodDecl().getSoapBinding();
         if (!var2.isDocumentStyle() && var2.isLiteral()) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1.getWebMethodDecl().getJMethod(), "method.rpc.xmlBeanNotSupported", new Object[0])));
         } else {
            JAnnotation var3 = var1.getJParameter().getAnnotation(WebParam.class);
            if (var1.getTypeFamily() == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT && var3 != null) {
               this.checkXmlBeanDocument(var1.getWebMethodDecl().getJMethod(), var1.getJParameter().getType(), var3);
            }
         }
      }

   }

   private void checkResult(WebMethodDecl var1) {
      JMethod var2 = var1.getJMethod();
      if (var1.getWebResult().isHeader()) {
         this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "result.jaxrpc.headerNotSupported", new Object[]{var2.getQualifiedName()})));
      }

      if (var1.getWebResult().getTypeFamily() != WebTypeDecl.TypeFamily.POJO) {
         if (!var1.getSoapBinding().isDocumentStyle() && var1.getSoapBinding().isLiteral()) {
            this.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2, "method.rpc.xmlBeanNotSupported", new Object[0])));
         } else {
            JAnnotation var3 = var2.getAnnotation(WebResult.class);
            if (var1.getWebResult().getTypeFamily() == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT && var3 != null) {
               this.checkXmlBeanDocument(var2, var2.getReturnType(), var3);
            }
         }
      }

   }

   private void checkTypes(WebMethodDecl var1) {
      if (((WebServiceSEIDecl)this.webService).getCowReader() == null) {
         TypeValidator var2 = new TypeValidator(this.getLogger(), var1);
         if (var1.getWebResult().hasReturn()) {
            var2.validate(var1.getWebResult());
         }

         int var3 = 0;
         Iterator var4 = var1.getWebParams();

         while(var4.hasNext()) {
            if (((WebParamDecl)var4.next()).getType().equals(SOAPELEMENT_CLASSNAME)) {
               ++var3;
            }
         }

         if (var3 > 1) {
            this.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1.getJMethod(), "MultipleSoapElements.InputParam", new Object[]{var1.getJMethod().getQualifiedName()})));
         }

         var4 = var1.getWebParams();

         while(var4.hasNext()) {
            var2.validate((WebTypeDecl)var4.next());
         }

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

   private void checkTypesAnnotation(WebMethodDecl var1) {
      JClass var2 = var1.getJMethod().getContainingClass();
      TypesUtil.ProcessStrategy var3 = new TypesUtil.ProcessStrategy() {
         public void process(JClass var1, JClass var2, int var3) {
         }
      };
      TypesUtil.ReportStrategy var4 = new TypesUtil.ReportStrategy() {
         public void report(JwsLogEvent var1) {
            EventLevel var2 = null;
            if (!((WebServiceSEIDecl)EIValidator.this.webService).isWlw81UpgradedService()) {
               var2 = EventLevel.ERROR;
            } else {
               var2 = EventLevel.WARNING;
            }

            EIValidator.this.getLogger().log(var2, (LogEvent)var1);
         }
      };

      try {
         TypesUtil.processParamTypes(var2, var1, 0, var3, var4);
         TypesUtil.processReturnTypes(var2, var1, 0, var3, var4);
      } catch (WsBuildException var6) {
         var6.printStackTrace();
      }

   }
}
