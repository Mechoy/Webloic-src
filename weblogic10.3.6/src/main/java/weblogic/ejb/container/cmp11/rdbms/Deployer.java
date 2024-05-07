package weblogic.ejb.container.cmp11.rdbms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSDescriptor;
import weblogic.ejb.container.cmp11.rdbms.codegen.RDBMSCodeGenerator;
import weblogic.ejb.container.cmp11.rdbms.codegen.TypeUtils;
import weblogic.ejb.container.cmp11.rdbms.compliance.RDBMSComplianceChecker;
import weblogic.ejb.container.cmp11.rdbms.finders.Finder;
import weblogic.ejb.container.cmp11.rdbms.finders.FinderNotFoundException;
import weblogic.ejb.container.cmp11.rdbms.finders.IllegalExpressionException;
import weblogic.ejb.container.cmp11.rdbms.finders.InvalidFinderException;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.ejb.container.persistence.spi.JarDeployment;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
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
   private List primaryKeyList = null;
   private Map parameterMap = null;
   private Class ejbClass = null;
   private Class homeInterfaceClass = null;
   private Class remoteInterfaceClass = null;
   private Class primaryKeyClass = null;
   private boolean isCompoundPK = true;
   private CMPBeanDescriptor bd = null;
   private RDBMSDeployment rdbmsDeployment = null;

   public void setup(JarDeployment var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.setup()");
      }

      this.currBean = new RDBMSBean();
      this.rdbmsDeployment = (RDBMSDeployment)var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      assert var1 != null;

      this.bd = var1;
      this.ejbClass = this.bd.getBeanClass();
      this.homeInterfaceClass = this.bd.getHomeInterfaceClass();
      this.remoteInterfaceClass = this.bd.getRemoteInterfaceClass();
      this.primaryKeyClass = this.bd.getPrimaryKeyClass();
      this.finderList = ClassUtils.getFinderMethodList(this.homeInterfaceClass);
      this.primaryKeyList = new ArrayList(this.bd.getPrimaryKeyFieldNames());
      this.isCompoundPK = this.bd.hasComplexPrimaryKey();
      this.fieldList = new ArrayList(this.bd.getCMFieldNames());
   }

   public void setBeanMap(Map var1) {
      throw new AssertionError("Deployer.setBeanMap called for a 1.1 CMP bean.");
   }

   public void setRelationships(Relationships var1) {
      throw new AssertionError("Deployer.setRelationships called for a 1.1 CMP bean.");
   }

   public void setDependentMap(Map var1) {
      throw new AssertionError("Deployer.setDependentMap called for a 1.1 CMP bean.");
   }

   public void setParameters(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.setParameters()");
      }

      this.parameterMap = new HashMap(var1);
   }

   public void initializePersistenceManager(PersistenceManager var1) throws WLDeploymentException {
      assert this.bd != null;

      PersistenceManagerImpl var2 = (PersistenceManagerImpl)var1;
      var2.setBeanInfo(this.currBean);
      var2.bd = this.bd;
   }

   private boolean fieldListContainsField(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.fieldListContainsField()");
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
         debug("ejb20.cmp11.rdbms.Deployer.currBeanContainsField()");
      }

      Iterator var2 = this.currBean.getObjectLinks();

      RDBMSBean.ObjectLink var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (RDBMSBean.ObjectLink)var2.next();
      } while(!var3.getBeanField().equals(var1));

      return true;
   }

   public synchronized RDBMSBean getTypeSpecificData() {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.getTypeSpecificData()");
      }

      return this.currBean;
   }

   public synchronized void setTypeSpecificData(RDBMSBean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.setTypeSpecificData()");
      }

      this.currBean = var1;
   }

   public RDBMSDeploymentInfo parseXMLFile(VirtualJarFile var1, String var2, String var3, ProcessorFactory var4, EjbDescriptorBean var5) throws Exception {
      BufferedInputStream var6 = null;
      if (debugLogger.isDebugEnabled()) {
         debug("getRDBMSBean(" + var1 + ")");
      }

      WeblogicRdbmsJarBean var7 = null;
      CMPDDParser.CompatibilitySettings var8 = null;

      try {
         Loggable var10;
         try {
            ZipEntry var9 = var1.getEntry(var2);
            if (var9 == null) {
               var7 = this.parseXMLFileWithSchema((VirtualJarFile)null, var2, var5);
               if (var7 == null) {
                  var10 = EJBLogger.logRdbmsDescriptorNotFoundInJarLoggable(var2);
                  throw new RDBMSException(var10.getMessage());
               }
            } else {
               InputStream var32 = var1.getInputStream(var9);
               var6 = new BufferedInputStream(var32);
               String var11 = DDUtils.getXMLEncoding(var6, var2);
               var6.mark(1048576);
               CMPDDParser var12 = null;
               XMLProcessor var13 = var4.getProcessor((InputStream)var6, RDBMSUtils.validRdbmsCmp11JarPublicIds);
               var6.reset();
               if (var13 != null) {
                  if (!(var13 instanceof CMPDDParser)) {
                     Loggable var14 = EJBLogger.logincorrectXMLFileVersionLoggable(var2, var3, var13.getClass().getName());
                     throw new RDBMSException(var14.getMessage());
                  }

                  var12 = (CMPDDParser)var13;
               }

               if (null == var12) {
                  throw new AssertionError("Couldn't find a loader for weblogic-cmp-ejb-jar.xml. The document probably references an unknown dtd.");
               }

               var12.setCurrentEJBName(var3);
               var12.setFileName(var2);
               var12.setEJBDescriptor(var5);
               var12.setEncoding(var11);
               var12.process((InputStream)var6);
               var7 = var12.getDescriptorMBean();
               var8 = var12.getCompatibilitySettings();
            }
         } catch (ProcessorFactoryException var29) {
            try {
               var6.reset();
               var7 = this.parseXMLFileWithSchema(var1, var2, var5);
               if (var7 == null) {
                  var10 = EJBLogger.logRdbmsDescriptorNotFoundInJarLoggable(var2);
                  throw new RDBMSException(var10.getMessage());
               }
            } catch (RDBMSException var27) {
               throw var27;
            } catch (Exception var28) {
               throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var28));
            }

            var5.addWeblogicRdbms11JarBean(var7);
         } catch (IOException var30) {
            throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var30));
         }
      } finally {
         try {
            if (var6 != null) {
               var6.close();
            }
         } catch (IOException var26) {
         }

      }

      return new RDBMSDeploymentInfo(var7, var8, var2);
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

         return var4;
      } catch (Exception var6) {
         throw new RDBMSException(StackTraceUtils.throwable2StackTrace(var6));
      }
   }

   public synchronized void readTypeSpecificData(VirtualJarFile var1, String var2) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("readTypeSpecificData called.");
      }

      if (this.rdbmsDeployment.needToReadFile(var2)) {
         if (debugLogger.isDebugEnabled()) {
            debug("processing XML for bean: " + this.bd.getEJBName());
         }

         try {
            ProcessorFactory var3 = new ProcessorFactory();
            RDBMSDeploymentInfo var4 = this.parseXMLFile(var1, var2, this.bd.getEJBName(), var3, this.bd.getEjbDescriptorBean());
            this.rdbmsDeployment.addRdbmsBeans(var4.getRDBMSBeanMap());
            this.rdbmsDeployment.addDescriptorMBean(var4.getWeblogicRdbmsJarBean());
            this.rdbmsDeployment.addFileName(var2);
         } catch (Exception var12) {
            if (debugLogger.isDebugEnabled()) {
               debug("parseXMLFile exception: " + var12);
            }

            throw var12;
         }
      }

      this.currBean = this.rdbmsDeployment.getRDBMSBean(this.bd.getEJBName());
      this.currBean.setCMPBeanDescriptor(this.bd);
      if (this.currBean == null) {
         Loggable var14 = EJBLogger.logUnableToFindBeanInRDBMSDescriptorLoggable(this.bd.getEJBName(), var2);
         throw new RDBMSException(var14.getMessage());
      } else {
         RDBMSComplianceChecker var13 = new RDBMSComplianceChecker(this.bd, this.ejbClass, this.fieldList);
         var13.checkCompliance();
         Iterator var15 = this.currBean.getFinders();
         Finder var5 = null;

         while(var15.hasNext()) {
            var5 = (Finder)var15.next();
            Iterator var6 = var5.getFinderExpressions();
            if (var6.hasNext()) {
               Loggable var7 = EJBLogger.logPersistenceUsesFinderExpressionsLoggable();
               throw new WLDeploymentException(var7.getMessage());
            }
         }

         boolean var16 = false;
         var15 = this.currBean.getFinders();
         var5 = null;

         while(var15.hasNext()) {
            var5 = (Finder)var15.next();
            if (var5.getName().equals("findByPrimaryKey")) {
               var16 = true;
               var15.remove();
               break;
            }
         }

         Finder var17 = this.generateFindByPrimaryKeyFinder();
         if (var16) {
            var17.setFinderOptions(var5.getFinderOptions());
         }

         this.currBean.addFinder(var17);
         if (debugLogger.isDebugEnabled()) {
            debug("currBean=" + this.currBean);
         }

         String var8 = "False";
         String var9 = "";
         int var10 = 0;
         WeblogicRdbmsJarBean var11 = this.rdbmsDeployment.getDescriptorMBean(this.currBean.getEjbName());
         if (var11 != null) {
            if (var11.isCreateDefaultDbmsTables()) {
               var8 = "CreateOnly";
            } else {
               var8 = "Disabled";
            }

            var9 = var11.getValidateDbSchemaWith();
            var10 = MethodUtils.dbmsType2int(var11.getDatabaseType());
         }

         if (var9 == null) {
            var9 = "";
         }

         this.currBean.setCreateDefaultDBMSTables(var8);
         this.currBean.setValidateDbSchemaWith(var9);
         this.currBean.setDatabaseType(var10);
         this.currBean.setPrimaryKeyFields(this.primaryKeyList);
      }
   }

   public void saveTypeSpecificData(OutputStream var1) {
      assert var1 != null;

      assert this.currBean != null;

      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.saveTypeSpecificData()");
         debug("   Deployer is " + this);
      }

      BeanWriter var2 = new BeanWriter();
      var2.putRDBMSBean(this.currBean, var1);
   }

   public void preCodeGeneration(CMPCodeGenerator var1) throws ErrorCollectionException {
      assert var1 instanceof RDBMSCodeGenerator;

      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.preCodeGeneration()");
      }

      RDBMSCodeGenerator var2 = (RDBMSCodeGenerator)var1;
      this.validateBeanSettings();
      this.currBean.generateFinderSQLStatements();
      var2.setCMPBeanDescriptor(this.bd);
      var2.setRDBMSBean(this.currBean);
      var2.setFinderList(this.finderList);
      var2.setPrimaryKeyFields(this.primaryKeyList);
      var2.setParameterMap(this.parameterMap);
      var2.setCMFields(this.currBean.getFieldNamesList());
   }

   public void postCodeGeneration(CMPCodeGenerator var1) {
      assert var1 instanceof RDBMSCodeGenerator;

      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.postCodeGeneration()");
      }

      RDBMSCodeGenerator var2 = (RDBMSCodeGenerator)var1;
   }

   private void validateBeanSettings() throws InvalidBeanException {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.validateBeanSettings()");
      }

      InvalidBeanException var1 = new InvalidBeanException();
      this.validateAttributeMapAndFieldList(var1);
      this.validateAttributeFieldsInBean(var1);
      this.validateFinderMethodsHaveDescriptors(var1);
      this.validateFinderExpressionsHaveAppropriateTypes(var1);
      this.validateFinderQueries(var1);
      if (var1.getExceptions().size() > 0) {
         throw var1;
      }
   }

   private boolean validateFinderMethodsHaveDescriptors(InvalidBeanException var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.validateFinderMethodsHaveDescriptors()");
      }

      Iterator var2 = this.finderList.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         Finder var4 = this.currBean.getFinderForMethod(var3);
         if (var4 == null) {
            var1.add(new FinderNotFoundException(var3, this.currBean.getEjbName(), this.currBean.getFileName()));
         }
      }

      return true;
   }

   private void validateFinderExpressionsHaveAppropriateTypes(InvalidBeanException var1) {
      IllegalExpressionException var2 = null;
      Iterator var3 = this.currBean.getFinders();

      while(var3.hasNext()) {
         Finder var4 = (Finder)var3.next();
         Iterator var5 = var4.getFinderExpressions();

         while(var5.hasNext()) {
            Finder.FinderExpression var6 = (Finder.FinderExpression)var5.next();
            String var7 = var6.getExpressionType();
            Class var8 = null;

            try {
               var8 = ClassUtils.nameToClass(var7, this.getClass().getClassLoader());
            } catch (ClassNotFoundException var10) {
               var2 = new IllegalExpressionException(3, var7, var6);
               var2.setFinder(var4);
               var1.add(var2);
            }

            if (!TypeUtils.isValidSQLType(var8)) {
               var2 = new IllegalExpressionException(3, var7, var6);
               var2.setFinder(var4);
               var1.add(var2);
            }
         }
      }

   }

   private void validateAttributeFieldsInBean(InvalidBeanException var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.validateAttributeFieldsInBean()");
      }

      Iterator var2 = this.currBean.getObjectLinks();

      while(var2.hasNext()) {
         RDBMSBean.ObjectLink var3 = (RDBMSBean.ObjectLink)var2.next();
         String var4 = var3.getBeanField();

         try {
            this.ejbClass.getField(var4);
         } catch (NoSuchFieldException var6) {
            var1.add(new AttributeMapException(2, var4));
         }
      }

   }

   private void validateAttributeMapAndFieldList(InvalidBeanException var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.validateAttributeMapAndFieldList()");
      }

      Iterator var2 = this.currBean.getObjectLinks();

      String var4;
      while(var2.hasNext()) {
         RDBMSBean.ObjectLink var3 = (RDBMSBean.ObjectLink)var2.next();
         var4 = var3.getBeanField();
         if (!this.fieldListContainsField(var4)) {
            var1.add(new AttributeMapException(3, var4));
         }
      }

      Iterator var5 = this.fieldList.iterator();

      while(var5.hasNext()) {
         var4 = (String)var5.next();
         if (!this.currBeanContainsField(var4)) {
            var1.add(new AttributeMapException(4, var4));
         }
      }

   }

   private void validateFinderQueries(InvalidBeanException var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.Deployer.validateFinderQueries(()");
      }

      Iterator var2 = this.currBean.getFinders();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();

         IllegalExpressionException var5;
         try {
            var3.parseExpression();
         } catch (EJBCException var6) {
            var5 = new IllegalExpressionException(4, var3.getWeblogicQuery());
            var5.setFinder(var3);
            var1.add(var5);
         } catch (InvalidFinderException var7) {
            var5 = new IllegalExpressionException(4, var3.getWeblogicQuery());
            var5.setFinder(var3);
            var1.add(var5);
         }
      }

   }

   private Finder generateFindByPrimaryKeyFinder() {
      assert this.primaryKeyClass != null : "PrimaryKeyClass is null";

      assert this.ejbClass != null : "ejbClass is null";

      assert this.currBean != null : "currBean is null";

      String var1 = "findByPrimaryKey";
      StringBuffer var2 = new StringBuffer();
      Set var3 = this.bd.getPrimaryKeyFieldNames();

      assert var3 != null : "No primaryKeyList set in Deployer.";

      String[] var4 = (String[])((String[])var3.toArray(new String[0]));
      if (var4.length > 1) {
         var2.append("(& ");
      }

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var6 = var4[var5];

         assert var6 != null : "Field " + var5 + " in pkArray is null.";

         String var7 = " (= $" + var5 + " " + var6 + " ) ";
         var2.append(var7);
      }

      if (var4.length > 1) {
         var2.append(")");
      }

      if (debugLogger.isDebugEnabled()) {
         debug("Created findByPrimaryKey query of " + var2);
      }

      Finder var9 = null;

      try {
         var9 = new Finder(var1, var2.toString());
      } catch (InvalidFinderException var8) {
         throw new AssertionError("Caught an InvalidFinderException in generated finder: " + var8);
      }

      var9.addParameterType(this.primaryKeyClass.getName());
      return var9;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(150);
      var1.append("[weblogic.cmp11.rdbms.Deployer =\n");
      var1.append("\tremote interface = " + (this.remoteInterfaceClass != null ? this.remoteInterfaceClass.getName() : "null"));
      var1.append("\n");
      var1.append("\thome interface = " + (this.homeInterfaceClass != null ? this.homeInterfaceClass.getName() : "null"));
      var1.append("\n");
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

   public Object getEntityManager() {
      return null;
   }

   private static void debug(String var0) {
      debugLogger.debug("[Deployer] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
