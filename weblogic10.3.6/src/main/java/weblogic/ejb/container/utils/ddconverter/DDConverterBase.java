package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;

public abstract class DDConverterBase {
   private static final boolean debug = true;
   protected static String DDCONVERTER_WORKING_DIR;
   protected String[] sources;
   protected boolean combineBeans = false;
   protected String targetDirName = ".";
   protected ConvertLog log;
   protected EJBddcTextFormatter fmt;
   private boolean isCMP20beanFlag = true;
   protected boolean convertTo20 = true;

   public DDConverterBase() {
   }

   public DDConverterBase(String[] var1, String var2, ConvertLog var3, boolean var4) throws DDConverterException {
      this.isCMP20beanFlag = var4;
      this.sources = var1;
      if (var2 != null) {
         this.targetDirName = var2;
      }

      this.log = var3;
      this.fmt = new EJBddcTextFormatter();
   }

   public boolean isCMP20() {
      return this.isCMP20beanFlag;
   }

   public abstract boolean convert(String var1) throws DDConverterException, IOException;

   public abstract boolean convert() throws DDConverterException, IOException;

   protected void normalizeDescriptor(EjbDescriptorBean var1) throws DDConverterException {
      EnterpriseBeanBean[] var2 = CompositeMBeanDescriptor.getEnterpriseBeans(var1.getEjbJarBean());

      for(int var3 = 0; var3 < var2.length; ++var3) {
         CompositeMBeanDescriptor var4 = null;

         try {
            var4 = new CompositeMBeanDescriptor(var2[var3], var1);
         } catch (WLDeploymentException var12) {
            throw new DDConverterException(var12);
         }

         WeblogicEnterpriseBeanBean var5 = var4.getWlBean();
         if (var4.isEntity()) {
            EntityBeanBean var6 = (EntityBeanBean)var4.getBean();
            if ("bean".equalsIgnoreCase(var6.getPersistenceType())) {
               PersistenceBean var7 = var5.getEntityDescriptor().getPersistence();
               if (null != var5.getEntityDescriptor()) {
                  PersistenceBean var8 = var5.getEntityDescriptor().getPersistence();
                  if (var8 != null) {
                     String var9 = var8.getIsModifiedMethodName();
                     boolean var10 = var8.isDelayUpdatesUntilEndOfTx();
                     var5.getEntityDescriptor().destroyPersistence(var8);
                     PersistenceBean var11 = var5.getEntityDescriptor().createPersistence();
                     if (var9 != null) {
                        var11.setIsModifiedMethodName(var9);
                     }

                     var11.setDelayUpdatesUntilEndOfTx(var10);
                  }
               }
            }
         } else if (!var4.isStatefulSession() && var4.isStatelessSession()) {
         }
      }

   }

   protected WeblogicRdbmsJarBean getWeblogicRdbms11JarBean(EjbDescriptorBean var1) {
      WeblogicRdbmsJarBean[] var2 = var1.getWeblogicRdbms11JarBeans();
      WeblogicRdbmsJarBean var3;
      if (null != var2 && var2.length != 0) {
         var3 = var2[0];
      } else {
         var3 = var1.createWeblogicRdbms11JarBean();
      }

      return var3;
   }

   protected void writeEJBDDsToJar(EjbDescriptorBean var1, WorkingJar var2) throws DDConverterException, IOException {
      String var3 = this.targetDirName + File.separatorChar + var2.getFile().getName();
      this.normalizeDescriptor(var1);
      var1.usePersistenceDestination(var3);
      var1.persist();
   }

   public static String makeAbstractSchemaName(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",./");
      return var1.nextToken();
   }
}
