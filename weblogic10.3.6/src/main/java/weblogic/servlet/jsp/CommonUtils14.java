package weblogic.servlet.jsp;

import javax.el.ExpressionFactory;
import javax.el.VariableMapper;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class CommonUtils14 implements JspConfig.ICommonUtils {
   public ExpressionEvaluator getExpressionEvaluator() {
      return null;
   }

   public VariableResolver getVariableResolver(PageContextImpl var1) {
      return null;
   }

   public String getDefaultJSPServlet() {
      return "weblogic.servlet.JSPServlet";
   }

   public String getJspPrecompilerClass() {
      return "weblogic.servlet.jsp.Precompiler";
   }

   public ExpressionFactory getExpressionFactory() {
      return null;
   }

   public VariableMapper getVariableMapper() {
      return null;
   }
}
