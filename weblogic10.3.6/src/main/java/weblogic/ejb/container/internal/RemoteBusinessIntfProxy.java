package weblogic.ejb.container.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.util.Map;
import javax.ejb.EJBException;
import weblogic.application.AppClassLoaderManager;
import weblogic.ejb.container.ejbc.bytecodegen.RemoteBusIntfClassAdapter;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.spi.BusinessObject;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.SoftHashMap;

public class RemoteBusinessIntfProxy implements InvocationHandler, Serializable {
   private Object ejbObject;
   private String applicationName;
   private String remoteBusinessInterfaceName;
   private String generatedRemoteInterfaceName;
   private boolean newProxy = false;
   /** @deprecated */
   @Deprecated
   private Map methodsCache = null;
   private static final long serialVersionUID = 4118555029445466816L;
   private static boolean debug = Boolean.getBoolean("weblogic.ejb.enhancement.debug");

   public RemoteBusinessIntfProxy(Object var1, String var2, String var3, String var4) {
      this.ejbObject = var1;
      this.applicationName = var2;
      this.remoteBusinessInterfaceName = var3;
      this.generatedRemoteInterfaceName = var4;
      this.newProxy = true;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      Throwable var5;
      try {
         Object var4 = this.getTargetObject(var2);
         var5 = null;
         Method var9;
         if (this.newProxy) {
            var9 = var4.getClass().getMethod(var2.getName(), var2.getParameterTypes());
         } else {
            var9 = this.getTargetMethod(var2, var4);
         }

         if (var9.getName().equals("equals") && var9.getParameterTypes().length == 1 && var9.getParameterTypes()[0].equals(Object.class)) {
            if (!Proxy.isProxyClass(var3[0].getClass())) {
               return Boolean.FALSE;
            }

            InvocationHandler var11 = Proxy.getInvocationHandler(var3[0]);
            if (!(var11 instanceof RemoteBusinessIntfProxy)) {
               return Boolean.FALSE;
            }

            var3[0] = ((RemoteBusinessIntfProxy)var11).ejbObject;
         }

         return var9.invoke(var4, var3);
      } catch (Throwable var8) {
         if (var8 instanceof InvocationTargetException) {
            var5 = ((InvocationTargetException)var8).getTargetException();
            if (var5 instanceof RemoteRuntimeException) {
               RemoteRuntimeException var10 = (RemoteRuntimeException)var5;
               if (var10.getCause() instanceof RemoteException) {
                  RemoteException var7 = (RemoteException)var10.getCause();
                  throw this.unwrapRemoteException(var7);
               } else {
                  throw var5;
               }
            } else if (var5 instanceof RemoteException) {
               RemoteException var6 = (RemoteException)var5;
               throw this.unwrapRemoteException(var6);
            } else {
               throw var5;
            }
         } else {
            throw var8;
         }
      }
   }

   private Throwable unwrapRemoteException(RemoteException var1) {
      if (var1 instanceof ServerException && var1.detail instanceof RemoteException) {
         var1 = (RemoteException)var1.detail;
      }

      if (var1.detail != null && var1.detail instanceof Exception) {
         return (Throwable)(var1.detail instanceof EJBException ? (EJBException)var1.detail : (new EJBException(var1.getMessage(), (Exception)var1.detail)).initCause(var1.detail));
      } else {
         return (new EJBException(var1.getMessage(), var1)).initCause(var1);
      }
   }

   private Object getTargetObject(Method var1) {
      return var1.getDeclaringClass() == BusinessObject.class ? PortableRemoteObject.narrow(this.ejbObject, BusinessObject.class) : this.ejbObject;
   }

   /** @deprecated */
   @Deprecated
   private Method getTargetMethod(Method var1, Object var2) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
      if (this.methodsCache == null) {
         this.methodsCache = new SoftHashMap();
      }

      Method var3 = (Method)this.methodsCache.get(var1.toGenericString());
      if (var3 == null) {
         boolean var4 = !var1.toGenericString().equals(var1.toString());
         if (!var4) {
            var3 = var2.getClass().getMethod(var1.getName(), var1.getParameterTypes());
         } else {
            Class var5 = this.getClassLoader().loadClass(this.remoteBusinessInterfaceName);
            MethodSignature var6 = new MethodSignature(var1, var5);
            Method[] var7 = var2.getClass().getMethods();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               if (var1.getTypeParameters().length > 0) {
                  var3 = var2.getClass().getMethod(var1.getName(), var1.getParameterTypes());
               } else {
                  MethodSignature var9 = new MethodSignature(var7[var8], var2.getClass());
                  if (MethodSignature.equalsMethodsBySig(var6, var9)) {
                     var3 = var7[var8];
                     break;
                  }
               }
            }

            if (var3 == null) {
               throw new AssertionError("Can not find generic method " + var6 + " in EJB Object");
            }
         }

