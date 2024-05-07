package weblogic.cache;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class CacheValue implements Serializable {
   private Object content;
   private Hashtable attributes = new Hashtable();
   private Hashtable variables;
   private long created;
   private int timeout;
   private boolean flush;

   public void setContent(Object var1) {
      this.content = var1;
   }

   public Object getContent() {
      return this.content;
   }

   public void setVariables(Hashtable var1) {
      this.variables = var1;
   }

   public Hashtable getVariables() {
      return this.variables;
   }

   public void setCreated(long var1) {
      this.created = var1;
   }

   public long getCreated() {
      return this.created;
   }

   public void setTimeout(int var1) {
      this.timeout = var1;
   }

   public int getTimeout() {
      return this.timeout;
   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public Enumeration getAttributeNames() {
      return this.attributes.keys();
   }

   public void setFlush(boolean var1) {
      this.flush = var1;
   }

   public boolean getFlush() {
      return this.flush;
   }
}
