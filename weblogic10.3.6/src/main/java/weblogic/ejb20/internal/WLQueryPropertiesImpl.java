package weblogic.ejb20.internal;

import java.io.Serializable;
import java.util.Properties;
import javax.ejb.FinderException;
import weblogic.ejb.WLQueryProperties;

public class WLQueryPropertiesImpl implements WLQueryProperties, Serializable {
   private static final long serialVersionUID = 1917450515366196167L;
   private short txAttribute = 1;
   private int maxElements = 0;
   private int isolationLevel = -1;
   private boolean includeUpdates = false;
   private boolean resultTypeRemote = false;
   private boolean sqlSelectDistinct = false;
   private String fieldGroupName = null;
   private String relationshipCachingName = null;
   private String sqlShapeName = null;
   private boolean enableCaching = false;

   public void setTransaction(short var1) throws FinderException {
      switch (var1) {
         case 1:
         case 3:
         case 4:
            this.txAttribute = var1;
            return;
         case 2:
         default:
            throw new FinderException("Invalid Transaction Attribute setting: " + var1);
      }
   }

   public short getTransaction() throws FinderException {
      return this.txAttribute;
   }

   public void setMaxElements(int var1) throws FinderException {
      if (var1 < 0) {
         throw new FinderException("Invalid setting for MaxElements: " + var1);
      } else {
         this.maxElements = var1;
      }
   }

   public int getMaxElements() throws FinderException {
      return this.maxElements;
   }

   public void setIncludeUpdates(boolean var1) throws FinderException {
      this.includeUpdates = var1;
   }

   public boolean getIncludeUpdates() throws FinderException {
      return this.includeUpdates;
   }

   public void setResultTypeRemote(boolean var1) throws FinderException {
      this.resultTypeRemote = var1;
   }

   public boolean isResultTypeRemote() throws FinderException {
      return this.resultTypeRemote;
   }

   public void setFieldGroupName(String var1) throws FinderException {
      this.fieldGroupName = var1;
   }

   public String getFieldGroupName() throws FinderException {
      return this.fieldGroupName;
   }

   public void setSQLSelectDistinct(boolean var1) throws FinderException {
      this.sqlSelectDistinct = var1;
   }

   public boolean getSQLSelectDistinct() throws FinderException {
      return this.sqlSelectDistinct;
   }

   public void setIsolationLevel(int var1) throws FinderException {
      switch (var1) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            this.isolationLevel = var1;
            return;
         default:
            throw new FinderException("Invalid Transaction Isolation setting: " + var1);
      }
   }

   public int getIsolationLevel() throws FinderException {
      return this.isolationLevel;
   }

   public String getRelationshipCachingName() throws FinderException {
      return this.relationshipCachingName;
   }

   public void setRelationshipCachingName(String var1) throws FinderException {
      this.relationshipCachingName = var1;
   }

   protected void setProperties(Properties var1) throws FinderException {
      if (var1 != null) {
         String var2 = var1.getProperty("GROUP_NAME");
         if (var2 != null && !var2.equals("")) {
            this.setFieldGroupName(var2);
         }

         String var3 = var1.getProperty("SQL_SELECT_DISTINCT");
         if (var3 != null) {
            if (var3.equalsIgnoreCase("true")) {
               this.setSQLSelectDistinct(true);
            } else {
               if (!var3.equalsIgnoreCase("false")) {
                  throw new FinderException("Invalid SQL_SELECT_DISTINCT property value: " + var3);
               }

               this.setSQLSelectDistinct(false);
            }
         }

         String var4 = var1.getProperty("ISOLATION_LEVEL");
         if (var4 != null && !var4.equals("")) {
            try {
               this.setIsolationLevel(Integer.valueOf(var4));
            } catch (NumberFormatException var6) {
               throw new FinderException("Invalid isolation level property: " + var4);
            }
         }

         String var5 = var1.getProperty("RELATIONSHIP_CACHING_NAME");
         if (var5 != null && !var5.equals("")) {
            this.setRelationshipCachingName(var5);
         }
      }

   }

   public String getSqlShapeName() throws FinderException {
      return this.sqlShapeName;
   }

   public void setSqlShapeName(String var1) throws FinderException {
      this.sqlShapeName = var1;
   }

   public void setEnableQueryCaching(boolean var1) throws FinderException {
      this.enableCaching = var1;
   }

   public boolean getEnableQueryCaching() throws FinderException {
      return this.enableCaching;
   }
}
