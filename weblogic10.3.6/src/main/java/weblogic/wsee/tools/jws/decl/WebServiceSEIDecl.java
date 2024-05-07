package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import com.bea.util.jam.mutable.MAnnotatedElement;
import com.bea.util.jam.mutable.MAnnotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jws.WebMethod;
import weblogic.ejbgen.Session;
import weblogic.jws.AsyncResponseBean;
import weblogic.jws.Callback;
import weblogic.jws.Context;
import weblogic.jws.Conversation;
import weblogic.jws.Conversational;
import weblogic.jws.Exclude;
import weblogic.jws.ServiceClient;
import weblogic.jws.WLDeployment;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.clientgen.callback.ClientSideCallbackService;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.validation.EIValidatorFactory;
import weblogic.wsee.tools.jws.validation.SBValidatorFactory;
import weblogic.wsee.tools.jws.validation.Validator;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;

public class WebServiceSEIDecl extends WebServiceDecl {
   private String serviceEndpointInterfaceName;
   private boolean serviceClientsPresent;
   private boolean complexFieldsPresent;
   private final SOAPBindingDecl soapBinding;
   private CallbackServiceDecl callbackService;
   private final Set<String> deploymentListeners;
   protected WildcardBindingsDecl wildcardBindings;
   private static final String CONTROL_ANNOTATION = "org.apache.beehive.controls.api.bean.Control";
   private static final String CONTEXT_ANNOTATION = "weblogic.controls.jws.Common.Context";
   private final Map<String, WebMethodDecl> webMethods;
   private final List<PolicyDecl> poilices;
   private static Set<String> complexAnnotationSet = null;

   public WebServiceSEIDecl(JwsBuildContext var1, JwsInfo var2, String var3) {
      this(var1, var2, var3, (WebServiceDecl)null);
   }

   WebServiceSEIDecl(JwsBuildContext var1, JwsInfo var2, String var3, WebServiceDecl var4) {
      super(var1, var2, var3, var4);
      this.serviceEndpointInterfaceName = null;
      this.serviceClientsPresent = false;
      this.complexFieldsPresent = false;
      this.callbackService = null;
      this.deploymentListeners = new HashSet();
      this.webMethods = new HashMap();
      this.poilices = new ArrayList();
      this.soapBinding = new SOAPBindingDecl(var1, this.eiClass);
      this.initCallbackService(var1);
      this.findDeploymentListeners(this.sbClass);
      this.checkFields();
      this.poilices.addAll(PolicyDeclBuilder.build(this.getSourceFile(), this.sbClass));
      this.setServiceEndpointInterfaceName();
      this.wildcardBindings = new WildcardBindingsDecl(this.sbClass);
      this.initMethods();
   }

   private void setServiceEndpointInterfaceName() {
      this.serviceEndpointInterfaceName = this.sbClass.getQualifiedName() + "PortType";
      if (this.isEjb() && this.getType() == WebServiceType.JAXRPC) {
         Object var1 = this.sbClass.getAnnotation(Session.class);
         String var2;
         if (var1 == null) {
            var1 = ((MAnnotatedElement)this.sbClass).addLiteralAnnotation(Session.class.getName());
            var2 = this.sbClass.getSimpleName();
            if (var2.endsWith("Bean") && var2.length() > 4) {
               var2 = var2.substring(0, var2.length() - 4);
            }

            ((MAnnotation)var1).setSimpleValue("ejbName", var2, this.sbClass.forName("java.lang.String"));
         }

         var2 = JamUtil.getAnnotationStringValue((JAnnotation)var1, "serviceEndpoint");
         if (!StringUtil.isEmpty(var2) && !var2.equals("UNSPECIFIED")) {
            this.serviceEndpointInterfaceName = var2;
         } else {
            ((MAnnotation)var1).setSimpleValue("serviceEndpoint", this.serviceEndpointInterfaceName, this.sbClass.forName("java.lang.String"));
         }
      }

   }

   public String getEndpointInterfaceName() {
      return this.serviceEndpointInterfaceName;
   }

   public SOAPBindingDecl getSoapBinding() {
      return this.soapBinding;
   }

