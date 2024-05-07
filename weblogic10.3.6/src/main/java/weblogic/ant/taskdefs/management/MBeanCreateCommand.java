package weblogic.ant.taskdefs.management;

import java.util.ArrayList;
import java.util.Iterator;

public class MBeanCreateCommand implements MBeanCommand {
   private String mbeanType;
   private String mbeanName;
   private String mbean;
   private String property;
   private String domain;
   private String realm;
   private ArrayList setCommands = new ArrayList();
   private ArrayList createCommands = new ArrayList();
   private ArrayList invokeCommands = new ArrayList();

   public int getCommandType() {
      return 1;
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

   public void setRealm(String var1) {
      this.realm = var1;
   }

   public String getRealm() {
      return this.realm;
   }

   public void setName(String var1) {
      this.mbeanName = var1;
   }

   public String getName() {
      return this.mbeanName;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public String getProperty() {
      return this.property;
   }

   public void addSet(MBeanSetCommand var1) {
      this.setCommands.add(var1);
   }

   public Iterator getSetCommands() {
      return this.setCommands.iterator();
   }

   public void addInvoke(MBeanInvokeCommand var1) {
      this.invokeCommands.add(var1);
   }

   public Iterator getInvokeCommands() {
      return this.invokeCommands.iterator();
   }

   public void addCreate(MBeanCreateCommand var1) {
      this.createCommands.add(var1);
   }

   public Iterator getCreateCommands() {
      return this.createCommands.iterator();
   }
}
