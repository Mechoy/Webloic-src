package weblogic.wsee.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public abstract class AbstractExpirable extends AbstractStorable {
   private static final long serialVersionUID = 1L;
   protected transient String _serialFormVersion;
   private String _maxLifetime;
   private String _idleTimeout;
   private transient Duration _maxLifetimeDuration;
   private transient Duration _idleTimeoutDuration;

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this._serialFormVersion);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this._serialFormVersion = (String)var1.readObject();
      var1.defaultReadObject();
      this.initTransients();
   }

   public AbstractExpirable(Serializable var1, long var2, long var4) {
      this(var1, parseDuration(var2).toString(), parseDuration(var4).toString());
   }

   public AbstractExpirable(Serializable var1, String var2, String var3) {
      super(var1);
      this._serialFormVersion = "10.3.6";
      this._maxLifetime = var2;
      this._idleTimeout = var3;
      this.initTransients();
   }

   protected void initTransients() {
      if (this._maxLifetime != null) {
         this._maxLifetimeDuration = parseDuration(this._maxLifetime);
      }

      if (this._idleTimeout != null) {
         this._idleTimeoutDuration = parseDuration(this._idleTimeout);
      }

   }

   public Duration getMaxLifetimeDuration() {
      return this._maxLifetimeDuration;
   }

   public Duration getIdleTimeoutDuration() {
      return this._idleTimeoutDuration;
   }

   protected void setMaxLifetime(String var1) {
      this._maxLifetimeDuration = parseDuration(var1);
      this._maxLifetime = var1;
   }

   protected void setIdleTimeout(String var1) {
      this._idleTimeoutDuration = parseDuration(var1);
      this._idleTimeout = var1;
   }

   public boolean hasExplicitExpiration() {
      return this._maxLifetime != null || this._idleTimeout != null;
   }

   public boolean isExpired() {
      return this._maxLifetime != null && this._maxLifetimeDuration.getTimeInMillis(new Date(this.getCreationTime())) + this.getCreationTime() < System.currentTimeMillis() || this._idleTimeout != null && this._idleTimeoutDuration.getTimeInMillis(new Date(this.getLastUpdatedTime())) + this.getLastUpdatedTime() < System.currentTimeMillis();
   }

   private static Duration parseDuration(String var0) {
      try {
         return DatatypeFactory.newInstance().newDuration(var0);
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString(), var2);
      }
   }

   private static Duration parseDuration(long var0) {
      try {
         return DatatypeFactory.newInstance().newDuration(var0);
      } catch (Exception var3) {
         throw new RuntimeException(var3.toString(), var3);
      }
   }
}
