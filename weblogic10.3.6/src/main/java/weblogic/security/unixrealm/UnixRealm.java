package weblogic.security.unixrealm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.UnixRealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.AbstractListableRealm;
import weblogic.security.acl.ClosableEnumeration;
import weblogic.security.acl.DebuggableRealm;
import weblogic.security.acl.User;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.Pool;

public class UnixRealm extends AbstractListableRealm implements DebuggableRealm {
   private UnixRealm thisRealm = this;
   private static final int MAX_REQUESTS = 500;
   private static final int POOL_SIZE = 6;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Pool delegatePool;
   LogOutputStream log;

   public UnixRealm() {
      super("Unix Realm");
      UnixRealmMBean var1 = (UnixRealmMBean)ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm().getBasicRealm();
      this.delegatePool = new Pool(new UnixDelegate.DFactory(var1.getAuthProgram(), 500, this), 6);
      this.setDebug(ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getDebugSecurityRealm());
   }

   private UnixDelegate getDelegate() {
      try {
         return (UnixDelegate)this.delegatePool.getInstance();
      } catch (InvocationTargetException var2) {
         throw new SubprocessException("could not get subprocess", var2);
      }
   }

   public User getUser(String var1) {
      if (this.log != null) {
         this.log.debug("getUser(\"" + var1 + "\")");
      }

      UnixDelegate var2 = this.getDelegate();

      UnixUser var4;
      try {
         UnixDelegate.Chat var3 = var2.chat(new String[]{"user_exists", var1});
         if (var3.expect("0")) {
            var4 = new UnixUser(var1, this);
            return var4;
         }

         var4 = null;
      } catch (IOException var8) {
         var2 = null;
         throw new SubprocessException("auth process failed", var8);
      } finally {
         if (var2 != null) {
            this.delegatePool.returnInstance(var2);
         }

      }

      return var4;
   }

   protected User authUserPassword(String var1, String var2) {
      if (this.log != null) {
         this.log.debug("authUserPassword(\"" + var1 + "\")");
      }

      UnixUser var3 = null;
      UnixDelegate var4 = this.getDelegate();

      try {
         UnixDelegate.Chat var5 = var4.chat(new String[]{"user_auth", var1, var2});
         var5.require("0");
         if (var5.expect("0")) {
            var3 = new UnixUser(var1, this);
         }
      } catch (IOException var9) {
         var4 = null;
         throw new SubprocessException("auth process failed", var9);
      } finally {
         if (var4 != null) {
            this.delegatePool.returnInstance(var4);
         }

      }

      return var3;
   }

   protected Hashtable getGroupMembersInternal(String var1) {
      if (this.log != null) {
         this.log.debug("getGroup(\"" + var1 + "\")");
      }

      UnixDelegate var2 = this.getDelegate();

      Hashtable var4;
      try {
         UnixDelegate.Chat var3 = var2.chat(new String[]{"group_members", var1});
         if (var3.expect("0")) {
            var4 = this.readGroupMembers(var3);
            return var4;
         }

         var4 = null;
      } catch (IOException var8) {
         var2 = null;
         throw new SubprocessException("auth process failed", var8);
      } finally {
         if (var2 != null) {
            this.delegatePool.returnInstance(var2);
         }

      }

      return var4;
   }

   public Group getGroup(String var1) {
      Hashtable var2 = this.getGroupMembersInternal(var1);
      return var2 != null ? new UnixGroup(var1, this, var2) : null;
   }

   private Hashtable readGroupMembers(UnixDelegate.Chat var1) throws IOException {
      Hashtable var2 = new Hashtable();

      String var3;
      while((var3 = var1.read()).length() != 0) {
         var2.put(var3, new UnixUser(var3, this));
      }

      return var2;
   }

