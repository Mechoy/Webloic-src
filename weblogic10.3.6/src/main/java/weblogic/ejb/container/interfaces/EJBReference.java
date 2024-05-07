package weblogic.ejb.container.interfaces;

public interface EJBReference {
   void setDescription(String var1);

   String getDescription();

   void setName(String var1);

   String getName();

   void setRefType(String var1);

   String getRefType();

   void setHomeInterfaceName(String var1);

   String getHomeInterfaceName();

   void setRemoteInterfaceName(String var1);

   String getRemoteInterfaceName();

   void setLinkedEjbName(String var1);

   String getLinkedEjbName();

   void setJNDIName(String var1);

   String getJNDIName();
}
