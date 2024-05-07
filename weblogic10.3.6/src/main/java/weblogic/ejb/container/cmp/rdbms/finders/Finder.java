package weblogic.ejb.container.cmp.rdbms.finders;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.cache.QueryCacheKey;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.compliance.Log;
import weblogic.ejb.container.internal.QueryCachingHandler;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.utils.AssertionError;

public abstract class Finder {
   protected static final DebugLogger debugLogger;
   public static final int IS_FINDER_LOCAL_BEAN = 0;
   public static final int IS_SELECT_THIS_BEAN = 2;
   public static final int IS_SELECT_THIS_BEAN_FIELD = 3;
   public static final int IS_SELECT_LOCAL_BEAN = 4;
   public static final int IS_SELECT_LOCAL_BEAN_FIELD = 5;
   public static final int IS_SELECT_RESULT_SET = 6;
   private boolean isSqlFinder = true;
   protected RDBMSBean rdbmsBean = null;
   protected String methodName = null;
   private boolean isFinder = false;
   protected boolean isSelect = false;
   protected boolean isSelectInEntity = false;
   private boolean keyFinder = false;
   private String resultTypeMapping = null;
   protected int queryType = 0;
   protected int maxElements = 0;
   private RDBMSBean selectBeanTarget = null;
   private String selectFieldColumn = null;
   private Class selectFieldClass = null;
   private boolean includeUpdates = true;
   protected Class returnClassType = null;
   protected Class[] parameterClassTypes = null;
   private Class[] exceptionClassTypes = null;
   private String modifierString = null;
   private String[] parameterClassNames = null;
   protected boolean finderLoadsBean = false;
   protected List externalMethodParmList = new ArrayList();
   private List internalQueryParmList = new ArrayList();
   protected List internalInEntityParmList = new ArrayList();
   private boolean isSelectDistinct = false;
   protected String sqlQuery = null;
   protected String sqlQueryForUpdate = null;
   private String sqlQueryForSelectForUpdateDisabled = null;
   protected String sqlQueryForUpdateNoWait = null;
   protected boolean queryCachingEnabled = false;
   protected boolean eagerRefreshEnabled = false;
   protected Integer finderIndex = null;
   protected TTLManager ownerManager = null;
   protected Object[] arguments = null;
   protected QueryCacheKey queryCacheKey = null;
   private Map queryCacheEntries;
   protected Log log;
   protected EJBComplianceTextFormatter fmt;

   public Finder(String var1, boolean var2) throws InvalidFinderException {
      if (!var1.startsWith("ejbSelect") && !var1.equals("execute")) {
         this.isFinder = true;
      } else if (var1.endsWith("InEntity")) {
         this.isSelectInEntity = true;
      } else {
         this.isSelect = true;
      }

      this.setName(var1);
      this.isSqlFinder = var2;
      this.log = new Log();
      this.fmt = new EJBComplianceTextFormatter();
   }

   public String getSQLQuery() {
      throw new AssertionError("Should never be called");
   }

   public String getSQLQueryForUpdate() {
      throw new AssertionError("Should never be called");
   }

   public String getSQLQueryForUpdateNoWait() {
      throw new AssertionError("Should never be called");
   }

   public String getSQLQueryForUpdateSelective() {
      throw new AssertionError("Should never be called");
   }

   public int getQueryType() {
      return this.queryType;
   }

   public void setQueryType(int var1) {
      this.queryType = var1;
   }

   public boolean isSelectInEntity() {
      return this.isSelectInEntity;
   }

   public void setIsSelectInEntity(boolean var1) {
      this.isSelectInEntity = var1;
   }

   public boolean isSelectDistinct() {
      return this.isSelectDistinct;
   }

   public void setSelectDistinct(boolean var1) {
      this.isSelectDistinct = var1;
   }

   public void setSelectBeanTarget(RDBMSBean var1) {
      this.selectBeanTarget = var1;
   }

   public RDBMSBean getSelectBeanTarget() {
      return this.selectBeanTarget;
   }

   public void setSelectFieldColumn(String var1) {
      this.selectFieldColumn = var1;
   }

   public String getSelectFieldColumn() {
      return this.selectFieldColumn;
   }

   public void setSelectFieldClass(Class var1) {
      this.selectFieldClass = var1;
   }

   public Class getSelectFieldClass() {
      return this.selectFieldClass;
   }

   public void setFinderLoadsBean(boolean var1) {
      this.finderLoadsBean = var1;
   }

