package weblogic.ejb.container.ejbc.bytecodegen;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.utils.FileUtils;

public final class GeneratorFactory {
   private GeneratorFactory() {
   }

   public static void generate(MessageDrivenBeanInfo var0, NamingConvention var1, String var2) throws EJBCException {
      if (!var0.getIsWeblogicJMS()) {
         write(var2, (new MDOImplGenerator(var1, var0)).generate());
      } else if (var0.isIndirectlyImplMessageListener()) {
         write(var2, (new MessageDrivenBeanImplGenerator(var1, var0)).generate());
      }

   }

   public static void generate(InterceptorBean var0, NamingConvention var1, String var2) throws EJBCException {
      SerializableIcptrGenerator var3 = new SerializableIcptrGenerator(var1.getInterceptorImplClassName(), var0.getInterceptorClass());
      write(var2, var3.generate());
   }

   public static void generate(SessionBeanInfo var0, NamingConvention var1, String var2) throws EJBCException {
      write(var2, (new BeanIntfGenerator(var1, var0)).generate());
      write(var2, (new BeanImplGenerator(var1, var0)).generate());
      Set var3;
      Iterator var4;
      Class var5;
      if (var0.hasRemoteClientView()) {
         if (var0.hasDeclaredRemoteHome()) {
            write(var2, (new HomeImplGenerator(var1, var0)).generate());
            write(var2, (new EOImplGenerator(var1, var0)).generate());
         }

         var3 = var0.getBusinessRemotes();

         for(var4 = var3.iterator(); var4.hasNext(); write(var2, (new RemoteBusImplGenerator(var1, var0, var5)).generate())) {
            var5 = (Class)var4.next();
            if (!Remote.class.isAssignableFrom(var5)) {
               write(var2, (new RemoteBusIntfGenerator(var1.getRemoteBusinessIntfClassName(var5), var5)).generate());
            }
         }
      }

      if (var0.hasLocalClientView()) {
         if (var0.hasDeclaredLocalHome()) {
            write(var2, (new LocalHomeImplGenerator(var1, var0)).generate());
            write(var2, (new ELOImplGenerator(var1, var0)).generate());
         }

         var3 = var0.getBusinessLocals();
         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (Class)var4.next();
            write(var2, (new LocalBusImplGenerator(var1, var0, var5)).generate());
         }
      }

      if (var0.hasWebserviceClientView()) {
         write(var2, (new WSOImplGenerator(var1, var0)).generate());
      }

   }

   private static void write(String var0, Generator.Output var1) throws EJBCException {
      try {
         FileUtils.writeToFile(var1.bytes(), var0 + File.separator + var1.relativeFilePath());
      } catch (IOException var3) {
         throw new EJBCException("Error writing bytes to class file.", var3);
      }
   }
}
