package weblogic.wsee.tools.wsdlc.jaxrpc;

import java.util.Iterator;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingUtil;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Body;

public class SOAPBindingInfo {
   private static String DOC_STYLE = "SOAPBinding.Style.DOCUMENT";
   private static String RPC_STYLE = "SOAPBinding.Style.RPC";
   private static String USE_LITERAL = "SOAPBinding.Use.LITERAL";
   private static String USE_ENCODED = "SOAPBinding.Use.ENCODED";
   private static String BARE = "SOAPBinding.ParameterStyle.BARE";
   private static String WRAP = "SOAPBinding.ParameterStyle.WRAPPED";
   private String style;
   private String use;
   private String parameterStyle;
   private boolean isDocStyle;
   private boolean methodLevel;
   private boolean soap12;

   SOAPBindingInfo(JsMethod var1) {
      this.style = DOC_STYLE;
      this.use = USE_LITERAL;
      this.parameterStyle = WRAP;
      this.isDocStyle = true;
      this.methodLevel = false;
      this.soap12 = false;
      this.setStyle(var1.getStyle());
      this.setUse(var1.getUse());
      this.setWrapped(var1.isWrapped());
      this.setMethodLevel(true);
   }

   SOAPBindingInfo(JsClass var1, WsdlBinding var2) {
      this.style = DOC_STYLE;
      this.use = USE_LITERAL;
      this.parameterStyle = WRAP;
      this.isDocStyle = true;
      this.methodLevel = false;
      this.soap12 = false;
      JsMethod[] var3 = var1.getMethods();
      if (var3.length > 0) {
         this.setWrapped(var3[0].isWrapped());
      }

      Object var4 = SoapBinding.narrow(var2);
      if (var4 == null) {
         var4 = Soap12Binding.narrow(var2);
         this.setSoap12(true);
      }

      Iterator var5 = var2.getPortType().getOperations().values().iterator();

      while(var5.hasNext()) {
         WsdlOperation var6 = (WsdlOperation)var5.next();
         WsdlBindingOperation var7 = (WsdlBindingOperation)var2.getOperations().get(var6.getName());
         if (var7 != null) {
            SoapBindingOperation var8 = this.getAnySoapBindingOperation(var7);
            this.setStyle(SoapBindingUtil.getStyle(var8, (SoapBinding)var4));
            WsdlBindingMessage var9 = var7.getInput();
            if (var9 == null) {
               var9 = var7.getOutput();
            }

            if (var9 != null) {
               this.setUse(this.getAnySoapBody(var9).getUse());
            }
            break;
         }
      }

   }

   private SoapBody getAnySoapBody(WsdlBindingMessage var1) {
      Object var2 = SoapBody.narrow(var1);
      if (var2 == null) {
         var2 = Soap12Body.narrow(var1);
      }

      return (SoapBody)var2;
   }

   private SoapBindingOperation getAnySoapBindingOperation(WsdlBindingOperation var1) {
      Object var2 = SoapBindingOperation.narrow(var1);
      if (var2 == null) {
         var2 = Soap12BindingOperation.narrow(var1);
      }

      return (SoapBindingOperation)var2;
   }

   void setMethodLevel(boolean var1) {
      this.methodLevel = var1;
   }

   void setStyle(String var1) {
      if (var1 != null) {
         if (var1.equals("document")) {
            this.style = DOC_STYLE;
         } else {
            this.style = RPC_STYLE;
            this.isDocStyle = false;
         }
      }

   }

   public String getStyle() {
      return this.style;
   }

   void setUse(String var1) {
      if (var1 != null) {
         if (var1.equals("literal")) {
            this.use = USE_LITERAL;
         } else {
            this.use = USE_ENCODED;
         }
      }

   }

   public String getUse() {
      return this.use;
   }

   void setWrapped(boolean var1) {
      if (var1) {
         this.parameterStyle = WRAP;
      } else {
         this.parameterStyle = BARE;
      }

   }

   public String getParameterStyle() {
      return this.parameterStyle;
   }

   public boolean isDocumentStyle() {
      return this.isDocStyle;
   }

   public boolean isSoap12() {
      return this.soap12;
   }

   void setSoap12(boolean var1) {
      this.soap12 = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SOAPBindingInfo) {
         SOAPBindingInfo var2 = (SOAPBindingInfo)var1;
         if (this.getStyle().equals(var2.getStyle()) && this.getUse().equals(var2.getUse()) && this.getParameterStyle().equals(var2.getParameterStyle())) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.methodLevel) {
         var1.append("@weblogic.jws.soap.SOAPBinding(");
      } else {
         var1.append("@SOAPBinding(");
      }

      var1.append("style=" + this.getStyle() + ", ");
      var1.append("use=" + this.getUse());
      if (this.isDocumentStyle()) {
         var1.append(",parameterStyle=" + this.getParameterStyle());
      }

      var1.append(")");
      if (this.isSoap12()) {
         var1.append("\n@weblogic.jws.Binding(weblogic.jws.Binding.Type.SOAP12)");
      }

      return var1.toString();
   }
}
