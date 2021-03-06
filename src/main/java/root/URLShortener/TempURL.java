package root.URLShortener;

import org.hibernate.annotations.Type;
import root.Services.TempVisitsServices;

import javax.persistence.*;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "url", cascade = CascadeType.REMOVE)
    private List<TempVisits> visits = new ArrayList<>();
    @Transient
    private Statistics statistics;

    public TempURL(){ }

    public TempURL(String url,String identifier){
        this.url = url;
        this.setHash(urlHash(this.url,identifier));
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
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

    public String urlHash(String url,String identifier){
        System.out.println("Identifier"+identifier);
        String toHash = url+identifier;
        CRC32 crc32 = new CRC32();
        crc32.update(toHash.getBytes());
        String hash = Long.toHexString(crc32.getValue());
        return (hash);
    }
    public Statistics createStatistics(){
        return (new Statistics(TempVisitsServices.getInstance().getIPS(hash),TempVisitsServices.getInstance().getByOs(hash,"Linux"),TempVisitsServices.getInstance().getByOs(hash,"Windows"),TempVisitsServices.getInstance().getByOs(hash,"iOS"),TempVisitsServices.getInstance().getByOs(hash,"Android"),TempVisitsServices.getInstance().listBrowser(hash)));
    }
}
