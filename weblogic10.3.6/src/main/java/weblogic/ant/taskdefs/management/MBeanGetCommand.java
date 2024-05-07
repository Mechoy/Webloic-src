package weblogic.ant.taskdefs.management;

public class MBeanGetCommand implements MBeanCommand {
   private String mbean;
   private String attribute;
   private String property = null;

   public int getCommandType() {
      return 4;
   }

   public void setAttribute(String var1) {
      this.attribute = var1;
   }

   public String getAttribute() {
      return this.attribute;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public String getProperty() {
      return this.property;
   }

   public void setMBean(String var1) {
      this.mbean = var1;
   }

   public String getMBean() {
      return this.mbean;
   }
}
