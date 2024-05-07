package weblogic.deploy.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.service.Version;

public class DeploymentVersion implements Version {
   private static final long serialVersionUID = 7424059632370370194L;
   public static final String ARCHIVE_TIMESTAMP = "ARCHIVE_TIMESTAMP";
   public static final String PLAN_TIMESTAMP = "PLAN_TIMESTAMP";
   private String identity;
   private Map versionMap;

   public DeploymentVersion() {
      this.identity = null;
      this.versionMap = null;
      this.versionMap = new HashMap();
   }

   public DeploymentVersion(String var1, long var2, long var4) {
      this();
      this.identity = var1;
      this.versionMap.put("ARCHIVE_TIMESTAMP", new Long(var2));
      this.versionMap.put("PLAN_TIMESTAMP", new Long(var4));
   }

   public String getIdentity() {
      return this.identity;
   }

   public Map getVersionComponents() {
      synchronized(this.versionMap) {
         return this.versionMap;
      }
   }

   public long getArchiveTimeStamp() {
      return this.getValue("ARCHIVE_TIMESTAMP");
   }

   public long getPlanTimeStamp() {
      return this.getValue("PLAN_TIMESTAMP");
   }

   public void update(boolean var1, boolean var2) {
      long var3 = System.currentTimeMillis();
      if (var1) {
         this.resetArchiveTimeStamp(var3);
         this.resetPlanTimeStamp(var3);
      } else if (!var2) {
         this.resetPlanTimeStamp(var3);
      }

   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (var1 instanceof DeploymentVersion) {
         Map var2 = this.getVersionComponents();
         Map var3 = ((Version)var1).getVersionComponents();
         return var2.equals(var3);
      } else {
         return false;
      }
   }

   public int hashCode() {
      synchronized(this.versionMap) {
         return this.versionMap.hashCode();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(").append("id: ");
      var1.append(this.identity);
      var1.append(" - versionMap{");
      synchronized(this.versionMap) {
         Iterator var3 = this.versionMap.entrySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            var1.append(" [Component: ");
            var1.append(var5);
            var1.append(" - version: ");
            var1.append(var4.getValue().toString());
            var1.append("]");
         }
      }

      var1.append("}").append(")");
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.identity);
      synchronized(this.versionMap) {
         var1.writeObject(this.versionMap);
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.identity = (String)var1.readObject();
      synchronized(this.versionMap) {
         this.versionMap.putAll((Map)var1.readObject());
      }
   }

   private void resetArchiveTimeStamp(long var1) {
      synchronized(this.versionMap) {
         this.versionMap.put("ARCHIVE_TIMESTAMP", new Long(var1));
      }
   }

   private void resetPlanTimeStamp(long var1) {
      synchronized(this.versionMap) {
         this.versionMap.put("PLAN_TIMESTAMP", new Long(var1));
      }
   }

   private long getValue(String var1) {
      synchronized(this.versionMap) {
         return (Long)this.versionMap.get(var1);
      }
   }
}
