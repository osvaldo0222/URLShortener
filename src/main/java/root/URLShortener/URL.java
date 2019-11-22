package root.URLShortener;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import root.Services.URLServices;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

@Entity
public class URL implements Serializable {
    @Id
    private String hash;
    @Type(type = "text")
    private String url;
    @ManyToOne
    private User user;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "url")
    private List<Visit> visits = new ArrayList<>();

    public URL(){

    }
    public URL(String url){
        this.url = url;
        this.setHash(urlHash(this.url));
    }

    public URL(String url, User user) {
        this.url = url;
        this.user = user;
        this.setHash(urlHash(url));
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Visit> getVisits() {return visits;}

    public void setVisits(List<Visit> visits) {this.visits = visits;}

    public void addVisits(Visit visit){ visits.add(visit);}

    public String urlHash(String url){
        CRC32 crc32 = new CRC32();
        crc32.update(url.getBytes());
        String hash = Long.toHexString(crc32.getValue());
        System.out.println("2");
        return (hash);
    }

}
