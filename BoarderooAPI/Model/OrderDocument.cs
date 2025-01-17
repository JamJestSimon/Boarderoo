using Google.Cloud.Firestore;
using Google.Cloud.Firestore.V1;
namespace BoarderooAPI.Model;

[FirestoreData]
public class OrderDocument
{
    [FirestoreProperty]
    public string Id {get; set;}
    [FirestoreProperty]
    public Timestamp Start {get; set;}
    [FirestoreProperty]
    public Timestamp End {get; set;}
    [FirestoreProperty]

    public StatusType Status {get; set;} //wybieralne z listy
    [FirestoreProperty]

    public string User{get; set;}
    [FirestoreProperty]

    public List<string> Items{get; set;}
    [FirestoreProperty]

    public float Price {get; set;}
    
    public enum StatusType
    {
        Zamówione,
        Zapłacone,
        Potwierdzone,
        Anulowane,
        Zakończone,
    }
}