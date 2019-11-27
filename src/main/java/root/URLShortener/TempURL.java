package root.URLShortener;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
@Entity
public class TempURL implements Serializable {
    @Id
    private String hash;
    @Type(type = "text")
    private String url;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "url")
    private List<TempVisits> visits = new ArrayList<>();

    public TempURL(){

    }
    public TempURL(String url){
        this.url = url;
        this.setHash(urlHash(this.url));
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TempVisits> getVisits() {
        return visits;
    }

    public void setVisits(List<TempVisits> visits) {
        this.visits = visits;
    }
    public void addVisits(TempVisits visit){ visits.add(visit);}
    public String urlHash(String url){
        String toHash = url+"djdjd";
        CRC32 crc32 = new CRC32();
        crc32.update(url.getBytes());
        String hash = Long.toHexString(crc32.getValue());
        return (hash);
    }
}