   public boolean finderLoadsBean() {
      return this.finderLoadsBean;
   }

   public boolean isFinder() {
      return this.isFinder;
   }

   public void setIsSelect(boolean var1) {
      this.isSelect = var1;
   }

   public boolean isSelect() {
      return this.isSelect;
   }

   public boolean isAggregateQuery() {
      return false;
   }

   protected void setName(String var1) throws InvalidFinderException {
      if (var1 == null) {
         throw new InvalidFinderException(1, var1);
      } else if (var1.equals("")) {
         throw new InvalidFinderException(2, var1);
      } else if (!var1.startsWith("find") && !var1.startsWith("ejbSelect") && !var1.equals("execute")) {
         throw new InvalidFinderException(3, var1);
      } else {
         this.methodName = var1;
      }
   }

   public String getName() {
      return this.methodName;
   }

   public void setReturnClassType(Class var1) {
      this.returnClassType = var1;
   }

   public Class getReturnClassType() {
      return this.returnClassType;
   }

   public void setParameterClassTypes(Class[] var1) {
      this.parameterClassTypes = var1;
      this.parameterClassNames = new String[this.parameterClassTypes.length];

      for(int var2 = 0; var2 < this.parameterClassTypes.length; ++var2) {
         this.parameterClassNames[var2] = this.parameterClassTypes[var2].getName();
      }

   }

   public Class[] getParameterClassTypes() {
      return this.parameterClassTypes;
   }

   public Class getParameterTypeAt(int var1) {
      return var1 < this.parameterClassTypes.length ? this.parameterClassTypes[var1] : null;
   }

   public void setParameterClassNames(String[] var1) {
      this.parameterClassNames = var1;
   }

   public String[] getParameterClassNames() {
      return this.parameterClassNames;
   }

   public void setExceptionClassTypes(Class[] var1) {
      this.exceptionClassTypes = var1;
   }

   public Class[] getExceptionClassTypes() {
      return this.exceptionClassTypes;
   }

   public void setModifierString(String var1) {
      this.modifierString = var1;
   }

   public String getModifierString() {
      return this.modifierString;
   }

   public void setRDBMSBean(RDBMSBean var1) {
      this.rdbmsBean = var1;
   }

   public RDBMSBean getRDBMSBean() {
      return this.rdbmsBean;
   }

   public int getMaxElements() {
      return this.maxElements;
   }

   public void setMaxElements(int var1) {
      this.maxElements = var1;
   }

   public void setIncludeUpdates(boolean var1) {
      this.includeUpdates = var1;
   }

   public boolean getIncludeUpdates() {
      return this.includeUpdates;
   }

   public String getResultTypeMapping() {
      return this.resultTypeMapping;
   }

   public void setResultTypeMapping(String var1) {
      this.resultTypeMapping = var1;
   }

   public boolean hasLocalResultType() {
      return "Local".equals(this.resultTypeMapping);
   }

   public boolean hasRemoteResultType() {
      return "Remote".equals(this.resultTypeMapping);
   }

   public boolean isMultiFinder() {
      return this.getReturnClassType() == null ? false : Collection.class.isAssignableFrom(this.getReturnClassType());
   }

   public boolean isScalarFinder() {
      return !this.isMultiFinder();
   }

   public boolean isSetFinder() {
      return this.getReturnClassType() == null ? false : this.getReturnClassType().getName().equals("java.util.Set");
   }

   public boolean isResultSetFinder() {
      return this.getReturnClassType() == null ? false : ResultSet.class.isAssignableFrom(this.getReturnClassType());
   }

   public boolean isFindByPrimaryKey() {
      return this.getName().equals("findByPrimaryKey");
   }

   public boolean isKeyFinder() {
      return this.keyFinder;
   }

   public void setKeyFinder(boolean var1) {
      this.keyFinder = var1;
   }

   protected void setMethod(Method var1) throws Exception {
      this.setReturnClassType(var1.getReturnType());
      this.setExceptionClassTypes(var1.getExceptionTypes());
      StringBuffer var2 = new StringBuffer();
      int var3 = var1.getModifiers();
      if (Modifier.isAbstract(var3)) {
         var3 ^= 1024;
      }

      var2.append(Modifier.toString(var3)).append(" ");
      String var4 = var2.toString();
      int var5 = var4.indexOf("strict ");
      if (var5 != -1) {
         var2.insert(var5 + "strict".length(), "fp");
      }

      this.setModifierString(var2.toString());
   }

