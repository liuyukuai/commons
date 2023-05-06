/*
 *  
 */
package dm.jdbc.driver;

import dm.jdbc.desc.Configuration;
import dm.jdbc.desc.DmProperties;
import dm.jdbc.desc.EP;
import dm.jdbc.desc.EPGroup;

import java.util.ArrayList;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 12:37 PM
 */
public class DaMDriver extends DmDriver {

    public DmdbConnection do_connect(DmProperties var1) {
        if (var1 == null) {
            return null;
        } else {
            DmdbConnection var2 = null;
            EPGroup var3 = (EPGroup)var1.getObject(Configuration.epGroup.getName());
            if (var3 != null) {
                var3.setAttributes(var1);
                var2 = new DmdbConnection(var1);
                var3.connect(var2);
            } else {
                String var4 = var1.getTrimString(Configuration.host);
                int var5 = var1.getInt(Configuration.port);
                ArrayList var6 = new ArrayList(1);
                var6.add(new EP(var4, var5));
                var3 = new EPGroup(var4 + ":" + var5, var6);
                var3.props = var1;
                var3.setAttributes(var1);
                var1.setObject(Configuration.epGroup.getName(), var3);
                var2 = new DaMDbConnection(var1);
                var3.connect(var2);
            }
            return var2;
        }
    }
}
