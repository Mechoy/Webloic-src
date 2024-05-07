package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JMethod;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import weblogic.jws.Conversation;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;

public class WebMethodDecl {
   private String operationName = null;
   private String methodName = null;
   private String action = null;
   private boolean oneway = false;
   private List<WebParamDecl> webParams = new ArrayList();
   private WebResultDecl webResult = null;
   private WebServiceSEIDecl webService = null;
   private Conversation.Phase converationPhase = null;
   private List<PolicyDecl> poilices = new ArrayList();
   private JMethod sbMethod = null;
   private SOAPBindingDecl soapBinding;
   private JwsBuildContext context;

   public WebMethodDecl(JwsBuildContext var1, WebServiceSEIDecl var2, JMethod var3, JMethod var4) {
      this.context = var1;
      this.webService = var2;
      this.sbMethod = var4;
      JAnnotation var5 = var3.getAnnotation(WebMethod.class);
      this.methodName = var3.getSimpleName();
      this.oneway = var3.getAnnotation(Oneway.class) != null;
      this.operationName = JamUtil.getAnnotationStringValue(var5, "operationName");
      if (StringUtil.isEmpty(this.operationName)) {
         this.operationName = this.methodName;
      }

      this.action = JamUtil.getAnnotationStringValue(var5, "action");
      if (StringUtil.isEmpty(this.action)) {
         this.action = "";
      }

      JAnnotation var6 = this.sbMethod.getAnnotation(Conversation.class);
      if (var6 != null) {
         this.converationPhase = (Conversation.Phase)JamUtil.getAnnotationEnumValue(var6, "value", Conversation.Phase.class, Conversation.Phase.CONTINUE);
      } else if (var2.isConversational()) {
         this.converationPhase = Conversation.Phase.CONTINUE;
      }

      this.poilices.addAll(PolicyDeclBuilder.build(var2.getSourceFile(), var4));
      this.soapBinding = getSoapBinding(var3, var2);
      this.webResult = new WebResultDecl(var1, var3, this);
   }

   private static SOAPBindingDecl getSoapBinding(JMethod var0, WebServiceSEIDecl var1) {
      JAnnotation var2 = var0.getAnnotation(SOAPBinding.class);
      if (var2 == null) {
         var2 = var0.getAnnotation(weblogic.jws.soap.SOAPBinding.class);
      }

      SOAPBindingDecl var3 = null;
      if (var2 != null) {
         var3 = new SOAPBindingDecl(var2);
      } else {
         var3 = var1.getSoapBinding();
      }

      return var3;
   }

   public JMethod getJMethod() {
      return this.sbMethod;
   }

   public String getName() {
      return this.operationName;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void addWebParam(WebParamDecl var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("webParamaDecl must not be null.");
      } else {
         this.webParams.add(var1);
      }
   }

   public Iterator<WebParamDecl> getWebParams() {
      return this.webParams.iterator();
   }

   public Iterator<WebParamDecl> getHeaderParams() {
      return this.getParamsInternal(true);
   }

   public Iterator<WebParamDecl> getNonHeaderParams() {
      return this.getParamsInternal(false);
   }

   public List<WebParamDecl> getParameters(WebParam.Mode var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.getNonHeaderParams();

      while(var3.hasNext()) {
         WebParamDecl var4 = (WebParamDecl)var3.next();
         if (var4.getMode() == var1) {
            var2.add(var4);
         } else if (var4.getMode() == Mode.INOUT) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private Iterator<WebParamDecl> getParamsInternal(boolean var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.webParams.iterator();

      while(var3.hasNext()) {
         WebParamDecl var4 = (WebParamDecl)var3.next();
         if (var1 == var4.isHeader()) {
            var2.add(var4);
         }
      }

      return Collections.unmodifiableList(var2).iterator();
   }

   public WebResultDecl getWebResult() {
      return this.webResult;
   }

   public String getAction() {
      return this.action;
   }

   public boolean isOneway() {
      return this.oneway;
   }

   public WebServiceSEIDecl getWebService() {
      return this.webService;
   }

   public Conversation.Phase getConverationPhase() {
      return this.converationPhase;
   }

   public Iterator<PolicyDecl> getPoilices() {
      return this.poilices.iterator();
   }

   public SOAPBindingDecl getSoapBinding() {
      return this.soapBinding;
   }

   public QName getSoapDispatchName() {
      QName var1 = null;
      if (this.getSoapBinding().isDocLiteralBare()) {
         Iterator var2 = this.webParams.iterator();

         while(var2.hasNext()) {
            WebParamDecl var3 = (WebParamDecl)var2.next();
            if (!var3.isHeader()) {
               var1 = new QName(var3.getTargetNamespace(), var3.getName());
               break;
            }
         }
      } else if (this.getSoapBinding().isDocLiteralWrapped()) {
         var1 = new QName(this.webService.getTargetNamespace(), this.getName());
      } else {
         var1 = new QName(this.getName());
      }

      return var1;
   }

   public QName getResultElementQName() {
      QName var1 = null;
      if (this.getSoapBinding().isDocLiteralBare()) {
         var1 = new QName(this.webResult.getTargetNamespace(), this.webResult.getName());
      } else {
         var1 = new QName(this.webResult.getTargetNamespace(), this.getName());
      }

      return var1;
   }

   JwsBuildContext getContext() {
      return this.context;
   }
}
