package weblogic.wsee.async;

import java.io.Serializable;
import java.util.HashMap;
import weblogic.wsee.util.Verbose;

public class AsyncPreCallContextImpl implements AsyncPreCallContext, Serializable {
   private HashMap properties = new HashMap();
   private long timeout = -1L;
   private static final boolean verbose = Verbose.isVerbose(AsyncPreCallContextImpl.class);

   AsyncPreCallContextImpl() {
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   HashMap getProperties() {
      return this.properties;
   }

   public void setTimeout(long var1) {
      this.timeout = var1;
   }

   long getTimeout() {
      return this.timeout;
   }
}
