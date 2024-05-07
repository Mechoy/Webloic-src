package weblogic.j2ee.dd.xml.validator.listener;

import java.util.EventListener;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidator;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.management.DeploymentException;
import weblogic.servlet.WebLogicServletContextListener;
import weblogic.utils.ErrorCollectionException;

public class ListenerBeanValidator implements AnnotationValidator {
   private static final Class[] LISTENER_CLASSES = new Class[]{ServletContextListener.class, ServletContextAttributeListener.class, ServletRequestListener.class, ServletRequestAttributeListener.class, HttpSessionListener.class, HttpSessionBindingListener.class, HttpSessionAttributeListener.class, HttpSessionActivationListener.class, WebLogicServletContextListener.class};

   public void validate(DescriptorBean var1, ClassLoader var2) throws ErrorCollectionException {
      if (var1 instanceof WebAppBean) {
         ListenerBean[] var3 = ((WebAppBean)var1).getListeners();
         if (var3 != null && var3.length >= 1) {
            ErrorCollectionException var4 = new ErrorCollectionException();
            ListenerBean[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               ListenerBean var8 = var5[var7];

               try {
                  Class var9 = var2.loadClass(var8.getListenerClass());
                  if (!isListener(var9)) {
                     var4.add(new DeploymentException("User defined class " + var8.getListenerClass() + " is not a Listener, as it" + " does not implement the correct interface(s)."));
                  }
               } catch (ClassNotFoundException var10) {
                  var4.add(var10);
               }
            }

            if (var4.size() != 0) {
               throw var4;
            }
         }
      }
   }

   private static boolean isListener(Class var0) {
      if (!EventListener.class.isAssignableFrom(var0)) {
         return false;
      } else {
         Class[] var1 = LISTENER_CLASSES;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Class var4 = var1[var3];
            if (var4.isAssignableFrom(var0)) {
               return true;
            }
         }

         return false;
      }
   }
}
