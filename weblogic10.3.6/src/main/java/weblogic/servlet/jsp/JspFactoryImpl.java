package weblogic.servlet.jsp;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import weblogic.servlet.internal.WebAppServletContext;

public final class JspFactoryImpl extends JspFactory {
   private static boolean inited = false;

   private JspFactoryImpl() {
   }

   public static synchronized void init() {
      if (!inited) {
         setDefaultFactory(new JspFactoryImpl());
         inited = true;
      }
   }

   public PageContext getPageContext(Servlet var1, ServletRequest var2, ServletResponse var3, String var4, boolean var5, int var6, boolean var7) {
      try {
         return new PageContextImpl(this, var1, var2, var3, var4, var5, var6, var7);
      } catch (IOException var9) {
         return null;
      }
   }

   public void releasePageContext(PageContext var1) {
      var1.release();
   }

   public JspEngineInfo getEngineInfo() {
      return new JspEngineInfo() {
         public String getSpecificationVersion() {
            return "2.1";
         }
      };
   }

   public JspApplicationContext getJspApplicationContext(ServletContext var1) {
      return ((WebAppServletContext)var1).getJspApplicationContext();
   }
}
