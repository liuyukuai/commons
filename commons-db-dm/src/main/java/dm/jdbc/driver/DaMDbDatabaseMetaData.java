/*
 *  
 */
package dm.jdbc.driver;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 12:33 PM
 */
public class DaMDbDatabaseMetaData extends DmdbDatabaseMetaData {

    public DaMDbDatabaseMetaData(DmdbConnection dmdbConnection) {
        super(dmdbConnection);
    }

    public int do_getDatabaseMinorVersion() {
        if (this.connection.compatibleOracle()) {
            return 1;
        } else {
            String[] var1 = this.do_getDatabaseProductVersion().split("\\.");
            String minor = var1[1];
            if (StringUtils.isBlank(minor)) {
                return 0;
            }
            return Integer.parseInt(minor);
        }
    }
}
