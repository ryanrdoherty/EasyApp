/** 
 * DbConnectionBroker.  
 * @version 1.0.13 3/12/02
 * @author Marc A. Mnich
 */
package edu.upenn.bbl.common.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pooled Data Source
 * 
 * Creates and manages a pool of database connections.
 * 
 * Modified by Ryan Doherty to implement javax.sql.DataSource, from an original work by Marc Mnich
 * 
 * @version 1.0.13 3/12/02
 * @author Marc A. Mnich
 * @author Ryan R. Doherty
 */
public class DbConnectionBroker implements DataSource, Runnable {

	private static Logger LOG = LoggerFactory.getLogger(DbConnectionBroker.class.getName());
	
	private Thread runner;
    
    private Connection[] _connPool;
    private int[] _connStatus;

    private long[] _connLockTime, _connCreateDate;
    private String[] _connId;
    private String _dbDriver, _dbServer, _dbLogin, _dbPassword;
    private int _currConnections, _connLast, _maxConns, _maxConnMSec, _maxCheckoutSeconds, _debugLevel;

    //available: set to false on destroy, checked by getConnection()
    private boolean _available = true;

    private SQLWarning _currSQLWarning;

    private final int DEFAULTMAXCHECKOUTSECONDS = 60;
    private final int DEFAULTDEBUGLEVEL = 2;
    
