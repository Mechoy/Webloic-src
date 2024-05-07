package weblogic.wsee.jaxrpc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.xml.schema.binding.util.ClassUtil;

public final class HandlerRegistryImpl implements HandlerRegistry {
   private Map chainMap = Collections.synchronizedMap(new HashMap());
   private ServiceRefHandlerBean[] handlers = new ServiceRefHandlerBean[0];
   private boolean changed = false;

   HandlerRegistryImpl() {
   }

   HandlerRegistryImpl(ServiceRefHandlerBean[] var1) {
      this.handlers = var1;
   }

   public List getHandlerChain(QName var1) {
      List var2 = (List)this.chainMap.get(var1.getLocalPart());
      if (var2 == null) {
         var2 = this.createChain(var1);
         this.setHandlerChain(var1, var2);
      }

      this.changed = true;
      return var2;
   }

   public void setHandlerChain(QName var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (!(var4 instanceof HandlerInfo)) {
            throw new JAXRPCException("The List argument to  setHandlerChain must contain instances of javax.xml.rpc.handler.HandlerInfo.  The list contained " + var4.getClass().getName() + " which is not an instanceof" + " HandlerInfo.");
         }

         this.validateHandlerInfo((HandlerInfo)var4, var1);
      }

      this.chainMap.put(var1.getLocalPart(), var2);
      this.changed = true;
   }

   private void validateHandlerInfo(HandlerInfo var1, QName var2) {
      Class var3 = var1.getHandlerClass();
      if (var3 == null) {
         throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " had a null HandlerClass.");
      } else if (!Handler.class.isAssignableFrom(var3)) {
         throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which is not an instanceof javax.xml.rpc.handler.Handler");
      } else {
         try {
            Constructor var4 = var3.getConstructor((Class[])null);
            if (!Modifier.isPublic(var4.getModifiers())) {
               throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which has a default, no argument constructor.  However," + "this constructor is not public.");
            }
         } catch (NoSuchMethodException var5) {
            throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which does not have a public, no argument constructor.");
         }
      }
   }

   private List createChain(QName var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.handlers.length; ++var3) {
         if (this.containName(this.handlers[var3].getPortNames(), var1.getLocalPart())) {
            String var4 = this.handlers[var3].getHandlerClass();
            QName[] var5 = this.handlers[var3].getSoapHeaders();
            Map var6 = this.buildInitParams(this.handlers[var3].getInitParams());
            var2.add(new HandlerInfo(this.loadClass(var4), var6, var5));
         }
      }

      return var2;
   }

   private boolean containName(String[] var1, String var2) {
      if (var1 != null && var1.length != 0) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var2.equals(var1[var3])) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private Map buildInitParams(ParamValueBean[] var1) {
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3].getParamName();
            String var5 = var1[var3].getParamValue();
            var2.put(var4, var5);
         }

         return var2;
      }
   }

   public boolean isChanged() {
      return this.changed;
   }

   public void setChanged(boolean var1) {
      this.changed = var1;
   }

   private Class loadClass(String var1) {
      try {
         return ClassUtil.loadClass(var1);
      } catch (ClassUtil.ClassUtilException var3) {
         throw new JAXRPCException("Failed to load class " + var3, var3);
      }
   }

   public String toString() {
      return "[HandlerRegistry]: " + this.chainMap.toString();
   }
}
