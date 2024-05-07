package weblogic.deploy.api.shared;

public class WebLogicTargetType {
   private int value;
   private static final WebLogicTargetType[] enumValueTable;
   private static String[] stringTable;
   public static final WebLogicTargetType SERVER = new WebLogicTargetType(0);
   public static final WebLogicTargetType CLUSTER = new WebLogicTargetType(1);
   public static final WebLogicTargetType VIRTUALHOST = new WebLogicTargetType(2);
   public static final WebLogicTargetType JMSSERVER = new WebLogicTargetType(3);
   public static final WebLogicTargetType SAFAGENT = new WebLogicTargetType(4);

   protected WebLogicTargetType(int var1) {
      this.value = var1;
   }

   public int getValue() {
      return this.value;
   }

   protected String[] getStringTable() {
      return stringTable;
   }

   protected WebLogicTargetType[] getEnumValueTable() {
      return enumValueTable;
   }

   public static WebLogicTargetType getWebLogicTargetType(int var0) {
      return enumValueTable[var0];
   }

   public String toString() {
      String[] var1 = this.getStringTable();
      int var2 = this.value - this.getOffset();
      return var1 != null && var2 >= 0 && var2 < var1.length ? var1[var2] : Integer.toString(this.value);
   }

   protected int getOffset() {
      return 0;
   }

   static {
      enumValueTable = new WebLogicTargetType[]{SERVER, CLUSTER, VIRTUALHOST, JMSSERVER, SAFAGENT};
      stringTable = new String[]{"server", "cluster", "virtual host", "JMS server", "SAF agent"};
   }
}
