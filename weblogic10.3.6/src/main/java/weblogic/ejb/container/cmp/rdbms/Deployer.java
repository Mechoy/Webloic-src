package weblogic.ejb.container.cmp.rdbms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import javax.ejb.FinderException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator;
import weblogic.ejb.container.cmp.rdbms.compliance.RDBMSBeanChecker;
import weblogic.ejb.container.cmp.rdbms.compliance.RDBMSComplianceChecker;
import weblogic.ejb.container.cmp.rdbms.finders.EjbqlFinder;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.container.deployer.EntityBeanQueryImpl;
import weblogic.ejb.container.interfaces.EntityBeanQuery;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.JarDeployment;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLCompilerException;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.QueryBean;
import weblogic.j2ee.descriptor.wl.CompatibilityBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.validation.ComplianceException;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.ProcessorFactoryException;
import weblogic.xml.process.XMLProcessor;

public final class Deployer implements CMPDeployer {
   private static final DebugLogger debugLogger;
   private RDBMSBean currBean = null;
   private List fieldList = null;
   private List finderList = null;
   private List localFinderList = null;
   private List finderObjectList = null;
   private List primaryKeyList = null;
   private Map parameterMap = null;
   private Class ejbClass = null;
   private Class primaryKeyClass = null;
   private boolean isCompoundPK = true;
   private CMPBeanDescriptor bd = null;
   private Map beanMap = null;
   private Relationships relationships = null;
   private Map dependentMap = null;
   private Map rdbmsRelationMap = null;
   private Map rdbmsDependentMap = null;
   private RDBMSDeployment rdbmsDeployment = null;
   private static final String DOCTYPE_DECL_START = "<!DOCTYPE";

   public void setup(JarDeployment var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setup()");
      }

      assert var1 != null;

