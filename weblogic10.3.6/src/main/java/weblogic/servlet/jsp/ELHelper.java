package weblogic.servlet.jsp;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

public class ELHelper {
   public static Object evaluate(String var0, Class var1, PageContext var2, FunctionMapper var3) {
      ValueExpression var4 = createValueExpression(var0, var1, var2, var3);
      return var4.getValue(var2.getELContext());
   }

   public static ValueExpression createValueExpression(String var0, Class var1, PageContext var2, FunctionMapper var3) {
      ExpressionFactory var4 = getExpressionFactory(var2);
      if (var0.length() == 0) {
         return var4.createValueExpression(var0, var1);
      } else {
         ELContextImpl var5 = (ELContextImpl)var2.getELContext();
         var5.setFunctionMapper(var3);
         return var4.createValueExpression(var5, var0, var1);
      }
   }

   public static MethodExpression createMethodExpression(String var0, Class var1, Class[] var2, PageContext var3, FunctionMapper var4) {
      ExpressionFactory var5 = getExpressionFactory(var3);
      ELContextImpl var6 = (ELContextImpl)var3.getELContext();
      var6.setFunctionMapper(var4);
      return var5.createMethodExpression(var6, var0, var1, var2);
   }

   public static void mapValueExpression(PageContext var0, String var1, ValueExpression var2) {
      ELContext var3 = var0.getELContext();
      var3.getVariableMapper().setVariable(var1, var2);
   }

   public static void mapMethodExpression(PageContext var0, String var1, MethodExpression var2) {
      ExpressionFactory var3 = getExpressionFactory(var0);
      ValueExpression var4 = var3.createValueExpression(var2, Object.class);
      mapValueExpression(var0, var1, var4);
   }

   private static ExpressionFactory getExpressionFactory(PageContext var0) {
      JspApplicationContext var1 = ELHelper.Holder.jspFactory.getJspApplicationContext(var0.getServletContext());
      return var1.getExpressionFactory();
   }

   private static class Holder {
      static JspFactory jspFactory = JspFactory.getDefaultFactory();
   }
}
