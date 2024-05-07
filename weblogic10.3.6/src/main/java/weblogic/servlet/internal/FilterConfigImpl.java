package weblogic.servlet.internal;

import java.util.Enumeration;
import java.util.Map;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;

public final class FilterConfigImpl implements FilterConfig {
   private final String filterName;
   private final ServletContext ctx;
   private final Map initParams;

   public FilterConfigImpl(String var1, ServletContext var2, Map var3) {
      this.filterName = var1;
      this.ctx = var2;
      this.initParams = var3;
   }

   public String getFilterName() {
      return this.filterName;
   }

   public ServletContext getServletContext() {
      return this.ctx;
   }

   public String getInitParameter(String var1) {
      return this.initParams == null ? null : (String)this.initParams.get(var1);
   }

   public Enumeration getInitParameterNames() {
      return (Enumeration)(this.initParams == null ? new EmptyEnumerator() : new IteratorEnumerator(this.initParams.keySet().iterator()));
   }
}
