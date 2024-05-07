package weblogic.wsee.reliability2.headers;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Packet;
import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability.headers.WsrmMsgHeaderFactory;
import weblogic.wsee.reliability2.exception.WsrmException;

public class WsrmHeaderFactory {
   private static final WsrmHeaderFactory _instance = new WsrmHeaderFactory();
   private WsrmMsgHeaderFactory _headerFactory = new WsrmMsgHeaderFactory();

   public static WsrmHeaderFactory getInstance() {
      return _instance;
   }

   private WsrmHeaderFactory() {
   }

   public <T extends WsrmHeader> T createEmptyWsrmHeader(Class<T> var1, WsrmConstants.RMVersion var2) {
      QName var3 = WsrmHeader.getQName(var1, var2);
      return this.createHeader(var3);
   }

   public <T extends WsrmHeader> T createWsrmHeaderFromHeader(Class<T> var1, Header var2) throws WsrmException {
      try {
         WsrmHeader var3 = (WsrmHeader)var1.newInstance();
         var3.read(var2.readHeader());
         return var3;
      } catch (MsgHeaderException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new MsgHeaderException("Could not build header for " + var1, var5);
      }
   }

   public WsrmHeader createHeader(QName var1) {
      WsrmHeader var2 = (WsrmHeader)this._headerFactory.createMsgHeader(var1);
      return var2;
   }

   public <T extends WsrmHeader> T getHeaderFromPacket(Class<T> var1, Packet var2) throws WsrmException {
      Header var3 = null;
      WsrmConstants.RMVersion[] var4 = WsrmConstants.RMVersion.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         WsrmConstants.RMVersion var7 = var4[var6];
         QName var8 = this._headerFactory.getHeaderQName(var1, var7);
         var3 = var2.getMessage().getHeaders().get(var8, true);
         if (var3 != null) {
            break;
         }
      }

      if (var3 == null) {
         return null;
      } else {
         try {
            WsrmHeader var10 = getInstance().createWsrmHeaderFromHeader(var1, var3);
            return var10;
         } catch (Exception var9) {
            throw new WsrmException(var9.toString(), var9);
         }
      }
   }
}
