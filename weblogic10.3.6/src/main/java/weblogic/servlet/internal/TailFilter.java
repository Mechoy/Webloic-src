package weblogic.servlet.internal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class TailFilter implements Filter {
   private final ServletStubImpl servletStub;

   public TailFilter(ServletStubImpl var1) {
      this.servletStub = var1;
   }

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws ServletException, IOException {
      this.servletStub.execute(var1, var2, (FilterChainImpl)var3);
   }

   public void init(FilterConfig var1) throws ServletException {
   }

   public void destroy() {
   }
}
