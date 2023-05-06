package dm.jdbc.driver;
/*
 *
 */

import dm.jdbc.desc.DmProperties;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 12:30 PM
 */
public class DaMDbConnection extends dm.jdbc.driver.DmdbConnection {

    public DaMDbConnection(DmProperties dmProperties) {
        super(dmProperties);
    }

    @Override
    public DmdbDatabaseMetaData do_getMetaData() {
        this.checkClosed();
        return new DaMDbDatabaseMetaData(this);
    }

}
