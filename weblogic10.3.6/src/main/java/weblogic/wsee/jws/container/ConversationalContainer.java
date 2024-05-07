package weblogic.wsee.jws.container;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.jws.Conversational;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.Version;
import weblogic.wsee.deploy.VersioningHelper;
import weblogic.wsee.jws.ServiceHandle;
import weblogic.wsee.jws.ServiceHandleImpl;
import weblogic.wsee.jws.conversation.ConversationState;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

class ConversationalContainer extends Container implements ConversationState, Serializable {
   private static final long serialVersionUID = 342753828856966520L;
   private static final boolean verbose = Verbose.isVerbose(ConversationalContainer.class);
   static final int STATE_ACTIVE = 0;
   static final int STATE_IDLE_TIMEOUT = 1;
   static final int STATE_AGE_TIMEOUT = 2;
   static final int STATE_FINISH_PENDING = 3;
   static final int STATE_FINISHED = 4;
   private static final List<Integer> TIMEOUT_STATES = Arrays.asList(2, 1);
   private long _lastReqTime = System.currentTimeMillis();
   private String _conversationId = null;
   private long _startTime = 0L;
   private long _maxIdleSeconds = 0L;
   private long _maxAgeTime = 0L;
   private int _state = 0;
   private String _startUser = null;
   private AuthenticatedSubject altSubject = null;
   private boolean _singlePrincipal = false;
   private String appName;
   private String version;
   private transient List<ConversationTimeoutListener> _timeoutListeners;
   private static final String MAX_AGE_DEFAULT = "1 day";
   private static ConcurrentHashMap<String, ConversationTimeoutListener> _serializedTimeoutListeners = new ConcurrentHashMap();

   protected ConversationalContainer(Object var1, WlMessageContext var2, String var3) {
      super(var1, var2);
      this.init(var3);
      this.appName = ApplicationVersionUtils.getApplicationName(ApplicationVersionUtils.getCurrentApplicationId());
      this.version = ApplicationVersionUtils.getCurrentVersionId();
      if (verbose) {
         Verbose.say("Constructed ConversationalContainer: " + var3);
      }

   }

   private void init(String var1) {
      this._conversationId = var1;

      assert !StringUtil.isEmpty(this._conversationId);

      this._startTime = System.currentTimeMillis();
      Conversational var2 = (Conversational)this.getTargetJWS().getClass().getAnnotation(Conversational.class);
      if (var2 != null) {
         this.setMaxIdleTime(var2.maxIdleTime());
         this._singlePrincipal = var2.singlePrincipal();
         if (var2.runAsStartUser()) {
            this.altSubject = ServerSecurityHelper.getCurrentSubject();
         }

         this.setMaxAge(var2.maxAge());
      } else {
         this.setMaxAge("1 day");
      }

   }

   public AuthenticatedSubject getAltAuthenticatedSubject() {
      return this.altSubject;
   }

   public ServiceHandle getService() {
      return new ServiceHandleImpl(this.getEndpointAddress(), this.getContextPath(), this.getURI(), this._conversationId);
   }

   void finish() throws Exception {
      if (verbose) {
         Verbose.say("Finishing conversation: " + this.getId());
      }

      boolean var1 = TIMEOUT_STATES.contains(this._state);
      this.finishConversation();
      this.getListeners().onFinish(var1);
      VersioningHelper.updateCount(this.appName, this.version, -1);
      this._state = 4;
      if (this._timeoutListeners != null) {
         Iterator var2 = this._timeoutListeners.iterator();

         while(var2.hasNext()) {
            ConversationTimeoutListener var3 = (ConversationTimeoutListener)var2.next();
            var3.cancel();
         }

         this._timeoutListeners.clear();
         this._timeoutListeners = null;
      }

   }

   public void resetIdleTime() throws IllegalStateException {
      this._lastReqTime = System.currentTimeMillis();
   }

   public boolean isFinished() {
      return this._state != 0;
   }

   public void finishConversation() {
      this._state = 3;
   }

   public void setMaxAge(Date var1) throws IllegalStateException, IllegalArgumentException {
      if (var1 == null) {
         this._maxAgeTime = 0L;
      } else {
         this._maxAgeTime = var1.getTime();
      }

   }

