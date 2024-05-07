package weblogic.servlet.internal;

import java.io.IOException;
import weblogic.common.internal.PassivationUtils;

public class ContextAttributeWrapper extends AttributeWrapper {
   static final long serialVersionUID = 639350188940216525L;
   private transient Object convertedObject = null;

   public ContextAttributeWrapper(Object var1) {
      super(var1);
   }

   ContextAttributeWrapper(Object var1, WebAppServletContext var2) {
      super(var1, var2);
   }

   protected Object getObject(boolean var1, WebAppServletContext var2) throws ClassNotFoundException, IOException {
      if (this.serializedObject != null) {
         synchronized(this) {
            return this.serializedObject == null ? this.object : this.convertBytesToObject(this.serializedObject);
         }
      } else if (!var1) {
         return this.object;
      } else if (needToWrap(this.object) && !this.isOptimisticSerialization(var2)) {
         ClassLoader var3 = Thread.currentThread().getContextClassLoader();
         if (this.needToConvert(var3, var2)) {
            synchronized(this) {
               if (this.convertedObject != null && this.convertedObject.getClass().getClassLoader() == var3) {
                  return this.convertedObject;
               } else {
                  this.convertedObject = PassivationUtils.copy(this.object);
                  this.setForceToConvert(false);
                  return this.convertedObject;
               }
            }
         } else {
            return this.object;
         }
      } else {
         return this.object;
      }
   }

   public synchronized void convertToBytes() throws IOException {
      super.convertToBytes();
      this.convertedObject = null;
      this.object = null;
   }

   protected void reInitClassLoaderInfo() {
   }

   protected boolean isSameThreadClassLoader(int var1) {
      return false;
   }
}
