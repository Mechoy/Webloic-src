package weblogic.ant.taskdefs.management;

public class MBeanDeleteCommand implements MBeanCommand {
   private String mbean;

   public int getCommandType() {
      return 2;
   }

   public void setMBean(String var1) {
      this.mbean = var1;
   }

   public String getMBean() {
      return this.mbean;
   }
}
