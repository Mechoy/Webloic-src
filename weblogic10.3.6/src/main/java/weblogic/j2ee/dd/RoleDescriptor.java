package weblogic.j2ee.dd;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.SecurityRoleMBean;
import weblogic.utils.io.XMLWriter;

public final class RoleDescriptor extends XMLElementMBeanDelegate implements SecurityRoleMBean {
   private static final long serialVersionUID = 5643331597356419085L;
   private static boolean debug = false;
   private String description;
   private String roleName;

   public RoleDescriptor() {
   }

   public RoleDescriptor(String var1, String var2) {
      this.description = var1;
      this.roleName = var2;
   }

   public RoleDescriptor(String var1) {
      this.roleName = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      this.checkChange("description", var2, var1);
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String var1) {
      String var2 = this.roleName;
      this.roleName = var1;
      this.checkChange("roleName", var2, var1);
   }

   public String getName() {
      return this.roleName;
   }

   public void setName(String var1) {
      this.setRoleName(var1);
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public void unregister() throws ManagementException {
      super.unregister();
   }

   private String indentStr(int var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(' ');
      }

      return var2.toString();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<security-role>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<role-name>" + this.getName() + "</role-name>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</security-role>\n";
      return var2;
   }

   public void toXML(XMLWriter var1) {
      var1.println("<security-role>");
      var1.incrIndent();
      String var2 = this.getDescription();
      if (var2 != null) {
         var1.println("<description>" + var2 + "</description>");
      }

      var1.println("<role-name>" + this.getName() + "</role-name>");
      var1.decrIndent();
      var1.println("</security-role>");
   }

   public String toXML() {
      StringWriter var1 = new StringWriter();
      this.toXML(new XMLWriter(var1));
      return var1.toString();
   }
}
