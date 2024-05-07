package weblogic.ejb.container.cmp.rdbms.compliance;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.finders.RDBMSFinder;
import weblogic.ejb.container.compliance.BaseComplianceChecker;
import weblogic.ejb.container.compliance.EJBComplianceChecker;
import weblogic.ejb.container.interfaces.EntityBeanQuery;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.validation.ComplianceException;
import weblogic.utils.ErrorCollectionException;

public final class RDBMSComplianceChecker extends BaseComplianceChecker {
   private Map beanMap = null;
   private Relationships relationships = null;
   private Map dependentMap = null;
   private Map rdbmsBeanMap = null;
   private Map rdbmsRelationMap = null;
   private Map rdbmsDependentMap = null;
   private WeblogicRdbmsJarBean cmpDesc = null;
   private ErrorCollectionException errors = null;

   public RDBMSComplianceChecker(Map var1, Relationships var2, Map var3, Map var4, Map var5, Map var6, WeblogicRdbmsJarBean var7) {
      this.beanMap = var1;
      this.relationships = var2;
      this.dependentMap = var3;
      this.rdbmsBeanMap = var4;
      this.rdbmsRelationMap = var5;
      this.rdbmsDependentMap = var6;
      this.cmpDesc = var7;
      this.errors = new ErrorCollectionException();
   }

   public void checkCompliance() throws ErrorCollectionException {
      if (!EJBComplianceChecker.isNeedCheck) {
         if (this.dependentMap != null) {
            this.errors.add(new ComplianceException(this.fmt.DEPENDENT_OBJECTS_NOT_SUPPORTED()));
         }

         if (!this.errors.isEmpty()) {
            throw this.errors;
         } else {
            this.checkConnectedSubGraph();
            if (!this.errors.isEmpty()) {
               throw this.errors;
            } else {
               HashMap var1 = new HashMap();
               HashMap var2 = new HashMap();
               this.calculateEjbMaps(var1, var2);
               if (!this.errors.isEmpty()) {
                  throw this.errors;
               } else {
                  RDBMSBeanChecker var3 = null;
                  RDBMSRelationChecker var4 = null;
                  var3 = new RDBMSBeanChecker(var1, var2, this.rdbmsBeanMap, this.rdbmsRelationMap, this.cmpDesc);
                  var4 = new RDBMSRelationChecker(var1, var2, this.rdbmsBeanMap, this.rdbmsRelationMap);

                  try {
                     var3.runComplianceCheck();
                  } catch (ErrorCollectionException var7) {
                     this.errors.addCollection(var7);
                  }

                  try {
                     var4.runComplianceCheck();
                  } catch (ErrorCollectionException var6) {
                     this.errors.addCollection(var6);
                  }

                  if (!this.errors.isEmpty()) {
                     throw this.errors;
                  }
               }
            }
         }
      }
   }

   private void calculateEjbMaps(Map var1, Map var2) {
      String var4;
      CMPBeanDescriptor var5;
      for(Iterator var3 = this.rdbmsBeanMap.keySet().iterator(); var3.hasNext(); this.checkFinderQuery(var5, (RDBMSBean)this.rdbmsBeanMap.get(var4))) {
         var4 = (String)var3.next();
         var5 = (CMPBeanDescriptor)this.beanMap.get(var4);
         if (var5 == null) {
            this.errors.add(new ComplianceException(this.fmt.NO_MATCHING_CMP_BEAN(var4), new DescriptorErrorInfo("<entity>", var4, var4)));
         } else {
            var1.put(var4, var5);
         }
      }

      Iterator var8 = this.rdbmsRelationMap.keySet().iterator();

      while(var8.hasNext()) {
         String var7 = (String)var8.next();
         EjbRelation var6 = this.relationships.getEjbRelation(var7);
         if (var6 == null) {
            this.errors.add(new ComplianceException(this.fmt.NO_MATCHING_EJB_RELATION_IN_EJB_DD(var7), new DescriptorErrorInfo("<ejb-relation>", var7, var7)));
         } else {
            var2.put(var7, var6);
         }
      }

   }

   private void checkFinderQuery(CMPBeanDescriptor var1, RDBMSBean var2) {
      Collection var3 = var1.getAllQueries();
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(true) {
            EntityBeanQuery var5;
            RDBMSFinder var6;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     var5 = (EntityBeanQuery)var4.next();
                  } while(var5.getQueryText() != null && var5.getQueryText().trim().length() > 0);

                  var6 = (RDBMSFinder)var2.getRdbmsFinders().get(new RDBMSFinder.FinderKey(var5.getMethodName(), var5.getMethodParams()));
                  if (var6 == null) {
                     this.errors.add(new ComplianceException(this.fmt.EJBQLCANNOTBENULL(var1.getEJBName(), var5.getMethodName())));
                     return;
                  }
               } while(var6.getSqlQueries() != null);
            } while(var6.getEjbQlQuery() != null && var6.getEjbQlQuery().trim().length() != 0);

            this.errors.add(new ComplianceException(this.fmt.EJBANDWLQLCANNOTBENULL(var1.getEJBName(), var5.getMethodName())));
         }
      }
   }

   private void checkConnectedSubGraph() {
      if (this.relationships != null) {
         Map var1 = this.relationships.getAllEjbRelations();
         HashSet var2 = new HashSet(this.rdbmsBeanMap.keySet());
         HashSet var3 = new HashSet(this.rdbmsRelationMap.keySet());
         Iterator var4 = var1.values().iterator();

         while(var4.hasNext()) {
            EjbRelation var5 = (EjbRelation)var4.next();
            Iterator var6 = var5.getAllEjbRelationshipRoles().iterator();
            EjbRelationshipRole var7 = (EjbRelationshipRole)var6.next();
            EjbRelationshipRole var8 = (EjbRelationshipRole)var6.next();
            RoleSource var9 = var7.getRoleSource();
            RoleSource var10 = var8.getRoleSource();
            this.checkConnection(var2, var3, var5, var9, var10);
            this.checkConnection(var2, var3, var5, var10, var9);
         }
      }

   }

   private void checkConnection(Set var1, Set var2, EjbRelation var3, RoleSource var4, RoleSource var5) {
      if (var1.contains(var4.getEjbName())) {
         if (!var2.contains(var3.getEjbRelationName())) {
            this.errors.add(new ComplianceException(this.fmt.MISSING_RELATION_FOR_BEAN(var4.getEjbName(), var3.getEjbRelationName()), new DescriptorErrorInfo("<weblogic-rdbms-relation>", var3.getEjbRelationName(), var4.getEjbName())));
         }

         if (!var1.contains(var5.getEjbName())) {
            this.errors.add(new ComplianceException(this.fmt.MISSING_BEAN_FOR_BEAN(var4.getEjbName(), var5.getEjbName()), new DescriptorErrorInfo("<weblogic-rdbms-bean>", var3.getEjbRelationName(), var5.getEjbName())));
         }
      }

   }
}
