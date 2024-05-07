package weblogic.deploy.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Version;

public final class AggregateDeploymentVersion implements Version {
   private static final long serialVersionUID = 7559709643035775633L;
   private static final String IDENTITY = "Deployments";
   private Map versionComponentsMap = null;

   public AggregateDeploymentVersion() {
      this.versionComponentsMap = new HashMap();
   }

   public static AggregateDeploymentVersion createAggregateDeploymentVersion() {
      return new AggregateDeploymentVersion();
   }

   public static AggregateDeploymentVersion createAggregateDeploymentVersion(Map var0) {
      AggregateDeploymentVersion var1 = new AggregateDeploymentVersion();
      if (var0 != null) {
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.addOrUpdateDeploymentVersion((String)var3.getKey(), (DeploymentVersion)var3.getValue());
         }
      }

      return var1;
   }

   public String getIdentity() {
      return "Deployments";
   }

   public Map getVersionComponents() {
      synchronized(this.versionComponentsMap) {
         return new HashMap(this.versionComponentsMap);
      }
   }

   public void addOrUpdateDeploymentVersion(String var1, DeploymentVersion var2) {
      Version var3 = null;
      synchronized(this.versionComponentsMap) {
         var3 = (Version)this.versionComponentsMap.put(var1, var2);
      }

      if (Debug.isDeploymentDebugEnabled()) {
         String var4 = null;
         if (var3 != null) {
            var4 = "AggregateDeploymentVersion: Updated version for id '" + var1 + "' from: '" + var3 + "' to: '" + var2 + "', '" + this.toString() + "'";
         } else {
            var4 = "AggregateDeploymentVersion: Updated version for id '" + var1 + "' from: '" + var3 + "' to: '" + var2 + "', '" + this.toString() + "'";
         }

         Debug.deploymentDebug(var4);
      }

   }

   public void removeDeploymentVersionFor(String var1) {
      synchronized(this.versionComponentsMap) {
         this.versionComponentsMap.remove(var1);
      }

      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("AggregateDeploymentVersion: removed id '" + var1 + ", " + this.toString() + "'");
      }

   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (var1 instanceof AggregateDeploymentVersion) {
         Map var2 = this.getVersionComponents();
         Map var3 = ((AggregateDeploymentVersion)var1).getVersionComponents();
         return var2.equals(var3);
      } else {
         return false;
      }
   }

   public int hashCode() {
      synchronized(this.versionComponentsMap) {
         return this.versionComponentsMap.hashCode();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("Aggregated Deployment Version: (");
      var1.append("id: ").append(this.getIdentity());
      var1.append(" - versionComponentsMap{");
      synchronized(this.versionComponentsMap) {
         Iterator var3 = this.versionComponentsMap.entrySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            var1.append(" [id: ");
            var1.append(var5);
            var1.append(", version: ");
            var1.append(var4.getValue().toString());
            var1.append("]");
         }
      }

      var1.append("}").append(")");
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      synchronized(this.versionComponentsMap) {
         var1.writeObject(this.versionComponentsMap);
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      synchronized(this.versionComponentsMap) {
         this.versionComponentsMap.putAll((Map)var1.readObject());
      }
   }
}
