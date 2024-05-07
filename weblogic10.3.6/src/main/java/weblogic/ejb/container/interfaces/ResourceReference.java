package weblogic.ejb.container.interfaces;

public interface ResourceReference {
   void setDescription(String var1);

   String getDescription();

   void setName(String var1);

   String getName();

   void setResourceType(String var1);

   String getResourceType();

   void setResourceAuthMode(String var1);

   String getResourceAuthMode();

   void setJNDIName(String var1);

   String getJNDIName();

   String getResRefName();
}
