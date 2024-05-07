package weblogic.servlet.security.internal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.internal.WebAppServletContext;

final class AuthFilterChain implements FilterChain {
   private final WebAppServletContext context;
   private final Filter[] filters;
   private final int size;
   private int index = 0;

   AuthFilterChain(Filter[] var1, WebAppServletContext var2) {
      this.filters = var1;
      this.context = var2;
      this.size = var1.length;
   }

   public void doFilter(ServletRequest var1, ServletResponse var2) throws IOException, ServletException {
      Object var3;
      if (this.index >= this.size) {
         var3 = new LastFilter();
      } else {
         var3 = this.filters[this.index];
      }

      ++this.index;
      ((Filter)var3).doFilter(var1, var2, this);
   }

   private class LastFilter implements Filter {
      private LastFilter() {
      }

      public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
         try {
            AuthFilterChain.this.context.securedExecute((HttpServletRequest)var1, (HttpServletResponse)var2, false);
         } catch (IOException var5) {
            throw var5;
         } catch (ServletException var6) {
            throw var6;
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Throwable var8) {
            throw new ServletException(var8);
         }
      }

      public void init(FilterConfig var1) throws ServletException {
      }

      public void destroy() {
      }

      // $FF: synthetic method
      LastFilter(Object var2) {
         this();
      }
   }
}
