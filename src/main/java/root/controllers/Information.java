package root.controllers;

import net.sf.uadetector.ReadableUserAgent;
import org.json.JSONArray;
import root.Services.*;
import root.URLShortener.*;
import spark.Session;
import root.controllers.UserAgent;
import net.sf.uadetector.*;
import net.sf.uadetector.service.UADetectorServiceFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date.*;


import static spark.Spark.*;
public class Information {


    public void informationControllers(){

        get("/listUsers",(request, response) -> {
            Session session = request.session(true);
            User user = session.attribute("user");
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("users",UserService.getInstance().findAll());
            userMap.put("user",user);
            if (userMap == null){
                response.redirect("/dashBoard");
                return "";
            }
            return Template.renderFreemarker(userMap,"/getUsers.ftl");
        });

        get("/delete-link/:hash",(request, response) -> {
            Session session = request.session(true);
            User user = session.attribute("user");


            if (user != null && user.isAdministrator()){
                URL url = URLServices.getInstance().find(request.params("hash"));
                if (url != null){
                    VisitServices.getInstance().deleteVisits(request.params("hash"));
                    URLServices.getInstance().delete(request.params("hash"));
                    session.attribute("urls", user.urltoGet(user));
                    response.redirect("/dashBoard");
                    return "";
                }
            }
                response.redirect("/dashBoard");
                return "";
        });
        post("/getStatisctics",(request, response) -> {
            Session session = request.session(true);
            User user = session.attribute("user");
            int[] visitsPerHour = new int[24];
            if (VisitServices.getInstance().sanitizeDate(request.queryParams("date"))){
                List<Visit> visits = VisitServices.getInstance().visitsByDates(request.queryParams("hash"),dates(request.queryParams("date")));
                if (visits != null){
                    int visitsCount = 0 ;
                    for(int i = 0 ; i < 24 ; i++){
                        for (int j = 0 ; j < visits.size() ; j++){
                            if (visits.get(j).getTime().getHour() == i ){
                                visitsCount++;
                            }
                        }
                        visitsPerHour[i] = visitsCount;
                        visitsCount = 0;
                    }
                }
            }
            JSONArray jsonArray = new JSONArray(visitsPerHour);
            return jsonArray;
        });
        get("/info/:id",(request, response) -> {
            Map<String,Object> urlMap = new HashMap<>();
            Session session = request.session(true);
            String hash = request.params("id");
            User user = session.attribute("user");
            if (user != null){
                urlMap.put("demographicsURL",URLServices.getInstance().findURLCustomMethod(hash));
                urlMap.put("user",user);
                return Template.renderFreemarker(urlMap,"/dashboard.ftl");
            }else{
                response.redirect("/");
            }
            return "";
        });
        get("/dashBoard", (request, response) -> {
            Session session = request.session(true);
            Map<String,Object> urlMap = new HashMap<>();
            User user = session.attribute("user");

            urlMap.put("user",user);
            if(user != null){
                urlMap.put("urls",user.urltoGet(user));

                return Template.renderFreemarker(urlMap,"/links.ftl");
            }else{
                response.redirect("/");
                return "";
            }
        });
        get("/",(request, response) -> {
            Session session = request.session(true);
            Map<String,Object> urlMap = new HashMap<>();
            User user = request.session().attribute("user");

            urlMap.put("urls",request.session().attribute("urls"));
            urlMap.put("user",user);
            return Template.renderFreemarker(urlMap,"/index.ftl");
        });

        get("/link/:id",(request, response) -> {
            String hash = request.params("id");
            if(request.session().attribute("user") != null){
                URL url = URLServices.getInstance().find(hash);
                if(url != null){
                    UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
                    UserAgent userAgent = new UserAgent();
                    userAgent.printUa(parser.parse(request.userAgent()));
                    Visit visit = new Visit(url,userAgent.getBrowser(),request.ip(),userAgent.getOs(),userAgent.getDeviceType());
                    //url.addVisits(visit);
                    VisitServices.getInstance().create(visit);
                    response.redirect(url.getUrl());
                }

            }else{
                TempURL tempURL = TempURLServices.getInstance().find(hash);
                if(tempURL != null){
                    UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
                    UserAgent userAgent = new UserAgent();
                    userAgent.printUa(parser.parse(request.userAgent()));
                    TempVisits tempVisits = new TempVisits(tempURL,userAgent.getBrowser(),request.ip(),userAgent.getOs(),userAgent.getDeviceType());
                    tempURL.addVisits(tempVisits);
                    TempVisitsServices.getInstance().create(tempVisits);
                    response.redirect(tempURL.getUrl());
                }

            }
            return "";
        });

        get("/registerUser",(request, response) -> {
            Map<String, Object> values = new HashMap<>();
            return Template.renderFreemarker(values,"/registration.ftl");
        });
        post("/register-user", (request, response) -> {
            Map<String, Object> values = new HashMap<>();
            User user = UserService.getInstance().find(request.queryParams("username"));
            if(user == null){
                UserService.getInstance().create((new User(request.queryParams("username"),request.queryParams("name"),request.queryParams("password"))));;
                response.redirect("/");
                return "";
            }else{
                values.put("error","User not found");
                return Template.renderFreemarker(values,"/registration.ftl");
            }

        });

        post("/url",(request, response) -> {
            Session session = request.session();
            User user = request.session().attribute("user");
            Map<String,Object> urlMap = new HashMap<>();
            String url = request.queryParams("url-to-shorter");
            List<URL> urlList = session.attribute("urls");
            boolean statusList = (urlList == null)?false:true;
            URL urlCopy = new URL();
           if (URLServices.getInstance().checkURL(url)){

               if (user != null){
                   URL urlAux = new URL(url,user);
                   URLServices.getInstance().create(urlAux);
                   urlCopy = urlAux;
               }else{
                   TempURL tempURL = new TempURL(url);
                   TempURLServices.getInstance().create(tempURL);
               }
               if (statusList){
                   if (URLServices.getInstance().checkURLExistence(urlList,url) == false){
                       urlList.add(urlCopy);
                   }
               }else{
                   urlList = new ArrayList<>();
                   urlList.add(urlCopy);
               }
               request.session().attribute("urls",urlList);
               urlMap.put("url",request.session().attribute("urls"));

           }
         response.redirect("/");
            return "";
        });
    }

    public boolean chechListSession(List<URL> urls){
        return (urls == null) ? false:true;
    }

    public LocalDate dates(String date){
        String formatDate = date.substring(6,10) + "-"+date.substring(0,2)+"-"+date.substring(3,5);
        LocalDate localDate = LocalDate.parse(formatDate);
        return localDate;
    }


}
