package weblogic.tgiop;

import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import weblogic.iiop.ObjectKey;

public class TGIOPObjectKey extends ObjectKey {
   public static final int BEAOBJKEY_TPFW = 1;
   public static final int BEAOBJKEY_INTFREP = 2;
   public static final int BEAOBJKEY_FF = 3;
   public static final int BEAOBJKEY_ROOT = 4;
   public static final int BEAOBJKEY_NTS = 5;
   public static final int BEAOBJKEY_USER = 6;
   public static final int BEAOBJKEY_NAMESERVICE = 7;
   private static final Map initialRefIdMap = new HashMap();
   private static final Map initialRefOidMap;
   private static final Map initialRefOaIdMap;

   public TGIOPObjectKey(String var1, String var2) throws NamingException {
      this.setInterfaceName((String)initialRefIdMap.get(var1));
      this.setWLEObjectId((String)initialRefOidMap.get(var1));
      this.setWLEObjectAdapter((Integer)initialRefOaIdMap.get(var1));
      this.setWLEDomainId(var2);
      this.setKeyType(0);
      if (this.getInterfaceName() == null) {
         throw new NamingException("Invalid Initial Reference Name: " + var1);
      }
   }

   public static String getInitialRefObjectId(String var0) {
      return (String)initialRefOidMap.get(var0);
   }

   public static String getInitialRefObjectAdapter(String var0) {
      return (String)initialRefOaIdMap.get(var0);
   }

   public static String getInitialRefInterfaceName(String var0) {
      return (String)initialRefIdMap.get(var0);
   }

   static {
      initialRefIdMap.put("FactoryFinder", "IDL:beasys.com/Tobj/FactoryFinder:1.0");
      initialRefIdMap.put("InterfaceRepository", "IDL:omg.org/CORBA/Repository:1.0");
      initialRefIdMap.put("Tobj_SimpleEventsService", "IDL:beasys.com/TobjI_SimpleEvents/ChannelFactory:1.0");
      initialRefIdMap.put("NotificationService", "IDL:beasys.com/TobjI_Notification/EventChannelFactory:1.0");
      initialRefIdMap.put("NameService", "IDL:omg.org/CosNaming/NamingContextExt:1.0");
      initialRefOidMap = new HashMap();
      initialRefOidMap.put("FactoryFinder", "FactoryFinder");
      initialRefOidMap.put("InterfaceRepository", "Repository");
      initialRefOidMap.put("Tobj_SimpleEventsService", "Tobj_SimpleEventsService");
      initialRefOidMap.put("NotificationService", "NotificationService");
      initialRefOidMap.put("NameService", "BEA:NameService:Root");
      initialRefOaIdMap = new HashMap();
      initialRefOaIdMap.put("FactoryFinder", new Integer(3));
      initialRefOaIdMap.put("InterfaceRepository", new Integer(2));
      initialRefOaIdMap.put("Tobj_SimpleEventsService", new Integer(5));
      initialRefOaIdMap.put("NotificationService", new Integer(5));
      initialRefOaIdMap.put("NameService", new Integer(7));
   }
}
