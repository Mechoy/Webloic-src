package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.DeploymentListenersType;
import weblogic.wsee.tools.wseegen.schemas.ListenerListType;

public class DeploymentListenersTypeImpl extends XmlComplexContentImpl implements DeploymentListenersType {
   private static final long serialVersionUID = 1L;
   private static final QName CLIENT$0 = new QName("http://www.bea.com/wsee/ns/config", "client");
   private static final QName SERVER$2 = new QName("http://www.bea.com/wsee/ns/config", "server");

   public DeploymentListenersTypeImpl(SchemaType var1) {
      super(var1);
   }

   public ListenerListType getClient() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerListType var2 = null;
         var2 = (ListenerListType)this.get_store().find_element_user(CLIENT$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public boolean isSetClient() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(CLIENT$0) != 0;
      }
   }

   public void setClient(ListenerListType var1) {
      this.generatedSetterHelperImpl(var1, CLIENT$0, 0, (short)1);
   }

   public ListenerListType addNewClient() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerListType var2 = null;
         var2 = (ListenerListType)this.get_store().add_element_user(CLIENT$0);
         return var2;
      }
   }

   public void unsetClient() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(CLIENT$0, 0);
      }
   }

   public ListenerListType getServer() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerListType var2 = null;
         var2 = (ListenerListType)this.get_store().find_element_user(SERVER$2, 0);
         return var2 == null ? null : var2;
      }
   }

   public boolean isSetServer() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(SERVER$2) != 0;
      }
   }

   public void setServer(ListenerListType var1) {
      this.generatedSetterHelperImpl(var1, SERVER$2, 0, (short)1);
   }

   public ListenerListType addNewServer() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerListType var2 = null;
         var2 = (ListenerListType)this.get_store().add_element_user(SERVER$2);
         return var2;
      }
   }

   public void unsetServer() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(SERVER$2, 0);
      }
   }
}
