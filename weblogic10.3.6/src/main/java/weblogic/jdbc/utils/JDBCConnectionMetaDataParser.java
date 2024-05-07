package weblogic.jdbc.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.util.TypeFilter;

public final class JDBCConnectionMetaDataParser {
   private XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();
   private static final String XML_FILE_NAME = "jdbcdrivers.xml";
   private List driverInfos = new ArrayList();
   private JDBCDriverInfoFactory theFactory = null;
   private static final XMLName BASE_ATTRIBUTE = ElementFactory.createXMLName("JDBC-Drivers");
   private static final XMLName NAME_ATTRIBUTE = ElementFactory.createXMLName("Name");
   private static final XMLName DRIVER_ATTRIBUTE = ElementFactory.createXMLName("Driver");
   private static final XMLName DRIVER_DBMS_ATTRIBUTE = ElementFactory.createXMLName("Database");
   private static final XMLName DRIVER_VENDOR_ATTRIBUTE = ElementFactory.createXMLName("Vendor");
   private static final XMLName DRIVER_TYPE_ATTRIBUTE = ElementFactory.createXMLName("Type");
   private static final XMLName DRIVER_DBMS_VERSION_ATTRIBUTE = ElementFactory.createXMLName("DatabaseVersion");
   private static final XMLName DRIVER_FORXA_ATTRIBUTE = ElementFactory.createXMLName("ForXA");
   private static final XMLName DRIVER_CERT_ATTRIBUTE = ElementFactory.createXMLName("Cert");
   private static final XMLName DRIVER_CLASSNAME_ATTRIBUTE = ElementFactory.createXMLName("ClassName");
   private static final XMLName DRIVER_URLCLASSNAME_ATTRIBUTE = ElementFactory.createXMLName("URLHelperClassname");
   private static final XMLName DRIVER_TEST_SQL_ATTRIBUTE = ElementFactory.createXMLName("TestSql");
   private static final XMLName DRIVER_INSTALL_URL_ATTRIBUTE = ElementFactory.createXMLName("InstallationUrl");
   private static final XMLName DRIVER_DESCRIPTION_ATTRIBUTE = ElementFactory.createXMLName("Description");
   private static final XMLName ATTRIBUTE_ATTRIBUTE = ElementFactory.createXMLName("Attribute");
   private static final XMLName ATTRIBUTE_DISPLAY_NAME = ElementFactory.createXMLName("DisplayName");
   private static final XMLName ATTRIBUTE_PROP_NAME = ElementFactory.createXMLName("PropertyName");
   private static final XMLName ATTRIBUTE_URL_ATTRIBUTE = ElementFactory.createXMLName("InURL");
   private static final XMLName ATTRIBUTE_REQUIRED_ATTRIBUTE = ElementFactory.createXMLName("Required");
   private static final XMLName ATTRIBUTE_DEFAULT_VALUE_ATTRIBUTE = ElementFactory.createXMLName("DefaultValue");
   private static final XMLName ATTRIBUTE_DESCRIPTION_ATTRIBUTE = ElementFactory.createXMLName("Description");

   public JDBCConnectionMetaDataParser() throws IOException, ParseException {
      this.loadSchema(this.getClass().getResourceAsStream("/jdbcdrivers.xml"));
   }

   public JDBCConnectionMetaDataParser(String var1) throws IOException, ParseException {
      this.loadSchema(var1);
   }

   public JDBCConnectionMetaDataParser(InputStream var1) throws IOException, ParseException {
      this.loadSchema(var1);
   }

   public JDBCDriverInfoFactory getJDBCDriverInfoFactory() {
      if (this.theFactory == null) {
         this.theFactory = new JDBCDriverInfoFactory(this.driverInfos);
      }

      return this.theFactory;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.err.println("Usage weblogic.jdbc.utils.JDBCConnectionMetaDataParser <xml file>");
         System.exit(1);
      }

