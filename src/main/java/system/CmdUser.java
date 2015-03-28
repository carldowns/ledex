package system;

/**
 *
 */
public class CmdUser {

    public static final CmdUser ADMIN = new CmdUser("admin", "admin");
    public static final CmdUser SYSTEM = new CmdUser("system", "system");

    private String UUID;
    private String userName;
    private String psOneWayHash;
    private String cookie;

    public CmdUser(String UUID, String userName) {
        this.UUID = UUID;
        this.userName = userName;
    }

    public static CmdUser getAdminUser() {
        return ADMIN;
    }

    public static CmdUser getSystemUser() {
        return SYSTEM;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPsOneWayHash() {
        return psOneWayHash;
    }

    public void setPsOneWayHash(String psOneWayHash) {
        this.psOneWayHash = psOneWayHash;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}
