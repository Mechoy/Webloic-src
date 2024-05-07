package weblogic.servlet.jsp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContextEvent;
import javax.el.ELContextListener;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.el.ImplicitObjectELResolver;
import javax.servlet.jsp.el.ScopedAttributeELResolver;

public class JspApplicationContextImpl implements JspApplicationContext {
   private static ExpressionFactory exprFactory;
   private ServletContext context;
   private List listeners;
   private CompositeELResolver resolver;
   private CompositeELResolver appResolver;
   private boolean contextStarted;

   public JspApplicationContextImpl(ServletContext var1) {
      this.context = var1;
      this.listeners = new ArrayList();
      this.appResolver = new CompositeELResolver();
      this.resolver = new CompositeELResolver();
      this.initELResolver();
   }

   public void addELContextListener(ELContextListener var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }

   }

   public void addELResolver(ELResolver var1) {
      if (this.contextStarted) {
         throw new IllegalStateException("It is illegal to register an ELResolver after all ServletContextListeners have had their contextInitialized methods invoked.");
      } else {
         if (var1 != null) {
            this.appResolver.add(var1);
         }

      }
   }

   public ExpressionFactory getExpressionFactory() {
      return exprFactory;
   }

   public void setContextStarted(boolean var1) {
      this.contextStarted = var1;
   }

   public ELContextImpl createELContext(JspContext var1) {
      ELContextImpl var2 = new ELContextImpl(this.resolver, JspConfig.COMMON_UTILS.getVariableMapper(), (FunctionMapper)null);
      var2.putContext(JspContext.class, var1);
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ELContextListener var4 = (ELContextListener)var3.next();
         var4.contextCreated(new ELContextEvent(var2));
      }

      return var2;
   }

   private void initELResolver() {
      this.resolver.add(new ImplicitObjectELResolver());
      if (this.appResolver != null) {
         this.resolver.add(this.appResolver);
      }

      this.resolver.add(new MapELResolver());
      this.resolver.add(new ResourceBundleELResolver());
      this.resolver.add(new ListELResolver());
      this.resolver.add(new ArrayELResolver());
      this.resolver.add(new BeanELResolver());
      this.resolver.add(new ScopedAttributeELResolver());
   }

   static {
      exprFactory = JspConfig.COMMON_UTILS.getExpressionFactory();
   }
}
