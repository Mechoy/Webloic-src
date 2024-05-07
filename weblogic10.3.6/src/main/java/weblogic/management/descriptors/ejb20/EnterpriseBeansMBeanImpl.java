package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EnterpriseBeansMBeanImpl extends XMLElementMBeanDelegate implements EnterpriseBeansMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_sessions = false;
   private List sessions;
   private boolean isSet_messageDrivens = false;
   private List messageDrivens;
   private boolean isSet_entities = false;
   private List entities;

   public weblogic.management.descriptors.ejb11.SessionMBean[] getSessions() {
      if (this.sessions == null) {
         return new weblogic.management.descriptors.ejb11.SessionMBean[0];
      } else {
         weblogic.management.descriptors.ejb11.SessionMBean[] var1 = new weblogic.management.descriptors.ejb11.SessionMBean[this.sessions.size()];
         var1 = (weblogic.management.descriptors.ejb11.SessionMBean[])((weblogic.management.descriptors.ejb11.SessionMBean[])this.sessions.toArray(var1));
         return var1;
      }
   }

   public void setSessions(weblogic.management.descriptors.ejb11.SessionMBean[] var1) {
      weblogic.management.descriptors.ejb11.SessionMBean[] var2 = null;
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

   public void addSession(weblogic.management.descriptors.ejb11.SessionMBean var1) {
      this.isSet_sessions = true;
      if (this.sessions == null) {
         this.sessions = Collections.synchronizedList(new ArrayList());
      }

      this.sessions.add(var1);
   }

   public void removeSession(weblogic.management.descriptors.ejb11.SessionMBean var1) {
      if (this.sessions != null) {
         this.sessions.remove(var1);
      }
   }

   public MessageDrivenMBean[] getMessageDrivens() {
      if (this.messageDrivens == null) {
         return new MessageDrivenMBean[0];
      } else {
         MessageDrivenMBean[] var1 = new MessageDrivenMBean[this.messageDrivens.size()];
         var1 = (MessageDrivenMBean[])((MessageDrivenMBean[])this.messageDrivens.toArray(var1));
         return var1;
      }
   }

   public void setMessageDrivens(MessageDrivenMBean[] var1) {
      MessageDrivenMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getMessageDrivens();
      }

      this.isSet_messageDrivens = true;
      if (this.messageDrivens == null) {
         this.messageDrivens = Collections.synchronizedList(new ArrayList());
      } else {
         this.messageDrivens.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.messageDrivens.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("MessageDrivens", var2, this.getMessageDrivens());
      }

   }

   public void addMessageDriven(MessageDrivenMBean var1) {
      this.isSet_messageDrivens = true;
      if (this.messageDrivens == null) {
         this.messageDrivens = Collections.synchronizedList(new ArrayList());
      }

      this.messageDrivens.add(var1);
   }

   public void removeMessageDriven(MessageDrivenMBean var1) {
      if (this.messageDrivens != null) {
         this.messageDrivens.remove(var1);
      }
   }

   public weblogic.management.descriptors.ejb11.EntityMBean[] getEntities() {
      if (this.entities == null) {
         return new weblogic.management.descriptors.ejb11.EntityMBean[0];
      } else {
         weblogic.management.descriptors.ejb11.EntityMBean[] var1 = new weblogic.management.descriptors.ejb11.EntityMBean[this.entities.size()];
         var1 = (weblogic.management.descriptors.ejb11.EntityMBean[])((weblogic.management.descriptors.ejb11.EntityMBean[])this.entities.toArray(var1));
         return var1;
      }
   }

   public void setEntities(weblogic.management.descriptors.ejb11.EntityMBean[] var1) {
      weblogic.management.descriptors.ejb11.EntityMBean[] var2 = null;
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

   public void addEntity(weblogic.management.descriptors.ejb11.EntityMBean var1) {
      this.isSet_entities = true;
      if (this.entities == null) {
         this.entities = Collections.synchronizedList(new ArrayList());
      }

      this.entities.add(var1);
   }

   public void removeEntity(weblogic.management.descriptors.ejb11.EntityMBean var1) {
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

      if (null != this.getMessageDrivens()) {
         for(var3 = 0; var3 < this.getMessageDrivens().length; ++var3) {
            var2.append(this.getMessageDrivens()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</enterprise-beans>\n");
      return var2.toString();
   }
}
