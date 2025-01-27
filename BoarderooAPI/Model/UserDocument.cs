using Google.Cloud.Firestore;
using Google.Cloud.Firestore.V1;
namespace BoarderooAPI.Model;

[FirestoreData]
public class UserDocument
{
     [FirestoreProperty]
    public string Id {get; set;}
    [FirestoreProperty]
    public string Email{get; set;}

    [FirestoreProperty]
    public bool IsVerified {get; set;}
    [FirestoreProperty]
    public string Address {get; set;}
    [FirestoreProperty]
    public string Name {get; set;}
    [FirestoreProperty]
    public string Authorization {get; set;}

    [FirestoreProperty]
    public string Password {get; set;}
    [FirestoreProperty]
    public string Surname {get;set;}
    [FirestoreProperty]
    public string Token {get; set;}
    [FirestoreProperty]
    public DateTime TokenCreationDate {get; set;}
    

}