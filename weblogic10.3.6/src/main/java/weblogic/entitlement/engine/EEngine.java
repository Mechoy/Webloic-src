package weblogic.entitlement.engine;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import weblogic.entitlement.EntitlementLogger;
import weblogic.entitlement.data.EPolicyCollectionInfo;
import weblogic.entitlement.data.EResource;
import weblogic.entitlement.data.ERole;
import weblogic.entitlement.data.ERoleCollectionInfo;
import weblogic.entitlement.data.ERoleId;
import weblogic.entitlement.data.EnConflictException;
import weblogic.entitlement.data.EnCreateException;
import weblogic.entitlement.data.EnCursorResourceFilter;
import weblogic.entitlement.data.EnCursorRoleFilter;
import weblogic.entitlement.data.EnData;
import weblogic.entitlement.data.EnDataChangeListener;
import weblogic.entitlement.data.EnDuplicateKeyException;
import weblogic.entitlement.data.EnFinderException;
import weblogic.entitlement.data.EnRemoveException;
import weblogic.entitlement.data.EnResourceCursor;
import weblogic.entitlement.data.EnRoleCursor;
import weblogic.entitlement.engine.cache.ResourceDecisionCache;
import weblogic.entitlement.expression.EAuxiliary;
import weblogic.entitlement.expression.EExpression;
import weblogic.entitlement.expression.InvalidPredicateClassException;
import weblogic.entitlement.parser.Parser;
import weblogic.entitlement.util.Cache;
import weblogic.entitlement.util.SecondChanceCache;
import weblogic.entitlement.util.TextFilter;
import weblogic.entitlement.util.Version;
import weblogic.security.SecurityLogger;
import weblogic.security.providers.authorization.AugmentedContext;
import weblogic.security.providers.authorization.Predicate;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.SecurityRole;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.spi.Resource;
import weblogic.security.utils.ESubjectImpl;
import weblogic.utils.collections.CombinedSet;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.collections.ConcurrentHashSet;

public class EEngine implements EnDataChangeListener, PredicateRegistry {
   private static final LoggerWrapper LOG = LoggerWrapper.getInstance("SecurityEEngine");
   private static final Version VERSION = new Version("EEngine", 1, 1, "Build $Date: 2002/01/09 16:41:30 $", "$Revision: 1.57 $");
   public static final String ENTITLEMENT_PROPERTIES = "entitlement.properties";
   public static final String EN_DATA_CLASS_PROPERTY = "weblogic.entitlement.engine.endata_class";
   public static final String ROLE_CACHE_SIZE_PROPERTY = "weblogic.entitlement.engine.cache.max_role_count";
   public static final String RSRC_CACHE_SIZE_PROPERTY = "weblogic.entitlement.engine.cache.max_resource_count";
   public static final String PRED_CACHE_SIZE_PROPERTY = "weblogic.entitlement.engine.cache.max_predicate_count";
   public static final String PRELOAD_CACHE_PROPERTY = "weblogic.entitlement.engine.cache.preload";
   public static final String EN_DATA_CLASS = "weblogic.entitlement.data.ldap.EnDataImp";
   public static final String ROLE_DECISION_CACHE_SIZE_PROPERTY = "weblogic.entitlement.engine.cache.max_role_decision_count";
   public static final String RSRC_DECISION_CACHE_SIZE_PROPERTY = "weblogic.entitlement.engine.cache.max_resource_decision_count";
   public static final String DECISION_CACHE_CLASSES_PROPERTY = "weblogic.entitlement.engine.cache.decision_classes";
   private static final EResource NO_RESOURCE = new EResource("", (EExpression)null);
   private static final ERole NO_ROLE = new ERole("", "", (EExpression)null);
   private static final EDecision ABSTAIN_DECISION = new EDecision((Boolean)null);
   private static EEngine mEngine = null;
   private Cache mResourceCache;
   private long mResChangeNum;
   private ResourceDecisionCache mResourceDecisionCache;
   private Cache mRoleCache;
   private long mRoleChangeNum;
   private ResourceDecisionCache mRoleDecisionCache;
   private Set mDecisionCacheClasses;
   private Cache mPredicateCache;
   private long mPredChangeNum;
   private EnData mEnData;

   public static EEngine getInstance() {
      return mEngine != null ? mEngine : getInstance(readProperties());
   }

