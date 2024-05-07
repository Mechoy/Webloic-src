package weblogic.wsee.jaxws;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import weblogic.ejb.container.internal.BaseWSLocalObject;
import weblogic.ejb.spi.BaseWSObjectIntf;
import weblogic.ejb.spi.MethodUtils;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.wsee.deploy.DeployUtil;
import weblogic.wsee.server.WsSecurityContextHandler;
import weblogic.wsee.server.ejb.WsEjb;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.StringUtil;

public class WLSEjbInstanceResolver extends WLSInstanceResolver<BaseWSObjectIntf> implements WLSInstanceResolver.Factory<BaseWSObjectIntf> {
   private static final Method providerInvokeMethod;
   private WSObjectFactory wsObjectFactory = null;
   private Class c;
   private SessionContext context;

   public WLSEjbInstanceResolver(WSObjectFactory var1, Class var2) {
      this.wsObjectFactory = var1;
      this.c = var2;

      try {
         BaseWSLocalObject var3 = (BaseWSLocalObject)this.create();
         Context var4 = var3.getBeanManager().getEnvironmentContext();
         this.context = (SessionContext)var4.lookup("comp/EJBContext");
      } catch (NamingException var5) {
         throw new WebServiceException(var5);
      }
   }

   protected WLSInstanceResolver.Factory<BaseWSObjectIntf> getFactory() {
      return this;
   }

   public BaseWSObjectIntf create() {
      return this.wsObjectFactory.create();
   }

   public SessionContext getContext() {
      return this.context;
   }

   public void publishContext(WSWebServiceContext var1) {
      BaseWSLocalObject var2 = (BaseWSLocalObject)this.create();
      Context var3 = var2.getBeanManager().getEnvironmentContext();

      try {
         var3.bind("comp/WebServiceContext", var1);
      } catch (NameAlreadyBoundException var5) {
      } catch (NamingException var6) {
         throw new WebServiceException(var6);
      }

   }

   protected boolean isJsr250Needed() {
      return false;
   }

   @NotNull
   public Invoker createInvoker() {
      try {
         return new WLSEjbInvoker(this.c);
      } catch (Throwable var2) {
         throw new WebServiceException(var2);
      }
   }

   private static Class getParameterizedType(Class var0) {
      Type var1 = JAXBRIContext.getBaseType(var0, Provider.class);
      if (var1 != null && var1 instanceof ParameterizedType) {
         ParameterizedType var2 = (ParameterizedType)var1;
         Type[] var3 = var2.getActualTypeArguments();
         if (var3.length > 0 && var3[0] instanceof Class) {
            return (Class)var3[0];
         }
      }

      return null;
   }

   static {
      try {
         providerInvokeMethod = Provider.class.getMethod("invoke", Object.class);
      } catch (NoSuchMethodException var1) {
         throw new AssertionError(var1);
      }
   }

   protected class WLSEjbInvoker extends WLSInstanceResolver.WLSInvoker {
      private Map<Method, Method> businessMethods = new HashMap();
      private Map<Method, Method> preInvokeMethods = new HashMap();

      WLSEjbInvoker(Class var2) throws NoSuchMethodException, NamingException, ClassNotFoundException {
         super();
         WebServiceProvider var3 = (WebServiceProvider)var2.getAnnotation(WebServiceProvider.class);
         if (var3 != null) {
            this.webServiceProviderMapping(var2);
         } else {
            WebService var4 = (WebService)var2.getAnnotation(WebService.class);
            Class var5 = null;
            if (var4 != null) {
               String var6 = var4.endpointInterface();
               if (!StringUtil.isEmpty(var6)) {
                  var5 = ClassUtil.loadClass(var6);
               }
            }

            this.webServiceMapping(var2, var5);
         }

      }

      private void mappingMethods(Method var1, Method var2) throws NoSuchMethodException {
         Class var3 = ((BaseWSObjectIntf)WLSEjbInstanceResolver.this.resolve((Packet)null)).getClass();
         String var4 = MethodUtils.getWSOBusinessMethodName(var2);
         Method var5 = this.findMethod(var3, var4, var2.getParameterTypes());
         this.businessMethods.put(var1, var5);
         var4 = MethodUtils.getWSOPreInvokeMethodName(var2);
         var5 = this.findMethod(var3, var4, WsEjb.getPreinvokeParams(var2.getParameterTypes()));
         this.preInvokeMethods.put(var1, var5);
      }

      private void webServiceMapping(Class var1, Class var2) throws NoSuchMethodException {
         List var3 = DeployUtil.getWebServiceMethods(var1, var2);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Method var5 = (Method)var4.next();
            Method var6 = this.findMethod(var1, var5.getName(), var5.getParameterTypes());
            this.mappingMethods(var6, var5);
         }

      }

      private void webServiceProviderMapping(Class var1) throws NoSuchMethodException {
         Class var2 = WLSEjbInstanceResolver.getParameterizedType(var1);
         if (var2 == null) {
            throw new WebServiceException(var1.getName() + " should implmenent bounded javax.ws.xml.Provider when annotated with @WebServiceProvider");
         } else {
            Method var3 = var1.getMethod("invoke", var2);
            this.mappingMethods(WLSEjbInstanceResolver.providerInvokeMethod, var3);
         }
      }

      private Method findMethod(Class var1, String var2, Class[] var3) throws NoSuchMethodException {
         return var1.getMethod(var2, var3);
      }

      Object invoke(Object var1, Method var2, Object... var3) throws InvocationTargetException, IllegalAccessException {
         WsEjb var4 = null;

         try {
            var4 = new WsEjb(WLSEjbInstanceResolver.this.wsObjectFactory);
         } catch (NoSuchMethodException var7) {
            throw new AssertionError("Unable to load WsEjb");
         }

         Method var5 = (Method)this.preInvokeMethods.get(var2);
         if (var5 == null) {
            throw new WebServiceException("Method [" + var2 + "] is not a EJB business method");
         } else {
            var4.preInvoke(var5, (Object)null, (WsSecurityContextHandler)null);

            try {
               return var4.invoke((Method)this.businessMethods.get(var2), var3);
            } catch (InvocationTargetException var8) {
               if (var4 != null) {
                  var4.postInvoke();
               }

               throw var8;
            } catch (Throwable var9) {
               if (var4 != null) {
                  var4.postInvoke();
               }

               throw new InvocationTargetException(var9, "Error invoking JAXWS ejb.");
            }
         }
      }
   }
}
