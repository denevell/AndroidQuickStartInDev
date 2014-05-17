package org.denevell.AndroidProject.services;

import org.denevell.AndroidProject.networking.ErrorResponse;
import org.denevell.AndroidProject.networking.MessageBusService;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

public class XmlTestService {

    private final MessageBusService<Hi, XmlServiceInterface> mService;

    public XmlTestService() {
        mService = new MessageBusService<>();
    }

    public void fetch() {
        mService.fetch(
            "http://denevell.org",
            XmlServiceInterface.class,
            new HiError(),
            new SimpleXMLConverter(),
            new MessageBusService.GetResult<Hi, XmlServiceInterface>() {
                    @Override public Hi getResult(XmlServiceInterface service) {
                        return service.list();
                    }
                });
    }

    public static interface XmlServiceInterface {
        @GET("/xml1.xml")
        Hi list();
    }

    @Root
    public static class Hi {
        @Element private There there;
        public There getThere() { return there; }
        public void setThere(There there) { this.there = there; }
        public static class There {
            @Attribute private String yo;
            @Text private String value;
            public String getYo() { return yo; }
            public void setYo(String yo) { this.yo = yo; }
            public String getValue() { return value; }
            public void setValue(String value) { this.value = value; }
        }
    }

    public static class HiError extends ErrorResponse {}
}
