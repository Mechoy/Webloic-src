package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class DDConverter_1011 extends DDConverterBase {
   private static final boolean debug = true;
   private EjbDescriptorBean dd;
   DDConverter_10 ddc10 = null;

   public DDConverter_1011(String[] var1, String var2, ConvertLog var3) throws DDConverterException {
      super(var1, var2, var3, false);
      this.ddc10 = new DDConverter_10(var3, false);
   }

   public boolean convert() throws DDConverterException, IOException {
      for(int var1 = 0; var1 < this.sources.length; ++var1) {
         EjbDescriptorBean var2 = null;
         System.out.println(this.fmt.converting(this.sources[var1]));

         try {
            var2 = this.loadDD(this.sources[var1]);
         } catch (Exception var11) {
            this.log.logError(this.fmt.readDDError(this.sources[var1], StackTraceUtils.throwable2StackTrace(var11)));
         }

         new ArrayList();
         EnterpriseBeanBean[] var4 = CompositeMBeanDescriptor.getEnterpriseBeans(var2.getEjbJarBean());

         for(int var5 = 0; var5 < var4.length; ++var5) {
            EnterpriseBeanBean var6 = var4[var5];
            if (var6 instanceof EntityBeanBean) {
               EntityBeanBean var7 = (EntityBeanBean)var6;
               CompositeMBeanDescriptor var8 = null;

               try {
                  var8 = new CompositeMBeanDescriptor(var7, var2);
               } catch (WLDeploymentException var10) {
                  throw new DDConverterException(var10);
               }

               WeblogicEnterpriseBeanBean var9 = var8.getWlBean();
               if ("Container".equalsIgnoreCase(var7.getPersistenceType())) {
                  var7.setCmpVersion("1.x");
               }
            }
         }

         File var12 = new File(this.targetDirName, var2.getJarFileName());
         this.normalizeDescriptor(var2);
         var2.usePersistenceDestination(var12.getPath());
         var2.persist();
      }

      return !this.log.hasErrors();
   }

   public boolean convert(String var1) throws DDConverterException, IOException {
      Debug.assertion(var1 != null);
      this.dd = new EjbDescriptorBean();

      for(int var2 = 0; var2 < this.sources.length; ++var2) {
         try {
            this.dd = this.loadDD(this.dd, this.sources[var2]);
         } catch (Exception var9) {
            this.log.logError(this.fmt.readDDError(this.sources[var2], StackTraceUtils.throwable2StackTrace(var9)));
         }
      }

      File var10 = new File(this.targetDirName, var1);
      WorkingJar var3 = new WorkingJar(var10);
      EnterpriseBeanBean[] var4 = CompositeMBeanDescriptor.getEnterpriseBeans(this.dd.getEjbJarBean());
      new ArrayList();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         EnterpriseBeanBean var7 = var4[var6];
         if (var7 instanceof EntityBeanBean) {
            EntityBeanBean var8 = (EntityBeanBean)var7;
            if ("Container".equalsIgnoreCase(var8.getPersistenceType())) {
            }
         }
      }

      this.writeEJBDDsToJar(this.dd, var3);
      var3.close();
      return !this.log.hasErrors();
   }

   protected EjbDescriptorBean loadDD(EjbDescriptorBean var1, String var2) throws IOException, XMLProcessingException, XMLParsingException {
      this.dd = this.ddc10.invokeDDC10(var1, var2);
      String var3 = (new File(var2)).getName();
      String var4 = var3.substring(0, var3.length() - 3) + "jar";
      this.dd.setJarFileName(var4);
      return this.dd;
   }

   protected EjbDescriptorBean loadDD(String var1) throws IOException, XMLProcessingException, XMLParsingException {
      EjbDescriptorBean var2 = new EjbDescriptorBean();
      var2.createEjbJarBean();
      var2.createWeblogicEjbJarBean();
      var2.getEjbJarBean().createEnterpriseBeans();
      return this.loadDD(var2, var1);
   }
}
