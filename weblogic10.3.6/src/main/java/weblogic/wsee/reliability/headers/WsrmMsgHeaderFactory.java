package weblogic.wsee.reliability.headers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderFactoryIntf;
import weblogic.wsee.reliability.WsrmConstants;

public class WsrmMsgHeaderFactory implements MsgHeaderFactoryIntf {
   private Map<QName, Class<? extends WsrmHeader>> headerClasses = new HashMap();
   private static Set<Class> needFillQNameHeaders = new HashSet();

   public WsrmMsgHeaderFactory() {
      WsrmConstants.RMVersion[] var1 = WsrmConstants.RMVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WsrmConstants.RMVersion var4 = var1[var3];
         this.addMsgHeaderClass(WsrmHeader.getQName(AcknowledgementHeader.class, var4), AcknowledgementHeader.class);
         this.addMsgHeaderClass(WsrmHeader.getQName(AckRequestedHeader.class, var4), AckRequestedHeader.class);
         this.addMsgHeaderClass(WsrmHeader.getQName(SequenceHeader.class, var4), SequenceHeader.class);
         needFillQNameHeaders.add(AcknowledgementHeader.class);
         needFillQNameHeaders.add(AckRequestedHeader.class);
         needFillQNameHeaders.add(SequenceHeader.class);
         if (var4.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            this.addMsgHeaderClass(WsrmHeader.getQName(UsesSequenceSTRHeader.class, var4), UsesSequenceSTRHeader.class);
            this.addMsgHeaderClass(WsrmHeader.getQName(UsesSequenceSSLHeader.class, var4), UsesSequenceSSLHeader.class);
            this.addMsgHeaderClass(WsrmHeader.getQName(TestSequenceSSLHeader.class, var4), TestSequenceSSLHeader.class);
            needFillQNameHeaders.add(UsesSequenceSTRHeader.class);
            needFillQNameHeaders.add(UsesSequenceSSLHeader.class);
            needFillQNameHeaders.add(TestSequenceSSLHeader.class);
         }
      }

   }

   public MsgHeader createMsgHeader(QName var1) throws MsgHeaderException {
      try {
         Class var2 = (Class)this.headerClasses.get(var1);
         if (var2 != null) {
            MsgHeader var3 = (MsgHeader)var2.newInstance();
            if (needFillQNameHeaders.contains(var2)) {
               ((WsrmHeader)var3).setNamespaceUri(var1.getNamespaceURI());
            }

            return var3;
         } else {
            return null;
         }
      } catch (MsgHeaderException var4) {
         throw var4;
      } catch (IllegalAccessException var5) {
         throw new MsgHeaderException("Could not build header for " + var1, var5);
      } catch (InstantiationException var6) {
         throw new MsgHeaderException("Could not build header for " + var1, var6);
      }
   }

   private void addMsgHeaderClass(QName var1, Class<? extends WsrmHeader> var2) {
      this.headerClasses.put(var1, var2);
   }

   public Class<? extends WsrmHeader> getHeaderClass(QName var1) {
      return (Class)this.headerClasses.get(var1);
   }

   public QName getHeaderQName(Class<? extends WsrmHeader> var1, WsrmConstants.RMVersion var2) {
      Iterator var3 = this.headerClasses.keySet().iterator();

      QName var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (QName)var3.next();
      } while(!var1.isAssignableFrom((Class)this.headerClasses.get(var4)));

      return new QName(var2.getNamespaceUri(), var4.getLocalPart());
   }
}
