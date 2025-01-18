using BoarderooAPI.Model;
using Google.Cloud.Firestore;

namespace BoarderooAPI.Service
{
    public class AdminService
    {
        private readonly FirestoreDb _database;
        public AdminService(FireBaseService fireBaseService)
        {
            _database = fireBaseService.getDatabase();
        }

        public async Task<ServiceResult<string>> Login(string login, string password)
        {
            try
            {
                var adminCollection = this.getAdminByLogin(login);
                var data = await adminCollection.GetSnapshotAsync();
                if (data.Documents.Count < 1)
                {
                    return new ServiceResult<string>
                    {
                        Message = "Login or password incorrect",
                        ResultCode = 400
                    };
                }
                else
                {
                    var admin = data[0].ConvertTo<AdminDocument>();
                    if(admin.password != password)
                    {
                        return new ServiceResult<string>
                        {
                            Message = "Login or password incorrect",
                            ResultCode = 400
                        };
                    }
                    return new ServiceResult<string>
                    {
                        Message = "Logged in successfully",
                        ResultCode = 200,
                        Data = "Ok"
                    };
                }
            }
            catch (Exception e)
            {
                return new ServiceResult<string>
                {
                    Message = "Blad" + e.ToString(),
                    ResultCode = 500
                };
            }
        }

        public Google.Cloud.Firestore.Query getAdminByLogin(string login)
        {
            return _database.Collection("admins").WhereEqualTo("login", login);
        }
    }
}
