package qqCommon;

import java.io.Serializable;

/**
 * @author Carson
 * @Version
 */
public class User implements Serializable {
    private String userId;
    private String passwd;

    private static final long serialVersionUID = 1L;    // 增强稳定性

    public User(){  // 提供无参构造器然后在别的类里用Setter的方法来规制属性的思想
    }
    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
