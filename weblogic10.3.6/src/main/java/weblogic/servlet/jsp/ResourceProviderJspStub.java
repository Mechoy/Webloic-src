package weblogic.servlet.jsp;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import oracle.jsp.provider.JspResourceProvider;
import weblogic.servlet.JSPServlet;
import weblogic.servlet.internal.FilterChainImpl;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppConfigManager;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.servlet.utils.ServletMapping;
import weblogic.servlet.utils.URLMapping;

public class ResourceProviderJspStub extends ServletStubImpl {
   private static final Constructor PROVIDER_JAVELINX_STUB = getProviderStubConstructor();
   private String uri;
   private JspConfig jspConfig;
   private JspResourceProvider jspResourceProvider;
   private URLMapping servletMapping = new ServletMapping(WebAppConfigManager.isCaseInsensitive(), WebAppSecurity.getEnforceStrictURLPattern());

   public ResourceProviderJspStub(String var1, WebAppServletContext var2, JspConfig var3) {
      super(var1, (String)null, var2, (Map)null);
      this.uri = var1;
      this.jspConfig = var3;
      this.jspResourceProvider = var2.getJspResourceProvider();
   }

   public void execute(ServletRequest var1, ServletResponse var2, FilterChainImpl var3) throws ServletException, IOException {
      String var4 = this.jspResourceProvider.getProviderURI(this.uri);
      if (var4 == null) {
         ((HttpServletResponse)var2).sendError(404);
      } else {
         ServletStubImpl var5 = (ServletStubImpl)this.servletMapping.get(var4);
         if (var5 != null) {
            var5.execute(var1, var2, var3);
         } else {
            JspStub var6 = getNewProviderStub(var4, JSPServlet.uri2classname(this.jspConfig.getPackagePrefix(), var4), (Map)null, this.getContext(), this.jspConfig, this.uri);
            this.servletMapping.put(var4, var6);
            var6.execute(var1, var2, var3);
         }
      }
   }

   private static JspStub getNewProviderStub(String var0, String var1, Map var2, WebAppServletContext var3, JspConfig var4, String var5) throws ServletException {
      try {
         return (JspStub)PROVIDER_JAVELINX_STUB.newInstance(var0, var1, var2, var3, var4, var5);
      } catch (InstantiationException var7) {
         throw new AssertionError(var7);
      } catch (IllegalAccessException var8) {
         throw new AssertionError(var8);
      } catch (InvocationTargetException var9) {
         throw new AssertionError(var9);
      }
   }

   private static Constructor getProviderStubConstructor() {
      try {
         Class var0 = Class.forName("weblogic.servlet.jsp.ResourceProviderJavelinxJspStub");
         return var0.getConstructor(String.class, String.class, Map.class, WebAppServletContext.class, JspConfig.class, String.class);
      } catch (ClassNotFoundException var1) {
         throw new AssertionError("Unable to find class weblogic.servlet.jsp.ResourceProviderJavelinxJspStub");
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("Unable to find constructor weblogic.servlet.jsp.ResourceProviderJavelinxJspStub(String, String, Map, WebAppServletContext, JspConfig, String)");
      }
   }
}
