package weblogic.wsee.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.jaxws.cluster.spi.ClusterMsgHeaderFactory;
import weblogic.wsee.reliability.headers.WsrmMsgHeaderFactory;

public class MsgHeaderFactory implements MsgHeaderFactoryIntf {
   private static final MsgHeaderFactory instance = new MsgHeaderFactory();
   private List<MsgHeaderFactoryIntf> subFactories = new ArrayList();
   private StandardMsgHeaderFactory _stdFactory;

   public static MsgHeaderFactory getInstance() {
      return instance;
   }

   public void addMsgHeaderClass(QName var1, Class var2) {
      this._stdFactory.addMsgHeaderClass(var1, var2);
   }

   public MsgHeader createMsgHeader(QName var1) throws MsgHeaderException {
      try {
         Object var2 = null;
         Iterator var3 = this.subFactories.iterator();

         while(var3.hasNext()) {
            MsgHeaderFactoryIntf var4 = (MsgHeaderFactoryIntf)var3.next();
            var2 = var4.createMsgHeader(var1);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            var2 = new UnknownMsgHeader(var1);
         }

         return (MsgHeader)var2;
      } catch (MsgHeaderException var5) {
         throw var5;
      }
   }

   private void addMsgHeaderFactory(MsgHeaderFactoryIntf var1) {
      this.subFactories.add(var1);
   }

   private MsgHeaderFactory() {
   }

   static {
      MsgHeaderFactory var0 = getInstance();
      var0._stdFactory = new StandardMsgHeaderFactory();
      var0.addMsgHeaderFactory(var0._stdFactory);
      var0.addMsgHeaderFactory(new ClusterMsgHeaderFactory());
      var0.addMsgHeaderFactory(new WsrmMsgHeaderFactory());
   }
}
