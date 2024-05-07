package weblogic.wsee.async;

import java.util.HashMap;

public class AsyncPostCallContextImpl implements AsyncPostCallContext {
   private HashMap properties = new HashMap();
   private String messageId;
   private String stubName;
   private String className;
   private String methodName;
   private long absTimeout = -1L;

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   HashMap getProperties() {
      return this.properties;
   }

   public String getMessageId() {
      return this.messageId;
   }

   public String getStubName() {
      return this.stubName;
   }

   public long getAbsTimeout() {
      return this.absTimeout;
   }

   void setMessageId(String var1) {
      this.messageId = var1;
   }

   void setStubName(String var1) {
      this.stubName = var1;
   }

   public void setProperties(HashMap var1) {
      this.properties = (HashMap)var1.clone();
   }

   void setAbsTimeout(long var1) {
      this.absTimeout = var1;
   }

   void setProperty(String var1, Object var2) {
      this.properties.put(var1, var2);
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String var1) {
      this.className = var1;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }
}
