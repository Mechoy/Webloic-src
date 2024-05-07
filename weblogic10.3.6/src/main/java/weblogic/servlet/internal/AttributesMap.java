package weblogic.servlet.internal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.classloaders.ClassLoaderUtils;

class AttributesMap {
   static final String REQUEST = "request";
   static final String CONTEXT = "servlet-context";
   private final Map attributes;
   private final String scope;
   private final boolean isConcurrent;
   private Object lastAttribute;
   private String lastAttributeKey;

   AttributesMap(String var1) {
      this.scope = var1;
      if (this.scope == "servlet-context") {
         this.attributes = new ConcurrentHashMap();
         this.isConcurrent = true;
      } else {
         this.attributes = new HashMap();
         this.isConcurrent = false;
      }

   }

   Object put(String var1, Object var2, WebAppServletContext var3) {
      this.checkNullName(var1);
      if (var2 instanceof Serializable && AttributeWrapper.needToWrap(var2)) {
         if (this.scope == "servlet-context") {
            var2 = new ContextAttributeWrapper(var2, var3);
         } else {
            var2 = new AttributeWrapper(var2, var3);
         }
      }

      if (!this.isConcurrent) {
         this.lastAttributeKey = var1;
         this.lastAttribute = var2;
      }

      return this.attributes.put(var1, var2);
   }

   public void put(String var1, Object var2) {
      this.checkNullName(var1);
      if (!this.isConcurrent) {
         this.lastAttributeKey = var1;
         this.lastAttribute = var2;
      }

      this.attributes.put(var1, var2);
   }

   Object get(String var1, WebAppServletContext var2) {
      this.checkNullName(var1);
      Object var3;
      if (!this.isConcurrent && this.lastAttributeKey == var1) {
         var3 = this.lastAttribute;
      } else {
         var3 = this.attributes.get(var1);
      }

      if (!(var3 instanceof AttributeWrapper)) {
         return var3;
      } else {
         AttributeWrapper var4 = (AttributeWrapper)var3;

         try {
            return var4.getObject(var2);
         } catch (ClassNotFoundException var6) {
            HTTPLogger.logFailedToDeserializeAttribute(var2.getLogContext(), this.scope, var1, var6);
         } catch (IOException var7) {
            HTTPLogger.logFailedToDeserializeAttribute(var2.getLogContext(), this.scope, var1, var7);
         } catch (RuntimeException var8) {
            HTTPLogger.logFailedToDeserializeAttribute(var2.getLogContext(), this.scope, var1, var8);
         }

         return null;
      }
   }

   Object remove(String var1) {
      this.checkNullName(var1);
      if (!this.isConcurrent && this.lastAttributeKey == var1) {
         this.lastAttributeKey = null;
         this.lastAttribute = null;
      }

      return this.attributes.remove(var1);
   }

   Iterator keys() {
      return this.attributes.keySet().iterator();
   }

   void clear() {
      this.lastAttributeKey = null;
      this.lastAttribute = null;
      this.attributes.clear();
   }

   boolean isEmpty() {
      return this.attributes.isEmpty();
   }

   synchronized void removeTransientAttributes(ClassLoader var1, WebAppServletContext var2) {
      if (!this.isEmpty()) {
         int var3 = var1.hashCode();
         Iterator var4 = this.attributes.keySet().iterator();
         ArrayList var5 = new ArrayList();

         while(true) {
            String var6;
            ClassLoader var8;
            do {
               label44:
               do {
                  while(var4.hasNext()) {
                     var6 = (String)var4.next();
                     Object var7 = this.attributes.get(var6);
                     if (!(var7 instanceof AttributeWrapper)) {
                        var8 = var7.getClass().getClassLoader();
                        continue label44;
                     }

                     try {
                        ((AttributeWrapper)var7).convertToBytes();
                     } catch (IOException var9) {
                     }
                  }

                  if (!var5.isEmpty()) {
                     Iterator var10 = var5.iterator();

                     while(var10.hasNext()) {
                        String var11 = (String)var10.next();
                        var2.removeAttribute(var11);
                     }
                  }

                  return;
               } while(var8 == null);
            } while(var8 != var1 && !ClassLoaderUtils.isChild(var3, var8));

            var5.add(var6);
         }
      }
   }

   private void checkNullName(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Attribute name can NOT be null.");
      }
   }
}
