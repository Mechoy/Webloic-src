package weblogic.auddi.uddi.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.ConnectException;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.auddi.xml.DomBuilder;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SOAPParser;
import weblogic.auddi.xml.SchemaException;

public class UDDIParserFactory extends ParserFactory {
   public static final String UDDI_SCHEMA_REF = "/uddi_v2.xsd";
   public static final int DEFAULT_INITIAL_SIZE = 10;
   public static final int DEFAULT_MAX_SIZE = 50;
   public static final int DEFAULT_INCREMENT = 2;
   public static final int DEFAULT_SYSTEM_MAX_SIZE = 1000;
   public static final String INITIAL_SIZE_PROPERTY = "uddi.parser.pool.initialsize";
   public static final String MAX_SIZE_PROPERTY = "uddi.parser.pool.maxsize";
   public static final String INCREMENT_PROPERTY = "uddi.parser.pool.increment";
   public static final String SYSTEM_MAX_SIZE_PROPERTY = "uddi.parser.pool.system.maxsize";
   private static UDDIParserPool m_pool;
   private Map parserMap;
   private ParserFactory m_parserFactory = new ParserFactory();

   public UDDIParserFactory() {
      Logger.trace("+UDDIParserFactory.CTOR()");

      try {
         Class var1 = UDDIParserFactory.class;
         synchronized(UDDIParserFactory.class) {
            if (m_pool == null) {
               String var2 = PropertyManager.getRuntimeProperty("uddi.parser.pool.initialsize");
               String var3 = PropertyManager.getRuntimeProperty("uddi.parser.pool.maxsize");
               String var4 = PropertyManager.getRuntimeProperty("uddi.parser.pool.increment");
               String var5 = PropertyManager.getRuntimeProperty("uddi.parser.pool.system.maxsize");
               m_pool = new UDDIParserPool(var2 != null && var2.length() != 0 ? Integer.parseInt(var2) : 10, var3 != null && var3.length() != 0 ? Integer.parseInt(var3) : 50, var4 != null && var4.length() != 0 ? Integer.parseInt(var4) : 2, var5 != null && var5.length() != 0 ? Integer.parseInt(var5) : 1000);
            }
         }

         this.parserMap = new HashMap();
      } catch (ConnectException var8) {
         var8.printStackTrace();
         throw new RuntimeException(UDDIMessages.get("error.operation.failed", "UDDIParserFactory"));
      }

      Logger.trace("-UDDIParserFactory.CTOR()");
   }

   public UDDIParser getUDDIParser() {
      Logger.trace("+UDDIParserFactory.getUDDIParser()");

      try {
         SOAPParser var1 = this.m_parserFactory.createSOAPParser();
         DomBuilder var2 = this.m_parserFactory.createDOMParser("/uddi_v2.xsd");
         var2.setEntityResolver(new UDDIEntityResolver());
         UDDIParser var3 = new UDDIParser(var1, var2);
         Logger.trace("-UDDIParserFactory.getUDDIParser()");
         return var3;
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new RuntimeException(UDDIMessages.get("error.operation.failed", "getUDDIParser"));
      }
   }

   public void returnUDDIParser(UDDIParser var1) {
      Logger.trace("+UDDIParserFactory.returnUDDIParser()");
      Logger.trace("-UDDIParserFactory.returnUDDIParser()");
   }

   public static void main(String[] var0) {
      byte var1 = 10;
      PropertyManager.setRuntimeProperty("uddi.schema.resource", "/weblogic/auddi/uddi/resources/uddi_v2.xsd");
      PropertyManager.setRuntimeProperty("soap.schema.resource", "/weblogic/auddi/uddi/resources/soap-envelope.xml");
      PropertyManager.setRuntimeProperty("xml.schema.resource", "/weblogic/auddi/uddi/resources/xml.xml");
      PropertyManager.setRuntimeProperty("uddi.parser.pool.initialsize", "2");
      PropertyManager.setRuntimeProperty("uddi.parser.pool.maxsize", "5");
      PropertyManager.setRuntimeProperty("uddi.parser.pool.increment", "2");
      PropertyManager.setRuntimeProperty("uddi.parser.pool.system.maxsize", "10");
      UDDIParserFactory var2 = new UDDIParserFactory();
      Vector var3 = new Vector();

      int var4;
      for(var4 = 0; var4 < var1; ++var4) {
         var3.add(var2.getUDDIParser());
      }

      for(var4 = 0; var4 < var1; ++var4) {
         try {
            ((UDDIParser)var3.get(var4)).parseFile("C:/Projects/Test/JAXP/Data/Find_Business.xml");
         } catch (SchemaException var6) {
            var6.printStackTrace();
         }
      }

      for(var4 = 0; var4 < var1; ++var4) {
         var2.returnUDDIParser((UDDIParser)var3.get(var4));
      }

   }
}
