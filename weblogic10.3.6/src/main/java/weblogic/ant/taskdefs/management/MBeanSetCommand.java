package weblogic.ant.taskdefs.management;

public class MBeanSetCommand implements MBeanCommand {
   private String mbean;
   private String attribute;
   private String value;

   public int getCommandType() {
      return 3;
   }

   public void setAttribute(String var1) {
      this.attribute = var1;
   }

   public String getAttribute() {
      return this.attribute;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public String getValue() {
      return this.value;
   }

   public void setMBean(String var1) {
      this.mbean = var1;
   }

   public String getMBean() {
      return this.mbean;
   }
}
