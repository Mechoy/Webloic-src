package weblogic.wsee.jaxws.spi;

import com.sun.xml.ws.Closeable;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.WebServiceFeature;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;

class ClientInstanceInvocationHandler<T> implements InvocationHandler {
   private static final Logger LOGGER = Logger.getLogger(ClientInstanceInvocationHandler.class.getName());
   private ClientInstance<T> _instance;
   private Closeable c;

   ClientInstanceInvocationHandler(ClientInstance<T> var1, Closeable var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null instance");
      } else {
         this._instance = var1;
         this.c = var2;
      }
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      Class var4 = var2.getDeclaringClass();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Invoking method " + var2.getName() + " from declaring class " + var4.getSimpleName() + " on proxy for client instance: " + this._instance);
      }

      if (this._instance == null) {
         if ((var4 != Closeable.class && var4 != java.io.Closeable.class || !var2.getName().equals("close")) && (var4 != Object.class || !var2.getName().equals("finalize"))) {
            throw new IllegalStateException("Attempt to use a client instance (Port/Dispatch) that has been closed or finalized");
         } else {
            return null;
         }
      } else if ((var4 == Closeable.class || var4 == java.io.Closeable.class) && var2.getName().equals("close")) {
         this.closeInstance();
         return null;
      } else if (var4 == Object.class && var2.getName().equals("finalize")) {
         this.closeInstance();
         return null;
      } else {
         WebServiceFeatureList var5 = this._instance.getCreationInfo().getFeatures();
         if (this.isAsyncHandlerSupplied(var3) && var5.containsKey(AsyncClientHandlerFeature.class) && !var5.containsKey(AsyncHandlerAllowedInternalFeature.class)) {
            throw new IllegalArgumentException("Non-null javax.xml.ws.AsyncHandler cannot be used when a client instance (Port/Dispatch) already has AsyncClientHandlerFeature configured.");
         } else {
            try {
               return var2.invoke(this._instance.getInstance(), var3);
            } catch (InvocationTargetException var7) {
               throw var7.getCause();
            }
         }
      }
   }

   private void closeInstance() {
      this.c.close();
   }

   private boolean isAsyncHandlerSupplied(Object[] var1) {
      boolean var2 = false;
      if (var1 != null) {
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            if (var6 != null && var6 instanceof AsyncHandler) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }

   private Map<Class, WebServiceFeature> getFeatureMap(WebServiceFeature... var1) {
      HashMap var2 = new HashMap();
      WebServiceFeature[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WebServiceFeature var6 = var3[var5];
         var2.put(var6.getClass(), var6);
      }

      return var2;
   }
}
