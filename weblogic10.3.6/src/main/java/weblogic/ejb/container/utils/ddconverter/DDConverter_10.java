package weblogic.ejb.container.utils.ddconverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp11.rdbms.finders.WLQLtoEJBQL;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.CmpFieldBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.MethodParamsBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;
import weblogic.j2ee.descriptor.QueryBean;
import weblogic.j2ee.descriptor.QueryMethodBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.FieldMapBean;
import weblogic.j2ee.descriptor.wl.IdempotentMethodsBean;
import weblogic.j2ee.descriptor.wl.MethodBean;
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
import weblogic.j2ee.descriptor.wl60.FinderBean;
import weblogic.utils.Debug;

public final class DDConverter_10 {
   protected static boolean verbose = false;
   protected static boolean debug = false;
   private String oldDDFile = null;
   protected EjbDescriptorBean desc = null;
   private String ejbName = null;
   protected EJB10DescriptorStructure oldDDStruct = null;
   private Hashtable oldEnvProps = new Hashtable();
   private Hashtable oldControlDescriptors = new Hashtable();
   private Hashtable oldPStoreProps = new Hashtable();
   private Hashtable oldAccessControlEntries = new Hashtable();
   protected Hashtable oldMethodDescriptors = new Hashtable();
   private String beanType = new String();
   protected EJBddcTextFormatter fmt;
   protected ConvertLog log;
   Collection m_rdbmsBeans = new ArrayList();
   boolean isCMP20beanFlag = true;
   private StreamTokenizer st = null;
   private int nestingLevel = 0;
   private boolean eofOK = false;
   private String currFile = null;

   public DDConverter_10(ConvertLog var1, boolean var2) {
      this.log = var1;
      this.isCMP20beanFlag = var2;
      this.fmt = new EJBddcTextFormatter();
   }

   public EjbDescriptorBean invokeDDC10(EjbDescriptorBean var1, String var2) {
      this.desc = var1;

      try {
         this.parseOldDD(var2);
         this.createNewDD();
      } catch (DDConverterCollectionException var4) {
         this.log.logInfo(this.fmt.errorMessage());
         System.exit(1);
      }

      return var1;
   }

   public void parseOldDD(String var1) throws DDConverterCollectionException {
      this.oldDDFile = var1;
      if (verbose) {
         System.out.println("----- parseOldDD -----");
      }

      try {
         this.oldDDStruct = new EJB10DescriptorStructure(this.parseFile(var1));
         this.oldEnvProps = this.oldDDStruct.getEnvironmentProperties();
         this.oldControlDescriptors = this.oldDDStruct.getControlDescriptors();
         this.oldPStoreProps = this.oldDDStruct.getPersistentStoreProperties();
         this.oldAccessControlEntries = this.oldDDStruct.getAccessControlEntries();
         this.oldMethodDescriptors = this.getAllMethods(this.oldAccessControlEntries, this.oldControlDescriptors);
      } catch (FileNotFoundException var3) {
         this.log.logError(this.fmt.fileNotFound(var1));
         throw new DDConverterCollectionException(new DDConverterException(var3));
      } catch (Exception var4) {
         EJBLogger.logStackTrace(var4);
      }

   }

   public void createNewDD() throws DDConverterCollectionException {
      if (verbose) {
         System.out.println("----- createNewDD -----");
      }

      WeblogicEnterpriseBeanBean var2;
      CompositeMBeanDescriptor var3;
      if (this.oldDDStruct.name.equalsIgnoreCase("EntityDescriptor")) {
         this.beanType = "EntityDescriptor";
         EntityBeanBean var1 = this.desc.getEjbJarBean().getEnterpriseBeans().createEntity();
         var1.setEjbName(this.oldDDStruct.getBeanHomeName());
         var2 = this.desc.getWeblogicEjbJarBean().createWeblogicEnterpriseBean();
         var2.setEjbName(this.oldDDStruct.getBeanHomeName());
         var3 = null;

         try {
            var3 = new CompositeMBeanDescriptor(var1, var2, this.desc);
         } catch (WLDeploymentException var6) {
            throw new DDConverterCollectionException(new DDConverterException(var6));
         }

         this.doCommon(var3);
         this.doEntityProperties(var3);
      } else if (this.oldDDStruct.name.equalsIgnoreCase("SessionDescriptor")) {
         this.beanType = "SessionDescriptor";
         SessionBeanBean var7 = this.desc.getEjbJarBean().getEnterpriseBeans().createSession();
         if ("STATELESS_SESSION".equalsIgnoreCase(this.oldDDStruct.getStateManagementType())) {
            var7.setSessionType("Stateless");
         } else {
            var7.setSessionType("Stateful");
         }

         var2 = this.desc.getWeblogicEjbJarBean().createWeblogicEnterpriseBean();
         var7.setEjbName(this.oldDDStruct.getBeanHomeName());
         var2.setEjbName(this.oldDDStruct.getBeanHomeName());
         var3 = null;

         try {
            var3 = new CompositeMBeanDescriptor(var7, var2, this.desc);
         } catch (WLDeploymentException var5) {
            EJBLogger.logStackTrace(var5);
            throw new DDConverterCollectionException(new DDConverterException(var5));
         }

         this.doCommon(var3);
         this.doSessionProperties(var3);
      } else {
         this.log.logWarning(this.fmt.invalidDDFile());
      }

   }