   public abstract void setMethods(Method[] var1) throws Exception;

   public List getExternalMethodParmList() {
      return this.externalMethodParmList;
   }

   public List getExternalMethodAndInEntityParmList() {
      ArrayList var1 = new ArrayList(this.externalMethodParmList);
      return var1;
   }

   public List getInternalQueryAndInEntityParmList() {
      ArrayList var1 = new ArrayList(this.internalQueryParmList);
      Iterator var2 = this.internalInEntityParmList.iterator();

      while(var2.hasNext()) {
         ParamNode var3 = (ParamNode)var2.next();
         var1.add(var3);
      }

      return var1;
   }

   public void addInternalQueryParmList(ParamNode var1) {
      this.internalQueryParmList.add(var1);
   }

   protected ParamNode getInternalQueryParmNode(int var1) {
      return this.internalQueryParmList != null && !this.internalQueryParmList.isEmpty() && var1 < this.internalQueryParmList.size() ? (ParamNode)this.internalQueryParmList.get(var1) : null;
   }

   public void addInternalInEntityParmList(ParamNode var1) {
      this.internalInEntityParmList.add(var1);
   }

   public List getInternalInEntityParmList() {
      return this.internalInEntityParmList;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Finder ");
      var1.append("methodName = " + this.methodName + "; ");
      var1.append("Parameter types = " + this.parameterClassTypes + ";");
      var1.append("finderLoadsBean = " + new Boolean(this.finderLoadsBean) + ";");
      return var1.toString();
   }

   public boolean isSqlFinder() {
      return this.isSqlFinder;
   }

   public boolean isEjbqlFinder() {
      return !this.isSqlFinder;
   }

   public void setQueryCachingEnabled(RDBMSFinder var1, RDBMSBean var2) {
      if (!var1.isQueryCachingEnabled()) {
         this.queryCachingEnabled = false;
      } else if (!this.checkIfQueryCachingLegal(var2)) {
         this.queryCachingEnabled = false;
      } else {
         this.queryCachingEnabled = true;
         if (var1.isEnableEagerRefresh()) {
            this.eagerRefreshEnabled = true;
         }

      }
   }

   public void setQueryCachingEnabled(boolean var1) {
      if (!var1) {
         this.queryCachingEnabled = false;
      } else if (!this.checkIfQueryCachingLegal(this.rdbmsBean)) {
         this.queryCachingEnabled = false;
      } else {
         this.queryCachingEnabled = true;
      }
   }

   public boolean isQueryCachingEnabled() {
      return this.queryCachingEnabled;
   }

   public abstract QueryCachingHandler getQueryCachingHandler(Object[] var1, TTLManager var2);

   public String getFinderIndex() {
      return !this.isQueryCachingEnabled() ? null : this.generateFinderIndex();
   }

   protected String generateFinderIndex() {
      if (this.parameterClassNames == null) {
         throw new AssertionError("parameter class names is null; cannot generate finder index for query caching");
      } else {
         int var1 = this.methodName.hashCode();

         for(int var2 = 0; var2 < this.parameterClassNames.length; ++var2) {
            var1 ^= this.parameterClassNames[var2].hashCode();
         }

         return String.valueOf(var1);
      }
   }

   protected boolean checkIfQueryCachingLegal(RDBMSBean var1) {
      if (!var1.isReadOnly()) {
         this.log.logWarning(this.fmt.QUERY_CACHING_SUPPORTED_FOR_RO_BEANS_ONLY(var1.getEjbName(), this.getName()));
         return false;
      } else if (this.isFindByPrimaryKey()) {
         this.log.logWarning(this.fmt.QUERY_CACHING_UNNECESSARY_FOR_FIND_BY_PK(var1.getEjbName()));
         return false;
      } else if (this.isSelect()) {
         this.log.logWarning(this.fmt.QUERY_CACHING_NOT_SUPPORTED_FOR_EJBSELECTS(var1.getEjbName(), this.getName()));
         return false;
      } else if (this.getReturnClassType().equals(Enumeration.class)) {
         this.log.logWarning(this.fmt.QUERY_CACHING_NOT_SUPPORTED_FOR_ENUMFINDERS(var1.getEjbName(), this.getName()));
         return false;
      } else {
         return true;
      }
   }

   public boolean isEagerRefreshEnabled() {
      return this.eagerRefreshEnabled;
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
