package weblogic.servlet.internal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class FilterChainImpl implements FilterChain {
   private List<Filter> filters = new LinkedList();
   private int index = 0;
   private int headFilterInsertPos = 0;
   private ServletRequestImpl req;
   private ServletResponseImpl res;

   public void add(Filter var1) {
      if (var1 != null) {
         this.filters.add(var1);
      }

   }

   public void add(FilterWrapper var1) throws ServletException {
      if (var1 != null) {
         Filter var2 = var1.getFilter(true);
         if (var2 != null) {
            if (var1.isHeadFilter()) {
               this.filters.add(this.headFilterInsertPos, var2);
               ++this.headFilterInsertPos;
            } else {
               this.filters.add(var2);
            }

         }
      }
   }

   public void doFilter(ServletRequest var1, ServletResponse var2) throws IOException, ServletException {
      Filter var3 = (Filter)this.filters.get(this.index++);
      var3.doFilter(var1, var2, this);
   }

   public ServletRequestImpl getOrigRequest() {
      return this.req;
   }

   public void setOrigRequest(ServletRequestImpl var1) {
      this.req = var1;
   }

   public ServletResponseImpl getOrigResponse() {
      return this.res;
   }

   public void setOrigResponse(ServletResponseImpl var1) {
      this.res = var1;
   }
}
