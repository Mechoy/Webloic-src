package weblogic.ant.taskdefs.management;

import java.util.ArrayList;
import java.util.Iterator;

public class MBeanQueryCommand implements MBeanCommand {
   private String domainName = "*";
   private String mbeanName = null;
   private String mbeanType = null;
   private String pattern = null;
   private String property = null;
   private ArrayList setCommands = new ArrayList();
   private ArrayList getCommands = new ArrayList();
   private ArrayList createCommands = new ArrayList();
   private MBeanDeleteCommand mbdc = null;
   private MBeanInvokeCommand mbic = null;

   public int getCommandType() {
      return 5;
   }

   public void setDomain(String var1) {
      this.domainName = var1;
   }

   public String getDomain() {
      return this.domainName;
   }

   public void setType(String var1) {
      this.mbeanType = var1;
   }

   public String getType() {
      return this.mbeanType;
   }

   public void setName(String var1) {
      this.mbeanName = var1;
   }

   public String getName() {
      return this.mbeanName;
   }

   public void setPattern(String var1) {
      this.pattern = var1;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public String getProperty() {
      return this.property;
   }

   public String getPattern() {
      return this.pattern != null ? this.pattern : this.domainName + ":*" + (this.mbeanName != null ? ",Name=" + this.mbeanName : "") + (this.mbeanType != null ? ",Type=" + this.mbeanType : "");
   }

   public String getCommoPattern() {
      return this.pattern != null ? this.pattern : this.domainName + ":*" + (this.mbeanName != null ? ",Name=" + this.mbeanName : "");
   }

   public void addSet(MBeanSetCommand var1) {
      this.setCommands.add(var1);
   }

   public void addGet(MBeanGetCommand var1) {
      this.getCommands.add(var1);
   }

   public void addCreate(MBeanCreateCommand var1) {
      this.createCommands.add(var1);
   }

   public void addDelete(MBeanDeleteCommand var1) {
      this.mbdc = var1;
   }

   public MBeanDeleteCommand getDeleteCommand() {
      return this.mbdc;
   }

   public void addInvoke(MBeanInvokeCommand var1) {
      this.mbic = var1;
   }

   public MBeanInvokeCommand getInvokeCommand() {
      return this.mbic;
   }

   public Iterator getSetCommands() {
      return this.setCommands.iterator();
   }

   public Iterator getGetCommands() {
      return this.getCommands.iterator();
   }

   public Iterator getCreateCommands() {
      return this.createCommands.iterator();
   }
}
