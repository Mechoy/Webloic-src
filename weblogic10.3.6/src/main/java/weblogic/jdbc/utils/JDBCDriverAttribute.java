package weblogic.jdbc.utils;

import java.io.Serializable;

public class JDBCDriverAttribute implements Cloneable, Serializable {
   private String name;
   private String defaultValue;
   private String description;
   private String value;
   private String displayName;
   private String propertyName;
   private boolean inURL;
   private boolean isRequired;
   private MetaJDBCDriverInfo metaInfo;

   public JDBCDriverAttribute(MetaJDBCDriverInfo var1) {
      this.metaInfo = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setPropertyName(String var1) {
      this.propertyName = var1;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setInURL(String var1) {
      this.setInURL(new Boolean(var1));
   }

   public void setInURL(boolean var1) {
      this.inURL = var1;
   }

   public boolean isInURL() {
      return this.inURL;
   }

   public void setIsRequired(String var1) {
      this.setIsRequired(new Boolean(var1));
   }

   public void setIsRequired(boolean var1) {
      this.isRequired = var1;
   }

   public boolean isRequired() {
      return this.isRequired;
   }

   public void setDesription(String var1) {
      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("For : " + this.metaInfo.toString() + " \n");
      var1.append("\nProperty Name    : " + this.getName());
      var1.append("\nProperty Value   : " + this.getValue());
      var1.append("\nDefault Value    : " + (this.getDefaultValue() == null ? "null" : this.getDefaultValue()));
      var1.append("\nRequired?        : " + this.isRequired());
      var1.append("\nGoes in URL      : " + (new Boolean(this.isInURL())).toString());
      var1.append("\nDescription      : " + (this.getDescription() == null ? "null" : this.getDescription()));
      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
