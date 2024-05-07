package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.util.JamUtil;

public class SOAPBindingDecl {
   public static final String SOAP_HTTP_TRANSPORT = "http://schemas.xmlsoap.org/soap/http";
   private SOAPBinding.Style style;
   private SOAPBinding.Use use;
   private SOAPBinding.ParameterStyle parameterStyle;

   SOAPBindingDecl(JwsBuildContext var1, JClass var2) {
      this.style = Style.DOCUMENT;
      this.use = Use.LITERAL;
      this.parameterStyle = ParameterStyle.WRAPPED;
      JAnnotation var3 = var2.getAnnotation(SOAPBinding.class);
      SOAPBinding.Style var4 = var1.getTask() == JwsBuildContext.Task.JWSC ? Style.DOCUMENT : Style.RPC;
      SOAPBinding.Use var5 = var1.getTask() == JwsBuildContext.Task.JWSC ? Use.LITERAL : Use.ENCODED;
      SOAPBinding.ParameterStyle var6 = var1.getTask() == JwsBuildContext.Task.JWSC ? ParameterStyle.WRAPPED : ParameterStyle.BARE;
      this.style = var4;
      this.use = var5;
      this.parameterStyle = var6;
      if (var3 != null) {
         this.style = (SOAPBinding.Style)JamUtil.getAnnotationEnumValue(var3, "style", SOAPBinding.Style.class, var4);
         this.use = (SOAPBinding.Use)JamUtil.getAnnotationEnumValue(var3, "use", SOAPBinding.Use.class, var5);
         this.parameterStyle = (SOAPBinding.ParameterStyle)JamUtil.getAnnotationEnumValue(var3, "parameterStyle", SOAPBinding.ParameterStyle.class, var6);
      }

      if (this.style == Style.RPC) {
         this.parameterStyle = ParameterStyle.BARE;
      }

      if (this.parameterStyle == null) {
         this.parameterStyle = this.style == Style.DOCUMENT ? var6 : ParameterStyle.BARE;
      }

   }

   public SOAPBindingDecl(JAnnotation var1) {
      this.style = Style.DOCUMENT;
      this.use = Use.LITERAL;
      this.parameterStyle = ParameterStyle.WRAPPED;
      this.style = (SOAPBinding.Style)JamUtil.getAnnotationEnumValue(var1, "style", SOAPBinding.Style.class, this.style);
      this.use = (SOAPBinding.Use)JamUtil.getAnnotationEnumValue(var1, "use", SOAPBinding.Use.class, this.use);
      this.parameterStyle = (SOAPBinding.ParameterStyle)JamUtil.getAnnotationEnumValue(var1, "parameterStyle", SOAPBinding.ParameterStyle.class, this.parameterStyle);
   }

   public SOAPBinding.Style getStyle() {
      return this.style;
   }

   public SOAPBinding.Use getUse() {
      return this.use;
   }

   public SOAPBinding.ParameterStyle getParameterStyle() {
      return this.parameterStyle;
   }

   public boolean isDocumentStyle() {
      return this.style.equals(Style.DOCUMENT);
   }

   public boolean isWrapped() {
      return this.parameterStyle.equals(ParameterStyle.WRAPPED);
   }

   public boolean isLiteral() {
      return this.use.equals(Use.LITERAL);
   }

   public boolean isDocLiteralWrapped() {
      return this.isDocumentStyle() && this.isLiteral() && this.isWrapped();
   }

   public boolean isDocLiteralBare() {
      return this.isDocumentStyle() && this.isLiteral() && !this.isWrapped();
   }
}
