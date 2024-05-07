package weblogic.ejb.container.persistence;

import java.util.HashSet;
import java.util.Set;
import weblogic.ejb.container.persistence.spi.Dependent;

public final class DependentImpl implements Dependent {
   private String description;
   private String dependentClassName;
   private String name;
   private Set fields = new HashSet();
   private Set pkFields = new HashSet();

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getDependentClassName() {
      return this.dependentClassName;
   }

   public void setDependentClassName(String var1) {
      this.dependentClassName = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Set getCMFieldNames() {
      return this.fields;
   }

   public void addCMFieldName(String var1) {
      this.fields.add(var1);
   }

   public Set getPrimaryKeyFieldNames() {
      return this.pkFields;
   }

   public void addPrimaryKeyFieldName(String var1) {
      this.pkFields.add(var1);
   }
}
