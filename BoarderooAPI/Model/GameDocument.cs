using Google.Cloud.Firestore;
using Google.Cloud.Firestore.V1;
namespace BoarderooAPI.Model;

[FirestoreData]
public class GameDocument
{
    [FirestoreProperty]
    public List<string> Image {get; set;}
    [FirestoreProperty]
    public string Id {get; set;}
    [FirestoreProperty]
    public string Name {get; set;}
    [FirestoreProperty]
    public string Type {get; set;}
    [FirestoreProperty]
    public float Price {get; set;}
    [FirestoreProperty]
    public string Description {get; set;}
    [FirestoreProperty]
    public string Publisher {get; set; }
    [FirestoreProperty]
    public string Players_number {get; set;}
    [FirestoreProperty]
    public string Year { get; set;}
    [FirestoreProperty]
    public string Rating {get; set;}
    [FirestoreProperty]
    public bool Enabled {get; set;}
    [FirestoreProperty]
    public int Available_copies {get; set;}
    

}