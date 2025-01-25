using FirebaseAdmin;
using Firebase.Storage;
using Google.Apis.Auth.OAuth2;
  namespace BoarderooAPI.Service;

  public class FileService
  {
    public FileService()
    {
        
    }

    public async Task<ServiceResult<string>> AddFile(IFormFile file)
    {
        if (file == null || file.Length == 0)
            {
                return new ServiceResult<string>
        {
            Message="Niepoprawny plik!",
            ResultCode=400
        }; //409
            }
        
         try
            {
                using var fileStream = file.OpenReadStream();
                var firebaseStorage = new FirebaseStorage("boarderoo-71469.firebasestorage.app");
                var fileName = Path.GetFileName(file.FileName);
                var uploadTask = await firebaseStorage
            .Child("files")      // Folder w Firebase Storage
            .Child(fileName)     
            .PutAsync(fileStream); 
                 return new ServiceResult<string>
                 {
                    Message="Dodano plik",
                    ResultCode=200,
                    Data=uploadTask
                 };
            }
            catch (Exception ex)
            {
                        return new ServiceResult<string>
                {
                    Message=$"Błąd podczas przesyłania pliku: {ex.Message}",
                    ResultCode=500
                }; //409
            }
    }
  }
  
 
            