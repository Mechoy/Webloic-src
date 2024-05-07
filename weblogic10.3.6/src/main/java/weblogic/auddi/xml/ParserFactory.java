package weblogic.auddi.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;

public class ParserFactory {
   public static final String ACUMEN_JAXP_FACTORY = "acumenat.jaxp.DocumentBuilderFactory.class";
   private ParserBuilder builder;
   private String jaxpFactoryClass;

   public ParserFactory(String var1) {
      Logger.trace("+ParserFactory.CTOR(String)");
      this.jaxpFactoryClass = var1;
      Logger.trace("-ParserFactory.CTOR(String)");
   }

   public ParserFactory() {
      this(PropertyManager.getRuntimeProperty("acumenat.jaxp.DocumentBuilderFactory.class"));
   }

   public SOAPParser createSOAPParser() throws SchemaException {
      Logger.trace("+ParserFactory.createSOAPParser");
      DomBuilder var1 = this.createDOMParser("/soap-envelope.xsd");
      Logger.trace("-ParserFactory.createSOAPParser");
      return new SOAPParser(var1);
   }

   public DomBuilder createDOMParser(String var1) throws SchemaException {
      Logger.trace("+ParserFactory.createDOMParser(String)");

      try {
         this.initBuilder();
         DocumentBuilder var2 = this.builder.createParser(var1);
         if (var1 != null) {
            var2.setEntityResolver(new XMLEntityResolver());
         }

         Logger.trace("-ParserFactory.createDOMParser(String)");
         return new DomBuilder(var2);
      } catch (ParserConfigurationException var3) {
         throw new SchemaException(var3);
      }
   }

   public DomBuilder createDOMParser() throws SchemaException {
      Logger.trace("+ParserFactory.createDOMParser()");
      DomBuilder var1 = this.createDOMParser((String)null);
      Logger.trace("-ParserFactory.createDOMParser()");
      return var1;
   }

   public DomBuilder createDOMParserNS() throws SchemaException {
      Logger.trace("+ParserFactory.createDOMParserNS");

      try {
         this.initBuilder();
         DocumentBuilder var1 = this.builder.createParser((String)null, true);
         Logger.trace("-ParserFactory.createDOMParserNS");
         return new DomBuilder(var1);
      } catch (ParserConfigurationException var2) {
         throw new SchemaException(var2);
      }
   }

   private void initBuilder() {
      Logger.trace("+ParserFactory.initBuilder()");
      if (this.builder == null) {
         this.builder = new ParserBuilder(this.jaxpFactoryClass);
      }

      Logger.trace("-ParserFactory.initBuilder()");
   }
}