   public Enumeration getUsers() {
      if (this.log != null) {
         this.log.debug("getUsers()");
      }

      UnixDelegate var1 = this.getDelegate();

      ChatEnumeration var4;
      try {
         UnixDelegate.Chat var2 = var1.chat(new String[]{"user_list"});
         var2.require("0");
         ChatEnumeration var3 = new ChatEnumeration(var1, var2, new ChatNextHandler() {
            public Object handle(String var1, UnixDelegate.Chat var2) {
               return new UnixUser(var1, UnixRealm.this.thisRealm);
            }

            public void skip(String var1, UnixDelegate.Chat var2) throws IOException {
            }
         });
         var1 = null;
         var4 = var3;
      } catch (IOException var8) {
         var1 = null;
         throw new SubprocessException("auth process failed", var8);
      } finally {
         if (var1 != null) {
            this.delegatePool.returnInstance(var1);
         }

      }

      return var4;
   }

   public Enumeration getGroups() {
      if (this.log != null) {
         this.log.debug("getGroups()");
      }

      UnixDelegate var1 = this.getDelegate();

      ChatEnumeration var4;
      try {
         UnixDelegate.Chat var2 = var1.chat(new String[]{"group_list"});
         ChatEnumeration var3;
         if (!var2.expect("0")) {
            var3 = null;
            return var3;
         }

         var3 = new ChatEnumeration(var1, var2, new ChatNextHandler() {
            public Object handle(String var1, UnixDelegate.Chat var2) throws IOException {
               Hashtable var3 = UnixRealm.this.thisRealm.readGroupMembers(var2);
               return new UnixGroup(var1, UnixRealm.this.thisRealm, var3);
            }

            public void skip(String var1, UnixDelegate.Chat var2) throws IOException {
               Object var3 = null;

               while(var2.read().length() > 0) {
               }

            }
         });
         var1 = null;
         var4 = var3;
      } catch (IOException var8) {
         var1 = null;
         throw new SubprocessException("auth process failed", var8);
      } finally {
         if (var1 != null) {
            this.delegatePool.returnInstance(var1);
         }

      }

      return var4;
   }

   public void setDebug(boolean var1) {
      if (var1 && this.log == null) {
         this.log = new LogOutputStream("UnixRealm");
      }

      if (!var1) {
         this.log = null;
      }

   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   private class ChatEnumeration implements ClosableEnumeration {
      boolean closed = false;
      String current;
      UnixDelegate delegate;
      UnixDelegate.Chat chat;
      ChatNextHandler handler;

      ChatEnumeration(UnixDelegate var2, UnixDelegate.Chat var3, ChatNextHandler var4) {
         this.delegate = var2;
         this.chat = var3;
         this.handler = var4;
         this.increment();
      }

      private void handleIOException(IOException var1) {
         this.closed = true;
         this.delegate = null;
         throw new SubprocessException("auth process failed", var1);
      }

      private void increment() {
         try {
            this.current = this.chat.read();
            if (this.current.length() == 0) {
               this.close();
            }
         } catch (IOException var2) {
            this.handleIOException(var2);
         }

      }

      public boolean hasMoreElements() {
         return !this.closed;
      }

      public Object nextElement() {
         if (this.closed) {
            throw new NoSuchElementException("ChatEnumeration.nextElement");
         } else {
            try {
               Object var1 = this.handler.handle(this.current, this.chat);
               this.increment();
               return var1;
            } catch (IOException var2) {
               this.handleIOException(var2);
               return null;
            }
         }
      }

      public void close() {
         if (!this.closed) {
            this.closed = true;

            try {
               while(this.delegate != null && this.current.length() > 0) {
                  this.handler.skip(this.current, this.chat);
                  this.current = this.chat.read();
               }
            } catch (IOException var2) {
               this.delegate = null;
            }

            if (this.delegate != null) {
               UnixRealm.this.delegatePool.returnInstance(this.delegate);
            }

         }
      }
   }

   private interface ChatNextHandler {
      Object handle(String var1, UnixDelegate.Chat var2) throws IOException;

      void skip(String var1, UnixDelegate.Chat var2) throws IOException;
   }
}
