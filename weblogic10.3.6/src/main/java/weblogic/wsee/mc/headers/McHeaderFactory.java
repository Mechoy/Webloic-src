package weblogic.wsee.mc.headers;

import com.sun.xml.ws.api.message.Header;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.reliability2.compat.CommonHeader;

public class McHeaderFactory {
   private static final McHeaderFactory _instance = new McHeaderFactory();

   public static McHeaderFactory getInstance() {
      return _instance;
   }

   public <T extends CommonHeader> T createMcHeaderFromHeader(Class<T> var1, Header var2) {
      try {
         CommonHeader var3 = (CommonHeader)var1.newInstance();
         var3.read(var2.readHeader());
         return var3;
      } catch (MsgHeaderException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new MsgHeaderException(WseeMCLogger.logFailedToBuildHeaderLoggable(var1.getName(), var5).getMessage());
      }
   }
}
