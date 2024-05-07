package weblogic.wsee.jaxrpc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.util.Verbose;
import weblogic.xml.schema.binding.util.ClassUtil;

public final class ServiceFactoryImpl extends ServiceFactory {
   private static final boolean verbose = Verbose.isVerbose(ServiceFactoryImpl.class);

   public ServiceFactoryImpl() {
      if (verbose) {
         Verbose.log((Object)"JAX-RPC Service factory created");
      }

   }

   public Service createService(URL var1, QName var2) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("wsdlLocation can't be null.");
      } else {
         ClientDeployInfo var3 = new ClientDeployInfo();
         var3.setServiceName(var2);
         return new ServiceImpl(var3, var1.toString());
      }
   }

   public Service createService(QName var1) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("serviceName can't be null.");
      } else {
         ClientDeployInfo var2 = new ClientDeployInfo();
         var2.setServiceName(var1);
         return new ServiceImpl(var2, (String)null);
      }
   }

   public Service createService(URL var1, QName var2, InputStream var3) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("wsdlLocation can't be null.");
      } else {
         ClientDeployInfo var4 = new ClientDeployInfo();
         var4.setMappingdd(this.loadJaxRpcMappingFile(var3));
         var4.setServiceName(var2);
         return new ServiceImpl(var4, var1.toString());
      }
   }

   private JavaWsdlMappingBean loadJaxRpcMappingFile(InputStream var1) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("jaxRpcMappingStream can't be null.");
      } else {
         DescriptorManager var2 = new DescriptorManager();

         try {
            return (JavaWsdlMappingBean)var2.createDescriptor(var1).getRootBean();
         } catch (IOException var4) {
            throw new ServiceException("Failed to load JAX-RPC mapping file", var4);
         }
      }
   }

   public Service loadService(Class var1) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("Service interface can not be null");
      } else {
         String var2 = this.implClassName(var1);
         if (verbose) {
            Verbose.log((Object)("Loading service implementation: " + var2));
         }

         try {
            Service var3 = (Service)ClassUtil.newInstance(var2);
            return var3;
         } catch (ClassUtil.ClassUtilException var4) {
            throw new ServiceException("Failed to load service implementation class:" + var2, var4);
         }
      }
   }

   private String implClassName(Class var1) {
      return var1.getName() + "_Impl";
   }

   public Service loadService(URL var1, Class var2, Properties var3) throws ServiceException {
      if (var1 == null) {
         throw new IllegalArgumentException("WSDL cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Service interface can not be null");
      } else {
         String var4 = this.implClassName(var2);
         Class var5 = this.loadServiceClass(var4);
         return this.newService(var5, var1);
      }
   }

   private Service newService(Class var1, URL var2) throws ServiceException {
      try {
         Class[] var3 = new Class[]{String.class};
         Constructor var4 = var1.getConstructor(var3);
         Service var5 = (Service)var4.newInstance(var2.toString());
         return var5;
      } catch (NoSuchMethodException var6) {
         throw new ServiceException(var6);
      } catch (SecurityException var7) {
         throw new ServiceException(var7);
      } catch (InstantiationException var8) {
         throw new ServiceException(var8);
      } catch (IllegalAccessException var9) {
         throw new ServiceException(var9);
      } catch (InvocationTargetException var10) {
         throw new ServiceException(var10);
      }
   }

   private Class loadServiceClass(String var1) throws ServiceException {
      try {
         Class var2 = ClassUtil.loadClass(var1);
         return var2;
      } catch (ClassUtil.ClassUtilException var4) {
         throw new ServiceException("Failed to load impl class:" + var1, var4);
      }
   }

   public Service loadService(URL var1, QName var2, Properties var3) throws ServiceException {
      throw new Error("NYI");
   }
}
