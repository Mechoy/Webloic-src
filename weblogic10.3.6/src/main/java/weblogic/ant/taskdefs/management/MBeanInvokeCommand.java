package weblogic.ant.taskdefs.management;

import java.util.ArrayList;
import java.util.Iterator;

public class MBeanInvokeCommand implements MBeanCommand {
   private String mbeanType;
   private String methodName;
   private String mbean;
   private String arguments;
   private String domain;
   private String mbeanName;
   private ArrayList invokeCommands = new ArrayList();

   public int getCommandType() {
      return 7;
   }

   public void setType(String var1) {
      this.mbeanType = var1;
   }

   public String getType() {
      return this.mbeanType;
   }

   public void setDomain(String var1) {
      this.domain = var1;
   }

   public String getDomain() {
      return this.domain;
   }

   public void setName(String var1) {
      this.mbeanName = var1;
   }

   public String getName() {
      return this.mbeanName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMBean(String var1) {
      this.mbean = var1;
   }

   public String getMBean() {
      return this.mbean;
   }

   public void setArguments(String var1) {
      this.arguments = var1;
   }

   public String getArguments() {
      return this.arguments;
   }

   public void addInvoke(MBeanInvokeCommand var1) {
      this.invokeCommands.add(var1);
   }

   public Iterator getInvokeCommands() {
      return this.invokeCommands.iterator();
   }
}
