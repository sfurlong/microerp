/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dai.server.dbService;

import dai.shared.cmnSvcs.daiException;

/**
 * @author sfurlong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface DBSequenceGenerator {
    
    public int getNewSequenceNum(dbconnect dbconn, int seqId) throws daiException;
    public void setSequenceValue(dbconnect dbconn, int seqId, int seqVal) throws daiException;

}
