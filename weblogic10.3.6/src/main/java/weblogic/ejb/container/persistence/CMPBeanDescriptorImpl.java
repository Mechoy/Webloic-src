package weblogic.ejb.container.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.naming.Name;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.deployer.DeploymentDescriptorException;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.Debug;

public class CMPBeanDescriptorImpl implements CMPBeanDescriptor {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private Class beanClass;
   private Class homeClass;
   private Class remoteClass;
   private Class localHomeClass;
   private Class localClass;
   private String homeInterfaceName;
   private String remoteInterfaceName;
   private String localHomeInterfaceName;
   private String localInterfaceName;
   private String abstractSchemaName;
   private String javaClassName;
   private final Name jndiName;
   private final Name localJndiName;
   private final String ejbName;
   private final boolean cacheBetweenTransactions;
   private final boolean boxCarUpdates;
   private final Set containerFieldSet;
   private final String primaryKeyFieldName;
   private Class primaryKeyClass = null;
   private boolean hasComplexPrimaryKey = false;
   private final boolean isReentrant;
   private final String isModifiedMethodName;
   private final boolean findersLoadBean;
   private final Collection allQueries;
   private String genBeanClassName;
   private String genBeanInterfaceName;
   private int concurrencyStrategy;
   private boolean isDynamicQueriesEnabled;
   private EjbDescriptorBean desc = null;
   private List primaryKeyFieldList = null;
   private Set primaryKeyFieldNameSet = null;
   private Hashtable CMFieldTable;
   private String primaryKeyClassName = null;
   private boolean hasLocalClientView = false;
   private boolean hasRemoteClientView = false;
   private EntityBeanInfo ebi = null;

   public CMPBeanDescriptorImpl(EntityBeanInfo var1, EjbDescriptorBean var2) throws DeploymentDescriptorException {
      this.ebi = var1;
      this.desc = var2;
      if (var1.hasLocalClientView()) {
         this.localHomeInterfaceName = var1.getLocalHomeInterfaceName();
         this.localInterfaceName = var1.getLocalInterfaceName();
      }

      if (var1.hasRemoteClientView()) {
         this.homeInterfaceName = var1.getHomeInterfaceName();
         this.remoteInterfaceName = var1.getRemoteInterfaceName();
      }

      if (this.isEJB30()) {
         this.javaClassName = var1.getBeanClassName();
      }

      this.hasLocalClientView = var1.hasLocalClientView();
      this.hasRemoteClientView = var1.hasRemoteClientView();
      this.beanClass = var1.getBeanClass();
      if (debug) {
         Debug.assertion(this.beanClass != null);
      }

      this.jndiName = var1.getJNDIName();
      this.localJndiName = var1.getLocalJNDIName();
      this.ejbName = var1.getEJBName();
      CMPInfo var3 = var1.getCMPInfo();
      this.genBeanClassName = var3.getGeneratedBeanClassName();
      this.genBeanInterfaceName = var1.getGeneratedBeanInterfaceName();
      this.concurrencyStrategy = var1.getConcurrencyStrategy();
      this.containerFieldSet = new HashSet();
      this.CMFieldTable = new Hashtable();
      Collection var4 = var3.getAllContainerManagedFieldNames();

      String var6;
      Class var7;
      for(Iterator var5 = var4.iterator(); var5.hasNext(); this.CMFieldTable.put(var6, var7)) {
         var6 = (String)var5.next();
         this.containerFieldSet.add(var6);
         if (var1.getCMPInfo().uses20CMP()) {
            Method var8 = PersistenceUtils.getMethodIncludeSuper(this.beanClass, RDBMSUtils.getterMethodName(var6), (Class[])null);
            if (var8 == null) {
               throw new DeploymentDescriptorException("For cmp-field '" + var6 + "' of bean '" + this.ejbName + "', we expected to find a corresponding '" + RDBMSUtils.getterMethodName(var6) + "' method in the abstract bean class.  " + "Compilation cannot continue without this 'get' method", new DescriptorErrorInfo("<cmp-field>", this.ejbName, var6));
            }

            var7 = var8.getReturnType();
         } else {
            try {
               var7 = this.beanClass.getField(var6).getType();
            } catch (NoSuchFieldException var11) {
               throw new DeploymentDescriptorException("Unable to find public field '" + var6 + "' on bean class: " + this.beanClass.getName(), new DescriptorErrorInfo("<cmp-field>", this.ejbName, var6));
            }
         }
      }

      this.primaryKeyClassName = var1.getPrimaryKeyClassName();
      this.primaryKeyClass = var1.getPrimaryKeyClass();
      this.primaryKeyFieldList = new LinkedList();
      this.primaryKeyFieldNameSet = new TreeSet();
      this.primaryKeyFieldName = var3.getCMPrimaryKeyFieldName();
      if (this.primaryKeyFieldName == null && !var1.isUnknownPrimaryKey()) {
         var6 = null;
         if (this.primaryKeyClassName != null) {
            this.hasComplexPrimaryKey = true;
            Class var12 = this.primaryKeyClass;
            if (debug) {
               Debug.assertion(var12 != null, "Could not find PK class. " + this.primaryKeyClassName);
            }

            if (var12 != null) {
               Field[] var13 = var12.getFields();

               for(int var14 = 0; var14 < var13.length; ++var14) {
                  Field var9 = var13[var14];
                  if (!var9.getName().equals("serialVersionUID")) {
                     Class var10 = var9.getDeclaringClass();
                     if (!var10.equals(Object.class)) {
                        this.primaryKeyFieldList.add(var9);
                        this.primaryKeyFieldNameSet.add(var9.getName());
                     }
                  }
               }
            }
         }
      } else {
         this.hasComplexPrimaryKey = false;
         if (this.primaryKeyFieldName != null) {
            this.primaryKeyFieldNameSet.add(this.primaryKeyFieldName);
         }
      }

      this.cacheBetweenTransactions = var1.getCacheBetweenTransactions();
      this.boxCarUpdates = var1.getBoxCarUpdates();
      this.isReentrant = var1.isReentrant();
      this.isModifiedMethodName = var1.getIsModifiedMethodName();
      this.findersLoadBean = var3.findersLoadBean();
      this.abstractSchemaName = var3.getAbstractSchemaName();
      this.allQueries = var1.getAllQueries();
      this.isDynamicQueriesEnabled = var1.isDynamicQueriesEnabled();
   }

