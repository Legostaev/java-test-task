package rest.service.foreign.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.codehaus.jackson.map.ObjectMapper;

import rest.service.entity.Account;
import rest.service.manager.AccountDetailsManager;

/**
 * 
 * @author Veniamin
 *
 */
public class AccountDetailsManagerImpl implements AccountDetailsManager {
    
    protected static final class Details {
        private Long   id;
        private String accountDetails;
        
        public Details() {
            this.id = null;
            this.accountDetails = null;
        }
        
        public Long getId() {
            return this.id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getAccountDetails() {
            return this.accountDetails;
        }
        
        public void setAccountDetails(String accountDetails) {
            this.accountDetails = accountDetails;
        }
    }
    
    private static final class LocalSocket implements Closeable {
        
        private SSLSocket         socket;
        private BufferedWriter    writer;
        private BufferedReader    reader;
        
        public LocalSocket(
            SSLSocket socket) 
                throws IOException {
            
            this.socket = socket;
            
            this.writer = 
                new BufferedWriter(
                    new OutputStreamWriter(
                        socket.getOutputStream()));
            
            this.reader = 
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
        }
        
        public BufferedWriter getWriter() {
            return this.writer;
        }
        
        public BufferedReader getReader() {
            return this.reader;
        }
        
        public boolean isConnected() {
            return this.socket.isConnected();
        }

        public void close() throws IOException {
            this.writer.close();
            this.reader.close();
            this.socket.close();
        }
    }
    
    private String             host;
    private short              port;
    private SSLSocketFactory   sslSocketFactory;
    private List<LocalSocket>  socketList;
    private int                max;
    private int                count;
    
    public AccountDetailsManagerImpl(
        String host, 
        short port,
        int max) 
            throws 
                KeyManagementException, 
                NoSuchAlgorithmException  {
        
        Objects.requireNonNull(host);
        
        this.host       = host;
        this.port       = port;
        this.max        = Math.max(1, max);
        this.count      = 0;
        this.socketList = new LinkedList<LocalSocket>();
        
        this.initConnectionManager();
    }
    
    private void initConnectionManager() 
        throws 
            NoSuchAlgorithmException, 
            KeyManagementException {
        
        SSLContext ctx = SSLContext.getInstance("TLSV1.2");
        ctx.init(null, null, null);
        this.sslSocketFactory = ctx.getSocketFactory();
    }
    
    private SSLSocket createSocket() 
        throws 
            UnknownHostException, 
            IOException {
        SSLSocket socket = null;
        
        socket = (SSLSocket) 
            this.sslSocketFactory.createSocket(
                this.host, 
                this.port);
        
        socket.setEnabledProtocols(
            new String[] { "TLSv1.2" });
        
        return socket;
    }
    
    private synchronized LocalSocket getSocket() 
        throws 
            InterruptedException, 
            UnknownHostException, 
            IOException {
        
        LocalSocket    socket = null;
        
        while (this.count >= this.max) {
            this.wait();
        }
        
        if (this.socketList.size() > 0) {
            socket = this.socketList.remove(0);
        }
        else {
            socket = new LocalSocket(
                this.createSocket());
            
            this.count++;
        }
        
        return socket; 
    }
    
    private synchronized void freeSocket(
        LocalSocket socket) 
            throws IOException {
        
        if (!socket.isConnected()) {
            this.count--;
            socket.close();
        }
        else {
            this.socketList.add(
                socket);
            
            this.count--;
        }
        
        this.notifyAll();
    }
    
    public void getDetails(
        Account account) 
            throws 
                IOException, 
                URISyntaxException, 
                InterruptedException {
        
        Objects.requireNonNull(account);
        LocalSocket  socket  = null;
        LocalSocket  tmp     = null;
        String       str     = null;
        ObjectMapper mapper  = null;
        Details      details = null;
        
        try {
            do {
                if ((tmp = socket) != null) {
                    socket = null;
                    this.freeSocket(tmp);
                }
                
                socket = this.getSocket();
            } while (!socket.isConnected());
            
            socket.getWriter().write(
                String.format(
                    Locale.US, 
                    "{\"id\": %d}\r\n",
                    account.getId()));
            
            socket.getWriter().flush();
            str = socket.getReader().readLine();
            mapper = new ObjectMapper();
            
            details = mapper.readValue(
                str, 
                Details.class);
            
            account.setDetails(
                details.getAccountDetails());
        }
        
        finally {
            if (socket != null) {
                this.freeSocket(socket);
            }
        }
    }
}