      new JDBCConnectionMetaDataParser(var0[0]);
   }

   private Attribute getRequiredAttribute(StartElement var1, XMLName var2) throws ParseException {
      Attribute var3 = var1.getAttributeByName(var2);
      if (var3 != null) {
         return var3;
      } else {
         throw new ParseException("Expected required attribute: " + var2.getLocalName() + " on element: " + var1.getName().getLocalName());
      }
   }

   private String getNonRequiredAttribute(StartElement var1, XMLName var2) throws ParseException {
      Attribute var3 = var1.getAttributeByName(var2);
      return var3 != null ? var3.getValue() : null;
   }

   private void checkLocalName(String var1, XMLEvent var2) throws ParseException {
      this.checkLocalName(var1, var2.getName());
   }

   private void checkLocalName(String var1, XMLName var2) throws ParseException {
      if (!var1.equals(var2.getLocalName())) {
         throw new ParseException("Expected name: '" + var1 + "' but found '" + var2.getLocalName() + "'");
      }
   }

   private void loadSchema(String var1) throws IOException, ParseException {
      FileInputStream var2 = null;

      try {
         var2 = new FileInputStream(var1);
         this.loadSchema((InputStream)var2);
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

   }

   private void loadSchema(InputStream var1) throws IOException, ParseException {
      XMLInputStream var2 = null;

      try {
         var2 = this.factory.newInputStream(var1, new TypeFilter(2));
         this.parseSchema(var2);
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

   }

   private void parseSchema(XMLInputStream var1) throws IOException, ParseException {
      StartElement var2 = (StartElement)var1.next();
      this.checkLocalName(BASE_ATTRIBUTE.getLocalName(), (XMLEvent)var2);
      this.parseDrivers(var1);
   }

   private void parseDrivers(XMLInputStream var1) throws IOException, ParseException {
      while(var1.hasNext()) {
         this.parseDriver(var1);
      }

   }

   private void parseDriver(XMLInputStream var1) throws IOException, ParseException {
      StartElement var2 = (StartElement)var1.next();
      this.checkLocalName(DRIVER_ATTRIBUTE.getLocalName(), var2.getName());
      MetaJDBCDriverInfo var3 = new MetaJDBCDriverInfo();
      var3.setDbmsVendor(this.getRequiredAttribute(var2, DRIVER_DBMS_ATTRIBUTE).getValue());
      var3.setDriverVendor(this.getRequiredAttribute(var2, DRIVER_VENDOR_ATTRIBUTE).getValue());
      var3.setDriverClassName(this.getRequiredAttribute(var2, DRIVER_CLASSNAME_ATTRIBUTE).getValue());
      var3.setURLHelperClassName(this.getRequiredAttribute(var2, DRIVER_URLCLASSNAME_ATTRIBUTE).getValue());
      var3.setType(this.getRequiredAttribute(var2, DRIVER_TYPE_ATTRIBUTE).getValue());
      var3.setDbmsVersion(this.getRequiredAttribute(var2, DRIVER_DBMS_VERSION_ATTRIBUTE).getValue());
      var3.setForXA(this.getNonRequiredAttribute(var2, DRIVER_FORXA_ATTRIBUTE));
      var3.setCert(this.getNonRequiredAttribute(var2, DRIVER_CERT_ATTRIBUTE));
      var3.setTestSQL(this.getNonRequiredAttribute(var2, DRIVER_TEST_SQL_ATTRIBUTE));
      var3.setInstallURL(this.getNonRequiredAttribute(var2, DRIVER_INSTALL_URL_ATTRIBUTE));
      var3.setDescription(this.getNonRequiredAttribute(var2, DRIVER_DESCRIPTION_ATTRIBUTE));
      this.parseAttributes(var1, var3);
      this.driverInfos.add(var3);
   }

   private void parseAttributes(XMLInputStream var1, MetaJDBCDriverInfo var2) throws IOException, ParseException {
      while(true) {
         if (var1.hasNext()) {
            XMLName var3 = var1.peek().getName();
            if (ATTRIBUTE_ATTRIBUTE.equals(var3)) {
               this.parseAtribute(var1, var2);
               continue;
            }

            return;
         }

         return;
      }
   }

   private void parseAtribute(XMLInputStream var1, MetaJDBCDriverInfo var2) throws IOException, ParseException {
      StartElement var3 = (StartElement)var1.next();
      this.checkLocalName(ATTRIBUTE_ATTRIBUTE.getLocalName(), (XMLEvent)var3);
      JDBCDriverAttribute var4 = new JDBCDriverAttribute(var2);
      var4.setName(this.getRequiredAttribute(var3, NAME_ATTRIBUTE).getValue());
      var4.setDisplayName(this.getNonRequiredAttribute(var3, ATTRIBUTE_DISPLAY_NAME));
      var4.setPropertyName(this.getNonRequiredAttribute(var3, ATTRIBUTE_PROP_NAME));
      var4.setDefaultValue(this.getNonRequiredAttribute(var3, ATTRIBUTE_DEFAULT_VALUE_ATTRIBUTE));
      var4.setInURL(this.getNonRequiredAttribute(var3, ATTRIBUTE_URL_ATTRIBUTE));
      var4.setIsRequired(this.getNonRequiredAttribute(var3, ATTRIBUTE_REQUIRED_ATTRIBUTE));
      var4.setDesription(this.getNonRequiredAttribute(var3, ATTRIBUTE_DESCRIPTION_ATTRIBUTE));
      var2.setDriverAttribute(var4.getName(), var4);
   }
}
