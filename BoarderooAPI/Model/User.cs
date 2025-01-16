using Google.Cloud.Firestore;
namespace BoarderooAPI.Model;
public class User
{

    public string Email{get; set;}
    public bool IsVerified {get; set;}

    public GeoPoint Location {get; set;}
    public string Name {get; set;}

    public string Password {get; set;}
    public string Surname {get;set;}
    public string Token {get; set;}
    public DateTime TokenCreationDate {get; set;}

  
    
}