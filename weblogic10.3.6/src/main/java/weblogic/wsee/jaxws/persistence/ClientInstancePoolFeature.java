package weblogic.wsee.jaxws.persistence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.WLSWebServiceFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature;

public class ClientInstancePoolFeature extends WLSWebServiceFeature {
   private static final Logger LOGGER = Logger.getLogger(ClientInstancePoolFeature.class.getName());
   private static String ID = "Durable Client Pool Init Feature";
   private boolean _customized;
   private int _capacity;
   private boolean _durable;
   private ClientIdentityFeature _clientIdentityFeature;
   private List<PropertyChangeListener> _listeners;
   private boolean _disposed;
   private boolean _closedAllDurableClients;

   public ClientInstancePoolFeature() {
      super.enabled = true;
      this._capacity = 10;
      this._durable = false;
      this._listeners = new ArrayList();
      this._disposed = false;
      this._closedAllDurableClients = false;
      this.setTubelineImpact(false);
   }

   public String getID() {
      return ID;
   }

   public boolean isCustomized() {
      return this._customized;
   }

   public int getCapacity() {
      return this._capacity;
   }

   public void setCapacity(int var1) {
      this._customized = true;
      this._capacity = var1;
   }

   public boolean isDurable() {
      return this._durable;
   }

   public void setDurable(boolean var1) {
      this._customized = true;
      this._durable = var1;
   }

   public ClientIdentityFeature getClientIdentityFeature() {
      return this._clientIdentityFeature;
   }

   public void setClientIdentityFeature(ClientIdentityFeature var1) {
      this._clientIdentityFeature = var1;
   }

   public void dispose() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Disposing DurableClientPoolFeature");
      }

      boolean var1 = this._disposed;
      if (!var1) {
         this._disposed = true;
         this.firePropertyChangeEvent("Disposed", var1, this._disposed);
      }

   }

   public void closeAllDurableClients() {
      boolean var1 = this._closedAllDurableClients;
      if (!var1) {
         this._closedAllDurableClients = true;
      }

      this.firePropertyChangeEvent("ClosedAllDurableClients", var1, this._closedAllDurableClients);
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (!this._listeners.contains(var1)) {
         this._listeners.add(var1);
      }

   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this._listeners.remove(var1);
   }

   private void firePropertyChangeEvent(String var1, Object var2, Object var3) {
      if (!this.objectsEqual(var2, var3)) {
         PropertyChangeEvent var4 = new PropertyChangeEvent(this, var1, var2, var3);
         PropertyChangeListener[] var5 = (PropertyChangeListener[])this._listeners.toArray(new PropertyChangeListener[this._listeners.size()]);
         PropertyChangeListener[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            PropertyChangeListener var9 = var6[var8];

            try {
               var9.propertyChange(var4);
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         }

      }
   }

   private boolean objectsEqual(Object var1, Object var2) {
      if (var1 == null && var2 == null) {
         return true;
      } else {
         return var1 != null ? var1.equals(var2) : var2.equals(var1);
      }
   }
}
