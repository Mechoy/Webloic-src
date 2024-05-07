package weblogic.ejb.container.cmp11.rdbms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.cmp11.rdbms.finders.Finder;
import weblogic.ejb.container.cmp11.rdbms.finders.InvalidFinderException;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl60.FieldMapBean;
import weblogic.j2ee.descriptor.wl60.FinderBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;

public final class RDBMSDeploymentInfo {
   protected EJBComplianceTextFormatter fmt;
   private WeblogicRdbmsJarBean wlJar;
   private Map rdbmsBeanMap = new HashMap();

   public RDBMSDeploymentInfo(WeblogicRdbmsJarBean var1, CMPDDParser.CompatibilitySettings var2, String var3) throws RDBMSException, InvalidFinderException {
      this.wlJar = var1;
      this.fmt = new EJBComplianceTextFormatter();
      WeblogicRdbmsBeanBean[] var4 = var1.getWeblogicRdbmsBeans();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         RDBMSBean var6 = new RDBMSBean();
         var6.setEjbName(var4[var5].getEjbName());
         var6.setFileName(var3);
         var6.setPoolName(var4[var5].getPoolName());
         var6.setDataSourceName(var4[var5].getDataSourceJndiName());
         var6.setTableName(var4[var5].getTableName());
         var6.setEnableTunedUpdates(var4[var5].isEnableTunedUpdates());
         if (var2 != null) {
            var6.setUseQuotedNames(var2.useQuotedNames);
            var6.setTransactionIsolation(var2.isolationLevel);
         }

         this.initFieldMaps(var4[var5], var6);
         this.initFinders(var4[var5], var6, var2);
         this.rdbmsBeanMap.put(var6.getEjbName(), var6);
      }

   }

   public RDBMSBean getRDBMSBean(String var1) {
      return (RDBMSBean)this.rdbmsBeanMap.get(var1);
   }

   public Map getRDBMSBeanMap() {
      return this.rdbmsBeanMap;
   }

   public WeblogicRdbmsJarBean getWeblogicRdbmsJarBean() {
      return this.wlJar;
   }

   private void initFieldMaps(WeblogicRdbmsBeanBean var1, RDBMSBean var2) throws RDBMSException {
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();
      FieldMapBean[] var5 = var1.getFieldMaps();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (!var3.add(var5[var6].getCmpField())) {
            throw new RDBMSException(this.fmt.DUPLICATE_MAPPING_FOR_CMP_FIELD(var2.getFileName(), var2.getEjbName(), var5[var6].getCmpField()));
         }

         if (!var4.add(var5[var6].getDbmsColumn())) {
            throw new RDBMSException(this.fmt.DUPLICATE_MAPPING_FOR_DBMS_COLUMN(var2.getFileName(), var2.getEjbName(), var5[var6].getDbmsColumn()));
         }

         RDBMSBean.ObjectLink var7 = new RDBMSBean.ObjectLink(var5[var6].getCmpField(), var5[var6].getDbmsColumn());
         var2.addObjectLink(var7);
      }

   }

   private void initFinders(WeblogicRdbmsBeanBean var1, RDBMSBean var2, CMPDDParser.CompatibilitySettings var3) throws InvalidFinderException {
      FinderBean[] var4 = var1.getFinders();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         boolean var6 = true;
         String var7 = var4[var5].getFinderSql();
         if (var7 == null) {
            var7 = var4[var5].getFinderQuery();
            if ("empty-finder".equals(var7)) {
               var7 = "";
            }

            var6 = false;
         }

         if (var7 != null) {
            Finder var8 = new Finder(var4[var5].getFinderName(), var7);
            var8.setUsingSQL(var6);
            String[] var9 = var4[var5].getFinderParams();

            for(int var10 = 0; var10 < var9.length; ++var10) {
               var8.addParameterType(var9[var10]);
            }

            List var14 = null;
            if (var3 != null) {
               String var11 = DDUtils.getMethodSignature(var4[var5].getFinderName(), var4[var5].getFinderParams());
               var14 = (List)var3.finderExpressionMap.get(var11);
            }

            if (var14 != null) {
               Iterator var15 = var14.iterator();

               while(var15.hasNext()) {
                  CMPDDParser.FinderExpression var12 = (CMPDDParser.FinderExpression)var15.next();
                  Finder.FinderExpression var13 = new Finder.FinderExpression(var12.expressionNumber, var12.expressionText, var12.expressionType);
                  var8.addFinderExpression(var13);
               }
            }

            if (var4[var5].isFindForUpdate()) {
               Finder.FinderOptions var16 = new Finder.FinderOptions();
               var16.setFindForUpdate(true);
               var8.setFinderOptions(var16);
            }

            var2.addFinder(var8);
         }
      }

   }
}
