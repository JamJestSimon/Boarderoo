using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class FireBaseService
{
    private readonly FirestoreDb _firestoreDB;

    public FireBaseService(FirestoreDb firestoreDb)
    {
        _firestoreDB=firestoreDb;
    }
    public FirestoreDb getDatabase()
    {
        return _firestoreDB;
    } 

}