   public void setMaxAge(String var1) throws IllegalStateException, IllegalArgumentException {
      Duration var2 = new Duration(var1);
      this._maxAgeTime = var2.computeDate(this._startTime);
   }

   public long getMaxAge() throws IllegalStateException {
      return this._maxAgeTime > 0L ? (this._maxAgeTime - this._startTime) / 1000L : 0L;
   }

   public long getCurrentAge() throws IllegalStateException {
      return (System.currentTimeMillis() - this._startTime) / 1000L;
   }

   public long getCurrentIdleTime() throws IllegalStateException {
      return (System.currentTimeMillis() - this._lastReqTime) / 1000L;
   }

   public void setMaxIdleTime(long var1) throws IllegalStateException, IllegalArgumentException {
      if (var1 < 0L) {
         var1 = 0L;
      }

      this._maxIdleSeconds = var1;
   }

   public void setMaxIdleTime(String var1) throws IllegalStateException, IllegalArgumentException {
      Duration var2 = new Duration(var1);
      this._maxIdleSeconds = var2.convertToSeconds(new Date());
   }

   public long getMaxIdleTime() throws IllegalStateException {
      return this._maxIdleSeconds;
   }

   public String getId() {
      return this._conversationId;
   }

   public long getTimeStamp() {
      return this._lastReqTime;
   }

   static void removeSerializedTimeoutListener(String var0) {
      _serializedTimeoutListeners.remove(var0);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF("10.3");
      ArrayList var2 = null;
      if (this._timeoutListeners != null) {
         if (verbose) {
            Verbose.say("In writeObject for ConversationalContainer " + this.getId() + " with " + this._timeoutListeners.size() + " timeout listeners");
         }

         var2 = new ArrayList();
         Iterator var3 = this._timeoutListeners.iterator();

         while(var3.hasNext()) {
            ConversationTimeoutListener var4 = (ConversationTimeoutListener)var3.next();
            _serializedTimeoutListeners.put(var4.getId(), var4);
            var2.add(var4.getId());
         }
      }

      var1.writeObject(var2 != null ? var2 : null);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      if (Version.isLaterThanOrEqualTo(var2, "10.3")) {
         List var3 = (List)var1.readObject();
         if (var3 != null) {
            if (verbose) {
               Verbose.say("In readObject for ConversationalContainer " + this.getId() + " with " + var3.size() + " timeout listeners");
            }

            this._timeoutListeners = new ArrayList();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               ConversationTimeoutListener var6 = (ConversationTimeoutListener)_serializedTimeoutListeners.remove(var5);
               if (var6 != null) {
                  this._timeoutListeners.add(var6);
               }
            }
         }
      }

      var1.defaultReadObject();
   }

   long getLastReqTime() {
      return this._lastReqTime;
   }

   long getMaxAgeTime() {
      return this._maxAgeTime;
   }

   long getMaxIdleSeconds() {
      return this._maxIdleSeconds;
   }

   boolean isSinglePrincipal() {
      return this._singlePrincipal;
   }

   long getStartTime() {
      return this._startTime;
   }

   String getStartUser() {
      return this._startUser;
   }

   int getState() {
      return this._state;
   }

   void setStartUser(String var1) {
      this._startUser = var1;
   }

   void setState(int var1) {
      this._state = var1;
   }

   List<ConversationTimeoutListener> getTimeoutListeners() {
      return this._timeoutListeners;
   }

   void setTimeoutListeners(List<ConversationTimeoutListener> var1) {
      if (this._timeoutListeners != null) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this._timeoutListeners.iterator();

         label34:
         while(true) {
            ConversationTimeoutListener var4;
            do {
               do {
                  if (!var3.hasNext()) {
                     var3 = var2.iterator();

                     while(var3.hasNext()) {
                        var4 = (ConversationTimeoutListener)var3.next();
                        var4.cancel();
                     }
                     break label34;
                  }

                  var4 = (ConversationTimeoutListener)var3.next();
               } while(var4 == null);
            } while(var1 != null && var1.contains(var4));

            var2.add(var4);
         }
      }

      this._timeoutListeners = var1;
   }
}
