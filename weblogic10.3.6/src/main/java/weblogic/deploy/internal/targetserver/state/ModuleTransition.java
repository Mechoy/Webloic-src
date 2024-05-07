package weblogic.deploy.internal.targetserver.state;

import java.io.Serializable;

public final class ModuleTransition implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String curState;
   private final String newState;
   private final String name;
   private final long gentime;
   private final TargetModuleState tm;

   public ModuleTransition(String var1, String var2, String var3, long var4, TargetModuleState var6) {
      this.curState = var1;
      this.newState = var2;
      this.name = var3;
      this.gentime = var4;
      this.tm = var6;
   }

   public String getCurrentState() {
      return this.curState;
   }

   public String getNewState() {
      return this.newState;
   }

   public String getName() {
      return this.name;
   }

   public long getGenerationTime() {
      return this.gentime;
   }

   public TargetModuleState getModule() {
      return this.tm;
   }
}
