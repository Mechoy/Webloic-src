package weblogic.xml.jaxp;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class SAXFactoryProperties {
   public static String NAMESPACEAWARE = "Namespaceaware";
   public static String VALIDATING = "Validating";
   public static String XINCL = "XIncludeAware";
   public static String SCHEMA = "Schema";
   private LinkedHashMap factoryProperties = new LinkedHashMap();
   private Set facPropertySettingMarks;
   private LinkedHashMap features;
   private LinkedHashMap properties;
   private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

   public SAXFactoryProperties() {
      this.factoryProperties.put(NAMESPACEAWARE, Boolean.FALSE);
      this.factoryProperties.put(VALIDATING, Boolean.FALSE);
      this.factoryProperties.put(XINCL, Boolean.FALSE);
      this.facPropertySettingMarks = new HashSet();
      this.features = new LinkedHashMap();
      this.properties = new LinkedHashMap();
   }

   public void put(String var1, boolean var2) {
      this.factoryProperties.put(var1, new Boolean(var2));
      this.facPropertySettingMarks.add(var1);
   }

   public boolean get(String var1) {
      Boolean var2 = (Boolean)this.factoryProperties.get(var1);
      return var2;
   }

   public boolean isSetExplicitly(String var1) {
      return this.facPropertySettingMarks.contains(var1);
   }

   public void setFeature(String var1, boolean var2) {
      this.features.put(var1, new Boolean(var2));
   }

   public Boolean getFeature(String var1) {
      Boolean var2 = (Boolean)this.features.get(var1);
      return var2;
   }

   public void setProperty(String var1, Object var2) {
      this.properties.put(var1, var2);
      if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(var1) && this.properties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
         Object var3 = this.properties.remove("http://java.sun.com/xml/jaxp/properties/schemaSource");
         this.properties.put("http://java.sun.com/xml/jaxp/properties/schemaSource", var3);
      }

   }

   public Object getProperty(String var1) {
      Object var2 = this.properties.get(var1);
      return var2;
   }

   public Enumeration features() {
      return Collections.enumeration(this.features.keySet());
   }

   public Enumeration properties() {
      return Collections.enumeration(this.properties.keySet());
   }

   public Object clone() {
      SAXFactoryProperties var1 = new SAXFactoryProperties();
      var1.factoryProperties.clear();
      var1.facPropertySettingMarks.clear();
      var1.features.clear();
      var1.properties.clear();
      var1.factoryProperties.putAll(this.factoryProperties);
      var1.facPropertySettingMarks.addAll(this.facPropertySettingMarks);
      var1.features.putAll(this.features);
      var1.properties.putAll(this.properties);
      return var1;
   }

   public void copyFrom(SAXFactoryProperties var1) {
      this.factoryProperties.clear();
      this.facPropertySettingMarks.clear();
      this.features.clear();
      this.properties.clear();
      this.factoryProperties.putAll(var1.factoryProperties);
      this.facPropertySettingMarks.addAll(var1.facPropertySettingMarks);
      this.features.putAll(var1.features);
      this.properties.putAll(var1.properties);
   }
}