    /**
     * Creates a new Connection Broker<br>
     * dbDriver:        JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'<br>
     * dbServer:        JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'<br>
     * dbLogin:         Database login name.  e.g. 'Scott'<br>
     * dbPassword:      Database password.    e.g. 'Tiger'<br>
     * minConns:        Minimum number of connections to start with.<br>
     * maxConns:        Maximum number of connections in dynamic pool.<br>
     * logFileString:   Absolute path name for log file. e.g. 'c:/temp/mylog.log' <br>
     * maxConnTime:     Time in days between connection resets. (Reset does a basic cleanup)<br>
     * logAppend:       Append to logfile (optional)<br>
     * maxCheckoutSeconds:       Max time a connection can be checked out before being recycled. Zero value turns option off, default is 60 seconds.
     * debugLevel:      Level of debug messages output to the log file.  0 -> no messages, 1 -> Errors, 2 -> Warnings, 3 -> Information
     */
    public DbConnectionBroker(String dbDriver, String dbServer, String dbLogin,
    						  String dbPassword, int minConns, int maxConns, double maxConnTime)
    		throws IOException {
	
    	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns, 
    			maxConns, maxConnTime, false, 
    			DEFAULTMAXCHECKOUTSECONDS, DEFAULTDEBUGLEVEL);
    }

    /*
     * Special constructor to handle logfile append
     */
    public DbConnectionBroker(String dbDriver, String dbServer, String dbLogin,
    						  String dbPassword, int minConns, int maxConns,
    						  double maxConnTime, boolean logAppend) 
    		throws IOException {
	
    	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns, 
    			maxConns, maxConnTime, logAppend,
    			DEFAULTMAXCHECKOUTSECONDS, DEFAULTDEBUGLEVEL);
    }

    /*
     * Special constructor to handle connection checkout expiration
     */
    public DbConnectionBroker(String dbDriver, String dbServer, String dbLogin,
    						  String dbPassword, int minConns, int maxConns,
    						  double maxConnTime, boolean logAppend,
    						  int maxCheckoutSeconds, int debugLevel) 
    		throws IOException {
	
    	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns, 
    			maxConns, maxConnTime, logAppend, 
    			maxCheckoutSeconds, debugLevel);
    }

    private void setupBroker(String dbDriver, String dbServer, String dbLogin,
    						 String dbPassword, int minConns, int maxConns,
    						 double maxConnTime, boolean logAppend,
    						 int maxCheckoutSeconds, int debugLevel) 
    		throws IOException {
            
        _connPool = new Connection[maxConns];
        _connStatus = new int[maxConns];
        _connLockTime = new long[maxConns];
        _connCreateDate = new long[maxConns];
        _connId = new String[maxConns];
        _currConnections = minConns;
        _maxConns = maxConns;
        _dbDriver = dbDriver;
        _dbServer = dbServer;
        _dbLogin = dbLogin;
        _dbPassword = dbPassword;
        _maxCheckoutSeconds = maxCheckoutSeconds;
        _debugLevel = debugLevel;

        _maxConnMSec = (int)(maxConnTime * 86400000.0);  //86400 sec/day
        if(_maxConnMSec < 30000) {  // Recycle no less than 30 seconds.
            _maxConnMSec = 30000;
        }
	
        LOG.info("-----------------------------------------");
        LOG.info("-----------------------------------------");
        LOG.info("Starting DbConnectionBroker Version 1.0.13:");
        LOG.info("dbDriver = " + dbDriver);
        LOG.info("dbServer = " + dbServer);
        LOG.info("dbLogin = " + dbLogin);
        LOG.info("minconnections = " + minConns);
        LOG.info("maxconnections = " + maxConns);
        LOG.info("Total refresh interval = " + maxConnTime + " days");
        LOG.info("logAppend = " + logAppend);
        LOG.info("maxCheckoutSeconds = " + maxCheckoutSeconds);
        LOG.info("debugLevel = " + debugLevel);
        LOG.info("-----------------------------------------");
        
        // Initialize the pool of connections with the mininum connections:
        // Problems creating connections may be caused during reboot when the
        //    servlet is started before the database is ready.  Handle this
        //    by waiting and trying again.  The loop allows 5 minutes for 
        //    db reboot.
        boolean connectionsSucceeded=false;
        int dbLoop=20;
        
        try {
            for(int i=1; i < dbLoop; i++) {
                try {
                    for(int j=0; j < _currConnections; j++) { 
                        createConn(j);
                    }
                    connectionsSucceeded=true;
                    break;
                }
                catch (SQLException e){
                	if (debugLevel > 0) {
                		LOG.info("--->Attempt (" + String.valueOf(i) +
                				" of " + String.valueOf(dbLoop) + 
                		") failed to create new connections set at startup: ");
                		LOG.info("    " + e);
                		LOG.info("    Will try again in 15 seconds...");
                	}
                	try {
                		Thread.sleep(15000);
                	}
                	catch(InterruptedException e1) {}
                }
            }
            if(!connectionsSucceeded) { // All attempts at connecting to db exhausted
            	if(debugLevel > 0) {
            		LOG.info("\r\nAll attempts at connecting to Database exhausted");
            	}
                throw new IOException();
            }
        }
        catch (Exception e) { 
            throw new IOException(e);
        }
        
        // Fire up the background housekeeping thread

        runner = new Thread(this);
        runner.start();

    }//End DbConnectionBroker()


    /**
     * Housekeeping thread.  Runs in the background with low CPU overhead.
     * Connections are checked for warnings and closure and are periodically
     * restarted.
     * This thread is a catchall for corrupted
     * connections and prevents the buildup of open cursors. (Open cursors
     * result when the application fails to close a Statement).
     * This method acts as fault tolerance for bad connection/statement programming.
     */
    public void run() {
    	
        boolean forever = true;
        Statement stmt=null;
        long maxCheckoutMillis = _maxCheckoutSeconds * 1000;

        while (forever) {
		    	    
		    // Get any Warnings on connections and print to event file
		    for(int i=0; i < _currConnections; i++) {            
		    	try { 
		    		_currSQLWarning = _connPool[i].getWarnings(); 
		    		if(_currSQLWarning != null) {
		    			if(_debugLevel > 1) {
		    				LOG.info("Warnings on connection " + 
		    						String.valueOf(i) + " " + _currSQLWarning);
		    			}
		    			_connPool[i].clearWarnings();
		    		}
		    	}
		    	catch(SQLException e) {
		    		if(_debugLevel > 1) {
		    			LOG.info("Cannot access Warnings: " + e);
		    		}
		    	}		
		    }
	            
		    for(int i=0; i < _currConnections; i++) { // Do for each connection
		    	long age = System.currentTimeMillis() - _connCreateDate[i];
			
		    	try {  // Test the connection with createStatement call
		    		synchronized(_connStatus) {
		    			if (_connStatus[i] > 0) { // In use, catch it next time!
				    
		    				// Check the time it's been checked out and recycle
		    				long timeInUse = System.currentTimeMillis() - _connLockTime[i];			
		    				if (_debugLevel > 2) {
		    					LOG.info("Warning.  Connection " + i + 
		    							" in use for " + timeInUse + " ms");
		    				}
		    				if (maxCheckoutMillis != 0) {
		    					if (timeInUse > maxCheckoutMillis) {
		    						if(_debugLevel > 1) {
		    							LOG.info("Warning. Connection " + 
		    									i + " failed to be returned in time.  Recycling...");
		    						}
		    						throw new SQLException();
		    					}
		    				}
		    				continue;
		    			}
		    			_connStatus[i] = 2; // Take offline (2 indicates housekeeping lock)
		    		}
		    		
		    		if (age > _maxConnMSec) {  // Force a reset at the max conn time
		    			throw new SQLException();
		    		}
			    
		    		stmt = _connPool[i].createStatement();
		    		_connStatus[i] = 0;  // Connection is O.K.
		    		//log.info("Connection confirmed for conn = " +
		    		//             String.valueOf(i));
			    
		    		// Some DBs return an object even if DB is shut down
		    		if(_connPool[i].isClosed()) {
		    			throw new SQLException();
		    		}
			    
		    		// Connection has a problem, restart it
		    	}
		    	catch(SQLException e) {
	
		    		if(_debugLevel > 1) {
		    			LOG.info(new Date().toString() + 
		    					" ***** Recycling connection " + 
		    					String.valueOf(i) + ":");
		    		}
				
		    		try {
		    			_connPool[i].close(); 
		    		}
		    		catch(SQLException e0) {
		    			if(_debugLevel > 0) {
		    				LOG.info("Error!  Can't close connection!  Might have been closed already.  Trying to recycle anyway... (" + e0 + ")");
		    			}
		    		}
	
		    		try {
		    			createConn(i);
		    		}
		    		catch(SQLException e1) {
		    			if(_debugLevel > 0) {
		    				LOG.info("Failed to create connection: " + e1);
		    			}
		    			_connStatus[i] = 0;  // Can't open, try again next time
		    		}
		    	}
		    	finally {
		    		try{
		    			if (stmt != null) {
		    				stmt.close();
		    			}
		    		}
		    		catch(SQLException e1){};
		    	}
		    }
		    
		    try {
		    	Thread.sleep(20000);
		    }  // Wait 20 seconds for next cycle
		    
		    catch (InterruptedException e) {
		    	// Returning from the run method sets the internal 
		    	// flag referenced by Thread.isAlive() to false.
		    	// This is required because we don't use stop() to 
		    	// shutdown this thread.
		    	return;
		    }
        }
    } // End run
    
    /**
     * This method hands out the connections in round-robin order.
     * This prevents a faulty connection from locking
     * up an application entirely.  A browser 'refresh' will
     * get the next connection while the faulty
     * connection is cleaned up by the housekeeping thread.
     * 
     * If the min number of threads are ever exhausted, new
     * threads are added up the the max thread count.
     * Finally, if all threads are in use, this method waits
     * 2 seconds and tries again, up to ten times.  After that, it
     * returns a null.
     */
    public Connection getConnection() { 
    
        Connection conn=null;

        if(_available){
            boolean gotOne = false;
            
            for(int outerloop=1; outerloop<=10; outerloop++) {
            
                try  {
                	int loop=0;
                    int roundRobin = _connLast + 1;
                    if(roundRobin >= _currConnections) roundRobin=0;
                    
                    do {
                    	synchronized(_connStatus) {
                    		if ((_connStatus[roundRobin] < 1) &&
                    		    (! _connPool[roundRobin].isClosed())) {
                    			conn = _connPool[roundRobin];
                    			_connStatus[roundRobin]=1;
                    			_connLockTime[roundRobin] = System.currentTimeMillis();
                    			_connLast = roundRobin;
                    			gotOne = true;
                    			break;
                            }
                    		else {
                                loop++;
                                roundRobin++;
                                if(roundRobin >= _currConnections) roundRobin=0;
                    		}
                    	}
                    }
                    while((gotOne==false)&&(loop < _currConnections));                    
                }
                catch (SQLException e1) {
                	LOG.info("Error: " + e1);
                }
            
                if(gotOne) {
                	break;
                }
                else {
                	synchronized(this) {  // Add new connections to the pool
                		if(_currConnections < _maxConns) {
                			try {
                				createConn(_currConnections);
                				_currConnections++;
                            }
                			catch(SQLException e) {
                				if(_debugLevel > 0) {
                					LOG.info("Error: Unable to create new connection: " + e);
                				}
                            }
                        }
                    }
                    
                    try { Thread.sleep(2000); }
                    catch(InterruptedException e) {}

                    if(_debugLevel > 0) {
                    	LOG.info("-----> Connections Exhausted!  Will wait and try again in loop " + 
                    			String.valueOf(outerloop));
                    }
                }                
            } // End of try 10 times loop
        }
        else {
        	if(_debugLevel > 0) {
        		LOG.info("Unsuccessful getConnection() request during destroy()");
        	}
        } // End if(available)    
                    
        if(_debugLevel > 2) {
        	LOG.info("Handing out connection " + 
        			idOfConnection(conn) + " --> " +
        			(new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a")).format(new java.util.Date()));
        }

        return new DbConnection(this, conn);
    }
    
    /**
     * Returns the local JDBC ID for a connection.
     */
    private int idOfConnection(Connection conn) {

    	int match;
        String tag;
        
        try {
            tag = conn.toString();
        }
        catch (NullPointerException e1) {
            tag = "none";
        }
        
        match=-1;
        
        for(int i=0; i< _currConnections; i++) {
            if(_connId[i].equals(tag)) {
                match = i;
                break;
            }
        }
        return match;
    }
    
    /**
     * Frees a connection.  Replaces connection back into the main pool for
     * reuse.
     */
    public String freeConnection(Connection conn) {
        String res="";
        int thisconn = idOfConnection(conn);
        if (thisconn >= 0) {
        	_connStatus[thisconn]=0;
            res = "freed " + conn.toString();
            //log.info("Freed connection " + String.valueOf(thisconn) +
            //            " normal exit: ");
        }
        else {
        	if(_debugLevel > 0) {
        		LOG.info("----> Error: Could not free connection!!!");
        	}
        }
        return res;
    }
    
    /**
     * Returns the age of a connection -- the time since it was handed out to
     * an application.
     */
    public long getAge(Connection conn) { // Returns the age of the connection in millisec.
        int thisconn = idOfConnection(conn);
        return System.currentTimeMillis() - _connLockTime[thisconn];
    }

    private void createConn(int i)
        throws SQLException {

        Date now = new Date();        
        try {
            Class.forName (_dbDriver);
            _connPool[i] = DriverManager.getConnection(_dbServer,_dbLogin,_dbPassword);                           
            _connStatus[i]=0;
            _connId[i]=_connPool[i].toString();
            _connLockTime[i]=0;
            _connCreateDate[i] =  now.getTime();
        }
        catch (ClassNotFoundException e2) {
        	if(_debugLevel > 0) {
        		LOG.info("Error creating connection: " + e2);
        	}
        }
                
        LOG.info(now.toString() + "  Opening connection " + String.valueOf(i) + 
                    " " + _connPool[i].toString() + ":");
    }
    
    /**
     * Shuts down the housekeeping thread and closes all connections 
     * in the pool. Call this method from the destroy() method of the servlet.
     */

    /**
     * Multi-phase shutdown.  having following sequence:
     * <OL>
     * <LI><code>getConnection()</code> will refuse to return connections.
     * <LI>The housekeeping thread is shut down.<br>
     *    Up to the time of <code>millis</code> milliseconds after shutdown of
     *    the housekeeping thread, <code>freeConnection()</code> can still be
     *    called to return used connections.
     * <LI>After <code>millis</code> milliseconds after the shutdown of the
     *    housekeeping thread, all connections in the pool are closed.
     * <LI>If any connections were in use while being closed then a
     *    <code>SQLException</code> is thrown.
     * <LI>The log is closed.
     * </OL><br>
     * Call this method from a servlet destroy() method.
     *
     * @param      millis   the time to wait in milliseconds.
     * @exception  SQLException if connections were in use after 
     * <code>millis</code>.
     */
    public void destroy(int millis) throws SQLException {
    
        // Checking for invalid negative arguments is not necessary,
        // Thread.join() does this already in runner.join().

        // Stop issuing connections
        _available=false;

        // Shut down the background housekeeping thread
        runner.interrupt();

        // Wait until the housekeeping thread has died.
        try { runner.join(millis); } 
        catch(InterruptedException e){} // ignore 
        
        // The housekeeping thread could still be running
        // (e.g. if millis is too small). This case is ignored.
        // At worst, this method will throw an exception with the 
        // clear indication that the timeout was too short.

        long startTime=System.currentTimeMillis();

        // Wait for freeConnection() to return any connections
        // that are still used at this time.
        int useCount;
        while((useCount=getUseCount())>0 && System.currentTimeMillis() - startTime <=  millis) {
            try { Thread.sleep(500); }
            catch(InterruptedException e) {} // ignore 
        }

        // Close all connections, whether safe or not
        for(int i=0; i < _currConnections; i++) {
            try {
                _connPool[i].close();
            }
            catch (SQLException e1) {
            	if(_debugLevel > 0) {
            		LOG.info("Cannot close connections on Destroy");
            	}
            }
        }

        if(useCount > 0) {
            //bt-test successful
            String msg="Unsafe shutdown: Had to close " + useCount + " active DB connections after "+millis+"ms";
            LOG.info(msg);
            // Throwing following Exception is essential because servlet authors
            // are likely to have their own error logging requirements.
            throw new SQLException(msg);
        }

    }//End destroy()

    /**
     * Less safe shutdown.  Uses default timeout value.
     * This method simply calls the <code>destroy()</code> method 
     * with a <code>millis</code>
     * value of 10000 (10 seconds) and ignores <code>SQLException</code> 
     * thrown by that method.
     * @see     #destroy(int)
     */
    public void destroy() { 
        try {
            destroy(10000);
        }
        catch(SQLException e) {}
    }
    
    /**
     * Returns the number of connections in use.
     */
    // This method could be reduced to return a counter that is
    // maintained by all methods that update connStatus.
    // However, it is more efficient to do it this way because:
    // Updating the counter would put an additional burden on the most
    // frequently used methods; in comparison, this method is
    // rarely used (although essential).
    private int getUseCount() {
        int useCount=0;
        synchronized(_connStatus) {
            for(int i=0; i < _currConnections; i++) {
                if(_connStatus[i] > 0) { // In use
                    useCount++;
                }
            }
        }
        return useCount;
    }//End getUseCount()

    /**
     * Returns the number of connections in the dynamic pool.
     */
    public int getSize() {
        return _currConnections;
    }//End getSize()

    
    /**
     * Ignores the username and password passed in.  Once created, this
     * datasource will only connect as a single user.
     */
	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return getConnection();
	}
	
	@Override
	public int getLoginTimeout() throws SQLException {
		return _maxConnMSec;
	}
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		_maxConnMSec = seconds;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("This implementation does not wrap any particular class.");
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// do nothing here; logging by calling classes is disabled
	}

} // End class
