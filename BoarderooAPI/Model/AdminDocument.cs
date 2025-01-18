namespace BoarderooAPI.Model
{
    [FirestoreData]
    public class AdminDocument
    {
        [FirestoreProperty]
        public string Login { get; set; }
        [FirestoreProperty]
        public string Password { get; set; }
    }
}
