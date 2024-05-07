package weblogic.deploy.internal.targetserver.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class TargetModuleState implements Externalizable {
   private static final long serialVersionUID = 6815593282455552332L;
   private String moduleId;
   private String targetName;
   private String targetType;
   private String serverName;
   private String curState;
   private String type;
   private String submoduleId;

   public TargetModuleState() {
      this.submoduleId = null;
   }

   TargetModuleState(String var1, String var2, String var3, String var4, String var5, String var6) {
      this(createName(var1, var2), var3, var4, var5, var6);
      this.submoduleId = var2;
   }

   public TargetModuleState(String var1, String var2, String var3, String var4, String var5) {
      this.submoduleId = null;
      this.moduleId = var1;
      this.targetName = var3;
      this.targetType = var4;
      this.serverName = var5;
      this.type = var2;
   }

   public static String createName(String var0, String var1) {
      return var1 + "[" + var0 + "]";
   }

   public static String extractModule(String var0) {
      int var1 = var0.indexOf("[");
      return var1 == -1 ? var0 : var0.substring(var1, var0.indexOf("]"));
   }

   public static String extractSubmodule(String var0) {
      int var1 = var0.indexOf("[");
      return var1 == -1 ? null : var0.substring(0, var1);
   }

   public String getCurrentState() {
      return this.curState;
   }

   public void setCurrentState(String var1) {
      this.curState = var1;
   }

   public String getModuleId() {
      return this.moduleId;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getTargetType() {
      return this.targetType;
   }

   public String getType() {
      return this.type;
   }

   public String getSubmoduleId() {
      return this.submoduleId;
   }

   public boolean isSubmodule() {
      return this.getSubmoduleId() != null;
   }

   public String getParentModuleId() {
      return !this.isSubmodule() ? null : extractModule(this.getModuleId());
   }

   public boolean isLogicalTarget() {
      return !this.serverName.equals(this.targetName);
   }

   public String toString() {
      StringBuffer var1 = (new StringBuffer("TargetModuleState[")).append("ModuleId=").append(this.moduleId).append(",TargetName=").append(this.targetName).append("/").append(this.targetType);
      if (this.isLogicalTarget()) {
         var1.append("[").append(this.serverName).append("]");
      }

      var1.append(",State=").append(this.curState);
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.moduleId);
      var1.writeObject(this.targetName);
      var1.writeObject(this.targetType);
      var1.writeObject(this.serverName);
      var1.writeObject(this.curState);
      var1.writeObject(this.type);
      var1.writeObject(this.submoduleId);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.moduleId = (String)var1.readObject();
      this.targetName = (String)var1.readObject();
      this.targetType = (String)var1.readObject();
      this.serverName = (String)var1.readObject();
      this.curState = (String)var1.readObject();
      this.type = (String)var1.readObject();
      this.submoduleId = (String)var1.readObject();
   }
}
