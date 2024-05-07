package weblogic.deployment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.Service;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;

public class ServiceRefProcessorFactory {
   private static final ServiceRefProcessorFactory instance = new ServiceRefProcessorFactory();
   private static final String JAXRPC_PROCESSOR = "weblogic.wsee.jaxrpc.ServiceRefProcessorImpl";
   private static final String JAXWS_PROCESSOR = "weblogic.wsee.jaxws.ServiceRefProcessorImpl";

   private ServiceRefProcessorFactory() {
   }

   public static ServiceRefProcessorFactory getInstance() {
      return instance;
   }

   public ServiceRefProcessor getProcessor(ServiceRefBean var1, ServiceReferenceDescriptionBean var2, ServletContext var3) throws ServiceRefProcessorException {
      String var4 = "weblogic.wsee.jaxrpc.ServiceRefProcessorImpl";
      String var5 = var1.getServiceInterface();

      try {
         Class var6 = Thread.currentThread().getContextClassLoader().loadClass(var5);
         if (Service.class.isAssignableFrom(var6) || var6.isAnnotationPresent(WebService.class)) {
            var4 = "weblogic.wsee.jaxws.ServiceRefProcessorImpl";
         }
      } catch (ClassNotFoundException var7) {
         throw new ServiceRefProcessorException("Unable to load specified service-interface: " + var5, var7);
      }

      return this.constructProcessor(var4, var1, var2, var3);
   }

   private ServiceRefProcessor constructProcessor(String var1, ServiceRefBean var2, ServiceReferenceDescriptionBean var3, ServletContext var4) throws ServiceRefProcessorException {
      try {
         Class var5 = Class.forName(var1);
         Constructor var9 = var5.getDeclaredConstructor(ServiceRefBean.class, ServiceReferenceDescriptionBean.class, ServletContext.class);
         return (ServiceRefProcessor)var9.newInstance(var2, var3, var4);
      } catch (InvocationTargetException var7) {
         Throwable var6 = var7.getTargetException();
         throw new ServiceRefProcessorException(var6.getMessage(), var6);
      } catch (Exception var8) {
         throw new ServiceRefProcessorException("Error constructing ServiceRefProcessor: " + var1, var8);
      }
   }
}
