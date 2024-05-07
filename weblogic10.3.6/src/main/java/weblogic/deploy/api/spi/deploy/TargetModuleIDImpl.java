package weblogic.deploy.api.spi.deploy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;

public class TargetModuleIDImpl extends WebLogicTargetModuleID implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final String NAME_PROP = "Name";
   private static final String TARGET_PROP = "Target";
   private static final String TARGET_TYPE_PROP = "WebLogicTargetType";
   private static final String PARENT_PROP = "Application";
   private static final boolean debug = Debug.isDebug("deploy");
   private String name;
   private Target target;
   private WebLogicTargetModuleID parentTID;
   private List childTID;
   private transient WebLogicDeploymentManager manager;
   private Set servers;
   private boolean targeted;
   private Hashtable objectName;
   private String webURL;

   public TargetModuleIDImpl(String var1, Target var2, TargetModuleID var3, int var4, DeploymentManager var5) throws IllegalArgumentException {
      super(var4);
      this.parentTID = null;
      this.childTID = null;
      this.servers = null;
      this.targeted = true;
      this.name = var1;
      if (var1 == null) {
         throw new AssertionError("No name for TargetModuleID");
      } else {
         this.target = var2;
         this.parentTID = (WebLogicTargetModuleID)var3;
         this.manager = (WebLogicDeploymentManager)var5;
         this.objectName = this.setObjectName();
         if (this.parentTID != null) {
            ((TargetModuleIDImpl)this.parentTID).addChildTargetModuleID(this);
         }

      }
   }

   public TargetModuleIDImpl(String var1, Target var2, TargetModuleID var3, ModuleType var4, DeploymentManager var5) throws IllegalArgumentException {
      this(var1, var2, var3, var4.getValue(), var5);
   }

   public TargetModuleID[] getChildTargetModuleID() {
      return this.childTID != null && this.childTID.size() != 0 ? (TargetModuleID[])((TargetModuleID[])this.childTID.toArray(new TargetModuleID[this.childTID.size()])) : null;
   }

   public Target getTarget() {
      return this.target;
   }

   public String getModuleID() {
      return this.name;
   }

   public String getWebURL() {
      return this.webURL;
   }

   public void setWebURL(String var1) {
      this.webURL = var1;
   }

   public String toString() {
      return this.objectName.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return var1 instanceof TargetModuleID ? this.hashCode() == var1.hashCode() : false;
      }
   }

   public int hashCode() {
      return this.objectName.hashCode();
   }

   public TargetModuleID getParentTargetModuleID() {
      return this.parentTID;
   }

   public String getApplicationName() {
      return this.parentTID != null ? this.parentTID.getApplicationName() : ApplicationVersionUtils.getApplicationName(this.name);
   }

   public String getVersion() {
      return this.parentTID != null ? this.parentTID.getVersion() : ApplicationVersionUtils.getVersionId(this.name);
   }

   public String getArchiveVersion() {
      return this.parentTID != null ? this.parentTID.getVersion() : ApplicationVersionUtils.getArchiveVersion(this.getVersion());
   }

   public String getPlanVersion() {
      return this.parentTID != null ? this.parentTID.getVersion() : ApplicationVersionUtils.getPlanVersion(this.getVersion());
   }

   public Target[] getServers() throws IllegalStateException {
      try {
         Set var1 = this.getServersForTarget();
         return (Target[])((Target[])var1.toArray(new Target[var1.size()]));
      } catch (ServerConnectionException var3) {
         if (debug) {
            var3.printStackTrace();
         }

         IllegalStateException var2 = new IllegalStateException(SPIDeployerLogger.connectionError());
         var2.initCause(var3.getRootCause());
         throw var2;
      }
   }

   public boolean isOnVirtualHost() {
      return ((TargetImpl)this.getTarget()).isVirtualHost();
   }

   public boolean isOnServer() {
      return ((TargetImpl)this.getTarget()).isServer();
   }

   public boolean isOnCluster() {
      return ((TargetImpl)this.getTarget()).isCluster();
   }

   public boolean isOnJMSServer() {
      return ((TargetImpl)this.getTarget()).isJMSServer();
   }

   public boolean isOnSAFAgent() {
      return ((TargetImpl)this.getTarget()).isSAFAgent();
   }

   public Set getServersForTarget() throws ServerConnectionException {
      if (this.servers == null) {
         this.setServersForTarget();
      }

      return this.servers;
   }

   void addChildTargetModuleID(TargetModuleID var1) {
      if (var1 != null) {
         if (this.childTID == null) {
            this.childTID = new ArrayList();
         }

         this.childTID.add(var1);
      }

   }

   public WebLogicDeploymentManager getManager() {
      return this.manager;
   }

   public boolean isTargeted() {
      return this.targeted;
   }

   public void setTargeted(boolean var1) {
      this.targeted = var1;
   }

   private Hashtable setObjectName() {
      Hashtable var1 = new Hashtable(5);
      if (this.name != null && this.name.length() > 0) {
         var1.put("Name", this.name);
      }

      var1.put("Target", this.target.getName());
      var1.put("WebLogicTargetType", this.target.getDescription());
      if (this.parentTID != null) {
         var1.put("Application", this.parentTID.getModuleID());
      }

      return var1;
   }

   private void setServersForTarget() throws ServerConnectionException {
      this.servers = new HashSet();
      ServerConnection var1 = this.manager.getServerConnection();
      if (this.manager.isConnected()) {
         if (this.isOnServer()) {
            this.servers.add((TargetImpl)this.getTarget());
         } else if (this.isOnCluster()) {
            this.servers.addAll(var1.getServersForCluster((TargetImpl)this.getTarget()));
         } else if (this.isOnVirtualHost()) {
            this.servers.addAll(var1.getServersForHost((TargetImpl)this.getTarget()));
         } else if (this.isOnJMSServer()) {
            this.servers.addAll(var1.getServersForJmsServer((TargetImpl)this.getTarget()));
         } else if (this.isOnSAFAgent()) {
            this.servers.addAll(var1.getServersForSafAgent((TargetImpl)this.getTarget()));
         }
      }

      if (debug) {
         this.dumpServers();
      }

   }

   private void dumpServers() {
      try {
         Debug.say("Servers in target " + this.getTarget().getName() + ":");
         Iterator var1 = this.getServersForTarget().iterator();

         while(var1.hasNext()) {
            Debug.say("   " + ((TargetImpl)var1.next()).getName());
         }
      } catch (ServerConnectionException var2) {
         var2.printStackTrace();
      }

   }
}
