using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class FireBaseService
{
    private readonly FirestoreDb _firestoreDB;


    // Przydaloby sie dodac jeszcze jakas autoryzacje przed wykonywaniem tych metod
    public FireBaseService(FirestoreDb firestoreDb)
    {
        _firestoreDB=firestoreDb;
    }
    public FirestoreDb getDatabase()
    {
        return _firestoreDB;
    } 

    
}