      this.currBean = null;
      this.rdbmsDeployment = (RDBMSDeployment)var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setCMPBeanDescriptor()");
      }

      this.bd = var1;
      this.ejbClass = var1.getBeanClass();
      this.primaryKeyClass = var1.getPrimaryKeyClass();
      if (var1.hasRemoteClientView()) {
         this.finderList = MethodUtils.getFinderMethodList(var1.getHomeInterfaceClass());
      }

      if (var1.hasLocalClientView()) {
         this.localFinderList = MethodUtils.getFinderMethodList(var1.getLocalHomeInterfaceClass());
      }

      this.primaryKeyList = new ArrayList(var1.getPrimaryKeyFieldNames());
      this.isCompoundPK = var1.hasComplexPrimaryKey();
      this.fieldList = new ArrayList(var1.getCMFieldNames());
   }

   public void setBeanMap(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setBeanMap()");
      }

      this.beanMap = var1;
   }

   public void setRelationships(Relationships var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setRelationships()");
      }

      this.relationships = var1;
   }

   public void setDependentMap(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setDependentMap()");
      }

      this.dependentMap = var1;
   }

   public void setParameters(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setParameters()");
      }

      this.parameterMap = new HashMap(var1);
   }

   public void initializePersistenceManager(PersistenceManager var1) throws WLDeploymentException {
      if (debugLogger.isDebugEnabled()) {
         debug("called  initializePersistenceManager()");
      }

      RDBMSPersistenceManager var2 = (RDBMSPersistenceManager)var1;
      var2.setRdbmsBean(this.currBean);
      this.currBean.setRDBMSPersistenceManager(var2);
   }

   private boolean fieldListContainsField(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp.rdbms.Deployer.fieldListContainsField()");
      }

      Iterator var2 = this.fieldList.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var3.equals(var1));

      return true;
   }

   private boolean currBeanContainsField(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp.rdbms.Deployer.currBeanContainsField()");
      }

      Iterator var2 = this.currBean.getCmpFieldNames().iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var3.equals(var1));

      return true;
   }

   public RDBMSBean getTypeSpecificData() {
      if (debugLogger.isDebugEnabled()) {
         debug("called getTypeSpecificData()");
      }

      return this.currBean;
   }

   public void setTypeSpecificData(RDBMSBean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("called setTypeSpecificData()");
      }

      this.currBean = var1;
   }

   private static void adjustDefaults(CMPDDParser var0, EjbDescriptorBean var1, Map var2) throws RDBMSException, WLDeploymentException {
      WeblogicRdbmsJarBean var3 = var0.getDescriptorMBean();
      WeblogicRdbmsBeanBean[] var4 = var3.getWeblogicRdbmsBeans();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var6 = var4[var5].getEjbName();
         DefaultHelper var7 = var0.getDefaultHelper(var6);
         Debug.assertion(var7 != null);
         if (var2 != null) {
            CMPBeanDescriptor var8 = (CMPBeanDescriptor)var2.get(var6);
            Debug.assertion(var8 != null);
            var7.adjustDefaults(var8.getPrimaryKeyClassName(), var8.getAllQueries(), var8.getConcurrencyStrategy(), var3, var4[var5]);
         } else {
            EnterpriseBeanBean var17 = EJBDescriptorMBeanUtils.getEnterpriseMBean(var6, var1);
            if (var17 == null) {
               Loggable var18 = EJBLogger.logmissingEnterpriseBeanMBeanLoggable(var6);
               throw new RDBMSException(var18.getMessage());
            }

            EntityBeanBean var9 = null;
            if (!(var17 instanceof EntityBeanBean) || !"2.x".equals(((EntityBeanBean)var17).getCmpVersion())) {
               Loggable var19 = EJBLogger.logentityMBeanWrongVersionLoggable(var6, "ejb20");
               throw new RDBMSException(var19.getMessage());
            }

            var9 = (EntityBeanBean)var17;
            String var10 = var9.getPrimKeyClass();
            ArrayList var11 = new ArrayList();
            QueryBean[] var12 = var9.getQueries();

            for(int var13 = 0; var13 < var12.length; ++var13) {
               EntityBeanQueryImpl var14 = new EntityBeanQueryImpl(var12[var13]);
               var11.add(var14);
            }

            WeblogicEnterpriseBeanBean var20 = EJBDescriptorMBeanUtils.getWeblogicEnterpriseMBean(var6, var1);
            CompositeMBeanDescriptor var21 = new CompositeMBeanDescriptor(var17, var20, var1);
            String var15 = var21.getConcurrencyStrategy();
            int var16 = DDUtils.concurrencyStringToInt(var15);
            var7.adjustDefaults(var10, var11, var16, var3, var4[var5]);
         }
      }

   }

   public void readTypeSpecificData(VirtualJarFile var1, String var2) throws Exception {
      if (this.rdbmsDeployment.needToReadFile(var2)) {
         if (debugLogger.isDebugEnabled()) {
            debug("processing RDBMS CMP XML for bean: " + this.bd.getEJBName() + " file- " + var2);
         }

         this.rdbmsDeployment.addFileName(var2);
         new ProcessorFactory();
         WeblogicRdbmsJarBean var4 = this.parseXMLFile(var1, var2, this.bd.getEjbDescriptorBean());
         RDBMSDeploymentInfo var5 = new RDBMSDeploymentInfo(var4, this.beanMap, var2);
         Map var6 = var5.getRDBMSBeanMap();
         if (var5.getRDBMSBean(this.bd.getEJBName()) == null) {
            Loggable var16 = EJBLogger.logUnableToFindBeanInRDBMSDescriptorLoggable(this.bd.getEJBName(), var2);
            throw new RDBMSException(var16.getMessage());
         }

         String var7 = var4.getDefaultDbmsTablesDdl();
         Iterator var8 = var6.entrySet().iterator();

         while(var8.hasNext()) {
            RDBMSBean var9 = (RDBMSBean)((Map.Entry)var8.next()).getValue();
            var9.setDatabaseType(MethodUtils.dbmsType2int(var4.getDatabaseType()));
            var9.setOrderDatabaseOperations(var4.isOrderDatabaseOperations());
            var9.setEnableBatchOperations(var4.isEnableBatchOperations());
            var9.setCreateDefaultDBMSTables(var4.getCreateDefaultDbmsTables());
            var9.setValidateDbSchemaWith(var4.getValidateDbSchemaWith());
            var9.setDefaultDbmsTablesDdl(var7);
            CompatibilityBean var10 = var4.getCompatibility();
            if (var10 != null) {
               var9.setByteArrayIsSerializedToOracleBlob(var10.isSerializeByteArrayToOracleBlob());
               var9.setAllowReadonlyCreateAndRemove(var10.isAllowReadonlyCreateAndRemove());
               var9.setCharArrayIsSerializedToBytes(var10.isSerializeCharArrayToBytes());
               var9.setDisableStringTrimming(var10.isDisableStringTrimming());
               var9.setFindersReturnNulls(var10.isFindersReturnNulls());
               var9.setLoadRelatedBeansFromDbInPostCreate(var10.isLoadRelatedBeansFromDbInPostCreate());
            }
         }

         if (var7 != null) {
            RDBMSBean.deleteDefaultDbmsTableDdlFile(var7);
         }

         this.rdbmsDeployment.addRdbmsBeans(var6);
         this.rdbmsDeployment.addRdbmsRelations(var5.getRDBMSRelationMap());
         this.rdbmsDeployment.addDescriptorMBean(var4);
         RDBMSComplianceChecker var17 = new RDBMSComplianceChecker(this.beanMap, this.relationships, this.dependentMap, var5.getRDBMSBeanMap(), var5.getRDBMSRelationMap(), new HashMap(), var4);
         var17.checkCompliance();
         var8 = var6.entrySet().iterator();

         while(var8.hasNext()) {
            RDBMSBean var18 = (RDBMSBean)((Map.Entry)var8.next()).getValue();

            assert !var18.normalizeMultiTables_done();

            CMPBeanDescriptor var11 = (CMPBeanDescriptor)this.beanMap.get(var18.getEjbName());
            Set var12 = var11.getPrimaryKeyFieldNames();
            ArrayList var13 = new ArrayList(var12);
            var18.setPrimaryKeyFields(var13);
            var18.normalizeMultiTables((CMPBeanDescriptor)this.beanMap.get(var18.getEjbName()));
         }

         var8 = var6.entrySet().iterator();
         ErrorCollectionException var19 = null;

         RDBMSBean var20;
         while(var8.hasNext()) {
            var20 = (RDBMSBean)((Map.Entry)var8.next()).getValue();
            String var21 = var20.getEjbName();
            CMPBeanDescriptor var22 = (CMPBeanDescriptor)this.beanMap.get(var21);
            if (this.rdbmsDeployment.getRDBMSRelationMap().size() == 0) {
               this.relationships = null;
            }

            assert !var20.initialized();

            var20.processDescriptors(this.beanMap, this.relationships, this.dependentMap, this.rdbmsDeployment.getRDBMSBeanMap(), this.rdbmsDeployment.getRDBMSRelationMap(), this.rdbmsDeployment.getRDBMSDependentMap());

            try {
               this.processFinders(var20, var22, this.beanMap);
            } catch (Exception var15) {
               if (var19 == null) {
                  var19 = new ErrorCollectionException();
               }

               var19.add(var15);
            }

            var20.setupAutoKeyGen();
            var20.setupFieldGroupIndexes();
         }

         if (var19 != null && var19.getExceptions().size() > 0) {
            throw var19;
         }

         this.validateFinderQueries();
         this.checkIsValidRelationshipCaching();
         var8 = var6.entrySet().iterator();

         while(var8.hasNext()) {
            var20 = (RDBMSBean)((Map.Entry)var8.next()).getValue();
            var20.generateFinderSQLStatements(var20.getFinders());
            var20.createRelationFinders();
            var20.createRelationFinders2();
            var20.generateFinderSQLStatements(var20.getRelationFinders());
            var20.setupRelatedBeanMap();
            this.processEjbSelect(var20.getFinders());
            this.processEjbSelect(var20.getRelationFinders());
            if (var7 != null) {
               var20.addTableDefToDDLFile();
            }
         }

         if (var7 != null) {
            EJBLogger.logDDLFileCreated(var7);
         }
      }

      this.currBean = this.rdbmsDeployment.getRDBMSBean(this.bd.getEJBName());
      if (this.currBean == null) {
         Loggable var3 = EJBLogger.logUnableToFindBeanInRDBMSDescriptor1Loggable(this.bd.getEJBName());
         throw new RDBMSException(var3.getMessage());
      } else {
         this.checkResultTypeMapping(this.currBean.getFinders());
      }
   }

   private void checkResultTypeMapping(Iterator var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();

      while(true) {
         Finder var3;
         do {
            do {
               if (!var1.hasNext()) {
                  if (!var2.isEmpty()) {
                     throw var2;
                  }

                  return;
               }

               var3 = (Finder)var1.next();
            } while(!var3.isSelect());
         } while(var3.getQueryType() != 4 && var3.getQueryType() != 2);

         RDBMSBean var4 = var3.getSelectBeanTarget();
         CMPBeanDescriptor var5 = var4.getCMPBeanDescriptor();
         EJBComplianceTextFormatter var6 = new EJBComplianceTextFormatter();
         WLDeploymentException var7;
         if (var3.hasLocalResultType()) {
            if (!var5.hasLocalClientView()) {
               var7 = new WLDeploymentException(var6.INVALID_RESULT_TYPE_LOCAL(var3.getRDBMSBean().getEjbName(), DDUtils.getMethodSignature(var3.getName(), var3.getParameterClassNames()), var4.getEjbName()));
               var2.add(var7);
            }
         } else if (var3.hasRemoteResultType() && !var5.hasRemoteClientView()) {
            var7 = new WLDeploymentException(var6.INVALID_RESULT_TYPE_REMOTE(var3.getRDBMSBean().getEjbName(), DDUtils.getMethodSignature(var3.getName(), var3.getParameterClassNames()), var4.getEjbName()));
            var2.add(var7);
         }
      }
   }

   private boolean methodMatchesQuery(Method var1, EntityBeanQuery var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("methodMatchesQuery called for: " + var1.getName() + ", " + var2.getMethodName());
      }

      Class[] var3 = var1.getParameterTypes();
      String[] var4 = var2.getMethodParams();
      if (!var1.getName().equals(var2.getMethodName())) {
         if (debugLogger.isDebugEnabled()) {
            debug("\tmethodMatchesQuery: returning false");
         }

         return false;
      } else if (var3.length != var4.length) {
         if (debugLogger.isDebugEnabled()) {
            debug("\tmethodMatchesQuery: returning false");
         }

         return false;
      } else {
         for(int var5 = 0; var5 < var3.length; ++var5) {
            String var6 = MethodUtils.decodeArrayTypes(var3[var5]);
            if (!var6.equals(var4[var5])) {
               if (debugLogger.isDebugEnabled()) {
                  debug("\tmethodMatchesQuery: returning false");
               }

               return false;
            }
         }

         if (debugLogger.isDebugEnabled()) {
            debug("\tmethodMatchesQuery: returning true");
         }

         return true;
      }
   }

   private Method[] getMethodsForQuery(String var1, EntityBeanQuery var2, List var3, List var4) throws Exception {
      boolean var5 = false;
      Method[] var6 = new Method[2];
      int var7 = 0;
      Iterator var8 = null;
      EJBComplianceTextFormatter var9 = new EJBComplianceTextFormatter();
      if (var2.getMethodName().startsWith("find")) {
         var8 = var3.iterator();
      } else {
         if (!var2.getMethodName().startsWith("ejbSelect")) {
            throw new InvalidFinderException(8, var9.INVALID_QUERY_NAME(var1, DDUtils.getMethodSignature(var2.getMethodName(), var2.getMethodParams())), new DescriptorErrorInfo("<ejb-ql>", var1, var2.getMethodName()));
         }

         var8 = var4.iterator();
         var5 = true;
      }

      while(var8.hasNext()) {
         Method var10 = (Method)var8.next();
         if (this.methodMatchesQuery(var10, var2)) {
            var6[var7] = var10;
            ++var7;
            if (var5 || var7 == 2) {
               break;
            }
         }
      }

      if (var7 == 0) {
         throw new InvalidFinderException(8, var9.QUERY_NOT_FOUND(var1, var2.getMethodSignature()), new DescriptorErrorInfo("<ejb-ql>", var1, var2.getMethodName()));
      } else {
         return var6;
      }
   }

   private Method getFindByPrimaryKeyMethod(List var1) {
      Iterator var2 = var1.iterator();

      Method var3;
      do {
         if (!var2.hasNext()) {
            throw new AssertionError("Method 'findByPrimaryKey' not found on Home interface.");
         }

         var3 = (Method)var2.next();
      } while(!var3.getName().equals("findByPrimaryKey"));

      return var3;
   }

   private void processFinders(RDBMSBean var1, CMPBeanDescriptor var2, Map var3) throws Exception {
      Collection var4 = var2.getAllQueries();
      if (var4 == null) {
         Loggable var22 = EJBLogger.logFinderCollectionIsNullLoggable("Deployer.readTypeSpecificData");
         throw new Exception(var22.getMessage());
      } else {
         Iterator var5 = var4.iterator();
         Finder var6 = null;
         Class var7 = var2.getBeanClass();
         Class var8 = var2.getHomeInterfaceClass();
         Class var9 = var2.getLocalHomeInterfaceClass();
         List var10 = MethodUtils.getFinderMethodList(var8, var9);
         List var11 = MethodUtils.getSelectMethodList(var7);

         while(var5.hasNext()) {
            EntityBeanQuery var12 = (EntityBeanQuery)var5.next();

            try {
               if (debugLogger.isDebugEnabled()) {
                  debug("\n ++++++++  about to create new finder for: " + var12.getMethodName() + "  " + var12.getQueryText());
               }

               var6 = var1.createFinder(var12.getMethodName(), var12.getMethodParams(), var12.getQueryText());
            } catch (InvalidFinderException var21) {
               if (debugLogger.isDebugEnabled()) {
                  debug("getRDBMSBean returned exception " + var21);
               }

               var21.setDescriptorErrorInfo(new DescriptorErrorInfo("<query-method>", var1.getEjbName(), var12.getMethodName()));
               throw var21;
            }

            Method[] var13 = this.getMethodsForQuery(var2.getEJBName(), var12, var10, var11);
            var6.setMethods(var13);
            var6.setFinderLoadsBean(var2.getFindersLoadBean());
            if (var6.isSelect()) {
               var6.setResultTypeMapping(var12.getResultTypeMapping());
            }

            var1.perhapsSetQueryCachingEnabled(var6);
            var1.addFinder(var6);
         }

         String var23 = null;
         if (RDBMSBeanChecker.validateCategoryFieldAvailible(var1)) {
            var23 = var1.getCategoryCmpField();
         }

         if (var23 != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("category-cmp-field " + var23 + " is set to EJB " + var1.getEjbName());
            }

            String var24 = "findByCategory__WL_";
            Class var14 = var1.getCmpFieldClass(var23);
            String[] var15 = new String[]{var14.getName()};
            String var16 = "SELECT Object(bean) FROM " + var1.getAbstractSchemaName() + " AS bean WHERE bean." + var23 + " = ?1";
            Finder var17 = var1.createFinder(var24, var15, var16);
            var17.setReturnClassType(Collection.class);
            var17.setExceptionClassTypes(new Class[]{FinderException.class});
            var17.setModifierString("public ");
            var17.setParameterClassTypes(new Class[]{var14});
            var17.setFinderLoadsBean(true);
            if (debugLogger.isDebugEnabled()) {
               debug("categoryFinder created: " + var17);
            }

            var1.addFinder(var17);
         }

         ErrorCollectionException var25 = null;
         List var26;
         if (var2.hasRemoteClientView()) {
            var26 = MethodUtils.getFinderMethodList(var2.getHomeInterfaceClass());

            try {
               this.checkClassFinderForXMLFinder(var2.getEJBName(), var26.iterator(), var4);
            } catch (Exception var20) {
               if (var25 == null) {
                  var25 = new ErrorCollectionException();
               }

               var25.add(var20);
            }
         }

         if (var2.hasLocalClientView()) {
            var26 = MethodUtils.getFinderMethodList(var2.getLocalHomeInterfaceClass());

            try {
               this.checkClassFinderForXMLFinder(var2.getEJBName(), var26.iterator(), var4);
            } catch (Exception var19) {
               if (var25 == null) {
                  var25 = new ErrorCollectionException();
               }

               var25.add(var19);
            }
         }

         try {
            this.checkClassFinderForXMLFinder(var2.getEJBName(), var11.iterator(), var4);
         } catch (Exception var18) {
            if (var25 == null) {
               var25 = new ErrorCollectionException();
            }

            var25.add(var18);
         }

         if (var25 != null) {
            throw var25;
         } else {
            Finder var27 = this.generateFindByPrimaryKeyFinder(var1);
            var27.setFinderLoadsBean(var2.getFindersLoadBean());
            var1.addFinder(var27);
         }
      }
   }

   private void processEjbSelect(Iterator var1) {
      while(var1.hasNext()) {
         Finder var2 = (Finder)var1.next();
         if ((var2.isSelect() || var2.isSelectInEntity()) && (var2.getQueryType() == 4 || var2.getQueryType() == 2)) {
            RDBMSBean var3 = var2.getSelectBeanTarget();
            CMPBeanDescriptor var4 = (CMPBeanDescriptor)this.beanMap.get(var3.getEjbName());
            var2.setFinderLoadsBean(var4.getFindersLoadBean());
            var3.addToEjbSelectInternalList(var2);
         }
      }

   }

   private void checkClassFinderForXMLFinder(String var1, Iterator var2, Collection var3) throws ErrorCollectionException {
      ErrorCollectionException var4 = null;
      EJBComplianceTextFormatter var5 = new EJBComplianceTextFormatter();

      while(true) {
         Method var6;
         do {
            if (!var2.hasNext()) {
               if (var4 != null) {
                  throw var4;
               }

               return;
            }

            var6 = (Method)var2.next();
         } while(var6.getName().equals("findByPrimaryKey"));

         Iterator var7 = var3.iterator();
         boolean var8 = false;

         while(var7.hasNext()) {
            EntityBeanQuery var9 = (EntityBeanQuery)var7.next();
            if (this.methodMatchesQuery(var6, var9)) {
               var8 = true;
               break;
            }
         }

         if (!var8) {
            if (var4 == null) {
               var4 = new ErrorCollectionException();
            }

            var4.add(new InvalidFinderException(8, var5.MISSING_QUERY_IN_EJBJAR(var1, DDUtils.getMethodSignature(var6)), new DescriptorErrorInfo("<ejb-ql>", var1, DDUtils.getMethodSignature(var6))));
         }
      }
   }

   public WeblogicRdbmsJarBean parseXMLFile(VirtualJarFile var1, String var2, EjbDescriptorBean var3) throws Exception {
      WeblogicRdbmsJarBean var4 = null;
      if (debugLogger.isDebugEnabled()) {
         debug("called parseXMLFile() fileName=" + var2);
      }

      BufferedInputStream var5 = null;

      WeblogicRdbmsJarBean var8;
      try {
         WeblogicRdbmsJarBean var28;
         if (var1 == null) {
            var4 = this.parseXMLFileWithSchema((VirtualJarFile)null, var2, var3);
            if (var4 == null) {
               Loggable var29 = EJBLogger.logRdbmsDescriptorNotFoundInJarLoggable(var2);
               throw new RDBMSException(var29.getMessage());
            }

            var28 = var4;
            return var28;
         }

         ZipEntry var31 = var1.getEntry(var2);
         if (var31 != null) {
            InputStream var6 = var1.getInputStream(var31);
            var5 = new BufferedInputStream(var6);
            var28 = parseXMLFile(var5, var2, var3, this.beanMap);
            return var28;
         }

         var4 = this.parseXMLFileWithSchema(var1, var2, var3);
         if (var4 == null) {
            Loggable var32 = EJBLogger.logRdbmsDescriptorNotFoundInJarLoggable(var2);
            throw new RDBMSException(var32.getMessage());
         }

         var8 = var4;
      } catch (ProcessorFactoryException var25) {
         if (var5 != null) {
            try {
               var5.reset();
               var4 = this.parseXMLFileWithSchema(var1, var2, var3);
               if (var4 == null) {
                  Loggable var30 = EJBLogger.logRdbmsDescriptorNotFoundInJarLoggable(var2);
                  throw new RDBMSException(var30.getMessage());
               }

               WeblogicRdbmsJarBean var7 = var4;
               return var7;
            } catch (RDBMSException var23) {
               throw var23;
            } catch (Exception var24) {
               throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var24));
            }
         }

         throw var25;
      } catch (IOException var26) {
         throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var26));
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var22) {
         }

      }

      return var8;
   }

   public static WeblogicRdbmsJarBean parseXMLFile(BufferedInputStream var0, String var1, EjbDescriptorBean var2, Map var3) throws Exception {
      CMPDDParser var4 = null;
      ProcessorFactory var5 = new ProcessorFactory();
      DDUtils.getXMLEncoding(var0, var1);
      var0.mark(1048576);
      if (isSchemaBasedDD(var0)) {
         throw new ProcessorFactoryException("This is a Schema based DD");
      } else {
         var0.reset();
         var0.mark(1048576);
         XMLProcessor var7 = var5.getProcessor((InputStream)var0, RDBMSUtils.validRdbmsCmp20JarPublicIds);
         var0.reset();
         if (var7 instanceof CMPDDParser) {
            var4 = (CMPDDParser)var7;
            if (null != var4) {
               var4.setFileName(var1);
               var4.setEJBDescriptor(var2);
               var4.process((InputStream)var0);
               adjustDefaults(var4, var2, var3);
               return var4.getDescriptorMBean();
            } else {
               throw new AssertionError("Couldn't find a loader RDBMS CMP deployment descriptor '" + var1 + "'. The document probably references an unknown DTD.");
            }
         } else {
            Loggable var8 = EJBLogger.logCmp20DDHasWrongDocumentTypeLoggable();
            String var9 = var8.getMessage() + "\n";

            for(int var10 = 0; var10 < RDBMSUtils.validRdbmsCmp20JarPublicIds.length; ++var10) {
               var9 = var9 + "\"" + RDBMSUtils.validRdbmsCmp20JarPublicIds[var10] + "\"\n";
            }

            throw new RDBMSException(var9);
         }
      }
   }

   private WeblogicRdbmsJarBean parseXMLFileWithSchema(VirtualJarFile var1, String var2, EjbDescriptorBean var3) throws Exception {
      try {
         WeblogicRdbmsJarBean var4 = null;
         RDBMSDescriptor var5 = new RDBMSDescriptor(var1, var2, var3.getAppName(), var3.getUri(), var3.getDeploymentPlan(), var3.getConfigDirectory());
         if (var3.isReadOnly()) {
            var4 = (WeblogicRdbmsJarBean)var5.getDescriptorBean();
         } else {
            var4 = (WeblogicRdbmsJarBean)var5.getEditableDescriptorBean();
         }

         var3.addWeblogicRdbmsJarBean(var4);
         return var4;
      } catch (Exception var6) {
         throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var6));
      }
   }

   public void preCodeGeneration(CMPCodeGenerator var1) throws ErrorCollectionException {
      assert var1 instanceof RDBMSCodeGenerator;

      if (debugLogger.isDebugEnabled()) {
         debug("called preCodeGeneration()");
      }

      RDBMSCodeGenerator var2 = (RDBMSCodeGenerator)var1;
      var2.setCMPBeanDescriptor(this.bd);
      var2.setRDBMSBean(this.currBean);
      var2.setFinderList(this.currBean.getFinderList());
      var2.setEjbSelectInternalList(this.currBean.getEjbSelectInternalList());
      var2.setParameterMap(this.parameterMap);
   }

   public void postCodeGeneration(CMPCodeGenerator var1) {
      assert var1 instanceof RDBMSCodeGenerator;

      if (debugLogger.isDebugEnabled()) {
         debug("called postCodeGeneration()");
      }

   }

   private void validateFinderQueries() throws ErrorCollectionException {
      if (debugLogger.isDebugEnabled()) {
         debug("called validateFinderQueries.");
      }

      ErrorCollectionException var1 = new ErrorCollectionException();
      Map var2 = this.rdbmsDeployment.getRDBMSBeanMap();
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         RDBMSBean var4 = (RDBMSBean)((Map.Entry)var3.next()).getValue();
         Iterator var5 = var4.getFinders();

         while(var5.hasNext()) {
            Finder var6 = (Finder)var5.next();

            try {
               if (var6 instanceof EjbqlFinder) {
                  ((EjbqlFinder)var6).parseExpression();
               }
            } catch (EJBQLCompilerException var8) {
               var1.add(var8);
            }
         }
      }

      if (var1.getExceptions().size() > 0) {
         throw var1;
      }
   }

   private void checkIsValidRelationshipCaching() throws ErrorCollectionException {
      if (debugLogger.isDebugEnabled()) {
         debug("called checkIsValidRelationshipCaching.");
      }

      ErrorCollectionException var1 = new ErrorCollectionException();
      Map var2 = this.rdbmsDeployment.getRDBMSBeanMap();
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         RDBMSBean var4 = (RDBMSBean)((Map.Entry)var3.next()).getValue();
         Iterator var5 = var4.getRelationshipCachings().iterator();

         while(var5.hasNext()) {
            RelationshipCaching var6 = (RelationshipCaching)var5.next();
            Iterator var7 = var6.getCachingElements().iterator();
            this.checkIsValidRelationshipCachingForCachingElements(var4, var7, var6, var1, var4);
         }
      }

      if (var1.getExceptions().size() > 0) {
         throw var1;
      }
   }

   private void checkIsValidRelationshipCachingForCachingElements(RDBMSBean var1, Iterator var2, RelationshipCaching var3, ErrorCollectionException var4, RDBMSBean var5) {
      EJBComplianceTextFormatter var6 = new EJBComplianceTextFormatter();
      Map var7 = this.rdbmsDeployment.getRDBMSBeanMap();

      while(true) {
         while(var2.hasNext()) {
            RelationshipCaching.CachingElement var8 = (RelationshipCaching.CachingElement)var2.next();
            String var9 = var8.getCmrField();
            String var10 = var8.getGroupName();
            if (!var1.getAllCmrFields().contains(var9)) {
               var4.add(new ComplianceException(var6.RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_CMR_FIELD(var1.getEjbName(), var3.getCachingName(), var9), new DescriptorErrorInfo("<relationship-caching>", var1.getEjbName(), var3.getCachingName())));
            } else {
               RDBMSBean var11 = var1.getRelatedRDBMSBean(var9);
               if (var11.getFieldGroup(var10) == null) {
                  var4.add(new ComplianceException(var6.RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_GROUP_NAME(var1.getEjbName(), var3.getCachingName(), var11.getEjbName(), var10), new DescriptorErrorInfo("<relationship-caching>", var1.getEjbName(), var3.getCachingName())));
               }

               if (var5.isOptimistic() && !var11.isOptimistic() && !var11.isReadOnly()) {
                  var4.add(new ComplianceException(var6.RELATIONSHIP_CACHING_INCONSISTENT_CONCURRENCY_STRATEGY(var5.getEjbName(), var3.getCachingName(), var11.getEjbName()), new DescriptorErrorInfo("<relationship-caching>", var5.getEjbName(), var3.getCachingName())));
               }

               Iterator var12 = this.relationships.getAllEjbRelations().values().iterator();

               while(var12.hasNext()) {
                  EjbRelation var13 = (EjbRelation)var12.next();
                  Iterator var14 = var13.getAllEjbRelationshipRoles().iterator();
                  EjbRelationshipRole var15 = (EjbRelationshipRole)var14.next();
                  EjbRelationshipRole var16 = (EjbRelationshipRole)var14.next();
                  boolean var17 = false;
                  if (var15.getCmrField() != null) {
                     var17 = var9.equals(var15.getCmrField().getName());
                  }

                  if (var16.getCmrField() != null) {
                     var17 = var17 || var9.equals(var16.getCmrField().getName());
                  }

                  if (var17) {
                     RDBMSBean var18 = (RDBMSBean)var7.get(var15.getRoleSource().getEjbName());
                     RDBMSBean var19 = (RDBMSBean)var7.get(var16.getRoleSource().getEjbName());
                     if ((var1 == var18 && var11 == var19 || var1 == var19 && var11 == var18) && var15.getMultiplicity().equalsIgnoreCase("many") && var16.getMultiplicity().equalsIgnoreCase("many")) {
                        var4.add(new ComplianceException(var6.RELATIONSHIP_CACHING_CANNOT_BE_SPECIFIED(var1.getEjbName(), var3.getCachingName(), var11.getEjbName())));
                     }
                  }
               }

               Iterator var20 = var8.getCachingElements().iterator();
               if (var20.hasNext()) {
                  this.checkIsValidRelationshipCachingForCachingElements(var11, var20, var3, var4, var5);
               }
            }
         }

         return;
      }
   }

   private Finder generateFindByPrimaryKeyFinder(RDBMSBean var1) throws Exception {
      assert this.primaryKeyClass != null : "PrimaryKeyClass is null";

      assert this.ejbClass != null : "ejbClass is null";

      assert var1 != null : "currBean is null";

      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp.rdbms.Deployer.generateFindByPrimaryKeyFinder()");
      }

      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      if (var2 == null) {
         Loggable var13 = EJBLogger.logCmpBeanDescriptorIsNullLoggable("Deployer.generateFindByPrimaryKeyFinder", var1.getEjbName());
         throw new Exception(var13.getMessage());
      } else {
         Class var3 = var2.getPrimaryKeyClass();
         String var4 = "findByPrimaryKey";
         String var5 = var1.findByPrimaryKeyQuery();
         if (debugLogger.isDebugEnabled()) {
            debug("Created findByPrimaryKey query of " + var5);
         }

         Method var6 = null;

         Class var7;
         try {
            var7 = null;
            if (var2.hasRemoteClientView()) {
               var7 = var2.getHomeInterfaceClass();
            } else {
               var7 = var2.getLocalHomeInterfaceClass();
            }

            List var8 = MethodUtils.getFinderMethodList(var7);
            var6 = this.getFindByPrimaryKeyMethod(var8);
         } catch (Exception var11) {
            throw new AssertionError("Caught an Exception while setting in generated finder props: " + var11.toString());
         }

         var7 = null;

         EjbqlFinder var15;
         try {
            Class[] var14 = var6.getParameterTypes();
            String[] var9 = new String[var14.length];
            int var10 = 0;

            while(true) {
               if (var10 >= var9.length) {
                  var15 = (EjbqlFinder)var1.createFinder(var4, var9, var5);
                  break;
               }

               var9[var10] = var14[var10].getName();
               ++var10;
            }
         } catch (InvalidFinderException var12) {
            throw new AssertionError("Caught an InvalidFinderException in generated finder: " + var12);
         }

         var15.setKeyFinder(true);
         var15.setKeyBean(var1);
         var15.setMethods(new Method[]{var6});
         return var15;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(150);
      var1.append("[weblogic.cmp.rdbms.Deployer =\n");
      var1.append("\tbean class = " + (this.ejbClass != null ? this.ejbClass.getName() : "null"));
      var1.append("\n");
      var1.append("\tprimary key class = " + (this.primaryKeyClass != null ? this.primaryKeyClass.getName() : "null"));
      var1.append("\n");
      var1.append("\n");
      var1.append("\tparameterMap = {");
      Iterator var2;
      if (this.parameterMap != null && this.parameterMap.keySet() != null) {
         var2 = this.parameterMap.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append("(" + var3 + "," + "value" + ")");
            if (var2.hasNext()) {
               var1.append(", ");
            } else {
               var1.append("}\n");
            }
         }
      } else {
         var1.append("null}\n");
      }

      var1.append("\tfieldList = {");
      Field var4;
      if (this.fieldList == null) {
         var1.append("null}\n");
      } else {
         var2 = this.fieldList.iterator();

         while(var2.hasNext()) {
            var4 = (Field)var2.next();
            var1.append(var4.getName());
            if (var2.hasNext()) {
               var1.append(", ");
            } else {
               var1.append("}\n");
            }
         }
      }

      var1.append("\tfinderList = {");
      if (this.finderList == null) {
         var1.append("null}\n");
      } else {
         var2 = this.finderList.iterator();

         while(var2.hasNext()) {
            Method var5 = (Method)var2.next();
            var1.append(var5.getName());
            if (var2.hasNext()) {
               var1.append(", ");
            } else {
               var1.append("}\n");
            }
         }
      }

      var1.append("\tprimaryKeyList = {");
      if (this.primaryKeyList == null) {
         var1.append("null}\n");
      } else {
         var2 = this.primaryKeyList.iterator();

         while(var2.hasNext()) {
            var4 = (Field)var2.next();
            var1.append(var4.getName());
            if (var2.hasNext()) {
               var1.append(", ");
            } else {
               var1.append("}\n");
            }
         }
      }

      if (this.currBean == null) {
         var1.append("\tcurrBean = null\n");
      } else {
         var1.append("\t" + this.currBean.toString());
      }

      var1.append("]");
      return var1.toString();
   }

   public RDBMSDeployment getJarDeployment() {
      return this.rdbmsDeployment;
   }

   public static boolean isSchemaBasedDD(BufferedInputStream var0) throws ProcessorFactoryException, IOException {
      byte[] var1 = new byte[1000];
      var0.read(var1, 0, 1000);
      String var2 = new String(var1);
      return var2.indexOf("<!DOCTYPE") == -1;
   }

   private static void debug(String var0) {
      debugLogger.debug("[Deployer] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
