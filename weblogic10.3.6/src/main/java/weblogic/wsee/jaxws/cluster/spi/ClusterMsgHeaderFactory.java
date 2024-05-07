package weblogic.wsee.jaxws.cluster.spi;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderFactoryIntf;

public class ClusterMsgHeaderFactory implements MsgHeaderFactoryIntf {
   private Map<QName, Class<? extends MsgHeader>> headerClasses = new HashMap();

   public ClusterMsgHeaderFactory() {
      this.addMsgHeaderClass(PhysicalStoreNameMsgHeader.NAME, PhysicalStoreNameMsgHeader.class);
   }

   public MsgHeader createMsgHeader(QName var1) throws MsgHeaderException {
      try {
         Class var2 = (Class)this.headerClasses.get(var1);
         return var2 != null ? (MsgHeader)var2.newInstance() : null;
      } catch (MsgHeaderException var3) {
         throw var3;
      } catch (IllegalAccessException var4) {
         throw new MsgHeaderException("Could not build header for " + var1, var4);
      } catch (InstantiationException var5) {
         throw new MsgHeaderException("Could not build header for " + var1, var5);
      }
   }

   private void addMsgHeaderClass(QName var1, Class<? extends MsgHeader> var2) {
      this.headerClasses.put(var1, var2);
   }

   public Class<? extends MsgHeader> getHeaderClass(QName var1) {
      return (Class)this.headerClasses.get(var1);
   }
}