         this.methodsCache.put(var1.toGenericString(), var3);
      }

      return var3;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this.applicationName);
      var1.writeObject(this.generatedRemoteInterfaceName);
      var1.writeObject(this.remoteBusinessInterfaceName);
      var1.writeObject(this.ejbObject);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.applicationName = (String)var1.readObject();
      this.generatedRemoteInterfaceName = (String)var1.readObject();
      if (this.generatedRemoteInterfaceName.endsWith("RIntf")) {
         this.newProxy = true;
      }

      this.remoteBusinessInterfaceName = (String)var1.readObject();
      ClassLoader var2 = this.getClassLoader();
      Class var3 = null;
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      if (!KernelStatus.isThinIIOPClient()) {
         GenericClassLoader var5 = AugmentableClassLoaderManager.getAugmentableClassLoader(var2);

         try {
            var3 = var5.loadClass(this.generatedRemoteInterfaceName);
            this.debug("can load generated Remote interface: " + this.generatedRemoteInterfaceName + "\n\t on ClassLoader: " + var5);
         } catch (ClassNotFoundException var25) {
            this.debug("can't load generated Remote interface: " + this.generatedRemoteInterfaceName + "\n\t on ClassLoader: " + var5 + ", try to enhance");
            Class var7 = var5.loadClass(this.remoteBusinessInterfaceName);
            if (this.newProxy) {
               String var28 = this.generatedRemoteInterfaceName.replace('.', '/');
               byte[] var9 = RemoteBusIntfClassAdapter.getRBIBytes(var7, var28);
               var3 = var5.defineCodeGenClass(this.generatedRemoteInterfaceName, var9, (URL)null);
            } else {
               RemoteBusinessIntfGenerator var8 = new RemoteBusinessIntfGenerator(this.generatedRemoteInterfaceName, var7, var5);
               var3 = var8.generateRemoteInterface();
            }

            this.debug("generated Remote interface: " + var3);
         }

         Thread.currentThread().setContextClassLoader(var5);

         try {
            Object var6 = var1.readObject();
            this.ejbObject = PortableRemoteObject.narrow(var6, var3);
         } finally {
            Thread.currentThread().setContextClassLoader(var4);
         }
      } else {
         try {
            var3 = var2.loadClass(this.generatedRemoteInterfaceName);
            this.debug("can load generated Remote interface: " + this.generatedRemoteInterfaceName + "\n\t on ClassLoader: " + var2);
         } catch (ClassNotFoundException var23) {
            this.debug("can't load generated Remote interface: " + this.generatedRemoteInterfaceName + "\n\t on ClassLoader: " + var2 + ", try to load it after reading the stub from the stream.");
         }

         Thread.currentThread().setContextClassLoader(var2);

         try {
            Object var26 = var1.readObject();
            if (var3 == null) {
               ClassLoader var27 = var26.getClass().getClassLoader();
               this.debug("trying to load generated Remote interface: " + this.generatedRemoteInterfaceName + "\n\t on ClassLoader: " + var27);
               var3 = Class.forName(this.generatedRemoteInterfaceName, false, var27);
            }

            this.ejbObject = PortableRemoteObject.narrow(var26, var3);
         } finally {
            Thread.currentThread().setContextClassLoader(var4);
         }
      }

   }

   private ClassLoader getClassLoader() {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      if (Kernel.isApplet()) {
         return var1;
      } else {
         if (this.applicationName != null && KernelStatus.isServer()) {
            AppClassLoaderManager var2 = AppClassLoaderManager.getAppClassLoaderManager();
            Annotation var3 = new Annotation(this.applicationName);
            GenericClassLoader var4 = var2.findLoader(var3);
            if (var4 != null) {
               for(ClassLoader var5 = var1; var5 != null; var5 = var5.getParent()) {
                  if (var5 == var4) {
                     return var1;
                  }
               }
            }

            var4 = var2.findOrCreateInterAppLoader(var3, var1);
            if (var4 != null) {
               return var4;
            }
         }

         return var1;
      }
   }

   private void debug(String var1) {
      if (debug) {
         System.out.println("[" + this.getClass().getSimpleName() + "]" + "---" + Thread.currentThread() + "---" + "\n\t" + var1);
      }

   }
}
