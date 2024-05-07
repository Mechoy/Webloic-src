package weblogic.auddi.uddi.xml;

import weblogic.auddi.util.ConnectException;
import weblogic.auddi.util.Connection;
import weblogic.auddi.util.Logger;

class UDDIParserPooledItem extends Connection {
   public UDDIParserPooledItem(UDDIParser var1, String var2) {
      super(var1, var2);
      Logger.trace("+UDDIParserPooledItem.CTOR()");
      Logger.trace("-UDDIParserPooledItem.CTOR()");
   }

   public void close() throws ConnectException {
      Logger.trace("+UDDIParserPooledItem.close()");
      Logger.trace("-UDDIParserPooledItem.close()");
   }
}