   public Method getGetterMethod(Class var1, String var2) {
      Method var3 = null;
      if (this.ebi.getCMPInfo().uses20CMP()) {
         var3 = PersistenceUtils.getMethodIncludeSuper(var1, RDBMSUtils.getterMethodName(var2), (Class[])null);
      }

      return var3;
   }

   public Method getSetterMethod(Class var1, String var2) {
      Method var3 = null;
      if (this.ebi.getCMPInfo().uses20CMP()) {
         var3 = PersistenceUtils.getMethodIncludeSuper(var1, RDBMSUtils.setterMethodName(var2), new Class[]{this.getFieldClass(var2)});
      }

      return var3;
   }

   public String getAbstractSchemaName() {
      return this.abstractSchemaName;
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public boolean hasLocalClientView() {
      return this.hasLocalClientView;
   }

   public boolean hasRemoteClientView() {
      return this.hasRemoteClientView;
   }

   public boolean isEJB30() {
      return false;
   }

   public Class getLocalHomeClass() {
      if (this.localHomeClass == null) {
         this.localHomeClass = this.ebi.getLocalHomeClass();
      }

      return this.localHomeClass;
   }

   public String getLocalHomeInterfaceName() {
      return this.localHomeInterfaceName;
   }

   public Class getLocalHomeInterfaceClass() {
      return this.ebi.getLocalHomeInterfaceClass();
   }

   public Class getHomeClass() {
      if (this.homeClass == null) {
         this.homeClass = this.ebi.getHomeClass();
      }

      return this.homeClass;
   }

   public String getHomeInterfaceName() {
      return this.homeInterfaceName;
   }

   public Class getHomeInterfaceClass() {
      return this.ebi.getHomeInterfaceClass();
   }

   public Class getLocalClass() {
      if (this.localClass == null) {
         this.localClass = this.ebi.getLocalClass();
      }

      return this.localClass;
   }

   public String getLocalInterfaceName() {
      return this.localInterfaceName;
   }

   public Class getLocalInterfaceClass() {
      return this.ebi.getLocalInterfaceClass();
   }

   public Class getRemoteClass() {
      if (this.remoteClass == null) {
         this.remoteClass = this.ebi.getRemoteClass();
      }

      return this.remoteClass;
   }

   public String getRemoteInterfaceName() {
      return this.remoteInterfaceName;
   }

   public Class getRemoteInterfaceClass() {
      return this.ebi.getRemoteInterfaceClass();
   }

   public Class getJavaClass() {
      return this.beanClass;
   }

   public String getJavaClassName() {
      return this.javaClassName;
   }

   public Class getBeanClass() {
      return this.beanClass;
   }

   public String getGeneratedBeanClassName() {
      return this.genBeanClassName;
   }

   public String getGeneratedBeanInterfaceName() {
      return this.genBeanInterfaceName;
   }

   public Name getJNDIName() {
      return this.jndiName;
   }

   public Name getLocalJNDIName() {
      return this.localJndiName;
   }

   public boolean getCacheBetweenTransactions() {
      return this.cacheBetweenTransactions;
   }

   public boolean getBoxCarUpdates() {
      return this.boxCarUpdates;
   }

   public int getTransactionTimeoutMS() {
      return this.ebi.getTransactionTimeoutMS();
   }

   public boolean isReadOnly() {
      return this.ebi.isReadOnly();
   }

   public boolean isOptimistic() {
      return this.ebi.isOptimistic();
   }

   public boolean hasComplexPrimaryKey() {
      return this.hasComplexPrimaryKey;
   }

   public Class getPrimaryKeyClass() {
      return this.primaryKeyClass;
   }

   public String getPrimaryKeyClassName() {
      return this.primaryKeyClassName;
   }

   public Set getPrimaryKeyFieldNames() {
      return this.primaryKeyFieldNameSet;
   }

   public void setPrimaryKeyField(String var1) {
      assert this.primaryKeyFieldNameSet.isEmpty();

      this.primaryKeyFieldNameSet.add(var1);
      if (this.containerFieldSet.contains(var1)) {
         this.primaryKeyClass = this.getFieldClass(var1);
      } else {
         this.primaryKeyClass = Long.class;
         this.containerFieldSet.add(var1);
         this.CMFieldTable.put(var1, this.primaryKeyClass);
      }

      this.primaryKeyClassName = ClassUtils.getCanonicalName(this.primaryKeyClass);
   }

   public Set getCMFieldNames() {
      return this.containerFieldSet;
   }

   public Class getFieldClass(String var1) {
      return (Class)this.CMFieldTable.get(var1);
   }

   public ClassLoader getClassLoader() {
      return this.ebi.getClassLoader();
   }

   public boolean getIsReentrant() {
      return this.isReentrant;
   }

   public String getIsModifiedMethodName() {
      return this.isModifiedMethodName;
   }

   public boolean getFindersLoadBean() {
      return this.findersLoadBean;
   }

   public Collection getAllQueries() {
      return this.allQueries;
   }

   public int getConcurrencyStrategy() {
      return this.concurrencyStrategy;
   }

   public EjbDescriptorBean getEjbDescriptorBean() {
      return this.desc;
   }

   public void beanImplClassChangeNotification() {
      this.beanClass = this.ebi.getBeanClass();
   }

   public boolean getIsDynamicQueriesEnabled() {
      return this.isDynamicQueriesEnabled;
   }

   public boolean isBeanClassAbstract() {
      int var1 = this.beanClass.getModifiers();
      return Modifier.isAbstract(var1);
   }

   public int getMaxQueriesInCache() {
      return this.ebi.getMaxQueriesInCache();
   }

   public void dump() {
      String var1 = " -------  CMPBeanDescriptorImpl: ";
      Debug.say(var1 + "HomeClass: " + this.getHomeClass());
      Debug.say(var1 + "HomeInterfaceName: " + this.getHomeInterfaceName());
      Debug.say(var1 + "LocalHomeClass: " + this.getLocalHomeClass());
      Debug.say(var1 + "LocalHomeInterfaceName: " + this.getLocalHomeInterfaceName());
      Debug.say(var1 + "BeanClass: " + this.getBeanClass());
      Debug.say(var1 + "RemoteClass: " + this.getRemoteClass());
      Debug.say(var1 + "RemoteInterfaceName: " + this.getRemoteInterfaceName());
      Debug.say(var1 + "LocalClass: " + this.getLocalClass());
      Debug.say(var1 + "LocalInterfaceName: " + this.getLocalInterfaceName());
      Debug.say(var1 + "JNDIName: " + this.getJNDIName());
      Debug.say(var1 + "LocalJNDIName: " + this.getLocalJNDIName());
      Debug.say(var1 + "CacheBetweenTransactions: " + this.getCacheBetweenTransactions());
      Debug.say(var1 + "BoxCarUpdates: " + this.getBoxCarUpdates());
      Debug.say(var1 + "ComplexPrimaryKey: " + this.hasComplexPrimaryKey());
      Debug.say(var1 + "PrimaryKeyClass: " + this.getPrimaryKeyClass());
      Debug.say(var1 + "PrimaryKeyFieldNames: " + this.getPrimaryKeyFieldNames());
      Debug.say(var1 + "CMFieldNames: " + this.getCMFieldNames());
      Debug.say(var1 + "IsReentrant: " + this.getIsReentrant());
      Debug.say(var1 + "IsModifiedMethodName: " + this.getIsModifiedMethodName());
      Debug.say(var1 + "FindersLoadBean: " + this.getFindersLoadBean());
      Debug.say(var1 + "IsDynamicQueriesEnabled: " + this.getIsDynamicQueriesEnabled());
   }
}
