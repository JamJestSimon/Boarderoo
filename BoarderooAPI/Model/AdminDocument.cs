using Google.Cloud.Firestore;

namespace BoarderooAPI.Model
{
    [FirestoreData]
    public class AdminDocument
    {
        [FirestoreProperty]
        public string login { get; set; }
        [FirestoreProperty]
        public string password { get; set; }
    }
}
