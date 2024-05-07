package weblogic.ejb.container.swap;

import com.oracle.pitchfork.interfaces.EnterpriseBeanProxy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJBContext;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEJBException;
import weblogic.common.internal.ReplacerObjectInputStream;
import weblogic.common.internal.ReplacerObjectOutputStream;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLEJBContext;
import weblogic.ejb.container.interfaces.WLSessionEJBContext;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.protocol.ServerChannelManager;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.ClassLoaderResolver;
import weblogic.utils.io.Resolver;

final class PassivationUtils {
   private Resolver resolver;

   PassivationUtils(ClassLoader var1) {
      this.resolver = new ClassLoaderResolver(var1);
   }

   EnterpriseBean read(BeanManager var1, InputStream var2, Object var3) throws InternalException {
      try {
         EJBReplacer var4 = new EJBReplacer();
         EJBContext var5 = var1.allocateContext((EnterpriseBean)null, var3);
         var4.setContext(var5);
         ReplacerObjectInputStream var6 = new ReplacerObjectInputStream(var2, var4, this.resolver);
         Object var7 = var6.readObject();
         var6.close();
         BeanInfo var8 = var1.getBeanInfo();
         var7 = ((StatefulSessionManager)var1).assembleEJB3Proxy(var7, var8);
         if (!(var7 instanceof EnterpriseBean)) {
            throw new AssertionError("Deserialized stateful session bean with key: " + var3 + " but class was" + var7.getClass().getName());
         } else {
            ((WLEJBContext)var5).setBean((EnterpriseBean)var7);
            if (var8 instanceof Ejb3SessionBeanInfo && var1 instanceof StatefulSessionManager) {
               ((WLSessionEJBContext)var5).setPrimaryKey(var3);
            }

            return (EnterpriseBean)var7;
         }
      } catch (IOException var9) {
         EJBRuntimeUtils.throwInternalException("Error during read.", new NoSuchEJBException("Activation failed with: " + StackTraceUtils.throwable2StackTrace(var9)));
         throw new AssertionError("Should not reach.");
      } catch (ClassNotFoundException var10) {
         EJBRuntimeUtils.throwInternalException("Error during read.", new NoSuchEJBException("Activation failed with: " + StackTraceUtils.throwable2StackTrace(var10)));
         throw new AssertionError("Should not reach.");
      }
   }

   void write(BeanManager var1, OutputStream var2, Object var3, Object var4) throws InternalException {
      try {
         SessionBeanInfo var5 = (SessionBeanInfo)var1.getBeanInfo();
         if (var5 instanceof Ejb3SessionBeanInfo) {
            var4 = ((EnterpriseBeanProxy)var4).getTarget();
         }

         EJBReplacer var6 = new EJBReplacer();
         ReplacerObjectOutputStream var7 = new ReplacerObjectOutputStream(var2, var6);
         var7.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
         var7.writeObject(var4);
      } catch (IOException var8) {
         EJBRuntimeUtils.throwInternalException("Error during write.", var8);
      }

   }

   public void updateClassLoader(ClassLoader var1) {
      this.resolver = new ClassLoaderResolver(var1);
   }
}
