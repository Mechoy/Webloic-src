package weblogic.iiop.csi;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.login.LoginException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.MARSHAL;
import weblogic.corba.cos.security.GSSUtil;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.ReplyMessage;
import weblogic.iiop.RequestMessage;
import weblogic.iiop.ServiceContext;
import weblogic.iiop.ServiceContextList;
import weblogic.kernel.Kernel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.InvalidParameterException;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RemoteResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class SASServiceContext extends ServiceContext {
   private short ctxMsgType;
   private ContextBody ctxBody;
   private AuthenticatedSubject subject;
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public SASServiceContext() {
      super(15);
   }

   public SASServiceContext(short var1, ContextBody var2, AuthenticatedSubject var3) {
      super(15);
      this.ctxMsgType = var1;
      this.ctxBody = var2;
      this.subject = var3;
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("created " + this);
      }

   }

   public SASServiceContext(CompoundSecMechList var1, AuthenticatedSubject var2, EndPoint var3) {
      super(15);
      this.ctxMsgType = 0;
      long var4 = 0L;
      if (var1.isGSSUPTargetStateful()) {
         var4 = var3.getNextClientContextId();
      }

      byte[] var6 = null;
      IdentityToken var7 = null;
      PasswordCredential var8 = getPasswordCredential(var2, var3);
      String var9 = null;
      boolean var10 = var1.hasGSSUP();
      boolean var11 = var1.hasGSSUPIdentity();
      if (var10) {
         byte[] var12 = var1.getGSSUPTarget();
         var9 = GSSUtil.extractGSSUPGSSNTExportedName(var12);
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("create sasservice target: " + var9 + " hasGSSUP: " + var10 + " hasIdentity: " + var11 + " pc: " + var8);
      }

      if (var10 && var8 != null) {
         GSSUPImpl var15 = new GSSUPImpl(var8.getUsername(), var9, var8.getPassword(), var9);
         var6 = var15.getBytes();
         var7 = new IdentityToken(0, true, (byte[])null);
      } else if (var11) {
         if (var2 != null && !SubjectUtils.isUserAnonymous(var2) && !SecurityServiceManager.isKernelIdentity(var2)) {
            String var14 = SubjectUtils.getUsername(var2);
            if (var9 != null) {
               var14 = var14 + "@" + var9;
            }

            byte[] var13 = GSSUtil.createGSSUPGSSNTExportedName(var14);
            var7 = new IdentityToken(2, true, var13);
         } else {
            var7 = new IdentityToken(1, true, (byte[])null);
         }
      }

      this.ctxBody = new EstablishContext(var4, var6, var7);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("created " + this);
      }

   }

   public SASServiceContext(long var1) {
      super(15);
      this.ctxMsgType = 5;
      this.ctxBody = new MessageInContext(var1, false);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("created " + this);
      }

   }

   public SASServiceContext(IIOPInputStream var1) {
      super(15);
      this.readEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      short var2 = var1.read_short();
      switch (var2) {
         case 0:
            this.ctxBody = new EstablishContext(var1);
            break;
         case 1:
            this.ctxBody = new CompleteEstablishContext(var1);
            break;
         case 2:
         case 3:
         default:
            throw new MARSHAL("Unsupported CSI MsgType.");
         case 4:
            this.ctxBody = new ContextError(var1);
            break;
         case 5:
            this.ctxBody = new MessageInContext(var1);
      }

      this.ctxMsgType = var2;
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("read " + this);
      }

   }

   public short getMsgType() {
      return this.ctxMsgType;
   }

   public ContextBody getBody() {
      return this.ctxBody;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   public void writeEncapsulation(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("writing " + this);
      }

      var1.write_short(this.ctxMsgType);
      this.ctxBody.write(var1);
   }

   public void handleSASReply(EndPoint var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("handle SAS Reply " + this);
      }

      switch (this.ctxMsgType) {
         case 0:
         case 2:
         case 3:
         case 5:
         default:
            throw new MARSHAL("Unsupported Reply CSI MsgType.");
         case 1:
            CompleteEstablishContext var2 = (CompleteEstablishContext)this.ctxBody;
            if (var2.getContextStateful()) {
               var1.establishSASClientContext(var2.getClientContextId());
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("stateful CSIv2 session established.");
               }
            } else {
               var1.removeSASClientContext(var2.getClientContextId());
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("stateful CSIv2 session reset.");
               }
            }
            break;
         case 4:
            ContextError var3 = (ContextError)this.ctxBody;
            var1.removeSASClientContext(var3.getClientContextId());
            if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
               IIOPLogger.logDebugSecurity("received ContextError(" + var3.getMajorStatus() + ", " + var3.getMinorStatus() + ") for context " + var3.getClientContextId());
            }
      }

   }

   public boolean handleSASRequest(RequestMessage var1, EndPoint var2) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("handle SAS Request ");
      }

      boolean var3 = false;
      ContextError var4 = null;
      switch (this.ctxMsgType) {
         case 0:
            var4 = this.handleEstablishContext(var2);
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         default:
            throw new MARSHAL("Unsupported Request CSI MsgType.");
         case 5:
            MessageInContext var5 = (MessageInContext)this.ctxBody;
            SecurityContext var6 = var2.getSecurityContext(var5.getClientContextId());
            if (var6 != null) {
               this.subject = var6.getSubject();
               if (var5.getDiscardContext()) {
                  var2.removeSecurityContext(var5.getClientContextId());
               }
            } else {
               var4 = new ContextError(var5.getClientContextId(), 4, 1, (byte[])null);
            }
      }

      if (var4 != null) {
         if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
            IIOPLogger.logDebugSecurity("CSIv2 context error.");
         }

         SASServiceContext var11 = new SASServiceContext((short)4, var4, (AuthenticatedSubject)null);
         ServiceContextList var12 = new ServiceContextList();
         var12.addServiceContext(var11);
         ReplyMessage var7 = new ReplyMessage(var2, var1, var12, 2);
         IIOPOutputStream var8 = var7.getOutputStream();
         var7.write(var8);
         var8.write_string("IDL:omg.org/CORBA/NO_PERMISSION:1.0");
         var8.write_long(0);
         var8.write_long(CompletionStatus.COMPLETED_NO.value());

         try {
            var2.send(var8);
         } catch (RemoteException var10) {
            throw new MARSHAL("Sending reply on SAS failure");
         }

         var3 = true;
      }

      return var3;
   }

   public AuthenticatedSubject getSubject() {
      return this.subject;
   }

   public ClientSecurityContext getClientContext() {
      return new ClientSecurityContext(((EstablishContext)this.getBody()).getClientContextId(), this);
   }

   public SASServiceContext getCompleteEstablishContext() {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("getCompleteEstablishContexst");
      }

      EstablishContext var1 = (EstablishContext)this.ctxBody;
      CompleteEstablishContext var2 = new CompleteEstablishContext(var1.getClientContextId(), var1.getClientContextId() != 0L, (byte[])null);
      return new SASServiceContext((short)1, var2, this.subject);
   }

   private ContextError handleEstablishContext(final EndPoint var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         log("handleEstablishContext");
      }

      EstablishContext var2 = (EstablishContext)this.ctxBody;
      SecurityContext var3 = null;
      AuthenticatedSubject var4 = null;
      byte[] var5 = var2.getClientAuthenticationToken();
      final IdentityToken var6 = var2.getIdentityToken();
      if (var2.getClientContextId() != 0L) {
         var3 = var1.getSecurityContext(var2.getClientContextId());
         if (var3 != null) {
            if (var6 != null && !var6.equals(var3.getEstablishContext().getIdentityToken())) {
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("Invalid CSIv2 context token");
               }

               return new ContextError(var2.getClientContextId(), 3, 1, (byte[])null);
            }

            if (var5 != null) {
               byte[] var28 = var3.getEstablishContext().getClientAuthenticationToken();
               if (!Arrays.equals(var5, var28)) {
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     IIOPLogger.logDebugSecurity("Invalid CSIv2 auth token");
                  }

                  return new ContextError(var2.getClientContextId(), 3, 1, (byte[])null);
               }
            }

            this.subject = var3.getSubject();
            return null;
         }
      }

      if (var5 != null) {
         try {
            GSSUPImpl var7 = new GSSUPImpl(var5);
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               log("Handle establish username: " + var7.getUserName());
            }

            String var8 = "weblogicDEFAULT";
            PrincipalAuthenticator var9 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var8, ServiceType.AUTHENTICATION);
            SimpleCallbackHandler var10 = new SimpleCallbackHandler(var7.getUserName(), var7.getPassword());
            this.subject = var9.authenticate(var10, var1.getConnection().getContextHandler());
            PasswordCredential var11 = new PasswordCredential(var7.getUserName(), var7.getPassword());
            this.subject.getPrivateCredentials(kernelId).add(var11);
            if (ChannelHelper.isLocalAdminChannelEnabled() && SubjectUtils.isUserAnAdministrator(this.subject) && !ChannelHelper.isAdminChannel(var1.getServerChannel())) {
               return new ContextError(var2.getClientContextId(), 1, 1, (byte[])null);
            }

            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               log("Created subject for username: " + var7.getUserName() + " subject: " + this.subject);
            }
         } catch (GSSUPDecodeException var25) {
            if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
               IIOPLogger.logDebugSecurity("Error decoding CSIv2 GSS token: " + var25.getMessage());
            }

            return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
         } catch (LoginException var26) {
            if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
               IIOPLogger.logDebugSecurity("CSIv2 login error: " + var26.getMessage());
            }

            return new ContextError(var2.getClientContextId(), 1, 1, (byte[])null);
         }
      }

      if (var6 != null) {
         String var27 = "weblogicDEFAULT";
         final PrincipalAuthenticator var29 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var27, ServiceType.AUTHENTICATION);
         int var30 = var6.getIdentityType();
         var4 = this.getAssertAsSubject();
         switch (var30) {
            case 0:
               break;
            case 1:
               try {
                  this.subject = (AuthenticatedSubject)SecurityServiceManager.runAs(kernelId, var4, new PrivilegedExceptionAction() {
                     public Object run() throws LoginException {
                        return var29.assertIdentity("CSI.ITTAnonymous", new Boolean(var6.getAnonymous()), var1.getConnection().getContextHandler());
                     }
                  });
               } catch (PrivilegedActionException var24) {
                  LoginException var33 = (LoginException)var24.getException();
                  if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                     log("failed identity assertion - use connection subject " + var33);
                  }

                  this.subject = null;
               }
               break;
            case 2:
               byte[] var31 = var6.getPrincipalName();
               final String var32 = GSSUtil.extractGSSUPGSSNTExportedName(var31);
               String var12 = null;
               if (var32 == null) {
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     IIOPLogger.logDebugSecurity("Unsupported CSIv2 mechanism");
                  }

                  return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
               }

               int var13 = var32.indexOf(64);
               if (var13 >= 0) {
                  var12 = "weblogicDEFAULT";
                  var32 = var32.substring(0, var13);

                  try {
                     var29 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var12, ServiceType.AUTHENTICATION);
                  } catch (InvalidParameterException var23) {
                     if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                        log("Assert identity realm not found: " + var12);
                     }
                  }
               }

               if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                  log("Assert identity: " + var32);
               }

               try {
                  this.subject = (AuthenticatedSubject)SecurityServiceManager.runAs(kernelId, var4, new PrivilegedExceptionAction() {
                     public Object run() throws LoginException {
                        return var29.assertIdentity("CSI.PrincipalName", var32, var1.getConnection().getContextHandler());
                     }
                  });
                  break;
               } catch (PrivilegedActionException var21) {
                  LoginException var34 = (LoginException)var21.getException();
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     log("failed identity assertion prin " + var34);
                  }

                  return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
               }
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("Unsupported CSIv2 mechanism");
               }

               return new ContextError(var2.getClientContextId(), 1, 1, (byte[])null);
            case 4:
               byte[] var15 = var6.getCertChain();
               final X509Certificate[] var35 = GSSUtil.getX509CertChain(var15);
               if (var35 == null) {
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     IIOPLogger.logDebugSecurity("CSIv2 certification chain not found");
                  }

                  return new ContextError(var2.getClientContextId(), 1, 1, (byte[])null);
               }

               if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                  log("Assert identity chain: " + var35);
               }

               try {
                  this.subject = (AuthenticatedSubject)SecurityServiceManager.runAs(kernelId, var4, new PrivilegedExceptionAction() {
                     public Object run() throws LoginException {
                        return var29.assertIdentity("CSI.X509CertChain", var35, var1.getConnection().getContextHandler());
                     }
                  });
                  break;
               } catch (PrivilegedActionException var20) {
                  LoginException var18 = (LoginException)var20.getException();
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     log("failed identity assertion cert chain " + var18);
                  }

                  return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
               }
            case 8:
               final byte[] var14 = var6.getDistinguishedName();
               if (var14 == null) {
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     IIOPLogger.logDebugSecurity("CSIv2 distinguished name not found");
                  }

                  return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
               }

               if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                  log("Assert identity distinguished: " + var14);
               }

               try {
                  this.subject = (AuthenticatedSubject)SecurityServiceManager.runAs(kernelId, var4, new PrivilegedExceptionAction() {
                     public Object run() throws LoginException {
                        return var29.assertIdentity("CSI.DistinguishedName", var14, var1.getConnection().getContextHandler());
                     }
                  });
               } catch (PrivilegedActionException var22) {
                  LoginException var16 = (LoginException)var22.getException();
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     log("failed identity assertion dist name " + var16);
                  }

                  return new ContextError(var2.getClientContextId(), 2, 1, (byte[])null);
               }
         }
      }

      if (var2.getClientContextId() != 0L && var3 == null) {
         if (this.subject == null) {
            this.subject = SubjectUtils.getAnonymousSubject();
         }

         var3 = new SecurityContext(var2.getClientContextId(), var2, this.subject);
         var1.putSecurityContext(var2.getClientContextId(), var3);
      }

      return null;
   }

   private static final PasswordCredential getPasswordCredential(AuthenticatedSubject var0, EndPoint var1) {
      if (var0 != null && !SubjectUtils.isUserAnonymous(var0)) {
         PasswordCredential var2 = null;
         Set var3 = var0.getPrivateCredentials(kernelId, PasswordCredential.class);
         Iterator var4 = var3.iterator();
         if (var4.hasNext()) {
            var2 = (PasswordCredential)var4.next();
         }

         if (!Kernel.isServer()) {
            return var2;
         } else if (var0.getPrincipals(UserInfo.class).size() > 0) {
            return var2;
         } else {
            CredentialManager var5 = (CredentialManager)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.CREDENTIALMANAGER);
            if (var5 == null) {
               return var2;
            } else {
               Object[] var6 = var5.getCredentials(kernelId, var0, new RemoteResource(var1.getConnection().getRemoteChannel().getProtocolPrefix(), var1.getConnection().getRemoteChannel().getPublicAddress(), Integer.toString(var1.getConnection().getRemoteChannel().getPublicPort()), (String)null, (String)null), var1.getConnection().getContextHandler(), "weblogic.Password");

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  Object var8 = var6[var7];
                  if (var8 instanceof PasswordCredential) {
                     if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                        log("Mapped subject: " + var0 + " to " + var8);
                     }

                     return (PasswordCredential)var8;
                  }
               }

               if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                  log("No credential mapping for: " + var0 + ",  will use defaults: " + var2);
               }

               return var2;
            }
         }
      } else {
         return null;
      }
   }

   public String toString() {
      return "SASServiceContext Context (msgType = " + this.ctxMsgType + ", body = " + this.ctxBody + ", subject = " + this.subject + ")";
   }

   private static void log(String var0) {
      IIOPLogger.logDebugSecurity("<SASServiceContext>: " + var0);
   }

   private AuthenticatedSubject getAssertAsSubject() {
      AuthenticatedSubject var1 = null;
      if (null != this.subject) {
         if (this.subject.equals(kernelId)) {
            var1 = SubjectUtils.getAnonymousSubject();
         } else {
            var1 = this.subject;
         }
      } else {
         var1 = SecurityServiceManager.getCurrentSubject(kernelId);
      }

      return var1;
   }
}