   private void doCommon(CompositeMBeanDescriptor var1) {
      EnterpriseBeanBean var2 = var1.getBean();
      WeblogicEnterpriseBeanBean var3 = var1.getWlBean();
      if (verbose) {
         System.out.println("----- doCommon -----");
      }

      String var4 = this.oldDDStruct.getBeanHomeName();
      this.ejbName = var4;
      var1.setEJBName(var4);
      this.log.logWarning(this.fmt.ejbNameSetToJndi(var4));
      var1.setJNDIName(var4);
      var2.setEjbClass(this.oldDDStruct.getEnterpriseBeanClassName());
      if (var2 instanceof EntityBeanBean) {
         ((EntityBeanBean)var2).setHome(this.oldDDStruct.getHomeInterfaceClassName());
         ((EntityBeanBean)var2).setRemote(this.oldDDStruct.getRemoteInterfaceClassName());
      } else {
         ((SessionBeanBean)var2).setHome(this.oldDDStruct.getHomeInterfaceClassName());
         ((SessionBeanBean)var2).setRemote(this.oldDDStruct.getRemoteInterfaceClassName());
      }

      this.doCacheProperties(var1);
      this.doCommonClusterProperties(var1);
      this.doMethodDescriptors(var1);
      this.doRoleDescriptors(var1);
      this.doUserDefinedEnvironmentProperties(var1);
      if (this.oldDDStruct.getEjbObjectClassName() != null) {
         this.log.logWarning(this.fmt.noCustomEJBObjectClass());
      }

      if (this.oldDDStruct.getHomeClassName() != null) {
         this.log.logWarning(this.fmt.noCustomEJBHomeClass());
      }

   }

   private void doUserDefinedEnvironmentProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doUserDefinedEnvironmentProperties -----");
      }

      Hashtable var2 = new Hashtable();
      this.getUserDefinedProps(this.oldEnvProps, var2);
      if (!var2.isEmpty()) {
         Enumeration var3 = var2.keys();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            EnvEntryBean var5 = var1.createEnvironmentEntry();
            var5.setEnvEntryName(var4);
            var5.setEnvEntryType("java.lang.String");
            var5.setEnvEntryValue((String)var2.get(var4));
            this.log.logWarning(this.fmt.userPropsReadInAsString(var4));
         }
      }

   }

   private void getUserDefinedProps(Hashtable var1, Hashtable var2) {
      Hashtable var3 = EJB10DescriptorConstants.getConstantsAsHashtable();
      Enumeration var4 = var1.keys();
      String var5 = null;
      Object var6 = null;
      String var7 = null;
      Hashtable var8 = null;

      while(var4.hasMoreElements()) {
         var5 = (String)var4.nextElement();
         var6 = var1.get(var5);
         if (var6 instanceof String) {
            var7 = (String)var6;
            if (!var3.contains(var5)) {
               var2.put(var5, var7);
            }
         } else if (var6 instanceof Hashtable) {
            var8 = (Hashtable)var6;
            if (!var3.contains(var5)) {
               this.getUserDefinedProps(var8, var2);
            }
         }
      }

   }

   private void doCacheProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doCacheProperties -----");
      }

      if (this.oldDDStruct.getMaxBeansInFreePool() != null) {
         var1.setMaxBeansInFreePool(new Integer(this.oldDDStruct.getMaxBeansInFreePool()));
      }

      if (this.oldDDStruct.getMaxBeansInCache() != null) {
         var1.setMaxBeansInCache(new Integer(this.oldDDStruct.getMaxBeansInCache()));
      }

      if (this.oldDDStruct.getIdleTimeoutSeconds() != null) {
         var1.setIdleTimeoutSecondsCache(new Integer(this.oldDDStruct.getIdleTimeoutSeconds()));
      }

   }

   private void doCommonClusterProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doCommonClusterProperties -----");
      }

      String var2 = this.oldDDStruct.getHomeIsClusterable();
      if (var2 != null) {
         var1.setHomeIsClusterable(new Boolean(var2));
      }

   }

   private void doSessionClusterProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doSessionClusterProperties -----");
      }

      if (var1.isStatelessSession()) {
         var1.setStatelessBeanIsClusterable(new Boolean(this.oldDDStruct.getStatelessBeanIsClusterable()));
         if (this.oldDDStruct.getStatelessBeanLoadAlgorithm() != null) {
            var1.setStatelessBeanLoadAlgorithm(this.oldDDStruct.getStatelessBeanLoadAlgorithm());
         }

         if (this.oldDDStruct.getStatelessBeanCallRouterClassName() != null) {
            var1.setStatelessBeanCallRouterClassName(this.oldDDStruct.getStatelessBeanCallRouterClassName());
         }

         if (new Boolean(this.oldDDStruct.getStatelessBeanMethodsAreIdempotent())) {
            EjbDescriptorBean var2 = var1.getEJBDescriptor();
            WeblogicEjbJarBean var3 = var2.getWeblogicEjbJarBean();
            IdempotentMethodsBean var4 = var3.getIdempotentMethods();
            if (var4 == null) {
               var4 = var3.createIdempotentMethods();
            }

            MethodBean var5 = var4.createMethod();
            var5.setEjbName(var1.getEJBName());
            var5.setMethodName("*");
         }
      } else {
         if (this.oldDDStruct.getHomeLoadAlgorithm() != null) {
            var1.setHomeLoadAlgorithm(this.oldDDStruct.getHomeLoadAlgorithm());
         }

         if (this.oldDDStruct.getHomeCallRouterClass() != null) {
            var1.setHomeCallRouterClassName(this.oldDDStruct.getHomeCallRouterClass());
         }
      }

   }

   private boolean beanHasBeanManagedMethod() {
      Enumeration var1 = this.oldMethodDescriptors.keys();

      String var4;
      do {
         if (!var1.hasMoreElements()) {
            return false;
         }

         String var2 = (String)var1.nextElement();
         EJB10MethodDescriptorStructure var3 = (EJB10MethodDescriptorStructure)this.oldMethodDescriptors.get(var2);
         var4 = var3.getTransactionAttribute();
      } while(var4 == null || !var4.equals("TX_BEAN_MANAGED"));

      return true;
   }

   private void doMethodDescriptors(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doMethodDescriptors -----");
      }

      if (debug) {
         Enumeration var2 = this.oldMethodDescriptors.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            System.out.println(this.oldMethodDescriptors.get(var3));
         }
      }

      EjbJarBean var4 = var1.getEJBDescriptor().getEjbJarBean();
      AssemblyDescriptorBean var5 = var4.getAssemblyDescriptor();
      if (var5 == null) {
         var5 = var4.createAssemblyDescriptor();
      }

      if (this.setContainerManagedTransactions(var1)) {
         this.setTransactionAttributes(var5);
      }

      this.setMethodPermissions(var5);
      this.setIsolationLevels(var1.getEJBDescriptor().getWeblogicEjbJarBean());
   }

   private void setTransactionAttributes(AssemblyDescriptorBean var1) {
      Set var2 = this.oldMethodDescriptors.keySet();
      Iterator var3 = var2.iterator();

      while(true) {
         EJB10MethodDescriptorStructure var5;
         String var6;
         do {
            if (!var3.hasNext()) {
               return;
            }

            String var4 = (String)var3.next();
            var5 = (EJB10MethodDescriptorStructure)this.oldMethodDescriptors.get(var4);
            var6 = var5.getTransactionAttribute();
         } while(var6 == null);

         ContainerTransactionBean var7 = var1.createContainerTransaction();
         weblogic.j2ee.descriptor.MethodBean var8 = var7.createMethod();
         var8.setEjbName(this.ejbName);
         String var9 = var5.getMethodName();
         if (var9.equalsIgnoreCase("DEFAULT")) {
            var8.setMethodName("*");
         } else {
            var8.setMethodName(var9);
         }

         Vector var10 = var5.getMethodParams();
         if (var10 != null && var10.size() > 0) {
            MethodParamsBean var11 = var8.createMethodParams();

            for(int var12 = 0; var12 < var10.size(); ++var12) {
               var11.addMethodParam((String)var10.elementAt(var12));
               if (debug) {
                  System.out.println("**** " + var10.elementAt(var12));
               }
            }
         }

         var7.setTransAttribute(this.mapTxAttribute(var6, var5.getMethodSig()));
      }
   }

   private void setMethodPermissions(AssemblyDescriptorBean var1) {
      Set var2 = this.oldMethodDescriptors.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         EJB10MethodDescriptorStructure var5 = (EJB10MethodDescriptorStructure)this.oldMethodDescriptors.get(var4);
         Vector var6 = var5.getAccessControlEntries();
         if (var6 != null) {
            MethodPermissionBean var7 = var1.createMethodPermission();
            weblogic.j2ee.descriptor.MethodBean var8 = var7.createMethod();
            var8.setEjbName(this.ejbName);
            String var9 = var5.getMethodName();
            if (var9.equalsIgnoreCase("DEFAULT")) {
               var8.setMethodName("*");
            } else {
               var8.setMethodName(var9);
            }

            Vector var10 = var5.getMethodParams();
            if (var10 != null && var10.size() > 0) {
               MethodParamsBean var11 = var8.createMethodParams();

               for(int var12 = 0; var12 < var10.size(); ++var12) {
                  var11.addMethodParam((String)var10.elementAt(var12));
                  if (debug) {
                     System.out.println("**** " + var10.elementAt(var12));
                  }
               }
            }

            for(int var13 = 0; var13 < var6.size(); ++var13) {
               String var14 = (String)var6.elementAt(var13);
               var7.addRoleName(var14);
            }
         }

         if (var5.getRunAsMode() != null) {
            if (this.isCMP20beanFlag) {
               this.log.logWarning(this.fmt.noRunAsMode20(var5.getMethodSig()));
            } else {
               this.log.logWarning(this.fmt.noRunAsMode11(var5.getMethodSig()));
            }
         }

         if (var5.getRunAsIdentity() != null) {
            if (this.isCMP20beanFlag) {
               this.log.logWarning(this.fmt.noRunAsIdentity20(var5.getMethodSig()));
            } else {
               this.log.logWarning(this.fmt.noRunAsIdentity11(var5.getMethodSig()));
            }
         }
      }

   }

   private void setIsolationLevels(WeblogicEjbJarBean var1) {
      Set var2 = this.oldMethodDescriptors.keySet();
      Iterator var3 = var2.iterator();

      while(true) {
         EJB10MethodDescriptorStructure var5;
         String var6;
         do {
            if (!var3.hasNext()) {
               return;
            }

            String var4 = (String)var3.next();
            var5 = (EJB10MethodDescriptorStructure)this.oldMethodDescriptors.get(var4);
            var6 = var5.getIsolationLevel();
         } while(var6 == null);

         TransactionIsolationBean var7 = var1.createTransactionIsolation();
         MethodBean var8 = var7.createMethod();
         var8.setEjbName(this.ejbName);
         String var9 = var5.getMethodName();
         if (var9.equalsIgnoreCase("DEFAULT")) {
            var8.setMethodName("*");
         } else {
            var8.setMethodName(var9);
         }

         Vector var10 = var5.getMethodParams();
         if (var10 != null && var10.size() > 0) {
            weblogic.j2ee.descriptor.wl.MethodParamsBean var11 = var8.createMethodParams();

            for(int var12 = 0; var12 < var10.size(); ++var12) {
               var11.addMethodParam((String)var10.elementAt(var12));
               if (debug) {
                  System.out.println("**** " + var10.elementAt(var12));
               }
            }
         }

         if ("TRANSACTION_SERIALIZABLE".equalsIgnoreCase(var6)) {
            var6 = "TransactionSerializable";
         } else if ("TRANSACTION_READ_COMMITTED".equalsIgnoreCase(var6)) {
            var6 = "TransactionReadCommitted";
         } else if ("TRANSACTION_READ_COMMITTED_FOR_UPDATE".equalsIgnoreCase(var6)) {
            var6 = "TransactionReadCommittedForUpdate";
         } else if ("TRANSACTION_READ_UNCOMMITTED".equalsIgnoreCase(var6)) {
            var6 = "TransactionReadUncommitted";
         } else if ("TRANSACTION_REPEATABLE_READ".equalsIgnoreCase(var6)) {
            var6 = "TransactionRepeatableRead";
         }

         var7.setIsolationLevel(var6);
      }
   }

   private String mapTxAttribute(String var1, String var2) {
      if (verbose) {
         System.out.println("----- mapTxAttribute -----");
      }

      String var3 = null;
      if (var1 != null) {
         if (var1.equalsIgnoreCase("TX_NOT_SUPPORTED")) {
            var3 = "NotSupported";
         } else if (var1.equalsIgnoreCase("TX_REQUIRED")) {
            var3 = "Required";
         } else if (var1.equalsIgnoreCase("TX_SUPPORTS")) {
            var3 = "Supports";
         } else if (var1.equalsIgnoreCase("TX_REQUIRES_NEW")) {
            var3 = "RequiresNew";
         } else if (var1.equalsIgnoreCase("TX_MANDATORY")) {
            var3 = "Mandatory";
         } else if (var1.equalsIgnoreCase("TX_BEAN_MANAGED")) {
            if (this.beanType.equalsIgnoreCase("EntityDescriptor")) {
               var3 = "Required";
               this.log.logWarning(this.fmt.noBeanManagedForEntityBeans(var3, var2));
            } else {
               var3 = "BeanManaged";
            }
         } else {
            var3 = "NotSet";
            this.log.logWarning(this.fmt.unexpectedErrorTxNotSet(var1, var2));
         }
      }

      return var3;
   }

   private boolean setContainerManagedTransactions(CompositeMBeanDescriptor var1) {
      boolean var2;
      if (this.beanType.equals("EntityDescriptor")) {
         if (verbose) {
            System.out.println("bean is an entity bean with container managed transactions");
         }

         var2 = true;
      } else if (this.beanHasBeanManagedMethod()) {
         var2 = false;
         if (verbose) {
            System.out.println("bean is a session bean with bean managed transactions");
         }

         ((SessionBeanBean)var1.getBean()).setTransactionType("Bean");
      } else {
         var2 = true;
         if (verbose) {
            System.out.println("bean is a session bean with container managed transactions");
         }

         ((SessionBeanBean)var1.getBean()).setTransactionType("Container");
      }

      return var2;
   }

   private void doRoleDescriptors(CompositeMBeanDescriptor var1) {
      EJB10MethodDescriptorStructure var2 = null;
      HashSet var3 = new HashSet();
      Enumeration var4 = this.oldMethodDescriptors.keys();

      while(true) {
         Vector var5;
         do {
            if (!var4.hasMoreElements()) {
               AssemblyDescriptorBean var12 = var1.getEJBDescriptor().getEjbJarBean().getAssemblyDescriptor();
               WeblogicEjbJarBean var13 = var1.getEJBDescriptor().getWeblogicEjbJarBean();
               if (!var3.isEmpty()) {
                  Iterator var7 = var3.iterator();

                  while(var7.hasNext()) {
                     String var8 = (String)var7.next();
                     this.log.logWarning(this.fmt.securityPrincipalArbitrarilySet(var8, var8));
                     SecurityRoleBean var10 = var12.createSecurityRole();
                     var10.setRoleName(var8);
                     SecurityRoleAssignmentBean var11 = var13.createSecurityRoleAssignment();
                     var11.setRoleName(var8);
                     var11.addPrincipalName(var8);
                  }
               }

               return;
            }

            var2 = (EJB10MethodDescriptorStructure)this.oldMethodDescriptors.get(var4.nextElement());
            var5 = var2.getAccessControlEntries();
         } while(var5 == null);

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            var3.add(var5.elementAt(var6));
         }
      }
   }

   private Hashtable getAllMethods(Hashtable var1, Hashtable var2) {
      if (verbose) {
         System.out.println("----- getAllMethods -----");
      }

      Hashtable var3 = new Hashtable();
      Enumeration var4 = null;
      EJB10MethodDescriptorStructure var5 = null;
      int var6;
      String var7;
      if (var1 != null) {
         var4 = var1.keys();

         for(var6 = 0; var6 < var1.size(); ++var6) {
            var7 = (String)var4.nextElement();
            Vector var8 = this.oldDDStruct.getAccessControlEntry(var7);
            var5 = new EJB10MethodDescriptorStructure(var7, var8, (Hashtable)null);
            if (!var3.containsKey(var7)) {
               var3.put(var7, var5);
            } else {
               var5 = (EJB10MethodDescriptorStructure)var3.remove(var7);
               var5.setAccessControlEntries(var8);
               var3.put(var7, var5);
            }
         }
      }

      if (var2 != null) {
         var4 = var2.keys();

         for(var6 = 0; var6 < var2.size(); ++var6) {
            var7 = (String)var4.nextElement();
            Hashtable var10 = (Hashtable)var2.get(var7);
            var5 = new EJB10MethodDescriptorStructure(var7, (Vector)null, var10);
            if (!var3.containsKey(var7)) {
               var3.put(var7, var5);
            } else {
               var5 = (EJB10MethodDescriptorStructure)var3.remove(var7);
               var5.setControlDescriptors(var10);
               var3.put(var7, var5);
            }
         }
      }

      if (debug) {
         System.out.println("-----------------------------------------------");
         System.out.println("COMBINED: ");
         Enumeration var9 = var3.keys();

         while(var9.hasMoreElements()) {
            System.out.println(var3.get(var9.nextElement()));
         }
      }

      return var3;
   }

   private void doSessionProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doSessionProperties -----");
      }

      this.doSessionClusterProperties(var1);
      if (this.oldDDStruct.getStateManagementType().equalsIgnoreCase("STATEFUL_SESSION")) {
         this.doSFSessionProperties(var1);
      } else if (this.oldDDStruct.getStateManagementType().equalsIgnoreCase("STATELESS_SESSION")) {
         this.doSLSessionProperties(var1);
      }

   }

   private void doSFSessionProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doSFSessionProperties -----");
      }

      SessionBeanBean var2 = (SessionBeanBean)var1.getBean();
      var2.setSessionType("Stateful");
      Hashtable var3 = this.oldDDStruct.getPersistentStoreProperties();
      String var4 = this.oldDDStruct.getSessionPersistentDirectoryRoot();
      if (var4 != null) {
         var1.getWlBean().getStatefulSessionDescriptor().setPersistentStoreDir(var4);
      }

      try {
         int var5 = new Integer(this.oldDDStruct.getSessionTimeout());
         var1.getWlBean().getTransactionDescriptor().setTransTimeoutSeconds(var5);
      } catch (NumberFormatException var6) {
         EJBLogger.logStackTrace(var6);
      }

   }

   private void doSLSessionProperties(CompositeMBeanDescriptor var1) {
      if (verbose) {
         System.out.println("----- doSLSessionProperties -----");
      }

      try {
         int var2 = new Integer(this.oldDDStruct.getSessionTimeout());
         var1.getWlBean().getTransactionDescriptor().setTransTimeoutSeconds(var2);
      } catch (NumberFormatException var3) {
         EJBLogger.logStackTrace(var3);
      }

      SessionBeanBean var4 = (SessionBeanBean)var1.getBean();
      var4.setSessionType("Stateless");
   }

   private void doEntityProperties(CompositeMBeanDescriptor var1) throws DDConverterCollectionException {
      if (verbose) {
         System.out.println("----- doEntityProperties -----");
      }

      EntityBeanBean var2 = (EntityBeanBean)var1.getBean();
      WeblogicEnterpriseBeanBean var3 = var1.getWlBean();
      EntityDescriptorBean var4 = var3.getEntityDescriptor();
      var2.setPrimKeyClass(this.oldDDStruct.getPrimaryKeyClassName());
      var4.getEntityCache().setCacheBetweenTransactions(!new Boolean(this.oldDDStruct.getJdbcDbIsShared()));
      var2.setReentrant(new Boolean(this.oldDDStruct.getIsReentrant()));
      var4.getPersistence().setDelayUpdatesUntilEndOfTx(this.oldDDStruct.getDelayUpdatesUntilEndOfTx());
      Vector var5 = this.oldDDStruct.getContainerManagedFields();
      if (var5 == null) {
         var2.setPersistenceType("Bean");
         this.doBMPEntityProperties(var1);
      } else {
         if (this.isCMP20beanFlag) {
            var2.setAbstractSchemaName(DDConverterBase.makeAbstractSchemaName(var2.getEjbName()));
            Debug.say("@@@ SETTING ABSTRACT:" + var2.getAbstractSchemaName());
         }

         var2.setPersistenceType("Container");
         var3.getEntityDescriptor().getEntityCache().setConcurrencyStrategy("Exclusive");
         Object var6 = null;
         if (this.isCMP20beanFlag) {
            WeblogicRdbmsJarBean[] var7 = this.desc.getWeblogicRdbmsJarBeans();
            if (var7.length == 0) {
               this.desc.createWeblogicRdbmsJarBean();
            }

            var6 = this.desc.getWeblogicRdbmsJarBeans()[0].createWeblogicRdbmsBean();
         } else {
            weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[] var8 = this.desc.getWeblogicRdbms11JarBeans();
            if (var8.length == 0) {
               this.desc.createWeblogicRdbms11JarBean();
            }

            var6 = this.desc.getWeblogicRdbms11JarBeans()[0].createWeblogicRdbmsBean();
         }

         var1.setRDBMSBean((BaseWeblogicRdbmsBeanBean)var6);
         this.doCMPEntityProperties(var1, (BaseWeblogicRdbmsBeanBean)var6);
      }

   }

   private void doBMPEntityProperties(CompositeMBeanDescriptor var1) {
      EntityDescriptorBean var2 = var1.getWlBean().getEntityDescriptor();
      if (this.oldDDStruct.getIsModifiedMethodName() != null) {
         var2.getPersistence().setIsModifiedMethodName(this.oldDDStruct.getIsModifiedMethodName());
      }

   }

   private void doCMPEntityProperties(CompositeMBeanDescriptor var1, BaseWeblogicRdbmsBeanBean var2) throws DDConverterCollectionException {
      if (verbose) {
         System.out.println("----- doCMPEntityProperties -----");
      }

      Hashtable var3 = this.oldDDStruct.getJdbc();
      String var4 = this.oldDDStruct.getPersistentStoreType();
      Vector var5 = this.oldDDStruct.getContainerManagedFields();

      for(int var6 = 0; var6 < var5.size(); ++var6) {
         String var7 = (String)var5.elementAt(var6);
         if (var7.equals("*")) {
            this.log.logWarning(this.fmt.starNotSupportedForCmpFields());
            Hashtable var13 = this.oldDDStruct.getJdbcAttributeMap();
            Enumeration var14 = var13.keys();

            while(var14.hasMoreElements()) {
               String var10 = (String)var14.nextElement();
               EntityBeanBean var11 = (EntityBeanBean)var1.getBean();
               CmpFieldBean var12 = var11.createCmpField();
               var12.setFieldName(var10);
            }
         } else {
            EntityBeanBean var8 = (EntityBeanBean)var1.getBean();
            CmpFieldBean var9 = var8.createCmpField();
            var9.setFieldName(var7);
         }
      }

      if (var4.equalsIgnoreCase("file")) {
         this.doCMPFileProperties(var1);
      } else if (var4.equalsIgnoreCase("jdbc")) {
         if (this.isCMP20beanFlag) {
            this.doCMP20JDBCProperties(var1, var3, (WeblogicRdbmsBeanBean)var2);
         } else {
            this.doCMP11JDBCProperties(var1, var3, (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean)var2);
         }
      } else {
         this.log.logWarning(this.fmt.invalidPstoreType(var4));
      }

      if (this.oldDDStruct.getPersistentStoreClassName() != null) {
         this.log.logWarning(this.fmt.noCustomPstoreClass());
      }

   }

   private void doCMPFileProperties(CompositeMBeanDescriptor var1) throws DDConverterCollectionException {
      if (verbose) {
         System.out.println("----- doCMPFileEntityProperties -----");
      }

      this.log.logError(this.fmt.noFilePersistence());
      throw new DDConverterCollectionException(new DDConverterException("noFilePersistence"));
   }

   protected void doCMP20JDBCProperties(CompositeMBeanDescriptor var1, Hashtable var2, WeblogicRdbmsBeanBean var3) throws DDConverterCollectionException {
      WeblogicRdbmsBeanBean var4 = var3;

      try {
         String var5 = var1.getBean().getEjbName();
         var4.setEjbName(var5);
         var4.setDataSourceJndiName(this.oldDDStruct.getJdbcPoolName());
         TableMapBean var6 = var4.createTableMap();
         var6.setTableName(this.oldDDStruct.getJdbcTableName());
         Hashtable var7 = this.oldDDStruct.getJdbcAttributeMap();
         Enumeration var8 = var7.keys();

         while(var8.hasMoreElements()) {
            String var9 = (String)var8.nextElement();
            FieldMapBean var10 = var6.createFieldMap();
            var10.setCmpField(var9);
            var10.setDbmsColumn((String)var7.get(var9));
         }

         Hashtable var24 = this.oldDDStruct.getFinderDescriptors();
         if (var24 != null) {
            Enumeration var25 = var24.keys();
            FinderParser var11 = null;
            new String();
            new String();
            EntityBeanBean var14 = (EntityBeanBean)var1.getBean();

            while(var25.hasMoreElements()) {
               String var12 = (String)var25.nextElement();
               String var13 = (String)var24.get(var12);
               var11 = new FinderParser(var12, var13, this.fmt, this.log);
               var11.parseFinder();
               System.out.println("\n");
               System.out.println("finder  key: " + var12);
               System.out.println("finder elem: " + var13);
               String var15 = var11.getNewMethod();
               String var16 = null;
               String var17 = null;

               try {
                  QueryBean var18 = var14.createQuery();
                  QueryMethodBean var19 = var18.createQueryMethod();
                  MethodParamsBean var20 = var19.createMethodParams();
                  var19.setMethodName(var15);
                  Iterator var21 = var11.getParamTypes().iterator();

                  while(var21.hasNext()) {
                     var20.addMethodParam((String)var21.next());
                  }

                  var17 = var11.getNewQuery();
                  var16 = WLQLtoEJBQL.doWLQLtoEJBQL(var17, DDConverterBase.makeAbstractSchemaName(var5));
                  var18.setEjbQl(var16);
               } catch (RDBMSException var22) {
                  this.log.logWarning(this.fmt.qlConversion(var17, var22.toString()));
               }
            }
         }

         if (debug) {
            System.out.println("\n\nrdbmsBean: " + var4.toString());
         }

         String var26 = "META-INF/weblogic-rdbms-jar.xml";
         PersistenceBean var27 = var1.getWlBean().getEntityDescriptor().getPersistence();
         if (var27 != null) {
            var1.getWlBean().getEntityDescriptor().destroyPersistence(var27);
         }

         var27 = var1.getWlBean().getEntityDescriptor().createPersistence();
         PersistenceUseBean var28 = var27.createPersistenceUse();
         var28.setTypeIdentifier("WebLogic_CMP_RDBMS");
         var28.setTypeVersion("6.0");
         var28.setTypeStorage(var26);
         EntityBeanBean var29 = (EntityBeanBean)var1.getBean();
         var29.setCmpVersion("2.x");
      } catch (Exception var23) {
         this.log.logError(this.fmt.unexpectedError(var23.toString()));
         EJBLogger.logStackTrace(var23);
         throw new DDConverterCollectionException(new DDConverterException(var23));
      }

      this.m_rdbmsBeans.add(var3);
   }

   protected void doCMP11JDBCProperties(CompositeMBeanDescriptor var1, Hashtable var2, weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean var3) throws DDConverterCollectionException {
      try {
         EntityBeanBean var4 = (EntityBeanBean)var1.getBean();
         EntityDescriptorBean var5 = var1.getWlBean().getEntityDescriptor();
         var3.setEjbName(var4.getEjbName());
         var3.setTableName(this.oldDDStruct.getJdbcTableName());
         var3.setPoolName(this.oldDDStruct.getJdbcPoolName());
         Hashtable var6 = this.oldDDStruct.getJdbcAttributeMap();
         Enumeration var7 = var6.keys();

         while(var7.hasMoreElements()) {
            String var8 = (String)var7.nextElement();
            weblogic.j2ee.descriptor.wl60.FieldMapBean var9 = var3.createFieldMap();
            var9.setCmpField(var8);
            var9.setDbmsColumn((String)var6.get(var8));
         }

         Hashtable var18 = this.oldDDStruct.getFinderDescriptors();
         if (var18 != null) {
            Set var19 = var18.keySet();
            Iterator var10 = var19.iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               String var12 = (String)var18.get(var11);
               FinderParser var13 = new FinderParser(var11, var12, this.fmt, this.log);
               var13.parseFinder();
               if (debug) {
                  System.out.println("\n");
                  System.out.println("finder  key: " + var11);
                  System.out.println("finder elem: " + var12);
               }

               FinderBean var14 = var3.createFinder();
               var14.setFinderName(var13.getNewMethod());
               var14.setFinderQuery(var13.getNewQuery());
               Vector var15 = var13.getParamTypes();

               for(int var16 = 0; var16 < var15.size(); ++var16) {
                  var14.addFinderParam((String)var15.elementAt(var16));
               }
            }
         }

         if (debug) {
            System.out.println("\n\nrdbmsBean: " + var3.toString());
         }

         String var20 = "META-INF/weblogic-rdbms-jar.xml";
         PersistenceBean var21 = var5.createPersistence();
         if (this.oldDDStruct.getIsModifiedMethodName() != null) {
            var21.setIsModifiedMethodName(this.oldDDStruct.getIsModifiedMethodName());
         }

         PersistenceUseBean var22 = var21.createPersistenceUse();
         var22.setTypeIdentifier("WebLogic_CMP_RDBMS");
         var22.setTypeVersion("5.1.0");
         var22.setTypeStorage(var20);
         var4.setCmpVersion("1.x");
      } catch (Exception var17) {
         this.log.logError(this.fmt.unexpectedError(var17.toString()));
         EJBLogger.logStackTraceAndMessage(this.fmt.unexpectedError(var17.toString()), var17);
         throw new DDConverterCollectionException(new DDConverterException(var17));
      }
   }

   private Structure parseFile(String var1) throws Exception {
      this.currFile = var1;
      FileReader var2 = new FileReader(var1);
      BufferedReader var3 = new BufferedReader(var2);
      this.st = new StreamTokenizer(var3);
      this.st.resetSyntax();
      this.st.whitespaceChars(0, 32);
      this.st.wordChars(33, 126);
      this.st.eolIsSignificant(false);
      this.st.commentChar(59);
      this.st.ordinaryChar(40);
      this.st.ordinaryChar(41);
      this.st.ordinaryChar(91);
      this.st.ordinaryChar(93);
      this.st.ordinaryChar(44);
      this.st.quoteChar(34);
      Structure var4 = this.parseStructure();
      var2.close();
      return var4;
   }

   private Structure parseStructure() throws Exception {
      if (verbose) {
         System.out.println("Entering parseStructure");
      }

      this.match('(');
      this.eofOK = false;
      ++this.nestingLevel;
      Structure var1 = new Structure();
      var1.name = this.nextWord();
      var1.elements = this.parseElements();
      this.match(')');
      --this.nestingLevel;
      if (this.nestingLevel == 0) {
         this.eofOK = true;
      }

      return var1;
   }

   private int nextToken() throws Exception {
      int var1 = this.st.nextToken();
      if (var1 == 34) {
         var1 = -3;
      }

      if (verbose) {
         System.out.println(this.interpret());
      }

      if (var1 == -1 && !this.eofOK) {
         throw new ParseException(this.currFile, "Unexpected end of file");
      } else {
         return var1;
      }
   }

   private Hashtable parseElements() throws Exception {
      if (verbose) {
         System.out.println("Entering parseElements");
      }

      Hashtable var1 = new Hashtable();

      while(true) {
         int var4 = this.nextToken();
         char var5 = (char)var4;
         if (var5 == ')') {
            this.putBack();
            return var1;
         }

         String var2;
         Object var3;
         if (var5 == '(') {
            this.putBack();
            Structure var6 = this.parseStructure();
            var2 = var6.name;
            var3 = var6.elements;
         } else {
            var2 = this.st.sval;
            if (var2 == null) {
               this.fatalError("Expected a word. Got " + this.interpret());
            }

            var3 = this.parseValue();
         }

         var1.put(var2, var3);
      }
   }

   private Object parseValue() throws Exception {
      if (verbose) {
         System.out.println("Entering parseValue");
      }

      int var1 = this.nextToken();
      char var2 = (char)var1;
      if (var2 == '[') {
         this.putBack();
         return this.parseStringVector();
      } else if (var1 == -3) {
         return this.st.sval;
      } else {
         this.fatalError("Expected word or '[' words ']'. Got " + this.interpret());
         return this.st.sval;
      }
   }

   private Vector parseStringVector() throws Exception {
      if (verbose) {
         System.out.println("Entering parseStringVector");
      }

      Vector var1 = new Vector();
      this.match('[');

      while(true) {
         int var2 = this.nextToken();
         char var3 = (char)var2;
         if (var3 == ']') {
            return var1;
         }

         if (var3 != ',') {
            if (var2 == -3) {
               var1.addElement(this.st.sval);
            } else {
               this.fatalError("Expected string or ']'. Got " + this.interpret());
            }
         }
      }
   }

   private void match(char var1) throws Exception {
      int var2 = this.nextToken();
      if ((char)var2 != var1) {
         this.fatalError("Expected '" + var1 + "'. Got " + this.interpret());
      }
   }

   private void fatalError(String var1) throws ParseException {
      throw new ParseException(this.currFile, this.st.lineno(), var1);
   }

   private String nextWord() throws Exception {
      int var1 = this.nextToken();
      StreamTokenizer var10001 = this.st;
      if (var1 == -3) {
         return this.st.sval;
      } else {
         this.fatalError("Expected a word. Got " + this.interpret());
         return null;
      }
   }

   private String interpret() {
      switch (this.st.ttype) {
         case -3:
         case 34:
            return "word (" + this.st.sval + ")";
         case -2:
            return "number (" + this.st.nval + ")";
         case 10:
            return "end of line";
         default:
            return "character '" + (char)this.st.ttype + "'";
      }
   }

   private void putBack() throws Exception {
      this.st.pushBack();
   }

   class ParseException extends Exception {
      private static final long serialVersionUID = 7989961809471350639L;

      public ParseException(String var2, int var3, String var4) {
         super(var2 + ":(" + var3 + "): " + var4);
      }

      public ParseException(String var2, String var3) {
         super(var2 + ": " + var3);
      }
   }
}
