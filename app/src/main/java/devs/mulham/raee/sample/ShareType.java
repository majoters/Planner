package devs.mulham.raee.sample;

/**
 * Created by jameswich on 27/1/2561.
 */

public class ShareType {

    private int ForiegnKey;
    private String Ref;
    private String FirebaseKey;
    private int Status;
    private int owner;
    private int id;

    public ShareType(int foriegnKey, String ref, String firebaseKey,int status,int owner) {
        ForiegnKey = foriegnKey;
        Ref = ref;
        FirebaseKey = firebaseKey;
        Status=status;
        this.owner=owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getForiegnKey() {
        return ForiegnKey;
    }

    public void setForiegnKey(int foriegnKey) {
        ForiegnKey = foriegnKey;
    }

    public String getRef() {
        return Ref;
    }

    public void setRef(String ref) {
        Ref = ref;
    }

    public String getFirebaseKey() {
        return FirebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        FirebaseKey = firebaseKey;
    }
}