   public static EEngine getInstance(Properties var0) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getInstance\n");
      }

      if (mEngine == null) {
         Class var1 = EEngine.class;
         synchronized(EEngine.class) {
            if (mEngine == null) {
               mEngine = new EEngine(var0);
            }
         }
      }

      return mEngine;
   }

   private EEngine(Properties var1) {
      String var2 = null;
      String var3 = null;

      try {
         var2 = "weblogic.entitlement.engine.cache.max_role_count";
         var3 = var1.getProperty(var2, "2000");
         int var4 = Integer.valueOf(var3);
         this.mRoleCache = new SecondChanceCache(var4);
         var2 = "weblogic.entitlement.engine.cache.max_role_decision_count";
         var3 = System.getProperty(var2, "6000");
         var4 = Integer.valueOf(var3);
         if (var4 > 0) {
            this.mRoleDecisionCache = new ResourceDecisionCache(var4);
         }

         var2 = "weblogic.entitlement.engine.cache.max_resource_count";
         var3 = var1.getProperty(var2, "5000");
         var4 = Integer.valueOf(var3);
         this.mResourceCache = new SecondChanceCache(var4);
         var2 = "weblogic.entitlement.engine.cache.max_resource_decision_count";
         var3 = System.getProperty(var2, "15000");
         var4 = Integer.valueOf(var3);
         if (var4 > 0) {
            this.mResourceDecisionCache = new ResourceDecisionCache(var4);
         }

         if (this.mRoleDecisionCache != null || this.mResourceDecisionCache != null) {
            var2 = "weblogic.entitlement.engine.cache.decision_classes";
            var3 = System.getProperty(var2, "weblogic.security.service.JMSResource,weblogic.security.service.JDBCResource,weblogic.security.service.JMXResource");
            if (var3.length() > 0) {
               this.mDecisionCacheClasses = new HashSet();
               StringTokenizer var5 = new StringTokenizer(var3, ",");

               label48:
               while(true) {
                  String var6;
                  do {
                     if (!var5.hasMoreTokens()) {
                        break label48;
                     }

                     var6 = var5.nextToken();
                  } while(var6.length() <= 0);

                  try {
                     this.mDecisionCacheClasses.add(Class.forName(var6));
                  } catch (ClassNotFoundException var8) {
                     if (LOG.isDebugEnabled()) {
                        LOG.debug("Failed to load decision cache resource class: " + var6);
                     }
                  }
               }
            }
         }

         var2 = "weblogic.entitlement.engine.cache.max_predicate_count";
         var3 = var1.getProperty(var2, "200");
         var4 = Integer.valueOf(var3);
         this.mPredicateCache = new SecondChanceCache(var4);
      } catch (Exception var9) {
         EntitlementLogger.logInvalidPropertyValue(var2, var3);
         abort("Invalid value \"" + var3 + "\" for property \"" + var2 + "\"\nPositive integer is expected", var9);
      }

      this.mEnData = makeEnData(var1);
      boolean var10 = new Boolean(var1.getProperty("weblogic.entitlement.engine.cache.preload", "false"));
      if (var10) {
         this.preload();
      }

      this.mEnData.setDataChangeListener(this);
   }

   public Map getRoleMap(Subject var1, Resource var2, ContextHandler var3) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getRoleMap (" + new ESubjectImpl(var1) + " , " + var2.toString() + ")\n");
      }

      boolean var4 = this.mRoleDecisionCache != null && (this.mDecisionCacheClasses == null || this.mDecisionCacheClasses.contains(var2.getClass()));
      ECacheableRoles var5;
      if (var4) {
         var5 = (ECacheableRoles)this.mRoleDecisionCache.lookupDecision(var2, var1);
         if (var5 != null) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("getRoleMap returning roles from decision cache");
            }

            return new ERoleMapImpl(var1, var2, var3, var5);
         }
      }

      var5 = new ECacheableRoles();
      if (var4) {
         if (LOG.isDebugEnabled()) {
            LOG.debug("Caching roles for getRoleMap (" + new ESubjectImpl(var1) + " , " + var2.toString() + ")\n");
         }

         this.mRoleDecisionCache.cacheDecision(var2, var1, var5);
      }

      return new ERoleMapImpl(var1, var2, var3, var5);
   }

   private boolean checkCacheability(EExpression var1, int var2) {
      return var1 == null ? true : this.checkCacheability(var1.getDependsOn(), var2);
   }

   private boolean checkCacheability(int var1, int var2) {
      return (var1 | var2) == var2;
   }

   private boolean evaluate(ESubject var1, ERole var2, ResourceNode var3, ContextHandler var4) {
      boolean var5 = false;
      EExpression var6 = var2.getExpression();
      if (LOG.isDebugEnabled()) {
         LOG.debug("Evaluating role " + var2.getPrimaryKey() + " with expression: " + (var6 == null ? "null" : var6.externalize()));
      }

      if (var6 != null) {
         if (var4 != null) {
            EAuxiliary var7 = var2.getAuxiliary();
            if (var7 != null) {
               var4 = new AugmentedContext((ContextHandler)var4, "com.bea.contextelement.entitlement.EAuxiliaryID", var7);
            }
         }

         try {
            var5 = var6.evaluate(var1, var3, (ContextHandler)var4, this);
         } catch (UnregisteredPredicateException var8) {
            EntitlementLogger.logRoleUnregisteredPredicate(var2.getPrimaryKey().toString(), var8.getPredicateName());
         } catch (Exception var9) {
            EntitlementLogger.logPolicyEvaluationFailed(var2.getEntitlement(), var2.getPrimaryKey().toString());
            if (LOG.isDebugEnabled()) {
               LOG.debug("Caught exception thrown while evaluating role expression", var9);
            }
         }
      }

      return var5;
   }

   private Collection getRoles(ResourceNode var1) {
      String[] var2 = var1.getNamePathToRoot();
      HashMap var3 = new HashMap();
      String var4 = "";

      for(int var5 = var2.length; var5 >= 0; --var5) {
         if (var5 < var2.length) {
            var4 = var2[var5];
         }

         ERole[] var6 = this.getRoles(var4);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var3.put(var6[var7].getName(), var6[var7]);
         }
      }

      return var3.values();
   }

   private ERole[] getRoles(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      RoleCacheEntry var2 = (RoleCacheEntry)this.mRoleCache.get(var1);
      if (var2 != null && var2.all) {
         synchronized(this.mRoleCache) {
            if (var2.all) {
               return (ERole[])((ERole[])var2.values().toArray(new ERole[var2.size()]));
            }
         }
      }

      long var3 = this.mRoleChangeNum;
      Collection var5 = this.mEnData.fetchRoles(var1);
      ERole[] var6 = (ERole[])((ERole[])var5.toArray(new ERole[var5.size()]));
      synchronized(this.mRoleCache) {
         if (var3 == this.mRoleChangeNum) {
            this.updateRoleCache(var1, var6);
         }

         return var6;
      }
   }

   private ERole getRole(ResourceNode var1, String var2) {
      String[] var3 = var1.getNamePathToRoot();
      ERole var4 = null;
      ArrayList var5 = null;

      for(int var6 = 0; var6 <= var3.length; ++var6) {
         String var7 = var6 < var3.length ? var3[var6] : "";
         var4 = this.getFromRoleCache(var7, var2);
         if (var4 != null) {
            if (var4 != NO_ROLE) {
               break;
            }
         } else {
            if (var5 == null) {
               var5 = new ArrayList(var3.length);
            }

            var5.add(var7);
         }
      }

      if (var4 == NO_ROLE) {
         var4 = null;
      }

      if (var5 != null) {
         ERoleId[] var10 = new ERoleId[var5.size()];

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var10[var11] = new ERoleId((String)var5.get(var11), var2);
         }

         try {
            ERole[] var12 = this.getRoles(var10, true);

            for(int var8 = 0; var8 < var12.length; ++var8) {
               if (var12[var8] != null) {
                  return var12[var8];
               }
            }
         } catch (EnFinderException var9) {
            abort("Caught EnFinderException while ignoreNotFound was set to true", var9);
         }
      }

      return var4;
   }

   public Boolean evaluate(Subject var1, Map var2, Resource var3, ContextHandler var4) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("evaluate(" + new ESubjectImpl(var1, var2) + ", " + var3.toString() + ")\n");
      }

      boolean var5 = this.mResourceDecisionCache != null && (this.mDecisionCacheClasses == null || this.mDecisionCacheClasses.contains(var3.getClass()));
      if (var5) {
         EDecision var6 = (EDecision)this.mResourceDecisionCache.lookupDecision(var3, var1);
         if (var6 != null) {
            if (var6.isApplicable(var2)) {
               if (LOG.isDebugEnabled()) {
                  LOG.debug("evaluate returning value from decision cache");
               }

               return var6.getDecision();
            }

            if (LOG.isDebugEnabled()) {
               LOG.debug("evauate decision cache hit not applicable to current roles -- remove from cache");
            }

            this.mResourceDecisionCache.uncacheDecision(var3, var1);
         }
      }

      Boolean var13 = Boolean.FALSE;
      ResourceNodeImpl var7 = new ResourceNodeImpl(var3);
      EResource var8 = this.getPolicyResource((ResourceNode)var7);
      if (var8 == null) {
         var13 = null;
         if (LOG.isDebugEnabled()) {
            LOG.debug("No resource found, cannot evaluate");
         }

         if (var5) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("Caching abstain for evaluate (" + new ESubjectImpl(var1, var2) + " , " + var3.toString() + ")\n");
            }

            this.mResourceDecisionCache.cacheDecision(var3, var1, ABSTAIN_DECISION);
         }
      } else {
         EExpression var9 = var8.getExpression();
         if (LOG.isDebugEnabled()) {
            LOG.debug("Evaluating resource " + var8.toString() + " with expression: " + (var9 == null ? "null" : var9.externalize()));
         }

         if (var9 != null) {
            try {
               if (var9.evaluate(new ESubjectImpl(var1, var2), var7, var4, this)) {
                  var13 = Boolean.TRUE;
               }
            } catch (UnregisteredPredicateException var11) {
               EntitlementLogger.logResourceUnregisteredPredicate(var8.getName(), var11.getPredicateName());
            } catch (Exception var12) {
               EntitlementLogger.logPolicyEvaluationFailed(var9.externalize(), var8.getName());
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Caught exception thrown while evaluating resource expression", var12);
               }
            }

            if (var5) {
               int var10 = var9.getDependsOn();
               if (this.checkCacheability(var10, 3)) {
                  if ((var10 & 2) != 0) {
                     if (var2 == null || var2 instanceof ERoleMapImpl && ((ERoleMapImpl)var2).isCacheableOnly()) {
                        if (LOG.isDebugEnabled()) {
                           LOG.debug("Caching role-dependent decision for evaluate (" + new ESubjectImpl(var1, var2) + " , " + var3.toString() + ")\n");
                        }

                        this.mResourceDecisionCache.cacheDecision(var3, var1, new ERoleDependentDecision(var13, var2 != null ? ((ERoleMapImpl)var2).ecr : null));
                     }
                  } else {
                     if (LOG.isDebugEnabled()) {
                        LOG.debug("Caching decision for evaluate (" + new ESubjectImpl(var1, var2) + " , " + var3.toString() + ")\n");
                     }

                     this.mResourceDecisionCache.cacheDecision(var3, var1, new EDecision(var13));
                  }
               }
            }
         } else if (var5) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("Caching empty expression for evaluate (" + new ESubjectImpl(var1, var2) + " , " + var3.toString() + ")\n");
            }

            this.mResourceDecisionCache.cacheDecision(var3, var1, ABSTAIN_DECISION);
         }
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug("Evaluation result: " + var13);
      }

      return var13;
   }

   public EResource getPolicyResource(Resource var1) {
      return this.getPolicyResource((ResourceNode)(new ResourceNodeImpl(var1)));
   }

   public EResource getPolicyResource(ResourceNode var1) {
      String[] var2 = var1.getNamePathToRoot();
      EResource[] var3 = null;

      try {
         var3 = this.getResources(var2, true);
      } catch (EnFinderException var6) {
         abort("Caught EnFinderException while ignoreNotFound was set to true", var6);
      }

      EResource var4 = null;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] != null) {
            var4 = var3[var5];
            if (var4.getExpression() != null) {
               return var4;
            }
         }
      }

      return var4;
   }

   public void createRoles(ERoleId[] var1, String[] var2, boolean var3) throws EnDuplicateKeyException, EnCreateException {
      this.createRoles(var1, var2, new String[0], var3);
   }

   public void createRoles(ERoleId[] var1, String[] var2, String[] var3, boolean var4) throws EnDuplicateKeyException, EnCreateException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("createRoles (");

         for(int var5 = 0; var5 < var1.length; ++var5) {
            LOG.debug("[" + var1[var5] + " -- " + var2[var5] + "]");
         }

         LOG.debug(")\n");
      }

      ERole[] var15 = new ERole[var1.length];
      int var6 = -1;

      try {
         for(var6 = 0; var6 < var15.length; ++var6) {
            EExpression var7 = var2[var6] == null ? null : Parser.parseRoleExpression(var2[var6]);
            EAuxiliary var8 = null;
            if (var6 < var3.length && var3[var6] != null) {
               var8 = new EAuxiliary(var3[var6]);
            }

            var15[var6] = new ERole(var1[var6], var7, var8, var4);
         }
      } catch (Exception var14) {
         throw new EnCreateException(var14.getMessage(), var6, var14);
      }

      long var16 = this.mRoleChangeNum;
      this.mEnData.create(var15, var4);
      if (this.mRoleDecisionCache != null) {
         for(int var9 = 0; var9 < var15.length; ++var9) {
            this.mRoleDecisionCache.uncache(var15[var9].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var10 = this.mRoleChangeNum++;
         if (var16 == var10) {
            this.updateRoleCache(var15);
         } else {
            this.discardFromRoleCache(var1);
         }

      }
   }

   public void removeRoles(ERoleId[] var1) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("removeRoles (" + var1 + ")\n");
      }

      long var2 = this.mRoleChangeNum;
      this.mEnData.removeRoles(var1);
      if (this.mRoleDecisionCache != null) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            this.mRoleDecisionCache.uncache(var1[var4].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var5 = this.mRoleChangeNum++;
         if (var2 == var5) {
            this.removeFromRoleCache(var1);
         } else {
            this.discardFromRoleCache(var1);
         }

      }
   }

   public void createResourceForCollection(String var1, String[] var2, String[] var3) throws EnConflictException, EnDuplicateKeyException, EnCreateException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("createResourceForCollection (" + var1 + " -- ");

         for(int var4 = 0; var4 < var2.length; ++var4) {
            LOG.debug("[" + var2[var4] + " -- " + var3[var4] + "]");
         }

         LOG.debug(")\n");
      }

      EResource[] var14 = new EResource[var2.length];
      int var5 = -1;

      try {
         for(var5 = 0; var5 < var2.length; ++var5) {
            EExpression var6 = var3[var5] == null ? null : Parser.parseResourceExpression(var3[var5]);
            var14[var5] = new EResource(var2[var5], var6, true, var1);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var5, var13);
      }

      long var15 = this.mResChangeNum;
      this.mEnData.createForCollection(var14);
      if (this.mResourceDecisionCache != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            this.mResourceDecisionCache.uncache(var14[var8].getName());
         }
      }

      synchronized(this.mResourceCache) {
         int var9 = this.mResChangeNum++;
         if (var15 == var9) {
            this.updateResourceCache(var14);
         } else {
            this.discardFromResourceCache(var2);
         }

      }
   }

   public void createResources(String[] var1, String[] var2, boolean var3) throws EnDuplicateKeyException, EnCreateException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("createResources (");

         for(int var4 = 0; var4 < var1.length; ++var4) {
            LOG.debug("[" + var1[var4] + " -- " + var2[var4] + "]");
         }

         LOG.debug(")\n");
      }

      EResource[] var14 = new EResource[var1.length];
      int var5 = -1;

      try {
         for(var5 = 0; var5 < var1.length; ++var5) {
            EExpression var6 = var2[var5] == null ? null : Parser.parseResourceExpression(var2[var5]);
            var14[var5] = new EResource(var1[var5], var6, var3);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var5, var13);
      }

      long var15 = this.mResChangeNum;
      this.mEnData.create(var14, var3);
      if (this.mResourceDecisionCache != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            this.mResourceDecisionCache.uncache(var14[var8].getName());
         }
      }

      synchronized(this.mResourceCache) {
         int var9 = this.mResChangeNum++;
         if (var15 == var9) {
            this.updateResourceCache(var14);
         } else {
            this.discardFromResourceCache(var1);
         }

      }
   }

   public void removeResources(String[] var1) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("removeResources (" + var1 + ")\n");
      }

      long var2 = this.mResChangeNum;
      this.mEnData.removeResources(var1);
      if (this.mResourceDecisionCache != null) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            this.mResourceDecisionCache.uncache(var1[var4]);
         }
      }

      synchronized(this.mResourceCache) {
         int var5 = this.mResChangeNum++;
         if (var2 == var5) {
            this.removeFromResourceCache(var1);
         } else {
            this.discardFromResourceCache(var1);
         }

      }
   }

   public void setRoleEntitlements(ERoleId[] var1, String[] var2, boolean var3) throws EnCreateException, EnFinderException {
      int var5;
      if (LOG.isDebugEnabled()) {
         StringBuffer var4 = new StringBuffer();
         var4.append("setRoleEntitlements (");

         for(var5 = 0; var5 < var1.length; ++var5) {
            var4.append("[" + var1[var5] + " -- " + var2[var5] + "]");
         }

         var4.append(")\n");
         LOG.debug(var4.toString());
      }

      ERole[] var14 = new ERole[var1.length];
      byte var15 = -1;

      try {
         for(var5 = 0; var5 < var14.length; ++var5) {
            EExpression var6 = var2[var5] == null ? null : Parser.parseRoleExpression(var2[var5]);
            var14[var5] = new ERole(var1[var5], var6, var3);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var15, var13);
      }

      long var16 = this.mRoleChangeNum;
      this.mEnData.update(var14, var3);
      if (this.mRoleDecisionCache != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            this.mRoleDecisionCache.uncache(var14[var8].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var9 = this.mRoleChangeNum++;
         if (var16 == var9) {
            this.updateRoleCache(var14);
         } else {
            this.discardFromRoleCache(var1);
         }

      }
   }

   public void createRoleEntitlementsForCollection(String var1, ERoleId[] var2, String[] var3) throws EnConflictException, EnDuplicateKeyException, EnCreateException {
      ERole[] var4 = new ERole[var2.length];
      int var5 = -1;

      try {
         for(var5 = 0; var5 < var4.length; ++var5) {
            EExpression var6 = var3[var5] == null ? null : Parser.parseRoleExpression(var3[var5]);
            var4[var5] = new ERole(var2[var5], var6, true, var1);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var5, var13);
      }

      long var14 = this.mRoleChangeNum;
      this.mEnData.createForCollection(var4);
      if (this.mRoleDecisionCache != null) {
         for(int var8 = 0; var8 < var4.length; ++var8) {
            this.mRoleDecisionCache.uncache(var4[var8].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var9 = this.mRoleChangeNum++;
         if (var14 == var9) {
            this.updateRoleCache(var4);
         } else {
            this.discardFromRoleCache(var2);
         }

      }
   }

   public void setRoleEntitlementsForCollection(String var1, ERoleId[] var2, String[] var3) throws EnCreateException, EnFinderException {
      ERole[] var4 = new ERole[var2.length];
      int var5 = -1;

      try {
         for(var5 = 0; var5 < var4.length; ++var5) {
            EExpression var6 = var3[var5] == null ? null : Parser.parseRoleExpression(var3[var5]);
            var4[var5] = new ERole(var2[var5], var6, true, var1);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var5, var13);
      }

      long var14 = this.mRoleChangeNum;
      this.mEnData.update(var4, true);
      if (this.mRoleDecisionCache != null) {
         for(int var8 = 0; var8 < var4.length; ++var8) {
            this.mRoleDecisionCache.uncache(var4[var8].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var9 = this.mRoleChangeNum++;
         if (var14 == var9) {
            this.updateRoleCache(var4);
         } else {
            this.discardFromRoleCache(var2);
         }

      }
   }

   public String[] getRoleEntitlements(ERoleId[] var1) throws EnFinderException {
      if (LOG.isDebugEnabled()) {
         StringBuffer var2 = new StringBuffer("getRoleEntitlements ([");

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.append(var1[var3]);
            if (var3 + 1 < var1.length) {
               var2.append(",");
            }
         }

         var2.append("])\n");
         LOG.debug(var2.toString());
      }

      ERole[] var5 = this.getRoles(var1, false);
      String[] var6 = new String[var5.length];

      for(int var4 = 0; var4 < var5.length; ++var4) {
         var6[var4] = var5[var4].getEntitlement();
      }

      return var6;
   }

   public void setRoleAuxiliary(ERoleId[] var1, String[] var2, boolean var3) throws EnCreateException, EnFinderException {
      int var5;
      if (LOG.isDebugEnabled()) {
         StringBuffer var4 = new StringBuffer();
         var4.append("setRoleAuxiliary (");

         for(var5 = 0; var5 < var1.length; ++var5) {
            var4.append("[" + var1[var5] + " -- " + var2[var5] + "]");
         }

         var4.append(")\n");
         LOG.debug(var4.toString());
      }

      ERole[] var14 = new ERole[var1.length];
      byte var15 = -1;

      try {
         for(var5 = 0; var5 < var14.length; ++var5) {
            EAuxiliary var6 = var2[var5] == null ? null : new EAuxiliary(var2[var5]);
            var14[var5] = new ERole(var1[var5], (EExpression)null, var6, var3);
         }
      } catch (Exception var13) {
         throw new EnCreateException(var13.getMessage(), var15, var13);
      }

      long var16 = this.mRoleChangeNum;
      this.mEnData.updateAuxiliary(var14, var3);
      if (this.mRoleDecisionCache != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            this.mRoleDecisionCache.uncache(var14[var8].getResourceName());
         }
      }

      synchronized(this.mRoleCache) {
         int var9 = this.mRoleChangeNum++;
         if (var16 == var9) {
            this.updateRoleCache(var14);
         } else {
            this.discardFromRoleCache(var1);
         }

      }
   }

   public String[] getRoleAuxiliary(ERoleId[] var1) throws EnFinderException {
      if (LOG.isDebugEnabled()) {
         StringBuffer var2 = new StringBuffer("getRoleAuxiliary ([");

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.append(var1[var3]);
            if (var3 + 1 < var1.length) {
               var2.append(",");
            }
         }

         var2.append("])\n");
         LOG.debug(var2.toString());
      }

      ERole[] var5 = this.getRoles(var1, false);
      String[] var6 = new String[var5.length];

      for(int var4 = 0; var4 < var5.length; ++var4) {
         var6[var4] = var5[var4].getAuxiliary().toString();
      }

      return var6;
   }

   public void setResourceEntitlementsForCollection(String var1, String[] var2, String[] var3) throws EnCreateException, EnFinderException {
      int var5;
      if (LOG.isDebugEnabled()) {
         StringBuffer var4 = new StringBuffer();
         var4.append("setResourceEntitlementsForCollection (");
         var4.append(var1 + " -- ");

         for(var5 = 0; var5 < var2.length; ++var5) {
            var4.append("[" + var2[var5] + " -- " + var3[var5] + "]");
         }

         var4.append(")\n");
         LOG.debug(var4.toString());
      }

      EResource[] var8 = new EResource[var2.length];
      byte var9 = -1;

      try {
         for(var5 = 0; var5 < var2.length; ++var5) {
            EExpression var6 = var3[var5] == null ? null : Parser.parseResourceExpression(var3[var5]);
            var8[var5] = new EResource(var2[var5], var6, true, var1);
         }
      } catch (Exception var7) {
         throw new EnCreateException(var7.getMessage(), var9, var7);
      }

      this.setResourceEntitlements(var2, var8, true);
   }

   public void setResourceEntitlements(String[] var1, String[] var2, boolean var3) throws EnCreateException, EnFinderException {
      int var5;
      if (LOG.isDebugEnabled()) {
         StringBuffer var4 = new StringBuffer();
         var4.append("setResourceEntitlements (");

         for(var5 = 0; var5 < var1.length; ++var5) {
            var4.append("[" + var1[var5] + " -- " + var2[var5] + "]");
         }

         var4.append(")\n");
         LOG.debug(var4.toString());
      }

      EResource[] var8 = new EResource[var1.length];
      byte var9 = -1;

      try {
         for(var5 = 0; var5 < var1.length; ++var5) {
            EExpression var6 = var2[var5] == null ? null : Parser.parseResourceExpression(var2[var5]);
            var8[var5] = new EResource(var1[var5], var6, var3);
         }
      } catch (Exception var7) {
         throw new EnCreateException(var7.getMessage(), var9, var7);
      }

      this.setResourceEntitlements(var1, var8, var3);
   }

   private void setResourceEntitlements(String[] var1, EResource[] var2, boolean var3) throws EnFinderException {
      long var4 = this.mResChangeNum;
      this.mEnData.update(var2, var3);
      if (this.mResourceDecisionCache != null) {
         for(int var6 = 0; var6 < var2.length; ++var6) {
            this.mResourceDecisionCache.uncache(var2[var6].getName());
         }
      }

      synchronized(this.mResourceCache) {
         int var7 = this.mResChangeNum++;
         if (var4 == var7) {
            this.updateResourceCache(var2);
         } else {
            this.discardFromResourceCache(var1);
         }

      }
   }

   public String[] getResourceEntitlements(String[] var1) throws EnFinderException {
      if (LOG.isDebugEnabled()) {
         StringBuffer var2 = new StringBuffer("getResourceEntitlements ([");

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.append(var1[var3]);
            if (var3 + 1 < var1.length) {
               var2.append(",");
            }
         }

         var2.append("])\n");
         LOG.debug(var2.toString());
      }

      EResource[] var5 = this.getResources(var1, false);
      String[] var6 = new String[var5.length];

      for(int var4 = 0; var4 < var5.length; ++var4) {
         var6[var4] = var5[var4].getEntitlement();
      }

      return var6;
   }

   private ERole[] getRoles(ERoleId[] var1, boolean var2) throws EnFinderException {
      ERole[] var3 = this.getFromRoleCache(var1);
      int var4;
      if (!var2) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] == NO_ROLE) {
               throw new EnFinderException(var3[var4].toString());
            }
         }
      }

      var4 = 0;

      int var5;
      for(var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] == null) {
            ++var4;
         }
      }

      if (var4 > 0) {
         ERoleId[] var15 = new ERoleId[var4];
         int var6 = 0;

         for(int var7 = 0; var7 < var4; ++var6) {
            if (var3[var6] == null) {
               var15[var7++] = var1[var6];
            }
         }

         long var16 = this.mRoleChangeNum;
         ERole[] var8 = this.mEnData.fetchRoles(var15, var2);
         synchronized(this.mRoleCache) {
            boolean var10 = var16 != this.mRoleChangeNum;
            int var11 = 0;

            for(int var12 = 0; var12 < var4; ++var11) {
               if (var3[var11] == null) {
                  var3[var11] = var8[var12++];
                  if (!var10) {
                     this.updateRoleCache(var1[var11], var3[var11]);
                  }
               }
            }
         }
      }

      for(var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] == NO_ROLE) {
            var3[var5] = null;
         }
      }

      return var3;
   }

   private EResource[] getResources(String[] var1, boolean var2) throws EnFinderException {
      EResource[] var3 = this.getFromResourceCache(var1);
      int var4;
      if (!var2) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] == NO_RESOURCE) {
               throw new EnFinderException(var1[var4]);
            }
         }
      }

      var4 = 0;

      int var5;
      for(var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] == null) {
            ++var4;
         }
      }

      if (var4 > 0) {
         String[] var15 = new String[var4];
         int var6 = 0;

         for(int var7 = 0; var7 < var4; ++var6) {
            if (var3[var6] == null) {
               var15[var7++] = var1[var6];
            }
         }

         long var16 = this.mResChangeNum;
         EResource[] var8 = this.mEnData.fetchResources(var15, var2);
         synchronized(this.mResourceCache) {
            boolean var10 = var16 != this.mResChangeNum;
            int var11 = 0;

            for(int var12 = 0; var12 < var4; ++var11) {
               if (var3[var11] == null) {
                  var3[var11] = var8[var12++];
                  if (!var10) {
                     this.updateResourceCache(var1[var11], var3[var11] == null ? NO_RESOURCE : var3[var11]);
                  }
               }
            }
         }
      }

      if (var2) {
         for(var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5] == NO_RESOURCE) {
               var3[var5] = null;
            }
         }
      }

      return var3;
   }

   public Collection getRoleIds(String var1, String var2) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getRoleIds-scoped (" + var1 + ", " + var2 + ")\n");
      }

      TextFilter var3 = var2 == null ? null : new TextFilter(var2);
      return this.mEnData.fetchRoleIds(var1, var3);
   }

   public Collection getRoleIds(ResourceNode var1, String var2) throws EnFinderException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getRoleIds-all (" + var1.getName() + " , " + var2 + ")\n");
      }

      TextFilter var3 = var2 == null ? null : new TextFilter(var2);
      String[] var4 = var1.getNamePathToRoot();
      ArrayList var5 = new ArrayList(this.mEnData.fetchRoleIds("", var3));

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var5.addAll(this.mEnData.fetchRoleIds(var4[var6], var3));
      }

      return var5;
   }

   public Collection getResourceNames(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getResourceNames (" + var1 + ")\n");
      }

      TextFilter var2 = var1 == null ? null : new TextFilter(var1);
      return this.mEnData.fetchResourceNames(var2);
   }

   public Collection getResourceRoleIds(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getResourceRoleIds (" + var1 + ")\n");
      }

      TextFilter var2 = var1 == null ? null : new TextFilter(var1);
      return this.mEnData.fetchResourceRoleIds(var2);
   }

   public static Version getVersion() {
      return VERSION;
   }

   public String[] getPredicates(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("getPredicates (" + var1 + ")\n");
      }

      TextFilter var2 = var1 == null ? null : new TextFilter(var1);
      long var3 = this.mPredChangeNum;
      Collection var5 = this.mEnData.fetchPredicates(var2);
      ArrayList var6 = new ArrayList(var5.size());
      synchronized(this.mPredicateCache) {
         boolean var8 = var3 != this.mPredChangeNum;
         Iterator var9 = var5.iterator();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            boolean var11 = true;
            if (!this.mPredicateCache.containsKey(var10)) {
               try {
                  validatePredicate(var10);
                  if (!var8) {
                     this.mPredicateCache.put(var10, var10);
                  }
               } catch (InvalidPredicateClassException var14) {
                  var11 = false;
                  EntitlementLogger.logRetrievedInvalidPredicate(var10);
                  if (LOG.isDebugEnabled()) {
                     LOG.debug("Retrieved invalid predicate class name\n" + var14.getMessage(), var14);
                  }
               }
            }

            if (var11) {
               var6.add(var10);
            }
         }
      }

      return (String[])((String[])var6.toArray(new String[var6.size()]));
   }

   public boolean isRegistered(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("isRegistered (" + var1 + ")\n");
      }

      boolean var2 = this.mPredicateCache.containsKey(var1);
      if (var2) {
         return true;
      } else {
         long var3 = this.mPredChangeNum;
         var2 = this.mEnData.predicateExists(var1);
         if (var2) {
            try {
               validatePredicate(var1);
               synchronized(this.mPredicateCache) {
                  boolean var6 = var3 != this.mPredChangeNum;
                  if (!var6) {
                     this.mPredicateCache.put(var1, var1);
                  }
               }
            } catch (InvalidPredicateClassException var9) {
               var2 = false;
               EntitlementLogger.logRetrievedInvalidPredicate(var1);
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Retrieved invalid predicate class name\n" + var9.getMessage(), var9);
               }
            }
         }

         return var2;
      }
   }

   public void registerPredicate(String var1) throws InvalidPredicateClassException, EnDuplicateKeyException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("registerPredicate (" + var1 + ")\n");
      }

      validatePredicate(var1);
      long var2 = this.mPredChangeNum;
      this.mEnData.createPredicate(var1);
      synchronized(this.mPredicateCache) {
         int var5 = this.mPredChangeNum++;
         if (var2 == var5) {
            this.mPredicateCache.put(var1, var1);
         } else {
            this.mPredicateCache.remove(var1);
         }

      }
   }

   public void unregisterPredicate(String var1) throws EnFinderException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("unregisterPredicate (" + var1 + ")\n");
      }

      this.mEnData.removePredicate(var1);
      synchronized(this.mPredicateCache) {
         ++this.mPredChangeNum;
         this.mPredicateCache.remove(var1);
      }
   }

   public static Predicate validatePredicate(String var0) throws InvalidPredicateClassException {
      PrivilegedAction var1 = new PrivilegedAction() {
         public Object run() {
            return Thread.currentThread().getContextClassLoader();
         }
      };
      ClassLoader var2 = (ClassLoader)AccessController.doPrivileged(var1);

      try {
         return (Predicate)Class.forName(var0, true, var2).newInstance();
      } catch (ClassCastException var4) {
         throw new InvalidPredicateClassException(var0 + " class does not implement interface " + Predicate.class.getName());
      } catch (ClassNotFoundException var5) {
         throw new InvalidPredicateClassException(var0 + " class cannot be found ");
      } catch (IllegalAccessException var6) {
         throw new InvalidPredicateClassException(var0 + " class's constructor access denied");
      } catch (InstantiationException var7) {
         throw new InvalidPredicateClassException("Cannot instantiate predicate class " + var0 + "\n" + var7.getMessage());
      }
   }

   public void resourceChanged(String var1) {
      synchronized(this.mResourceCache) {
         ++this.mResChangeNum;
         this.mResourceCache.remove(var1);
      }

      if (this.mResourceDecisionCache != null) {
         this.mResourceDecisionCache.uncache(var1);
      }

   }

   public void roleChanged(ERoleId var1) {
      synchronized(this.mRoleCache) {
         ++this.mRoleChangeNum;
         this.discardFromRoleCache(var1);
      }

      if (this.mRoleDecisionCache != null) {
         this.mRoleDecisionCache.uncache(var1.getResourceName());
      }

   }

   public void predicateChanged(String var1) {
      synchronized(this.mPredicateCache) {
         ++this.mPredChangeNum;
         this.mPredicateCache.remove(var1);
      }
   }

   public void applicationDeletedResources(String var1, int var2, String var3) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("applicationDeletedResources (" + var1 + ")");
      }

      this.mEnData.applicationDeletedResources(var1, var2, var3);
   }

   public void cleanupAfterCollectionResources(String var1, long var2, List var4) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("cleanupAfterCollectionResources (" + var1 + ")");
      }

      this.mEnData.cleanupAfterCollectionResources(var1, var2, var4);
   }

   public void cleanupAfterCollectionRoles(String var1, long var2, List var4) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("cleanupAfterCollectionRoles (" + var1 + ")");
      }

      this.mEnData.cleanupAfterCollectionRoles(var1, var2, var4);
   }

   public void cleanupAfterDeployResources(String var1, int var2, String var3, long var4) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("cleanupAfterDeployResources (" + var1 + ")");
      }

      this.mEnData.cleanupAfterDeployResources(var1, var2, var3, var4);
   }

   public void applicationCopyResources(String var1, String var2) throws EnCreateException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("applicationCopyResources (" + var1 + "," + var2 + ")");
      }

      this.mEnData.applicationCopyResources(var1, var2);
   }

   public void applicationDeletedRoles(String var1, int var2, String var3) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("applicationDeletedRoles (" + var1 + ")");
      }

      this.mEnData.applicationDeletedRoles(var1, var2, var3);
   }

   public void cleanupAfterDeployRoles(String var1, int var2, String var3, long var4) throws EnFinderException, EnRemoveException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("cleanupAfterDeployRoles (" + var1 + ")");
      }

      this.mEnData.cleanupAfterDeployRoles(var1, var2, var3, var4);
   }

   public void applicationCopyRoles(String var1, String var2) throws EnCreateException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("applicationCopyRoles (" + var1 + "," + var2 + ")");
      }

      this.mEnData.applicationCopyRoles(var1, var2);
   }

   private static Properties readProperties() {
      Properties var0 = new Properties();

      try {
         InputStream var1 = EEngine.class.getClassLoader().getResourceAsStream("entitlement.properties");
         if (var1 != null) {
            var0.load(var1);
         } else {
            LOG.severe("Cannot read properties file entitlement.properties\nMake sure the file is in the classpath and is permitted to read");
         }
      } catch (SecurityException var2) {
         abort("Cannot read file entitlement.properties\nThe file might be read protected\n" + var2.getMessage(), var2);
      } catch (IOException var3) {
         abort("Cannot read entitlement.properties\n" + var3.getMessage(), var3);
      }

      return var0;
   }

   private static EnData makeEnData(Properties var0) {
      EnData var1 = null;
      String var2 = var0.getProperty("weblogic.entitlement.engine.endata_class", "weblogic.entitlement.data.ldap.EnDataImp");

      try {
         Class var3 = Class.forName(var2);
         Constructor var12 = var3.getConstructor(Properties.class);
         var1 = (EnData)var12.newInstance(var0);
      } catch (ClassNotFoundException var6) {
         abort("Cannot find class " + var2 + "\n" + "Make sure " + "weblogic.entitlement.engine.endata_class" + " is set in " + "entitlement.properties", var6);
      } catch (NoSuchMethodException var7) {
         abort("Class weblogic.entitlement.engine.endata_class does not have a constructor with one java.util.Properties argument", var7);
      } catch (SecurityException var8) {
         abort("Cannot access information about " + var2 + " class\n" + var8.getMessage(), var8);
      } catch (IllegalAccessException var9) {
         abort("Cannot access constructor of class " + var2 + "\n" + var9.getMessage(), var9);
      } catch (InstantiationException var10) {
         abort("Cannot create instance of class " + var2 + "\nMake sure class is not declared abstract.\n" + var10.getMessage(), var10);
      } catch (InvocationTargetException var11) {
         String var4 = "Cannot instantiate " + var2 + "\n" + var11.getMessage();
         Throwable var5 = var11.getTargetException();
         LOG.severe(var4, var5);
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         }

         throw new RuntimeException(var4);
      }

      return var1;
   }

   private static void abort(String var0, Throwable var1) {
      LOG.severe(var0, var1);
      throw new RuntimeException(var0);
   }

   private void updateRoleCache(ERoleId var1, ERole var2) {
      String var3 = var1.getResourceName();
      RoleCacheEntry var4 = (RoleCacheEntry)this.mRoleCache.get(var3);
      if (var4 == null) {
         var4 = new RoleCacheEntry();
         this.mRoleCache.put(var3, var4);
      }

      if (var2 == null) {
         var2 = NO_ROLE;
      }

      if (var2 == NO_ROLE && var4.all) {
         var4.remove(var1.getRoleName());
      } else {
         var4.put(var1.getRoleName(), var2);
      }

   }

   private void updateRoleCache(ERole[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateRoleCache((ERoleId)var1[var2].getPrimaryKey(), var1[var2]);
      }

   }

   private void removeFromRoleCache(ERoleId[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateRoleCache(var1[var2], NO_ROLE);
      }

   }

   private void discardFromRoleCache(ERoleId var1) {
      RoleCacheEntry var2 = (RoleCacheEntry)this.mRoleCache.get(var1.getResourceName());
      if (var2 != null) {
         var2.all = false;
         var2.remove(var1.getRoleName());
      }

   }

   private void discardFromRoleCache(ERoleId[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.discardFromRoleCache(var1[var2]);
      }

   }

   private void updateRoleCache(String var1, ERole[] var2) {
      RoleCacheEntry var3 = new RoleCacheEntry(var2.length);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3.put(var2[var4].getName(), var2[var4]);
      }

      var3.all = true;
      this.mRoleCache.put(var1, var3);
   }

   private ERole[] getFromRoleCache(ERoleId[] var1) {
      ERole[] var2 = new ERole[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.getFromRoleCache(var1[var3].getResourceName(), var1[var3].getRoleName());
      }

      return var2;
   }

   public ERole getFromRoleCache(String var1, String var2) {
      ERole var3 = null;
      RoleCacheEntry var4 = (RoleCacheEntry)this.mRoleCache.get(var1);
      if (var4 != null) {
         var3 = (ERole)var4.get(var2);
         if (var3 == null && var4.all) {
            var3 = NO_ROLE;
         }
      }

      return var3;
   }

   private void updateResourceCache(String var1, EResource var2) {
      this.mResourceCache.put(var1, var2);
   }

   private void updateResourceCache(EResource[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateResourceCache(var1[var2].getName(), var1[var2]);
      }

   }

   private void removeFromResourceCache(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateResourceCache(var1[var2], NO_RESOURCE);
      }

   }

   private void discardFromResourceCache(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateResourceCache(var1[var2], (EResource)null);
      }

   }

   private EResource[] getFromResourceCache(String[] var1) {
      EResource[] var2 = new EResource[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = (EResource)this.mResourceCache.get(var1[var3]);
      }

      return var2;
   }

   private void preload() {
      if (LOG.isDebugEnabled()) {
         LOG.debug("Preloading resources");
      }

      String[] var1 = (String[])((String[])this.getResourceNames((String)null).toArray(new String[0]));

      int var3;
      try {
         this.getResources(var1, false);
      } catch (EnFinderException var4) {
         var3 = var4.getTargetIndex();
         abort("Cannot find resource " + (var3 < 0 ? "" : var1[var3]), var4);
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug("Preloading roles");
      }

      int var2 = Math.min(this.mRoleCache.getMaximumSize() - 1, var1.length);

      for(var3 = 0; var3 < var2; ++var3) {
         this.getRoles(var1[var3]);
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug("Preloading global roles");
      }

      this.getRoles("");
   }

   public EnResourceCursor listResources(String var1, int var2, EnCursorResourceFilter var3) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("listResources (" + var1 + " , " + var2 + " , " + (var3 == null ? "null" : "filter") + ")\n");
      }

      TextFilter var4 = var1 == null ? null : new TextFilter(var1);
      return this.mEnData.findResources(var4, var2, var3);
   }

   public EnRoleCursor listRoles(String var1, String var2, int var3, EnCursorRoleFilter var4) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("listRoles (" + var1 + " , " + var2 + " , " + var3 + " , " + (var4 == null ? "null" : "filter") + ")\n");
      }

      TextFilter var5 = var1 == null ? null : new TextFilter(var1);
      TextFilter var6 = var2 == null ? null : new TextFilter(var2);
      return this.mEnData.findRoles(var5, var6, var3, var4);
   }

   public void createPolicyCollectionInfo(String var1, String var2, String var3) throws EnCreateException, EnConflictException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("createPolicyCollectionInfo (" + var1 + " , " + var2 + " , " + var3 + ")\n");
      }

      this.mEnData.createPolicyCollectionInfo(var1, var2, var3);
   }

   public void createRoleCollectionInfo(String var1, String var2, String var3) throws EnCreateException, EnConflictException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("createRoleCollectionInfo (" + var1 + " , " + var2 + " , " + var3 + ")\n");
      }

      this.mEnData.createRoleCollectionInfo(var1, var2, var3);
   }

   public EPolicyCollectionInfo fetchPolicyCollectionInfo(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("fetchPolicyCollectionInfo (" + var1 + ")\n");
      }

      return this.mEnData.fetchPolicyCollectionInfo(var1);
   }

   public ERoleCollectionInfo fetchRoleCollectionInfo(String var1) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("fetchRoleCollectionInfo (" + var1 + ")\n");
      }

      return this.mEnData.fetchRoleCollectionInfo(var1);
   }

   private static class RoleCacheEntry extends HashMap {
      public volatile boolean all = false;

      public RoleCacheEntry() {
      }

      public RoleCacheEntry(int var1) {
         super(var1);
      }
   }

   private static class ERoleDependentDecision extends EDecision {
      private ECacheableRoles roleDependency;

      public ERoleDependentDecision(Boolean var1, ECacheableRoles var2) {
         super(var1);
         this.roleDependency = var2;
      }

      public boolean isApplicable(Map var1) {
         return var1 == this.roleDependency || var1 != null && var1 instanceof ERoleMapImpl && ((ERoleMapImpl)var1).ecr == this.roleDependency;
      }
   }

   private static class EDecision {
      private Boolean decision;

      public EDecision(Boolean var1) {
         this.decision = var1;
      }

      public Boolean getDecision() {
         return this.decision;
      }

      public boolean isApplicable(Map var1) {
         return true;
      }
   }

   private class ERoleMapImpl implements Map {
      private ECacheableRoles ecr;
      private boolean loaded = false;
      private Map allowedRoles;
      private Set deniedRoles;
      private Subject subj;
      private Resource resource;
      private ContextHandler context;
      private ESubject eSubj;
      private ResourceNode eResource;

      public ERoleMapImpl(Subject var2, Resource var3, ContextHandler var4, ECacheableRoles var5) {
         this.subj = var2;
         this.resource = var3;
         this.context = var4;
         this.allowedRoles = null;
         this.deniedRoles = null;
         this.ecr = var5;
      }

      private void load() {
         if (!this.loaded) {
            if (this.eResource == null) {
               this.eResource = new ResourceNodeImpl(this.resource);
            }

            if (EEngine.LOG.isDebugEnabled()) {
               if (this.eSubj == null) {
                  this.eSubj = new ESubjectImpl(this.subj);
               }

               EEngine.LOG.debug("getRoles (" + this.eSubj + " , " + this.eResource.getName() + ")\n");
            }

            Collection var1 = EEngine.this.getRoles(this.eResource);
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               this.evaluateRolePermit((ERole)var2.next());
            }

            this.loaded = true;
            this.deniedRoles = null;
            this.ecr.getDeniedRoles().clear();
         }

      }

      private void evaluateRolePermit(ERole var1) {
         String var2 = var1.getName();
         if (!this.ecr.getDeniedRoles().contains(var2) && (this.deniedRoles == null || !this.deniedRoles.contains(var2)) && !this.ecr.getAllowedRoles().containsKey(var2) && (this.allowedRoles == null || !this.allowedRoles.containsKey(var2)) && this.evaluateRole(var1)) {
            if (EEngine.this.checkCacheability(var1.getExpression(), 1)) {
               this.ecr.getAllowedRoles().put(var2, new ESecurityRoleImpl(var2));
            } else {
               if (this.allowedRoles == null) {
                  this.allowedRoles = new HashMap();
               }

               this.allowedRoles.put(var2, new ESecurityRoleImpl(var2));
            }
         }

      }

      private boolean evaluateRoleRecord(String var1) {
         if (!this.ecr.getAllowedRoles().containsKey(var1) && (this.allowedRoles == null || !this.allowedRoles.containsKey(var1))) {
            if (!this.loaded && !this.ecr.getDeniedRoles().contains(var1) && (this.deniedRoles == null || !this.deniedRoles.contains(var1))) {
               if (this.eResource == null) {
                  this.eResource = new ResourceNodeImpl(this.resource);
               }

               ERole var2 = EEngine.this.getRole(this.eResource, var1);
               if (var2 == null) {
                  if (EEngine.LOG.isDebugEnabled()) {
                     EEngine.LOG.debug("No role found, cannot evaluate");
                  }

                  return false;
               } else if (this.evaluateRole(var2)) {
                  if (EEngine.this.checkCacheability(var2.getExpression(), 1)) {
                     this.ecr.getAllowedRoles().put(var1, new ESecurityRoleImpl(var1));
                  } else {
                     if (this.allowedRoles == null) {
                        this.allowedRoles = new HashMap();
                     }

                     this.allowedRoles.put(var1, new ESecurityRoleImpl(var1));
                  }

                  return true;
               } else {
                  if (EEngine.this.checkCacheability(var2.getExpression(), 1)) {
                     this.ecr.getDeniedRoles().add(var1);
                  } else {
                     if (this.deniedRoles == null) {
                        this.deniedRoles = new HashSet();
                     }

                     this.deniedRoles.add(var1);
                  }

                  return false;
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      }

      public boolean isCacheableOnly() {
         return this.allowedRoles == null && this.deniedRoles == null;
      }

      private boolean evaluateRole(ERole var1) {
         if (this.eSubj == null) {
            this.eSubj = new ESubjectImpl(this.subj);
         }

         if (this.eResource == null) {
            this.eResource = new ResourceNodeImpl(this.resource);
         }

         boolean var2 = EEngine.this.evaluate(this.eSubj, var1, this.eResource, this.context);
         if (EEngine.LOG.isDebugEnabled()) {
            EEngine.LOG.debug((var2 ? "Role is permitted: " : "Role is denied: ") + var1.getPrimaryKey());
         }

         return var2;
      }

      public Object get(Object var1) {
         if (this.containsKey(var1)) {
            Object var2 = this.ecr.getAllowedRoles().get(var1);
            return var2 != null ? var2 : this.allowedRoles.get(var1);
         } else {
            return null;
         }
      }

      public boolean containsKey(Object var1) {
         return var1 == null ? false : this.evaluateRoleRecord(var1.toString());
      }

      public Object put(Object var1, Object var2) {
         throw new UnsupportedOperationException(SecurityLogger.getMapCanNotBeModified());
      }

      public void putAll(Map var1) {
         throw new UnsupportedOperationException(SecurityLogger.getMapCanNotBeModified());
      }

      public Object remove(Object var1) {
         throw new UnsupportedOperationException(SecurityLogger.getMapCanNotBeModified());
      }

      public void clear() {
         throw new UnsupportedOperationException(SecurityLogger.getMapCanNotBeModified());
      }

      public boolean containsValue(Object var1) {
         Object var2 = var1 instanceof SecurityRole ? ((SecurityRole)var1).getName() : var1;
         if (!this.ecr.getDeniedRoles().contains(var2) && (this.deniedRoles == null || !this.deniedRoles.contains(var2))) {
            this.load();
            return this.ecr.getAllowedRoles().containsValue(var1) || this.allowedRoles != null && this.allowedRoles.containsValue(var1);
         } else {
            return false;
         }
      }

      public boolean isEmpty() {
         boolean var1 = this.ecr.getAllowedRoles().isEmpty() && (this.allowedRoles == null || this.allowedRoles.isEmpty());
         if (var1 && !this.loaded) {
            this.load();
            var1 = this.ecr.getAllowedRoles().isEmpty() && (this.allowedRoles == null || this.allowedRoles.isEmpty());
         }

         return var1;
      }

      public int size() {
         this.load();
         int var1 = this.ecr.getAllowedRoles().size();
         if (this.allowedRoles != null) {
            var1 += this.allowedRoles.size();
         }

         return var1;
      }

      public Set entrySet() {
         this.load();
         if (this.allowedRoles == null) {
            return this.ecr.getAllowedRoles().entrySet();
         } else {
            return (Set)(this.ecr.getAllowedRoles().isEmpty() ? this.allowedRoles.entrySet() : new CombinedSet(new Collection[]{this.allowedRoles.entrySet(), this.ecr.getAllowedRoles().entrySet()}));
         }
      }

      public Set keySet() {
         this.load();
         if (this.allowedRoles == null) {
            return this.ecr.getAllowedRoles().keySet();
         } else {
            return (Set)(this.ecr.getAllowedRoles().isEmpty() ? this.allowedRoles.keySet() : new CombinedSet(new Collection[]{this.allowedRoles.keySet(), this.ecr.getAllowedRoles().keySet()}));
         }
      }

      public Collection values() {
         this.load();
         if (this.allowedRoles == null) {
            return this.ecr.getAllowedRoles().values();
         } else {
            return (Collection)(this.ecr.getAllowedRoles().isEmpty() ? this.allowedRoles.values() : new CombinedSet(new Collection[]{this.allowedRoles.values(), this.ecr.getAllowedRoles().values()}));
         }
      }

      public String toString() {
         Iterator var1 = this.keySet().iterator();
         StringBuffer var2 = new StringBuffer();
         if (var1.hasNext()) {
            var2.append(var1.next());
         }

         while(var1.hasNext()) {
            var2.append(",").append(var1.next());
         }

         return var2.toString();
      }
   }

   private static class ECacheableRoles {
      private Map allowedRoles = new ConcurrentHashMap();
      private Set deniedRoles = new ConcurrentHashSet();

      public ECacheableRoles() {
      }

      public Map getAllowedRoles() {
         return this.allowedRoles;
      }

      public Set getDeniedRoles() {
         return this.deniedRoles;
      }
   }

   private static class ESecurityRoleImpl implements SecurityRole {
      private String roleName;

      public ESecurityRoleImpl(Object var1) {
         this.roleName = var1.toString();
      }

      public String getName() {
         return this.roleName;
      }

      public String getDescription() {
         return this.roleName;
      }

      public String toString() {
         return this.roleName;
      }

      public int hashCode() {
         return this.roleName.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof ESecurityRoleImpl)) {
            return false;
         } else {
            ESecurityRoleImpl var2 = (ESecurityRoleImpl)var1;
            return this.roleName.equals(var2.roleName);
         }
      }
   }
}