   void addWebMethod(WebMethodDecl var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("webMethodDecl must not be null.");
      } else {
         this.webMethods.put(var1.getJMethod().toString(), var1);
      }
   }

   public WebMethodDecl getWebMethod(JMethod var1) {
      return (WebMethodDecl)this.webMethods.get(var1.toString());
   }

   public Iterator<WebMethodDecl> getWebMethods() {
      return this.webMethods.values().iterator();
   }

   public boolean requiresContainer() {
      return this.ctx.getTask() == JwsBuildContext.Task.APT || this.isComplex();
   }

   private void checkFields() {
      JField[] var1 = this.sbClass.getFields();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.checkField(var1[var2]);
         if (this.complexFieldsPresent && this.serviceClientsPresent) {
            break;
         }
      }

   }

   private void checkField(JField var1) {
      JAnnotation var2 = null;
      if (!this.complexFieldsPresent) {
         Iterator var3 = complexAnnotationSet.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2 = var1.getAnnotation(var4);
            if (var2 != null) {
               this.complexFieldsPresent = true;
               break;
            }
         }
      }

      var2 = var1.getAnnotation(ServiceClient.class);
      if (var2 != null) {
         this.serviceClientsPresent = true;
         this.complexFieldsPresent = true;
      }

   }

   public boolean isComplex() {
      return this.complexFieldsPresent || this.isConversational();
   }

   private void findDeploymentListeners(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(WLDeployment.class);
      if (var2 != null) {
         JAnnotationValue var3 = var2.getValue("deploymentListener");
         if (var3 != null) {
            String[] var4 = var3.asStringArray();
            String[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               this.deploymentListeners.add(var8);
            }
         }
      }

   }

   public Iterator<String> getDeploymentListeners() {
      return this.deploymentListeners.iterator();
   }

   public Iterator<PolicyDecl> getPoilices() {
      return this.poilices.iterator();
   }

   public boolean isAsyncResponseRequired() {
      return this.serviceClientsPresent || this.sbClass.getAnnotation(AsyncResponseBean.class) != null;
   }

   private void initCallbackService(JwsBuildContext var1) {
      JField[] var2 = this.sbClass.getFields();
      JField[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JField var6 = var3[var5];
         if (var6.getAnnotation(Callback.class) != null) {
            if (this.callbackService != null) {
               var1.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.sbClass, "type.callback.moreThanOne", new Object[]{this.sbClass.getQualifiedName()})));
            } else {
               Jws var7 = new Jws();
               var7.setJClass(var6.getType());
               this.callbackService = new CallbackServiceDecl(var1, var7, (String)null, this);
            }
         }
      }

   }

   public CallbackServiceDecl getCallbackService() {
      return this.callbackService;
   }

   public boolean validate() {
      boolean var1 = true;
      Validator var2;
      if (!WebServiceType.JAXWS.equals(this.getType()) || this.getCowReader() == null) {
         var2 = EIValidatorFactory.newInstance(this.ctx, this);
         var1 = var2.validate();
      }

      var2 = SBValidatorFactory.newInstance(this.ctx, this, var1);
      boolean var3 = var2.validate();
      boolean var4 = true;
      if (this.callbackService != null) {
         var4 = this.callbackService.validate();
      }

      return var1 && var3 && var4;
   }

   public static Set<String> getComplexAnnotations() {
      return complexAnnotationSet;
   }

   public boolean isWlw81UpgradedService() {
      return this.getJClass().getAnnotation(UseWLW81BindingTypes.class) != null;
   }

   public boolean isClientSideCallbackService() {
      return this.getJClass().getAnnotation(ClientSideCallbackService.class) != null;
   }

   public WildcardBindingsDecl getWildcardBindings() {
      return this.wildcardBindings;
   }

   private boolean isAllWebMethodsFlag() {
      if (this.getType() == WebServiceType.JAXRPC) {
         if (!StringUtil.isEmpty(this.getEndPointInterface())) {
            return true;
         } else {
            Iterator var1 = this.getNonExcludedMethods().iterator();

            JMethod var2;
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               var2 = (JMethod)var1.next();
            } while(JamUtil.isObjectMethod(var2) || var2.getAnnotation(WebMethod.class) == null);

            return false;
         }
      } else {
         return true;
      }
   }

   void initMethods() {
      boolean var1 = this.isAllWebMethodsFlag();
      Iterator var2 = this.getNonExcludedMethods().iterator();

      while(var2.hasNext()) {
         JMethod var3 = (JMethod)var2.next();
         this.buildMethod(var3, var1);
      }

   }

   private void buildMethod(JMethod var1, boolean var2) {
      if (isWebMethod(var1, var2)) {
         JMethod var3 = JamUtil.getOverrideMethod(this.getJClass(), var1);
         if (var3 == null) {
            this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.getJClass(), "type.ei.MethodNotImplemented", new Object[]{var1.getQualifiedName()})));
            var3 = var1;
         }

         WebMethodDecl var4 = new WebMethodDecl(this.ctx, this, var1, var3);
         if (var4.getWebResult().hasReturn() && !var4.getWebResult().validate()) {
            this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.getJClass(), "type.ei.ResultNotSupport", new Object[]{var1.getQualifiedName()})));
         }

         this.addWebMethod(var4);
         JParameter[] var5 = var1.getParameters();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            WebParamDecl var7 = new WebParamDecl(var4, var5[var6], var6);
            if (!var7.validate()) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.getJClass(), "type.ei.ParameterNotSupport", new Object[]{var1.getQualifiedName(), var5[var6].getQualifiedName()})));
            }

            var4.addWebParam(var7);
         }
      }

   }

   private static boolean isWebMethod(JMethod var0, boolean var1) {
      return var0.getAnnotation(WebMethod.class) != null || var1 && var0.isPublic() && !JamUtil.isObjectMethod(var0);
   }

   private static boolean isExcluded(JAnnotatedElement var0) {
      if (var0.getAnnotation(Exclude.class) != null) {
         return true;
      } else {
         JAnnotation var1 = var0.getAnnotation(WebMethod.class);
         return var1 != null ? JamUtil.getAnnotationBooleanValue(var1, "exclude", false) : false;
      }
   }

   private List<JMethod> getNonExcludedMethods() {
      ArrayList var1 = new ArrayList();

      for(JClass var2 = this.getEIClass(); var2 != null && !isExcluded(var2); var2 = var2.getSuperclass()) {
         JMethod[] var3 = var2.getDeclaredMethods();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            JMethod var6 = var3[var5];
            if (!isExcluded(var6)) {
               var1.add(var6);
            }
         }
      }

      return var1;
   }

   static {
      complexAnnotationSet = new HashSet();
      complexAnnotationSet.add("weblogic.controls.jws.Common.Context");
      complexAnnotationSet.add("org.apache.beehive.controls.api.bean.Control");
      complexAnnotationSet.add(Context.class.getName());
      complexAnnotationSet.add(Callback.class.getName());
      complexAnnotationSet.add(ServiceClient.class.getName());
      complexAnnotationSet.add(Conversation.class.getName());
      complexAnnotationSet.add(Conversational.class.getName());
   }
}
