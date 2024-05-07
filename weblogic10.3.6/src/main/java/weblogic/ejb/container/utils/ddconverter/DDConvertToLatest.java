package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp11.rdbms.finders.WLQLtoEJBQL;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MethodParamsBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;
import weblogic.j2ee.descriptor.QueryBean;
import weblogic.j2ee.descriptor.QueryMethodBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.TableMapBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.descriptor.wl60.BaseWeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.FieldMapBean;
import weblogic.j2ee.descriptor.wl60.FinderBean;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class DDConvertToLatest extends DDConverterBase {
   private final boolean debug = false;
   private VirtualJarFile[] targetVirtualJars;
   private EjbDescriptorBean targetDesc;

   public DDConvertToLatest() {
   }

   public DDConvertToLatest(boolean var1) {
      this.convertTo20 = var1;
   }

   public DDConvertToLatest(String[] var1, String var2, ConvertLog var3, boolean var4) throws DDConverterException {
      super(var1, var2, var3, true);
      this.targetVirtualJars = new VirtualJarFile[var1.length];
      this.convertTo20 = var4;
   }

   public boolean convert() throws DDConverterException, IOException {
      for(int var1 = 0; var1 < this.sources.length; ++var1) {
         System.out.println(this.fmt.converting(this.sources[var1]));
         JarFile var2 = new JarFile(this.sources[var1]);
         EjbDescriptorBean var3 = null;

         String var4;
         try {
            var3 = EJBDescriptorMBeanUtils.createDescriptorFromJarFile(VirtualJarFactory.createVirtualJar(var2), true);
            var4 = var3.getParsingErrorMessage();
            if (var4 != null && var4.length() > 0) {
               this.log.logError(this.fmt.readDDError("ejb-jar.xml,weblogic-ejb-jar.xml or weblogic-cmp-rdbms-jar.xml", var4));
            }
         } catch (Exception var8) {
            this.log.logError(this.fmt.readDDError("ejb-jar.xml, weblogic-ejb-jar.xml", StackTraceUtils.throwable2StackTrace(var8)));
            return false;
         }

         this.convert(var3);
         var4 = (new File(var2.getName())).getName();
         File var5 = new File(this.targetDirName, var4);
         this.normalizeDescriptor(var3);
         var3.usePersistenceDestination(var5.getPath());

         try {
            var3.persist();
         } catch (Exception var7) {
            EJBLogger.logStackTrace(var7);
         }

         this.targetVirtualJars[var1] = VirtualJarFactory.createVirtualJar(new JarFile(var5));
      }

      return !this.log.hasErrors();
   }

   public boolean convert(String var1) throws DDConverterException, IOException {
      this.convert();
      this.targetDesc = new EjbDescriptorBean();
      String var2 = this.targetDirName + File.separatorChar + var1;
      this.targetDesc.usePersistenceDestination(var2);
      EjbJarBean var3 = this.targetDesc.getEjbJarBean();

      for(int var4 = 0; var4 < this.targetVirtualJars.length; ++var4) {
         JarFile var5 = new JarFile(this.sources[var4]);
         this.targetVirtualJars[var4] = VirtualJarFactory.createVirtualJar(var5);

         try {
            EjbDescriptorBean var6 = EJBDescriptorMBeanUtils.createDescriptorFromJarFile(this.targetVirtualJars[var4], true);
            String var7 = var6.getParsingErrorMessage();
            if (var7 != null && var7.trim().length() > 0) {
               throw new Exception(var7);
            }

            this.updateTargetEJBJarMBean(var3, var6);
            WeblogicEjbJarBean var8 = this.targetDesc.getWeblogicEjbJarBean();
            this.updateTargetWeblogicEJBJarMBean(var8, var6);
            WeblogicRdbmsJarBean[] var9 = var6.getWeblogicRdbmsJarBeans();
            if (var9 != null && var9.length > 0) {
               WeblogicRdbmsJarBean var10 = this.targetDesc.createWeblogicRdbmsJarBean();
               WeblogicEnterpriseBeanBean[] var11 = var8.getWeblogicEnterpriseBeans();
               this.combineRDBMSJarBeans(var10, var11, var9);
            }
         } catch (Exception var16) {
            this.log.logError(this.fmt.readDDError("ejb-jar.xml, weblogic-ejb-jar.xml", StackTraceUtils.throwable2StackTrace(var16)));
            throw new DDConverterException(var16);
         } finally {
            this.targetVirtualJars[var4].close();
         }
      }

      this.normalizeDescriptor(this.targetDesc);
      this.targetDesc.persist();
      return true;
   }

   public void convert(EjbDescriptorBean var1) throws DDConverterException {
      if (var1 != null && this.convertTo20) {
         this.convertCMPTo2x(var1);
      }

   }

   private void updateTargetEJBJarMBean(EjbJarBean var1, EjbDescriptorBean var2) {
      SessionBeanBean[] var3 = var2.getEjbJarBean().getEnterpriseBeans().getSessions();
      EntityBeanBean[] var4 = var2.getEjbJarBean().getEnterpriseBeans().getEntities();
      DescriptorBean var5 = (DescriptorBean)var1.getEnterpriseBeans();

      int var6;
      for(var6 = 0; var6 < var3.length; ++var6) {
         var5.createChildCopy("Session", (DescriptorBean)var3[var6]);
      }

      for(var6 = 0; var6 < var4.length; ++var6) {
         var5.createChildCopy("Entity", (DescriptorBean)var4[var6]);
      }

      AssemblyDescriptorBean var11 = var1.getAssemblyDescriptor();
      if (var11 == null) {
         var11 = var2.getEjbJarBean().getAssemblyDescriptor();
         var5 = (DescriptorBean)var1;
         var5.createChildCopy("AssemblyDescriptor", (DescriptorBean)var11);
      } else {
         SecurityRoleBean[] var7 = var2.getEjbJarBean().getAssemblyDescriptor().getSecurityRoles();
         var5 = (DescriptorBean)var11;

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var5.createChildCopy("SecurityRole", (DescriptorBean)var7[var8]);
         }

         MethodPermissionBean[] var12 = var2.getEjbJarBean().getAssemblyDescriptor().getMethodPermissions();

         for(int var9 = 0; var9 < var12.length; ++var9) {
            var5.createChildCopy("MethodPermission", (DescriptorBean)var12[var9]);
         }

         ContainerTransactionBean[] var13 = var2.getEjbJarBean().getAssemblyDescriptor().getContainerTransactions();

         for(int var10 = 0; var10 < var13.length; ++var10) {
            var5.createChildCopy("ContainerTransaction", (DescriptorBean)var13[var10]);
         }
      }

   }

   private void updateTargetWeblogicEJBJarMBean(WeblogicEjbJarBean var1, EjbDescriptorBean var2) {
      if (var1 == null) {
         var1 = var2.getWeblogicEjbJarBean();
         this.targetDesc.setWeblogicEjbJarBean(var1);
      } else {
         WeblogicEnterpriseBeanBean[] var3 = var2.getWeblogicEjbJarBean().getWeblogicEnterpriseBeans();
         DescriptorBean var4 = (DescriptorBean)var1;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4.createChildCopy("WeblogicEnterpriseBean", (DescriptorBean)var3[var5]);
         }

         SecurityRoleAssignmentBean[] var8 = var2.getWeblogicEjbJarBean().getSecurityRoleAssignments();

         for(int var6 = 0; var6 < var8.length; ++var6) {
            var4.createChildCopy("SecurityRoleAssignment", (DescriptorBean)var8[var6]);
         }

         TransactionIsolationBean[] var9 = var2.getWeblogicEjbJarBean().getTransactionIsolations();

         for(int var7 = 0; var7 < var9.length; ++var7) {
            var4.createChildCopy("TransactionIsolation", (DescriptorBean)var9[var7]);
         }
      }

   }

   private void combineRDBMSJarBeans(WeblogicRdbmsJarBean var1, WeblogicEnterpriseBeanBean[] var2, WeblogicRdbmsJarBean[] var3) {
      DescriptorBean var4 = (DescriptorBean)var1;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         WeblogicRdbmsBeanBean[] var6 = var3[var5].getWeblogicRdbmsBeans();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var4.createChildCopy("WeblogicRdbmsBean", (DescriptorBean)var6[var7]);
         }
      }

   }

   private void convertCMPTo2x(EjbDescriptorBean var1) throws DDConverterException {
      this.convertCMPBeansTo2x(var1);
      this.convertRDBMSDescriptorsTo2x(var1);
   }

   private void convertCMPBeansTo2x(EjbDescriptorBean var1) throws DDConverterException {
      EntityBeanBean[] var2 = var1.getEjbJarBean().getEnterpriseBeans().getEntities();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         EntityBeanBean var4 = var2[var3];
         CompositeMBeanDescriptor var5 = null;

         try {
            var5 = new CompositeMBeanDescriptor(var4, var1);
         } catch (WLDeploymentException var7) {
            throw new DDConverterException(var7);
         }

         if (var4.getPersistenceType().equalsIgnoreCase("Container")) {
            if (var4.getAbstractSchemaName() == null) {
               var4.setAbstractSchemaName(makeAbstractSchemaName(var4.getEjbName()));
               var4.setCmpVersion("2.x");
            }

            this.convertWLQLToEJBQL(var5);
            this.convertWLBean(var5);
         }
      }

   }

   private void convertWLBean(CompositeMBeanDescriptor var1) {
      WeblogicEnterpriseBeanBean var2 = var1.getWlBean();
      if (this.convertTo20) {
         PersistenceBean var3 = var2.getEntityDescriptor().getPersistence();
         if (var3 != null) {
            PersistenceUseBean var4 = var3.getPersistenceUse();
            if (var4 != null) {
               var4.setTypeVersion("7.0");
            }
         }
      }

   }

   private void convertWLQLToEJBQL(CompositeMBeanDescriptor var1) {
      EntityBeanBean var2 = (EntityBeanBean)var1.getBean();
      BaseWeblogicRdbmsBeanBean var3 = var1.getRDBMSBean();
      if (var3 instanceof weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean) {
         weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean var4 = (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean)var3;
         FinderBean[] var5 = var4.getFinders();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            FinderBean var7 = var5[var6];
            QueryBean var8 = var2.createQuery();
            QueryMethodBean var9 = var8.createQueryMethod();
            MethodParamsBean var10 = var9.createMethodParams();
            var9.setMethodName(var7.getFinderName());

            for(int var11 = 0; var11 < var7.getFinderParams().length; ++var11) {
               var10.addMethodParam(var7.getFinderParams()[var11]);
            }

            String var14 = var7.getFinderQuery();

            try {
               String var12 = WLQLtoEJBQL.doWLQLtoEJBQL(var14, var2.getAbstractSchemaName());
               Debug.say("@@@ EJB-QL:" + var12);
               var8.setEjbQl(var12);
            } catch (RDBMSException var13) {
               EJBLogger.logStackTrace(var13);
            }
         }

      }
   }

   private void convertRDBMSDescriptorsTo2x(EjbDescriptorBean var1) {
      weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[] var2 = var1.getWeblogicRdbms11JarBeans();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         WeblogicRdbmsJarBean var4 = var1.createWeblogicRdbmsJarBean();
         weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean[] var5 = var2[var3].getWeblogicRdbmsBeans();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean var7 = (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean)var5[var6];
            WeblogicRdbmsBeanBean var8 = var4.createWeblogicRdbmsBean();
            var8.setEjbName(var7.getEjbName());
            var8.setDataSourceJndiName(var7.getPoolName());
            TableMapBean var9 = var8.createTableMap();
            var9.setTableName(var7.getTableName());
            FieldMapBean[] var10 = var7.getFieldMaps();

            for(int var11 = 0; var11 < var10.length; ++var11) {
               weblogic.j2ee.descriptor.wl.FieldMapBean var12 = var9.createFieldMap();
               var12.setCmpField(var10[var11].getCmpField());
               var12.setDbmsColumn(var10[var11].getDbmsColumn());
            }
         }

         var1.addWeblogicRdbmsJarBean(var4);
         var1.removeWeblogicRdbms11JarBean(var2[var3]);
      }

   }
}
