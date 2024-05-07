package weblogic.servlet.jsp;

import javax.el.ExpressionFactory;
import javax.el.VariableMapper;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import weblogic.jsp.internal.jsp.el.ExpressionEvaluatorImpl;
import weblogic.jsp.internal.jsp.el.VariableResolverImpl;
import weblogic.jsp.internal.jsp.el21.ELFactory;

public class CommonUtils15 implements JspConfig.ICommonUtils {
   public ExpressionEvaluator getExpressionEvaluator() {
      return new ExpressionEvaluatorImpl();
   }

   public VariableResolver getVariableResolver(PageContextImpl var1) {
      return new VariableResolverImpl(var1);
   }

   public String getDefaultJSPServlet() {
      return "weblogic.servlet.JavelinxJSPServlet";
   }

   public String getJspPrecompilerClass() {
      return "weblogic.servlet.jsp.JavelinxJspPrecompiler";
   }

   public ExpressionFactory getExpressionFactory() {
      return ELFactory.getInstance().createExpressionFactory();
   }

   public VariableMapper getVariableMapper() {
      return ELFactory.getInstance().createVariableMapper();
   }
}
