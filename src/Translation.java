import java.util.List;

/**
 * Created by ZeroNight on 17/3/14.
 */
public class Translation {

    private List<String> translation;
    private String query;
    private int errorCode;

    public void setTranslation(List<String> translation){
        this.translation = translation;
    }
    public List<String> getTranslation(){
        return this.translation;
    }
    public void setQuery(String query){
        this.query = query;
    }
    public String getQuery(){
        return this.query;
    }
    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }
    public int getErrorCode(){
        return this.errorCode;
    }

}
