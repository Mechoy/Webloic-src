package weblogic.cluster;

import java.util.EventObject;

public final class ClusterMembersChangeEvent extends EventObject {
   private static final long serialVersionUID = 1894491991090401626L;
   public static final int ADD = 0;
   public static final int REMOVE = 1;
   public static final int UPDATE = 2;
   public static final int DISCOVER = 3;
   private int action;
   private ClusterMemberInfo member;

   public int getAction() {
      return this.action;
   }

   public ClusterMemberInfo getClusterMemberInfo() {
      return this.member;
   }

   public ClusterMembersChangeEvent(Object var1, int var2, ClusterMemberInfo var3) {
      super(var1);
      this.action = var2;
      this.member = var3;
   }
}
