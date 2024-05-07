package weblogic.management.descriptors.ejb11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EnterpriseBeansMBeanImpl extends XMLElementMBeanDelegate implements EnterpriseBeansMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_sessions = false;
   private List sessions;
   private boolean isSet_entities = false;
   private List entities;

   public SessionMBean[] getSessions() {
      if (this.sessions == null) {
         return new SessionMBean[0];
      } else {
         SessionMBean[] var1 = new SessionMBean[this.sessions.size()];
         var1 = (SessionMBean[])((SessionMBean[])this.sessions.toArray(var1));
         return var1;
      }
   }

   public void setSessions(SessionMBean[] var1) {
      SessionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getSessions();
      }

      this.isSet_sessions = true;
      if (this.sessions == null) {
         this.sessions = Collections.synchronizedList(new ArrayList());
      } else {
         this.sessions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.sessions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Sessions", var2, this.getSessions());
      }

   }

   public void addSession(SessionMBean var1) {
      this.isSet_sessions = true;
      if (this.sessions == null) {
         this.sessions = Collections.synchronizedList(new ArrayList());
      }

      this.sessions.add(var1);
   }

   public void removeSession(SessionMBean var1) {
      if (this.sessions != null) {
         this.sessions.remove(var1);
      }
   }

   public EntityMBean[] getEntities() {
      if (this.entities == null) {
         return new EntityMBean[0];
      } else {
         EntityMBean[] var1 = new EntityMBean[this.entities.size()];
         var1 = (EntityMBean[])((EntityMBean[])this.entities.toArray(var1));
         return var1;
      }
   }

   public void setEntities(EntityMBean[] var1) {
      EntityMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEntities();
      }

      this.isSet_entities = true;
      if (this.entities == null) {
         this.entities = Collections.synchronizedList(new ArrayList());
      } else {
         this.entities.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.entities.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Entities", var2, this.getEntities());
      }

   }

   public void addEntity(EntityMBean var1) {
      this.isSet_entities = true;
      if (this.entities == null) {
         this.entities = Collections.synchronizedList(new ArrayList());
      }

      this.entities.add(var1);
   }

   public void removeEntity(EntityMBean var1) {
      if (this.entities != null) {
         this.entities.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<enterprise-beans");
      var2.append(">\n");
      int var3;
      if (null != this.getSessions()) {
         for(var3 = 0; var3 < this.getSessions().length; ++var3) {
            var2.append(this.getSessions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getEntities()) {
         for(var3 = 0; var3 < this.getEntities().length; ++var3) {
            var2.append(this.getEntities()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</enterprise-beans>\n");
      return var2.toString();
   }
}
