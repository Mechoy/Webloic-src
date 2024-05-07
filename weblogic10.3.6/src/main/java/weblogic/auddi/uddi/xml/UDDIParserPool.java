package weblogic.auddi.uddi.xml;

import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.ConnectException;
import weblogic.auddi.util.Connection;
import weblogic.auddi.util.ConnectionPool;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.DomBuilder;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SOAPParser;
import weblogic.auddi.xml.SchemaException;

class UDDIParserPool extends ConnectionPool {
   private static int s_idNum = 0;
   private int m_systemMaxSize;
   private int m_increment;
   private int m_initialSize;
   private int m_maxSize;
   private ParserFactory m_parserFactory;

   public UDDIParserPool(int var1, int var2, int var3, int var4) throws ConnectException {
      super("UDDIParserPool");
      Logger.trace("+UDDIParserPool.CTOR()");
      this.m_systemMaxSize = var4;
      this.m_increment = var3;
      this.m_initialSize = var1;
      this.m_maxSize = var2;
      this.m_parserFactory = new ParserFactory();
      Logger.trace("-UDDIParserPool.CTOR()");
   }

   public int getPoolSystemMaxSize() {
      return this.m_systemMaxSize;
   }

   public int getPoolIncrement() {
      return this.m_increment;
   }

   public int getPoolInitialSize() {
      return this.m_initialSize;
   }

   public int getPoolMaxSize() {
      return this.m_maxSize;
   }

   public Connection getNewConnection() throws ConnectException {
      Logger.trace("+UDDIParserPool.getNewConnection()");

      try {
         SOAPParser var1 = this.m_parserFactory.createSOAPParser();
         DomBuilder var2 = this.m_parserFactory.createDOMParser("/uddi_v2.xsd");
         var2.setEntityResolver(new UDDIEntityResolver());
         ++s_idNum;
         UDDIParserPooledItem var3 = new UDDIParserPooledItem(new UDDIParser(var1, var2), String.valueOf(s_idNum));
         Logger.trace("-UDDIParserPool.getNewConnection(): BusySize: " + this.getBusySize() + " FreeSize: " + this.getFreeSize() + " SystemPoolSize: " + this.getSystemPoolSize() + " Counter: " + this.getCounter());
         return var3;
      } catch (SchemaException var4) {
         var4.printStackTrace();
         throw new RuntimeException(UDDIMessages.get("error.operation.failed", "getNewConnection"));
      }
   }
}
