package se.umu.cs.umume;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="person")
public class PersonBean {
    private URI resourceRef;
    private String givenName;
    private String familyName;
    private List<String> emails;
    private List<String> tweets;
    private String floor;
    private String street;
    private String postalCode;
    private String institution;
    private String roomNumber;
    private String phoneNumber;

    @XmlAttribute(name="self")
    public URI getResourceRef() {
        return resourceRef;
    }
    public void setResourceRef(URI resourceRef) {
        this.resourceRef = resourceRef;
    }

    public String getGivenName() {
        return givenName;
    }
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @XmlElementWrapper(name="emails")
    @XmlElements({@XmlElement(name="email")})
    public List<String> getEmails() {
        return emails;
    }
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
    
    
    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    

    @XmlElementWrapper(name="tweets")
    @XmlElements({@XmlElement(name="tweet")})
    public List<String> getTweets() {
        return tweets;
    }
    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }
}
