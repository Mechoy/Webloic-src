package weblogic.dbeans;

import java.util.Collection;

public interface Conversation {
   Query createSQLQuery(String var1);

   Query createSQLQuery(String var1, Class var2, String var3);

   Object findByPrimaryKey(Class var1, Object var2);

   Object getByPrimaryKey(Class var1, Object var2);

   Object create(Object var1);

   Collection create(Collection var1);

   Object update(Object var1);

   void update(Collection var1);

   void remove(Object var1);

   void remove(Collection var1);

   void evict(Object var1);

   void evict(Collection var1);

   void refresh(Object var1);

   void refresh(Collection var1);

   void disconnect();

   void reconnect();
}
